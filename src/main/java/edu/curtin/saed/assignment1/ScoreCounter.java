/*
    FILE:       ScoreCounter
    AUTHOR:     James Nicholls (20600642)
    UNIT:       COMP3003
    LAST MOD:   31/08/2023
    PURPOSE:    Is the class that keeps track and updates the score.
    NOTES:      Implements Runnable.
*/

package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ScoreCounter implements Runnable {
    private int score;
    private boolean gameOver;
    private JFXArena arena;
    private TextArea logger;
    private Label scoreLabel;
    private Object mutex = new Object();

    public ScoreCounter(JFXArena arena, TextArea logger, Label scoreLabel) {
        score = 0;
        gameOver = false;
        this.arena = arena;
        this.logger = logger;
        this.scoreLabel = scoreLabel;
    }

    /*
        NAME:       run
        PURPOSE:    Is the method that the thread will run, it adds 10 points
                    to the score very 10 seconds, until the game is over.
        IMPORTS:    none
        EXPORTS:    none
        THROWS:     none
    */
    @Override
    public void run() {
        try {
            while (!gameOver) {
                Thread.sleep(1000);
                synchronized (mutex) {
                    score += 10;
                }
                Platform.runLater(() -> {
                    scoreLabel.setText("Score: " + score);
                });
                if (arena.getTower().isKilled()) {
                    gameOver = true;
                }
            }
            Platform.runLater(() -> {
                logger.appendText("GAME OVER!\nYour Score is: " + score + "\n");
            });
        }
        catch (InterruptedException error) {
            System.out.println(error);
        }
    }

    /*
        NAME:       rebotDestroyed
        PURPOSE:    Adds 100 points to the score when called.
        IMPORTS:    none
        EXPORTS:    none
        THROWS:     none
    */
    public void robotDestroyed() {
        synchronized (mutex) {
            score += 100;
        }
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score);
        });
    }
}