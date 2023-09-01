/*
    FILE:       Wall
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is the context class for the wall state design pattern.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.wall;

import javafx.scene.image.Image;

public class Wall {
    private WallState state;
    private double x;
    private double y;

    public Wall(double x, double y) {
        state = new UndamagedWall();
        this.x = x;
        this.y = y;
    }

    public void setCoords(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getImage() {
        return state.getImage();
    }
    
    // Will change the state of the wall to be damaged.
    public void setDamaged() {
        state = new DamagedWall();
    }

    // Will return if the state of the wall is damaged.
    public boolean isDamaged() {
        return state.isDamaged();
    }
}
