/*
    FILE:       WallBuilder
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   31/08/2023
    PURPOSE:    Is the class that uses a BlockingQueue to queue up and place
                walls.
    NOTES:      Implements Runnable and uses an ArrayBlockingQueue.
*/

package edu.curtin.saed.assignment1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javafx.application.Platform;
import javafx.scene.control.*;

import edu.curtin.saed.assignment1.entities.wall.Wall;

public class WallBuilder implements Runnable {
    private BlockingQueue<Wall> wallQueue;
    private boolean gameOver;
    private JFXArena arena;
    private TextArea logger;
    private Label wallLabel;

    public WallBuilder(JFXArena arena, TextArea logger, Label wallLabel) {
        wallQueue = new ArrayBlockingQueue<>(10);
        gameOver = false;
        this.arena = arena;
        this.logger = logger;
        this.wallLabel = wallLabel;
    }

    /*
        NAME:       run
        PURPOSE:    Is the method that the thread will run. It will poll the
                    queue for walls and there is a wall, it will place it in
                    the arena.
        IMPORTS:    none
        EXPORTS:    none
        THROWS:     none
    */
    @Override
    public void run() {
        try {
            while (!gameOver) {
                Wall wall = wallQueue.poll();

                if (wall != null) {
                    Platform.runLater(() -> {
                        arena.setWall(wall);
                        logger.appendText("Wall placed at (" + wall.getX() + "," + wall.getY() + ")\n");
                        wallLabel.setText("Walls in Queue: " + wallQueue.size());
                        }
                    );
                    Thread.sleep(2000);
                }
                if (arena.getTower().isKilled()) {
                    gameOver = true;
                }
            }
        }
        catch (InterruptedException error) {
            System.out.println(error);
        }
    }

    /*
        NAME:       addWallToQueue
        PURPOSE:    Using parsed in coordinates it will create a new wall and
                    add it to the queue.
        IMPORTS:    x (double), y (double)
        EXPORTS:    none
        THROWS:     InterruptedException
    */
    public void addWallToQueue(double x, double y) throws InterruptedException {
        if (!alreadyQueued(x, y)) {
            wallQueue.put(new Wall(x, y));
            wallLabel.setText("Walls in Queue: " + wallQueue.size());
        }
    }
    
    public int getQueueSize() {
        return wallQueue.size();
    }

    /*
        NAME:       alreadyQueued
        PURPOSE:    Returns if there is already a wall queued at the parsed in
                    coordinates.
        IMPORTS:    x (double), y (double)
        EXPORTS:    isQueued (boolean)
        THROWS:     none
    */
    private boolean alreadyQueued(double x, double y) {
        boolean isQueued = false;

        for (Wall wall : wallQueue) {
            if (wall.getX() == x && wall.getY() == y) {
                isQueued = true;
            }
        }

        return isQueued;
    }
}
