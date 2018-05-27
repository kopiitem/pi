package com.kopiitem.pi.car.io;

import com.kopiitem.pi.car.model.Back;
import com.kopiitem.pi.car.model.Foward;
import com.kopiitem.pi.car.model.Left;
import com.kopiitem.pi.car.model.Move;
import com.kopiitem.pi.car.model.Right;
import com.kopiitem.pi.car.model.State;
import com.kopiitem.pi.car.model.Steady;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Engine extends BaseGpio {

    final GpioPinDigitalOutput msl1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "msl1", PinState.LOW);
    final GpioPinDigitalOutput msl2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "msl2", PinState.LOW);
    final GpioPinDigitalOutput msr1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "msr1", PinState.LOW);
    final GpioPinDigitalOutput msr2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "msr2", PinState.LOW);

    private Move move;

    public Engine() {
    }

    public void shutdown() {
        gpio.shutdown();
    }

    public void execute(State state) {
        switch (state) {
            case FOWARD:
                setMove(new Foward());
                break;
            case BACKWARD:
                setMove(new Back());
                break;
            case LEFT:
                setMove(new Left());
                break;
            case RIGHT:
                setMove(new Right());
                break;
            default:
                setMove(new Steady());
                break;
        }

        this.move.execute(this);
    }

    public GpioPinDigitalOutput getMsl1() {
        return msl1;
    }

    public GpioPinDigitalOutput getMsl2() {
        return msl2;
    }

    public GpioPinDigitalOutput getMsr1() {
        return msr1;
    }

    public GpioPinDigitalOutput getMsr2() {
        return msr2;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

}
