/*
    FILE:       UndamagedWall
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   30/08/2023
    PURPOSE:    Is a state class for the wall, used to determine if the wall
                is undamaged.
    NOTES:      Is part of a state design pattern.
*/

package edu.curtin.saed.assignment1.entities.wall;

import javafx.scene.image.Image;

import edu.curtin.saed.assignment1.entities.ImagesUtility;

public class UndamagedWall implements WallState {

    @Override
    public Image getImage() {
        return ImagesUtility.getInstance().getUnDamagedWallImage();
    }

    @Override
    public boolean isDamaged() {
        return false;
    }
    
}
