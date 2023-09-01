/*
    FILE:       MovingRobot
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is a state class for the robot, used to determine if the robot
                is moving and to get the old and new coordinates of the robot.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.robot;

public class MovingRobot implements RobotState {
    private double oldX;
    private double oldY;
    private double newX;
    private double newY;

    public MovingRobot(double oldX, double oldY, double newX, double newY) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public boolean isKilled() {
        return false;
    }

    @Override
    public boolean isMoving() {
        return true;
    }

    @Override
    public boolean isStill() {
        return false;
    }

    @Override
    public double getOldX() {
        return oldX;
    }

    @Override
    public double getOldY() {
        return oldY;
    }

    @Override
    public double getNewX() {
        return newX;
    }

    @Override
    public double getNewY() {
        return newY;
    }
}
