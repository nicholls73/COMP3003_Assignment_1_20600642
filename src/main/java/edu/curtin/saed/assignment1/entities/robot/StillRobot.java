/*
    FILE:       StillRobot
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is a state class for the robot, which is used to indicate that
                the robot is still.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.robot;

public class StillRobot implements RobotState {

    @Override
    public boolean isKilled() {
        return false;
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public boolean isStill() {
        return true;
    }

    @Override
    public double getOldX() {
        return -1.0;
    }

    @Override
    public double getOldY() {
        return -1.0;
    }

    @Override
    public double getNewX() {
        return -1.0;
    }

    @Override
    public double getNewY() {
        return -1.0;
    }    
}
