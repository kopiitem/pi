package com.kopiitem.pi.car;

import com.kopiitem.pi.car.manager.CarManager;
import com.kopiitem.pi.car.model.Car;
import com.kopiitem.pi.car.model.State;
import java.io.IOException;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        new CarManager().build(new Car("Kensei", State.STEADY)).run();
    }
}
