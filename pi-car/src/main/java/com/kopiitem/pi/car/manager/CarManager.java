package com.kopiitem.pi.car.manager;

import com.kopiitem.pi.car.io.Servo;
import com.kopiitem.pi.car.model.Car;
import com.kopiitem.pi.car.model.Move;
import com.kopiitem.pi.car.model.State;
import java.util.Scanner;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class CarManager implements Runnable {

    private Car car;
    private Automation automation;
    private boolean auto;

    public CarManager() {

    }

    public CarManager(Car car) {
        this.car = car;
        this.automation = new Automation(car);
        this.auto = false;
    }

    public CarManager build(Car car) {
        this.car = car;
        this.automation = new Automation(car);
        this.auto = false;
        return this;
    }

    public CarManager build(Car car, State state, Move move) {
        this.car = car;
        this.automation = new Automation(car);
        this.auto = false;
        car.setState(State.STEADY);
        return this;
    }

    public void shutdown() {
        setAuto(false);
        getAutomation().deActivated();
        car.getEngine().shutdown();
    }

    @Override
    public void run() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String in = sc.nextLine();
            if (in.isEmpty()) {
                continue;
            }
            char ch = in.charAt(0);

            if (ch == 'z') {
                shutdown();

                break;
            }
            switch (ch) {
                case 'w':
                    car.setState(State.FOWARD);
                    break;
                case 'x':
                    car.setState(State.BACKWARD);
                    break;
                case 'a':
                    car.setState(State.LEFT);
                    break;
                case 'd':
                    car.setState(State.RIGHT);
                    break;
                case 's':
                    car.setState(State.STEADY);
                    break;
                case 'r':
                    if (!isAuto()) {
                        setAuto(true);
                        getAutomation().activated();
                    } else {
                        setAuto(false);
                        getAutomation().deActivated();
                    }
                    break;
                case 'y':
                    car.getServo().turn(Servo.ServoState.MIDDLE);

                    break;
                case 'u':
                    car.getServo().turn(Servo.ServoState.RIGHT);

                    break;
                case 't':
                    car.getServo().turn(Servo.ServoState.LEFT);
                    break;
                default:
                    car.setState(State.STEADY);
                    break;
            }
            car.run();
        }
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Automation getAutomation() {
        return automation;
    }

    public void setAutomation(Automation automation) {
        this.automation = automation;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

}
