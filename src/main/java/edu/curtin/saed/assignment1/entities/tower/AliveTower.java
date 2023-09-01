/*
    FILE:       AliveTower
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is a state class for the tower, used to determine if the tower
                is killed.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.tower;

import javafx.scene.image.Image;

import edu.curtin.saed.assignment1.entities.ImagesUtility;

public class AliveTower implements TowerState {

    @Override
    public Image getImage() {
        return ImagesUtility.getInstance().getTowerImage();
    }

    @Override
    public boolean isKilled() {
        return false;
    }
}
