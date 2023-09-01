/*
    FILE:       Robot
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is the context class for the robot state design pattern.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.robot;

import javafx.scene.image.Image;

import edu.curtin.saed.assignment1.entities.ImagesUtility;

public class Robot {
    private RobotState state;
    private int id;
    private double x;
    private double y;

    public Robot(int id) {
        this.id = id;
        state = new StillRobot();
    }

    public void setCoords(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return ImagesUtility.getInstance().getRobotImage();
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public void setKilled() {
        state = new KilledRobot();
    }

    public boolean isKilled() {
        return state.isKilled();
    }

    public void setMoving(double oldX, double oldY, double newX, double newY) {
        state = new MovingRobot(oldX, oldY, newX, newY);
    }

    public boolean isMoving() {
        return state.isMoving();
    }

    public void setStill() {
        state = new StillRobot();
    }

    public boolean isStill() {
        return state.isStill();
    }

    public double getOldX() {
        return state.getOldX();
    }

    public double getOldY() {
        return state.getOldY();
    }

    public double getNewX() {
        return state.getNewX();
    }

    public double getNewY() {
        return state.getNewY();
    }
}
