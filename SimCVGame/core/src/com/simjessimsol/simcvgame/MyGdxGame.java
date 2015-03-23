package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.simjessimsol.scoreobjects.Bomb;
import com.simjessimsol.scoreobjects.ScoreIncreaser;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
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

    //debug
    //private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        currentState = GameState.RUN;
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();

        playerScaleWidth = gdxWidth / 12;
        player = new Player(gdxWidth / 2, 30, playerScaleWidth, playerScaleWidth * 0.5f);

        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.BLACK);
        scoreFont.setScale(3);
        gameOverFont = new BitmapFont();
        gameOverFont.setColor(Color.BLACK);
        gameOverFont.setScale(6);

        scoreIncreasers = new ArrayList<ScoreIncreaser>();
        increasersToDelete = new ArrayList<Integer>();
        spawner = new Spawner(scoreIncreasers);

        Gdx.input.setInputProcessor(this);

        //debug
        //shapeRenderer = new ShapeRenderer();

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
        Gdx.gl.glClearColor(0.25f, 0.75f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        switch (currentState) {
            case RUN:
                String score = "Score: " + player.getScore();
                scoreFont.draw(batch, score, gdxWidth / 2 - scoreFont.getBounds(score).width / 2, gdxHeight - 20);
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
                String gameOver = "Game Over, Score: " + player.getScore();
                gameOverFont.draw(batch, gameOver, gdxWidth / 2 - gameOverFont.getBounds(gameOver).width / 2, gdxHeight / 2);
                break;
        }
        batch.end();

        //player hotbox debug
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        Rectangle hitbox = player.getHitbox();
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        //shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        shapeRenderer.end();*/
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
        player.setScore(0);
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentState == GameState.STOPPED) {
            restart();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
