package com.kopiitem.pi.car.model;

import com.kopiitem.pi.car.io.Engine;
import com.pi4j.io.gpio.PinState;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Left implements Move {

    @Override
    public void execute(Engine engine) {
        engine.getMsl1().setState(PinState.HIGH);
        engine.getMsl2().setState(PinState.LOW);
        engine.getMsr1().setState(PinState.LOW);
        engine.getMsr2().setState(PinState.LOW);
        System.out.println("Left!");
    }

}
