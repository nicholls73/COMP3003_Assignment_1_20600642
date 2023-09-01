/*
    FILE:       App
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   31/08/2023
    PURPOSE:    Is the main class for the application.
    NOTES:      Was provided by the lecturer, and modified by me (James).
*/

package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application
{
    public static void main(String[] args) 
    {
        launch();
    }    
    
    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("COMP3003 | Assignment 1 | James Nicholls (20600642)");
        TextArea logger = new TextArea();
        JFXArena arena = new JFXArena();

        ToolBar toolbar = new ToolBar();
        Label wallLabel = new Label("Walls in Queue: 0");
        Label scoreLabel = new Label("Score: 0");
        toolbar.getItems().addAll(scoreLabel, wallLabel);

        ScoreCounter scoreCounter = new ScoreCounter(arena, logger, scoreLabel);
        WallBuilder wallBuilder = new WallBuilder(arena, logger, wallLabel);
        RobotManager robotManager = new RobotManager(arena, logger, scoreCounter);

        Thread scoreCounterThread = new Thread(scoreCounter);
        Thread wallBuilderThread = new Thread(wallBuilder);
        Thread robotManagerThread = new Thread(robotManager);

        scoreCounterThread.start();
        wallBuilderThread.start();
        robotManagerThread.start();

        arena.addListener((x, y) ->
        {
            try {
                // check if the square doesn't already have a wall and that
                // that there is less than 10 walls on the map and in queue.
                if (!arena.hasWall(x, y) && !isTenWalls(arena, wallBuilder) && !arena.getTower().isKilled()) {
                    wallBuilder.addWallToQueue(x, y);
                }
            }
            catch (InterruptedException error) {
                System.out.println(error);
            }
        });
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    /*
        NAME:       isTenWalls
        PURPOSE:    Returns if there are currently ten walls that exist on the
                    map or in the queue.
        IMPORTS:    arena (JFXArena), wallBuilder (WallBuilder)
        EXPORTS:    isTenWalls (boolean)
        THROWS:     none
    */
    private boolean isTenWalls(JFXArena arena, WallBuilder wallBuilder) {
        boolean isTenWalls = false;
        int numOfWalls = arena.getNumOfWalls() + wallBuilder.getNumOfWallsInQueue();
        if (numOfWalls >= 10) {
            isTenWalls = true;
        }

        return isTenWalls;
    }
}
