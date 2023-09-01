/*
    FILE:       Tower
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is the context class for the tower state design pattern.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.tower;

import javafx.scene.image.Image;

public class Tower {
    private TowerState state;
    private double x;
    private double y;

    public Tower(double x, double y) {
        state = new AliveTower();
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

    public void setKilled() {
        state = new KilledTower();     
    }

    public boolean isKilled() {
        return state.isKilled();
    }
}
