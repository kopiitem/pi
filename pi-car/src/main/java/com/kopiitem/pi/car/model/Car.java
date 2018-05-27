package com.kopiitem.pi.car.model;

import com.kopiitem.pi.car.io.Engine;
import com.kopiitem.pi.car.io.Servo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Car {

    private String name;
    private State state;
    private Servo servo;

    private Engine engine;

    public Car() {
        try {
            this.state = State.STEADY;
            this.engine = new Engine();
            this.servo = new Servo();
        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Car(String name) {
        try {
            this.name = name;
            this.state = State.STEADY;
            this.engine = new Engine();
            this.servo = new Servo();
        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Car(String name, State state) {
        try {
            this.name = name;
            this.state = state;
            this.engine = new Engine();
            this.servo = new Servo();
        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void run() {
        this.engine.execute(getState());
    }

    public void run(State state) {
        this.state = state;
        this.engine.execute(getState());
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Servo getServo() {
        return servo;
    }

    public void setServo(Servo servo) {
        this.servo = servo;
    }

}
