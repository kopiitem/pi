package com.kopiitem.pi.car.io;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.component.servo.impl.RPIServoBlasterProvider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Donny Lie <lie.donny@gmail.com>
 */
public class Servo {

    private final ServoProvider servoProvider;
    private final ServoDriver servo7;
    private ServoState servoState;

    public Servo() throws IOException, InterruptedException {
        this.servoProvider = new RPIServoBlasterProvider();
        this.servo7 = servoProvider.getServoDriver(servoProvider.getDefinedServoPins().get(0));
        this.servoState = ServoState.MIDDLE;
        turn(servoState);
    }

    public final void turn(ServoState servoState) {
        try {

            switch (servoState) {
                case RIGHT:
                    for (int i = servo7.getServoPulseWidth(); i > servoState.getValue(); i--) {
                        servo7.setServoPulseWidth(i);
                        Thread.sleep(10);
                    }
                    break;
                case MIDDLE:
                    if (servoState.getValue() < servo7.getServoPulseWidth()) {
                        for (int i = servo7.getServoPulseWidth(); i > servoState.getValue(); i--) {
                            servo7.setServoPulseWidth(i);
                            Thread.sleep(10);
                        }
                    }
                    if (servoState.getValue() > servo7.getServoPulseWidth()) {
                        for (int i = servo7.getServoPulseWidth(); i < servoState.getValue(); i++) {
                            servo7.setServoPulseWidth(i);
                            Thread.sleep(10);
                        }
                    }

                    break;
                case LEFT:
                    for (int i = servo7.getServoPulseWidth(); i < servoState.getValue(); i++) {
                        servo7.setServoPulseWidth(i);
                        Thread.sleep(10);
                    }
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Servo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ServoState getServoState() {
        return servoState;
    }

    public void setServoState(ServoState servoState) {
        this.servoState = servoState;
    }

    public enum ServoState {
        LEFT(200), MIDDLE(150), RIGHT(100);

        private int value;

        private ServoState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
