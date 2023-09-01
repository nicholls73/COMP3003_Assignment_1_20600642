/*
    FILE:       TowerState
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is the interface for the tower state design pattern.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.tower;

import javafx.scene.image.Image;

public interface TowerState {
    public Image getImage();
    public boolean isKilled();
}
