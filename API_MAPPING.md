# Pi-Car API & Interface Mapping

## 1. Public API Interfaces

### 1.1 CarManager - Control Interface

**Class**: `com.kopiitem.pi.car.manager.CarManager`  
**Type**: Main control orchestrator  
**Pattern**: Builder + Runnable

#### Methods

| Method | Signature | Purpose | Returns |
|--------|-----------|---------|---------|
| `build` | `build(Car car)` | Initialize with Car instance | `CarManager` |
| `build` | `build(Car car, State state, Move move)` | Initialize with Car and state | `CarManager` |
| `run` | `void run()` | Main control loop - reads keyboard input | void |
| `shutdown` | `void shutdown()` | Graceful cleanup and exit | void |
| `getCar` | `Car getCar()` | Get current car instance | `Car` |
| `setCar` | `void setCar(Car car)` | Set car instance | void |
| `getAutomation` | `Automation getAutomation()` | Get automation controller | `Automation` |
| `setAutomation` | `void setAutomation(Automation auto)` | Set automation controller | void |
| `isAuto` | `boolean isAuto()` | Check if in auto mode | `boolean` |
| `setAuto` | `void setAuto(boolean auto)` | Toggle auto mode flag | void |

#### Input Handling

```java
while (true) {
    Scanner sc = new Scanner(System.in);
    String in = sc.nextLine();
    char ch = in.charAt(0);
    
    switch(ch) {
        case 'w': car.setState(State.FOWARD); break;
        case 'x': car.setState(State.BACKWARD); break;
        case 'a': car.setState(State.LEFT); break;
        case 'd': car.setState(State.RIGHT); break;
        case 's': car.setState(State.STEADY); break;
        case 'r': automation.activated() / deActivated(); break;
        case 't': servo.turn(ServoState.LEFT); break;
        case 'y': servo.turn(ServoState.MIDDLE); break;
        case 'u': servo.turn(ServoState.RIGHT); break;
        case 'z': shutdown(); break;
    }
    car.run();
}
```

---

### 1.2 Car - State Model Interface

**Class**: `com.kopiitem.pi.car.model.Car`  
**Type**: State container and orchestrator

#### Constructors

```java
Car() throws IOException, InterruptedException                          // Default: STEADY state
Car(String name) throws IOException, InterruptedException               // With name
Car(String name, State state) throws IOException, InterruptedException  // With name and initial state
```

**Fixed**: constructors now declare `throws IOException, InterruptedException` instead of catching and logging them internally. This prevents a half-initialized `Car` (with a `null` servo) from being returned silently — callers (e.g. `App.main()`, which already declares `throws IOException, InterruptedException`) must now handle or propagate the failure.

#### Methods

| Method | Signature | Purpose | Returns |
|--------|-----------|---------|---------|
| `run` | `void run()` | Execute current state | void |
| `run` | `void run(State state)` | Set and execute state | void |
| `getState` | `State getState()` | Get current state | `State` |
| `setState` | `void setState(State state)` | Update state | void |
| `getName` | `String getName()` | Get car name | `String` |
| `setName` | `void setName(String name)` | Set car name | void |
| `getEngine` | `Engine getEngine()` | Get motor controller | `Engine` |
| `setEngine` | `void setEngine(Engine engine)` | Set motor controller | void |
| `getServo` | `Servo getServo()` | Get servo controller | `Servo` |
| `setServo` | `void setServo(Servo servo)` | Set servo controller | void |

---

### 1.3 Engine - Motor Control Interface

**Class**: `com.kopiitem.pi.car.io.Engine`  
**Type**: GPIO motor controller  
**Pattern**: Command executor

#### Methods

| Method | Signature | Purpose | Returns |
|--------|-----------|---------|---------|
| `execute` | `void execute(State state)` | Execute movement for state | void |
| `getMove` | `Move getMove()` | Get current Move command | `Move` |
| `setMove` | `void setMove(Move move)` | Set Movement command | void |
| `getMsl1` | `GpioPinDigitalOutput getMsl1()` | Get left motor pin 1 | `GpioPinDigitalOutput` |
| `getMsl2` | `GpioPinDigitalOutput getMsl2()` | Get left motor pin 2 | `GpioPinDigitalOutput` |
| `getMsr1` | `GpioPinDigitalOutput getMsr1()` | Get right motor pin 1 | `GpioPinDigitalOutput` |
| `getMsr2` | `GpioPinDigitalOutput getMsr2()` | Get right motor pin 2 | `GpioPinDigitalOutput` |
| `shutdown` | `void shutdown()` | Shutdown GPIO controller | void |

#### GPIO Pin Mapping

```java
final GpioPinDigitalOutput msl1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "msl1", PinState.LOW);
final GpioPinDigitalOutput msl2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "msl2", PinState.LOW);
final GpioPinDigitalOutput msr1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "msr1", PinState.LOW);
final GpioPinDigitalOutput msr2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "msr2", PinState.LOW);
```

#### Movement Execution Logic

```
execute(State state):
  ├─ FOWARD  → Foward.execute(this)
  ├─ BACKWARD → Back.execute(this)
  ├─ LEFT    → Left.execute(this)
  ├─ RIGHT   → Right.execute(this)
  └─ STEADY  → Steady.execute(this)
```

---

### 1.4 Servo - Direction Control Interface

**Class**: `com.kopiitem.pi.car.io.Servo`  
**Type**: GPIO servo controller

#### Constructors

```java
Servo() throws IOException, InterruptedException
// Initializes ServoProvider with RPIServoBlasterProvider
```

#### Methods

| Method | Signature | Purpose | Returns |
|--------|-----------|---------|---------|
| `turn` | `void turn(ServoState state)` | Rotate servo to angle | void |
| `getServoState` | `ServoState getServoState()` | Get current servo position | `ServoState` |
| `setServoState` | `void setServoState(ServoState state)` | Set servo position | void |

#### ServoState Enum

```java
enum ServoState {
    LEFT(200),      // 200 pulse width
    MIDDLE(150),    // 150 pulse width
    RIGHT(100);     // 100 pulse width
}
```

#### Servo Control Algorithm

```java
turn(ServoState state):
  if (state == RIGHT):
    loop i from current to 100 (decrement):
      setServoPulseWidth(i)
      sleep(10ms)
  
  if (state == MIDDLE):
    if current > 150:
      loop i from current to 150 (decrement)
    else:
      loop i from current to 150 (increment)
  
  if (state == LEFT):
    loop i from current to 200 (increment):
      setServoPulseWidth(i)
      sleep(10ms)
```

---

### 1.5 Distance - Sensor Interface

**Class**: `com.kopiitem.pi.car.io.Distance`  
**Type**: Ultrasonic sensor reader  
**Pattern**: Listener + Runnable (replaces deprecated `Observable`)

#### Methods

| Method | Signature | Purpose | Returns |
|--------|-----------|---------|---------|
| `run` | `void run()` | Background sensor loop | void |
| `doMeasureTheDistance` | `void doMeasureTheDistance()` | Trigger and read sensor | void |
| `activatedServo` | `void activatedServo(Car car)` | Sweep servo to find path | void |
| `terminate` | `void terminate()` | Stop sensor thread | void |
| `addListener` | `void addListener(DistanceListener listener)` | Register a distance-change callback | void |
| `getValue` | `int getValue()` | Get last distance reading | `int` (cm) |
| `setValue` | `void setValue(int value)` | Set distance value | void |
| `isRunning` | `boolean isRunning()` | Check if thread running | `boolean` |
| `setRunning` | `void setRunning(boolean running)` | Control thread execution | void |

#### DistanceListener Interface

**Interface**: `com.kopiitem.pi.car.io.DistanceListener`

```java
public interface DistanceListener {
    void onDistanceChanged(int distanceCm);
}
```

Replaces the deprecated `java.util.Observer` callback. `Distance` keeps a `List<DistanceListener>` and invokes `onDistanceChanged(int)` directly — no `Object` casting required.

#### GPIO Pin Mapping

```java
private final GpioPinDigitalOutput sensorTriggerPin = 
    getGpio().provisionDigitalOutputPin(RaspiPin.GPIO_28);  // Trigger output
private final GpioPinDigitalInput sensorEchoPin = 
    getGpio().provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_DOWN);  // Echo input
```

#### Distance Calculation Algorithm

```java
doMeasureTheDistance():
  1. Set trigger pin HIGH
  2. Sleep 0ms + 10,000ns (10µs) — Thread.sleep(0, 10_000)
  3. Set trigger pin LOW
  4. Wait for echo pin to go HIGH
  5. Record start time (nanoTime)
  6. Wait for echo pin to go LOW
  7. Record end time (nanoTime)
  8. Calculate: distance = ((endTime - startTime) * 17150) / 1e9
  
  // 17150 = speed of sound in cm/µs / 2 (round trip)
```

**Fixed**: previously used `Thread.sleep((long) 0.01)`, which truncated to `0` milliseconds and slept for no time at all. Now uses the `Thread.sleep(long millis, int nanos)` overload to actually wait ~10µs.

#### Sensor Thread Loop

```java
run():
  while (isRunning()):
    doMeasureTheDistance()
    notifyListeners(this.value)  // Calls each registered DistanceListener
```

#### Obstacle Avoidance Logic

```java
activatedServo(Car car):
  1. Turn servo RIGHT
  2. Measure distance (right)
  3. Turn servo LEFT
  4. Measure distance (left)
  5. if (left > right):
       car.run(LEFT)
     else:
       car.run(RIGHT)
  6. Turn servo MIDDLE
  7. car.run(FOWARD)
```

---

### 1.6 Automation - Listener Interface

**Class**: `com.kopiitem.pi.car.manager.Automation`  
**Type**: Autonomous driving controller  
**Pattern**: Listener (registers a `DistanceListener` lambda)

#### Constructors

```java
Automation(Car car)
// Initializes Distance sensor with a DistanceListener lambda callback
```

#### Methods

| Method | Signature | Purpose | Returns |
|--------|-----------|---------|---------|
| `activated` | `void activated()` | Start autonomous mode | void |
| `deActivated` | `void deActivated()` | Stop autonomous mode | void |
| `getDistance` | `Distance getDistance()` | Get sensor instance | `Distance` |
| `setDistance` | `void setDistance(Distance d)` | Set sensor instance | void |
| `getCar` | `Car getCar()` | Get car instance | `Car` |
| `setCar` | `void setCar(Car car)` | Set car instance | void |

#### Listener Callback Logic

```java
distance.addListener(distanceCm -> {
  if (distanceCm <= RANGE_DETECTION):  // 20 cm
    car.run(STEADY)
    getDistance().activatedServo(car)
    car.run(FOWARD)
  else:
    // Continue in current state
});
```

No more `Observable`/`Observer`, and no `(int) arg` cast — the lambda receives a typed `int` directly.

#### Activation Flow

```
activated():
  1. car.run(STEADY)
  2. distance.setRunning(true)
  3. car.run(FOWARD)
  4. new Thread(distance).start()  // Start background thread

deActivated():
  1. car.run(STEADY)
  2. distance.setRunning(false)   // Signal thread to stop
```

---

### 1.7 Move - Command Pattern Interface

**Interface**: `com.kopiitem.pi.car.model.Move`  
**Type**: Command interface

#### Method

```java
void execute(Engine engine)
```

#### Implementations

| Class | Logic | GPIO State |
|-------|-------|-----------|
| `Foward` | Go forward | msl1=HIGH, msr1=HIGH |
| `Back` | Go backward | msl2=HIGH, msr2=HIGH |
| `Left` | Turn left | msl1=HIGH, msr2=HIGH |
| `Right` | Turn right | msr1=HIGH, msl2=HIGH |
| `Steady` | Stop all | All pins LOW |

---

### 1.8 State - State Enum

**Enum**: `com.kopiitem.pi.car.model.State`

```java
enum State {
    FOWARD,
    BACKWARD,
    LEFT,
    RIGHT,
    STEADY
}
```

---

## 2. Control Flow Sequences

### 2.1 Manual Forward Movement

```
User presses 'w'
  ↓
CarManager.run() detects 'w'
  ↓
car.setState(State.FOWARD)
  ↓
car.run()
  ↓
engine.execute(State.FOWARD)
  ↓
Move move = new Foward()
move.execute(engine)
  ↓
msl1.high()  // Left motor forward
msr1.high()  // Right motor forward
  ↓
Motors rotate
```

### 2.2 Autonomous Obstacle Avoidance

```
Automation.activated()
  ↓
distance.setRunning(true)
new Thread(distance).start()
  ↓
[Background thread loop]
distance.doMeasureTheDistance()
  ↓
if (distance <= 20cm):
  distance.notifyObservers(value)
    ↓
    Automation.update() [Observer callback]
      ↓
      car.run(State.STEADY)
        ↓
        engine.execute(STEADY)
        → All pins LOW
      ↓
      distance.activatedServo(car)
        ↓
        servo.turn(RIGHT)  → Measure
        servo.turn(LEFT)   → Measure
        ↓
        if (leftDistance > rightDistance):
          car.run(State.LEFT)
        else:
          car.run(State.RIGHT)
      ↓
      servo.turn(MIDDLE)
      car.run(State.FOWARD)
else:
  Continue current movement
```

---

## 3. Exception Handling

### Handled Exceptions

| Class | Exception | Handling |
|-------|-----------|----------|
| `Car` | `IOException` | Propagated via `throws` (fixed — was: log SEVERE, continue with null servo) |
| `Car` | `InterruptedException` | Propagated via `throws` (fixed — was: log SEVERE, continue) |
| `Distance` | `InterruptedException` | Print stack trace, continue |
| `Servo` | `InterruptedException` | Log SEVERE, continue |

### Fixed Issues
- ✅ `Car` constructors now `throws IOException, InterruptedException` instead of swallowing them — no more half-built `Car` with a `null` servo
- ✅ Eliminates the previous NPE risk from `car.getServo().turn()` being called against a `null` servo after a silent init failure
- ✅ Failure now surfaces immediately at construction time instead of at an unrelated later call site

### Shutdown Robustness (New)
- ✅ `CarManager` registers a JVM shutdown hook (`Runtime.getRuntime().addShutdownHook`) in every `build(...)`/constructor path
- ✅ `Engine.shutdown()` forces `State.STEADY` (all motor pins LOW) before releasing the GPIO controller
- ✅ `CarManager.shutdown()` is now idempotent via an `AtomicBoolean` guard — safe whether triggered by the `'z'` key, the shutdown hook, or both
- ✅ Prevents motors from being left running if the process is killed (SIGTERM, Ctrl+C, crash) instead of exiting via the `'z'` key

---

## 4. Configuration Constants

**File**: `com.kopiitem.pi.car.util.Constants`

```java
public final class Constants {

    public static final int RANGE_DETECTION = 20;  // cm

    private Constants() {
    }
}
```

**Fixed**: `RANGE_DETECTION` is now `public static final` (immutable) inside a `final` class with a private constructor, preventing reassignment or instantiation.

---

## 5. GPIO Configuration Summary

### Motor Control Pins

| Pin | GPIO | Purpose | Mode | Initial State |
|-----|------|---------|------|---------------|
| msl1 | GPIO_22 | Left motor forward | Output | LOW |
| msl2 | GPIO_23 | Left motor backward | Output | LOW |
| msr1 | GPIO_24 | Right motor forward | Output | LOW |
| msr2 | GPIO_25 | Right motor backward | Output | LOW |

### Sensor Pins

| Pin | GPIO | Purpose | Mode | Pull Resistance |
|-----|------|---------|------|-----------------|
| Trigger | GPIO_28 | Sensor trigger pulse | Output | - |
| Echo | GPIO_29 | Sensor echo measurement | Input | PULL_DOWN |

### Servo Pin

| Pin | Controller | Purpose | Type |
|-----|------------|---------|------|
| Servo | RPIServoBlaster | Servo control | PWM |

---

## Summary

The pi-car API is organized into distinct layers:

1. **Control Layer** (`CarManager`) - User input handling, JVM shutdown hook registration
2. **Automation Layer** (`Automation`, `Distance`, `DistanceListener`) - Sensor-driven logic via typed listener callback
3. **Hardware Abstraction** (`Car`, `Engine`, `Servo`) - State and control; `Car` construction failures now propagate instead of being swallowed
4. **Movement Models** (`Move` interface) - Extensible movement strategies
5. **Utilities** (`Constants`) - Immutable configuration

Each component has clear responsibilities and uses appropriate design patterns for extensibility and maintainability. All five previously identified issues (timing bug, swallowed exceptions, missing shutdown hook, deprecated `Observable`/`Observer`, mutable constant) have been fixed and verified with a clean `mvn compile` build (zero deprecation warnings).
