package com.kopiitem.pi.car.model;

import com.kopiitem.pi.car.io.Engine;
import com.kopiitem.pi.car.io.Servo;
import java.io.IOException;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Car {

    private String name;
    private State state;
    private Servo servo;

    private Engine engine;

    public Car() throws IOException, InterruptedException {
        this.state = State.STEADY;
        this.engine = new Engine();
        this.servo = new Servo();
    }

    public Car(String name) throws IOException, InterruptedException {
        this.name = name;
        this.state = State.STEADY;
        this.engine = new Engine();
        this.servo = new Servo();
    }

    public Car(String name, State state) throws IOException, InterruptedException {
        this.name = name;
        this.state = state;
        this.engine = new Engine();
        this.servo = new Servo();
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
