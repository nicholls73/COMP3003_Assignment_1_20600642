/*
    FILE:       RobotManager
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   31/08/2023
    PURPOSE:    Is the class that contains the thread pool and logic for the
                robots.
    NOTES:      Implements Runnable and uses a CachedThreadPool.
*/

package edu.curtin.saed.assignment1;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import edu.curtin.saed.assignment1.entities.robot.Robot;
import edu.curtin.saed.assignment1.entities.wall.Wall;

public class RobotManager implements Runnable {
    private ExecutorService executorService;
    private int nextId;
    private boolean gameOver;
    private JFXArena arena;
    private TextArea logger;
    private ScoreCounter scoreCounter;

    public RobotManager(JFXArena arena, TextArea logger, ScoreCounter scoreCounter) {
        executorService = Executors.newCachedThreadPool();
        nextId = 1;
        gameOver = false;
        this.arena = arena;
        this.logger = logger;
        this.scoreCounter = scoreCounter;
    }

    /*
        NAME:       run
        PURPOSE:    Is the method that the thread will run. It will create a
                    robot every 1.5 seconds until the game is over.
        IMPORTS:    none
        EXPORTS:    none
        THROWS:     none
    */
    @Override
    public void run() {
        try {
            while (!gameOver) {
                createRobot();
                Thread.sleep(1500);
                if (arena.getTower().isKilled()) {
                    gameOver = true;
                    executorService.shutdown();
                }
            }
        }
        catch (InterruptedException error) {
            System.out.println(error);
        }
    }

    /*
        NAME:       createRobot
        PURPOSE:    Creates a robot and submits it to the thread pool.
        IMPORTS:    none
        EXPORTS:    none
        THROWS:     none
    */
    public void createRobot() {
        executorService.submit(() -> {
            Robot robot = generateRobot();
            
            try {
                // while the robot is still alive and the game is not over
                while (!robot.isKilled() && !gameOver) {
                    // sleep for a random amount of time of 500 - 2000, inclusively
                    Thread.sleep(new Random().nextInt(1500) + 501);
                    moveRobot(robot);
                }
            }
            catch (InterruptedException error) {
                System.out.println(error);
            }
        });
    }

    /*
        NAME:       moveRobot
        PURPOSE:    Randomly decides which direction the robot should move and
                    the corresponding method to move it.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     none
    */
    public void moveRobot(Robot robot) {
        boolean hasMoved = false;

        try {
            while (!hasMoved) {
                // randomly choose a number between 0 and 3 inclusively
                switch (new Random().nextInt(4)) {
                    case 0: // move up
                        if (robot.getY() > 0.0 && !arena.hasRobot(robot.getX(), robot.getY() - 1)) {
                            moveRobotUp(robot);
                            hasMoved = true;
                        }
                    break;
                    case 1: // move down
                        if (robot.getY() < arena.getGridHeight() - 1.0 && !arena.hasRobot(robot.getX(), robot.getY() + 1)) {
                            moveRobotDown(robot);
                            hasMoved = true;
                        }
                    break;
                    case 2: // move right
                        if (robot.getX() < arena.getGridWidth() - 1.0 && !arena.hasRobot(robot.getX() + 1, robot.getY())) {
                            moveRobotRight(robot);
                            hasMoved = true;
                        }
                    break;
                    case 3: // move left
                        if (robot.getX() > 0.0 && !arena.hasRobot(robot.getX() - 1, robot.getY())) {
                            moveRobotLeft(robot);
                            hasMoved = true;
                        }
                    break;
                }
            } 
        }
        catch (InterruptedException error) {
            System.out.println(error);
        }

    }

    /*
        NAME:       generateRobot
        PURPOSE:    Creates a new robot and randomly finds one of the four
                    corners as the spawn point.
        IMPORTS:    none
        EXPORTS:    newRobot (Robot)
        THROWS:     none
    */
    public Robot generateRobot() {
        boolean foundAvailableSpawn = false;
        Robot newRobot = new Robot(nextId);
        nextId++;

        while (!foundAvailableSpawn) {
            // randomly choose a number between 0 and 3 inclusively
            switch (new Random().nextInt(4)) {
                case 0: // top left
                    if (!arena.hasRobot(0, 0)) {
                        newRobot.setCoords(0, 0);
                        Platform.runLater(() -> {
                            arena.updateArena();
                        });
                        foundAvailableSpawn = true;
                    }
                break;
                case 1: // top right
                    if (!arena.hasRobot(arena.getGridWidth() - 1, 0)) {
                        newRobot.setCoords(arena.getGridWidth() - 1, 0);
                        Platform.runLater(() -> {
                            arena.updateArena();
                        });
                        foundAvailableSpawn = true;
                    }
                break;
                case 2: // bottom left
                    if (!arena.hasRobot(0, arena.getGridHeight() - 1)) {
                        newRobot.setCoords(0, arena.getGridHeight() - 1);
                        Platform.runLater(() -> {
                            arena.updateArena();
                        });
                        foundAvailableSpawn = true;
                    }
                break;
                case 3: // bottom right
                    if (!arena.hasRobot(arena.getGridWidth() - 1, arena.getGridHeight() - 1)) {
                        newRobot.setCoords(arena.getGridWidth()- 1, arena.getGridHeight() - 1);
                        Platform.runLater(() -> {
                            arena.updateArena();
                        });
                        foundAvailableSpawn = true;
                    }
                break;
            }
        }
        Platform.runLater(() -> {
            arena.addRobot(newRobot);
            logger.appendText("Robot " + newRobot.getId() + " has been created at (" + newRobot.getX() + "," + newRobot.getY() + ")\n");
        });
        return newRobot;
    }

    /*
        NAME:       moveRobotUp
        PURPOSE:    Performs all the checks to see if the parsed in robot can
                    legally move up and then moves it if so. Also checking for
                    walls and the tower.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     InterruptedException
    */
    private void moveRobotUp(Robot robot) throws InterruptedException {
        // change the robot's state to moving and set the old and new coordinates
        robot.setMoving(robot.getX(), robot.getY(), robot.getX(), robot.getY() - 1);

        // iterate over ten times (each taking ~40ms) moving the robot 0.1 squares each time
        for (int i = 0; i < 10; i++) {
            robot.setCoords(robot.getX(), robot.getY() - 0.1);
            Platform.runLater(() -> {
                arena.updateArena();
            });
            Thread.sleep(40);
        }

        // set the robot's coordinates to the whole number values of the new coordinates
        robot.setCoords(robot.getNewX(), robot.getNewY());
        Platform.runLater(() -> {
            arena.updateArena();
        });
        // change back the state.
        robot.setStill();

        checkForWall(robot);
        checkForTower(robot);
    }

    /*
        NAME:       moveRobotDown
        PURPOSE:    Performs all the checks to see if the parsed in robot can
                    legally move down and then moves it if so. Also checking for
                    walls and the tower.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     InterruptedException
    */
    private void moveRobotDown(Robot robot) throws InterruptedException {
        // change the robot's state to moving and set the old and new coordinates
        robot.setMoving(robot.getX(), robot.getY(), robot.getX(), robot.getY() + 1);

        // iterate over ten times (each taking ~40ms) moving the robot 0.1 squares each time
        for (int i = 0; i < 10; i++) {
            robot.setCoords(robot.getX(), robot.getY() + 0.1);
            Platform.runLater(() -> {
                arena.updateArena();
            });
            Thread.sleep(40);
        }

        // set the robot's coordinates to the whole number values of the new coordinates
        robot.setCoords(robot.getNewX(), robot.getNewY());
        Platform.runLater(() -> {
            arena.updateArena();
        });
        // change back the state.
        robot.setStill();

        checkForWall(robot);
        checkForTower(robot);
    }

    /*
        NAME:       moveRobotRight
        PURPOSE:    Performs all the checks to see if the parsed in robot can
                    legally move right and then moves it if so. Also checking for
                    walls and the tower.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     InterruptedException
    */
    private void moveRobotRight(Robot robot) throws InterruptedException {
        // change the robot's state to moving and set the old and new coordinates
        robot.setMoving(robot.getX(), robot.getY(), robot.getX() + 1, robot.getY());

        // iterate over ten times (each taking ~40ms) moving the robot 0.1 squares each time
        for (int i = 0; i < 10; i++) {
            robot.setCoords(robot.getX() + 0.1, robot.getY());
            Platform.runLater(() -> {
                arena.updateArena();
            });
            Thread.sleep(40);
        }

        // set the robot's coordinates to the whole number values of the new coordinates
        robot.setCoords(robot.getNewX(), robot.getNewY());
        Platform.runLater(() -> {
            arena.updateArena();
        });
        // change back the state.
        robot.setStill();

        checkForWall(robot);
        checkForTower(robot);
    }

    /*
        NAME:       moveRobotLeft
        PURPOSE:    Performs all the checks to see if the parsed in robot can
                    legally move left and then moves it if so. Also checking for
                    walls and the tower.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     InterruptedException
    */
    private void moveRobotLeft(Robot robot) throws InterruptedException {
        // change the robot's state to moving and set the old and new coordinates
        robot.setMoving(robot.getX(), robot.getY(), robot.getX() - 1, robot.getY());

        // iterate over ten times (each taking ~40ms) moving the robot 0.1 squares each time
        for (int i = 0; i < 10; i++) {
            robot.setCoords(robot.getX() - 0.1, robot.getY());
            Platform.runLater(() -> {
                arena.updateArena();
            });
            Thread.sleep(40);
        }

        // set the robot's coordinates to the whole number values of the new coordinates
        robot.setCoords(robot.getNewX(), robot.getNewY());
        Platform.runLater(() -> {
            arena.updateArena();
        });
        // change back the state.
        robot.setStill();

        checkForWall(robot);
        checkForTower(robot);
    }

    /*
        NAME:       checkForTower
        PURPOSE:    Checks if the parsed in robot is on the tower and if so,
                    kills the tower.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     none
    */
    private void checkForTower(Robot robot) {
        if (robot.getX() == arena.getTower().getX() && robot.getY() == arena.getTower().getY()) {
            arena.killTower();
        }
    }

    /*
        NAME:       checkForWall
        PURPOSE:    Checks if the parsed in robot is on a wall and if so,
                    damages or destroyes the wall. Also destroys the robot if
                    so.
        IMPORTS:    robot (Robot)
        EXPORTS:    none
        THROWS:     none
    */
    private void checkForWall(Robot robot) {
        if (arena.hasWall(robot.getX(), robot.getY())) {
            // set the robot's state to killed
            robot.setKilled();
            // add the 100 points for killing a robot
            scoreCounter.robotDestroyed();
            Platform.runLater(() -> {
                // remove the robot from the arena's robotList
                arena.removeRobot(robot);
            });
            Wall wall = arena.getWall(robot.getX(), robot.getY());
            // if the wall is damages and needs to be destroyed
            if (wall.isDamaged()) {
                Platform.runLater(() -> {
                    arena.removeWall(wall);
                    logger.appendText("Robot " + robot.getId() + " destroyed a wall at (" + robot.getX() + "," + robot.getY() + ")\n");
                });
            }
            // if the wall is not damaged and needs to be damaged
            else {
                wall.setDamaged();
                Platform.runLater(() -> {
                    logger.appendText("Robot " + robot.getId() + " damaged a wall at (" + robot.getX() + "," + robot.getY() + ")\n");
                });
            }
        }
    }
}
