package com.kopiitem.pi.car.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public abstract class BaseGpio {

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
