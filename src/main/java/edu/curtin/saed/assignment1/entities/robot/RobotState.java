/*
    FILE:       RobotState
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is the interface for the robot state design pattern.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.robot;

public interface RobotState {
    public boolean isKilled();
    public boolean isMoving();
    public boolean isStill();
    public double getOldX();
    public double getOldY();
    public double getNewX();
    public double getNewY();
}
