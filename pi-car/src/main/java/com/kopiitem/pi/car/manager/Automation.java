package com.kopiitem.pi.car.manager;

import com.kopiitem.pi.car.io.Distance;
import com.kopiitem.pi.car.model.Car;
import com.kopiitem.pi.car.model.State;
import com.kopiitem.pi.car.util.Constants;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Automation {

    private Distance distance;
    private Car car;

    public Automation(Car car) {
        this.distance = new Distance();
        this.distance.addListener(distanceCm -> {
            if (distanceCm <= Constants.RANGE_DETECTION) {
                System.out.println("Bellow <= 20 is On, " + distanceCm);
                getCar().run(State.STEADY);
                getDistance().activatedServo(getCar());
                getCar().run(State.FOWARD);
            } else {
                System.out.println("Bellow >= 20 is On, " + distanceCm);
            }
        });

        this.car = car;
    }

    public void activated() {
        getCar().run(State.STEADY);
        getDistance().setRunning(true);
        getCar().run(State.FOWARD);
        new Thread(getDistance()).start();
        System.out.println("Distance been activated!");
    }

    public void deActivated() {
        getCar().run(State.STEADY);
        getDistance().setRunning(false);

        System.out.println("Distance been deActivated!");
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
