/*
    FILE:       JFXArena
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   31/08/2023
    PURPOSE:    Is the class that is used to display and manage the arena.
    NOTES:      Was provided by the lecturer, and modified by me (James).
*/

package edu.curtin.saed.assignment1;

import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.*;

import edu.curtin.saed.assignment1.entities.ImagesUtility;
import edu.curtin.saed.assignment1.entities.robot.Robot;
import edu.curtin.saed.assignment1.entities.tower.Tower;
import edu.curtin.saed.assignment1.entities.wall.*;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 */
public class JFXArena extends Pane
{
    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private int gridWidth = 9;
    private int gridHeight = 9;

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.

    private List<ArenaListener> listeners = null;
    
    private List<Wall> wallList = new ArrayList<>(10);
    private List<Robot> robotList = new LinkedList<>();
    private Tower tower = new Tower(gridWidth / 2, gridHeight / 2);
    // private Tower tower = new Tower(1, 1);

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     */
    public JFXArena()
    {
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }    

    public Tower getTower() {
        return tower;
    }

    public void killTower() {
        tower.setKilled();
    }

    public void addRobot(Robot robot) {
        robotList.add(robot);
        requestLayout();
    }

    public void removeRobot(Robot robot) {
        robotList.remove(robot);
        requestLayout();
    }

    public void updateArena() {
        requestLayout();
    }

    /*
        NAME:       setWall
        PURPOSE:    Adds a wall to the wallList if there are less than 10
                    walls.
        IMPORTS:    wall (Wall)
        EXPORTS:    none
        THROWS:     none
    */
    public void setWall(Wall wall) {
        if (wallList.size() < 10) {
            wallList.add(wall);
        }
        requestLayout();
    }

    /*
        NAME:       hasWall
        PURPOSE:    Returns if the current coordinates have a wall.
        IMPORTS:    x (double), y (double)
        EXPORTS:    hasWall (boolean)
        THROWS:     none
    */
    public boolean hasWall(double x, double y) {
        boolean hasWall = false;

        for (Wall wall : wallList) {
            if (x == wall.getX() && y == wall.getY()) {
                hasWall = true;
            }
        }
        return hasWall;
    }

    /*
        NAME:       getWall
        PURPOSE:    Returns the wall at the parsed coordinates.
        IMPORTS:    x (double), y (double)
        EXPORTS:    wall (Wall)
        THROWS:     none
    */
    public Wall getWall(double x, double y) {
        Wall wall = null;

        for (Wall currWall : wallList) {
            if (x == currWall.getX() && y == currWall.getY()) {
                wall = currWall;
            }
        }
        return wall;
    }

    public void setWallDamaged(Wall wall) {
        wall.setDamaged();
        requestLayout();
    }

    public void removeWall(Wall wall) {
        wallList.remove(wall);
        requestLayout();
    }

    /*
        NAME:       hasRobot
        PURPOSE:    Returns if the parsed in coordinates are occupied by a
                    robot.
        IMPORTS:    x (double), y (double)
        EXPORTS:    hasRobot (boolean)
        THROWS:     none
    */
    public boolean hasRobot(double x, double y) {
        boolean hasRobot = false;

        for (Robot robot : robotList) {
            if (robot.isMoving()) {
                // if x and y are equal to either the old or new location of a moving robot.
                if ((x == robot.getOldX() && y == robot.getOldY()) ||
                    (x == robot.getNewX() && y == robot.getNewY())) {
                    hasRobot = true;
                }
            }
            else if (robot.isStill()) {
                //if x and y are equal to the current location of the still robot.
                if (x == robot.getX() && y == robot.getY()) {
                    hasRobot = true;
                }
            }
        }
        return hasRobot;
    }

    public boolean wallsFull() {
        return wallList.size() == 10;
    }

    public int getNumOfWalls() {
        return wallList.size();
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }
    
    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback 
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the 
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);
                
                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : listeners)
                    {   
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        listeners.add(newListener);
    }
        
        
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren()
    {
        super.layoutChildren(); 
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);
            
        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **

        drawImage(gfx, tower.getImage(), tower.getX(), tower.getY());

        for (Robot robot : robotList) {
            drawImage(gfx, robot.getImage(), robot.getX(), robot.getY());
            drawLabel(gfx, String.valueOf(robot.getId()), robot.getX(), robot.getY());
        }

        for (Wall wall : wallList) {
            drawImage(gfx, wall.getImage(), wall.getX(), wall.getY());
        }

    }
    
    
    /** 
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = ImagesUtility.getInstance().getRobotImage().getWidth();
        double fullSizePixelHeight = ImagesUtility.getInstance().getRobotImage().getHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0, 
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }
    
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * layoutChildren(). 
     *     
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY)
    {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }
    
    /** 
     * Draws a (slightly clipped) line between two grid coordinates.
     *     
     * You shouldn't need to modify this method.
     */
    private void drawLine(GraphicsContext gfx, double gridX1, double gridY1, 
                                               double gridX2, double gridY2)
    {
        gfx.setStroke(Color.RED);
        
        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;
        
        gfx.strokeLine((clippedGridX1 + 0.5) * gridSquareSize, 
                       (clippedGridY1 + 0.5) * gridSquareSize, 
                       (gridX2 + 0.5) * gridSquareSize, 
                       (gridY2 + 0.5) * gridSquareSize);
    }
}
