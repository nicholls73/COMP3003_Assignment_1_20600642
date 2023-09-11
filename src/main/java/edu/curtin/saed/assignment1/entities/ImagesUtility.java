/*
    FILE:       Tower
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   29/08/2023
    PURPOSE:    Is a utility class to load and store the images used in the
                game.
    NOTES:      Is a singleton class.
*/

package edu.curtin.saed.assignment1.entities;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;

public class ImagesUtility {
    private static ImagesUtility instance = null;
    private static final String UNDAMAGED_WALL_IMAGE_FILE = "181478.png";
    private static final String DAMAGED_WALL_IMAGE_FILE = "181479.png";
    private static final String ROBOT_IMAGE_FILE = "1554047213.png";
    private static final String CROSS_IMAGE_FILE = "cross.png";
    private static final String TOWER_IMAGE_FILE = "rg1024-isometric-tower.png";

    private Image unDamagedWallImage;
    private Image damagedWallImage;
    private Image robotImage;
    private Image crossImage;
    private Image towerImage;

    private ImagesUtility() {
        unDamagedWallImage = loadImage(UNDAMAGED_WALL_IMAGE_FILE);
        damagedWallImage = loadImage(DAMAGED_WALL_IMAGE_FILE);
        robotImage = loadImage(ROBOT_IMAGE_FILE);
        crossImage = loadImage(CROSS_IMAGE_FILE);
        towerImage = loadImage(TOWER_IMAGE_FILE);
    }

    private Image loadImage(String imageStr) {
        Image tempImage;

        // Here's how (in JavaFX) you get an Image object from an image file that's part of the 
        // project's "resources". If you need multiple different images, you can modify this code 
        // accordingly.
        
        // (NOTE: _DO NOT_ use ordinary file-reading operations here, and in particular do not try
        // to specify the file's path/location. That will ruin things if you try to create a 
        // distributable version of your code with './gradlew build'. The approach below is how a 
        // project is supposed to read its own internal resources, and should work both for 
        // './gradlew run' and './gradlew build'.)
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(imageStr))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + imageStr);
            }
            tempImage = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + imageStr, e);
        }
        return tempImage;
    }

    public static synchronized ImagesUtility getInstance() {
        if (instance == null) {
            instance = new ImagesUtility();
        }
        return instance;
    }

    public Image getUnDamagedWallImage() {
        return unDamagedWallImage;
    }

    public Image getDamagedWallImage() {
        return damagedWallImage;
    }

    public Image getRobotImage() {
        return robotImage;
    }

    public Image getCrossImage() {
        return crossImage;
    }

    public Image getTowerImage() {
        return towerImage;
    }
}
