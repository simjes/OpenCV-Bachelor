package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simjessimsol.scoreobjects.Bomb;
import com.simjessimsol.scoreobjects.ScoreIncreaser;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private BitmapFont scoreFont;
    private BitmapFont gameOverFont;

    private float timePassedTen = 0;
    private float timePassedFifty = 0;
    private float timePassedBomb = 0;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;

    private ArrayList<ScoreIncreaser> scoreIncreasers;
    private ArrayList<Integer> increasersToDelete;
    private Spawner spawner;
    private int speedIncreaser = 200;

    private GameState currentState;

    public enum GameState {
        RUN,
        STOPPED
    }

    @Override
    public void create() {
        currentState = GameState.RUN;
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();

        playerScaleWidth = gdxWidth / 12;
        player = new Player(gdxWidth / 2, 30, playerScaleWidth, playerScaleWidth * 0.3f);

        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.BLACK);
        scoreFont.setScale(3);
        gameOverFont = new BitmapFont();
        gameOverFont.setColor(Color.BLACK);
        gameOverFont.setScale(6);

        scoreIncreasers = new ArrayList<ScoreIncreaser>();
        increasersToDelete = new ArrayList<Integer>();
        spawner = new Spawner(scoreIncreasers);

    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        scoreFont.dispose();
        gameOverFont.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        switch (currentState) {
            case RUN:
                scoreFont.draw(batch, "Score: " + player.getScore(), gdxWidth / 2, gdxHeight - 20);
                batch.draw(player.getTexture(), player.getX(), player.getY(), player.getWidth(), player.getHeight());
                player.update();

                if (scoreIncreasers.size() > 0) {
                    for (ScoreIncreaser s : scoreIncreasers) {
                        batch.draw(s.getTexture(), s.getX(), s.getY(), s.getWidth(), s.getHeight());
                        s.update();
                        if (s.collides(player)) {
                            if (s instanceof Bomb) {
                                currentState = GameState.STOPPED;
                                break;
                            } else {
                                player.addToScore(s.getScoreModifier());
                            }
                            increasersToDelete.add(scoreIncreasers.indexOf(s));
                        } else if (s.getY() < -20) { //TODO: endre?
                            increasersToDelete.add(scoreIncreasers.indexOf(s));
                        }
                    }
                    if (increasersToDelete.size() > 0) {
                        for (Integer i : increasersToDelete) {
                            scoreIncreasers.get(i).dispose();
                            scoreIncreasers.remove((int) i);
                        }
                        increasersToDelete.clear();
                    }
                }

                spawnPointIncreasers();
                if (player.getScore() > speedIncreaser) {
                    spawner.speedUp();
                    speedIncreaser += 200;
                }
                break;
            case STOPPED:
                gameOverFont.draw(batch, "Game Over\nScore: " + player.getScore(), gdxWidth / 2, gdxHeight / 2);
                break;
        }
        batch.end();
    }

    private void spawnPointIncreasers() {
        timePassedTen += Gdx.graphics.getDeltaTime();
        if (timePassedTen > 0.75) {
            timePassedTen = 0;
            spawner.spawnTenPointers();
        }
        timePassedFifty += Gdx.graphics.getDeltaTime();
        if (timePassedFifty > 1) {
            timePassedFifty = 0;
            spawner.spawnFiftyPointers();
        }
        timePassedBomb += Gdx.graphics.getDeltaTime();
        if (timePassedBomb > 0.5) {
            timePassedBomb = 0;
            spawner.spawnBombs();
        }
    }

    public void restart() {
        player = new Player(gdxWidth / 2, 30, playerScaleWidth, playerScaleWidth * 0.3f);
        timePassedBomb = 0;
        timePassedTen = 0;
        timePassedFifty = 0;
        increasersToDelete.clear();
        scoreIncreasers.clear();
        currentState = GameState.RUN;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public Player getPlayer() {
        return player;
    }

}
