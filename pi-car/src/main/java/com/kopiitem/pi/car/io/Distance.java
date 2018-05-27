package com.kopiitem.pi.car.io;

import com.kopiitem.pi.car.model.Car;
import com.kopiitem.pi.car.model.State;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Distance extends BaseGpio implements Runnable {

    private final GpioPinDigitalOutput sensorTriggerPin = getGpio().provisionDigitalOutputPin(RaspiPin.GPIO_28); // Trigger pin as OUTPUT
    private final GpioPinDigitalInput sensorEchoPin = getGpio().provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_DOWN); // Echo pin as INPUT     

    private int value;

    public boolean running;

    public Distance() {
        this.running = false;
    }

    public void terminate() {
        setRunning(false);
    }

    public void activatedServo(Car car) {
        try {

            car.getServo().turn(Servo.ServoState.RIGHT);
            doMeasureTheDistance();
            int right = getValue();

            car.getServo().turn(Servo.ServoState.LEFT);
            doMeasureTheDistance();
            int left = getValue();

            System.out.println("LEFT: " + left + ", RIGHT: " + right);
            if (left > right) {
                car.run(State.LEFT);
                System.out.println("Go to LEFT!");
                Thread.sleep(500);
            } else {
                car.run(State.RIGHT);
                System.out.println("Go to RIGHT!");
                Thread.sleep(500);
            }
            car.getServo().turn(Servo.ServoState.MIDDLE);
            car.run(State.FOWARD);
        } catch (InterruptedException ex) {
            Logger.getLogger(Distance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (isRunning()) {
            doMeasureTheDistance();
            setChanged();
            notifyObservers(this.value);
        }
    }

    public void doMeasureTheDistance() {
        try {
            Thread.sleep(500);
            sensorTriggerPin.high(); // Make trigger pin HIGH
            Thread.sleep((long) 0.01);// Delay for 10 microseconds
            sensorTriggerPin.low(); //Make trigger pin LOW

            while (sensorEchoPin.isLow()) { //Wait until the ECHO pin gets HIGH

            }
            long startTime = System.nanoTime(); // Store the surrent time to calculate ECHO pin HIGH time.
            while (sensorEchoPin.isHigh()) { //Wait until the ECHO pin gets LOW

            }
            long endTime = System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.
            this.value = (int) Math.round(((endTime - startTime) * 17150) / 1e9);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
