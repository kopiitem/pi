package com.kopiitem.pi.car.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import java.util.Observable;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public abstract class BaseGpio extends Observable {

    final GpioController gpio = GpioFactory.getInstance();

    public BaseGpio() {

    }

    public GpioController getGpio() {
        return gpio;
    }

    public void shutdown() {
        gpio.shutdown();

    }

}
