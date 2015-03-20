package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.simjessimsol.scoreobjects.ScoreIncreaser;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private BitmapFont scoreFont;

    private float timePassedTen = 0;
    private float timePassedTwenty = 0;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;

    private ShapeRenderer shapeRenderer;

    private ArrayList<ScoreIncreaser> scoreIncreasers;
    private ArrayList<Integer> increasersToDelete;
    private Spawner spawner;
    private int speedIncreaser = 200;

    @Override
    public void create() {
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();

        playerScaleWidth = gdxWidth / 12;
        player = new Player(gdxWidth / 2, 30, playerScaleWidth, playerScaleWidth * 0.3f);
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.BLACK);
        scoreFont.setScale(3);

        scoreIncreasers = new ArrayList<ScoreIncreaser>();
        increasersToDelete = new ArrayList<Integer>();
        spawner = new Spawner(scoreIncreasers);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        scoreFont.draw(batch, "Score: " + player.getScore(), gdxWidth / 2, gdxHeight - 20);
        batch.draw(player.getTexture(), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        player.update();

        if (scoreIncreasers.size() > 0) {
            for (ScoreIncreaser s : scoreIncreasers) {
                batch.draw(s.getTexture(), s.getX(), s.getY(), s.getWidth(), s.getHeight());
                s.update();
                if (s.collides(player)) {
                    player.addToScore(s.getScoreModifier());
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
        batch.end();

        spawnPointIncreasers();
        if (player.getScore() > speedIncreaser) {
            spawner.speedUp();
            speedIncreaser += 200;
        }
        /*//TODO: debug stuff
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        Rectangle hitbox = player.getHitbox();
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        //shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        shapeRenderer.end();
*/
    }

    private void spawnPointIncreasers() {
        timePassedTen += Gdx.graphics.getDeltaTime();
        if (timePassedTen > 0.75) {
            timePassedTen = 0;
            spawner.spawnTenPointers();
        }
        timePassedTwenty += Gdx.graphics.getDeltaTime();
        if (timePassedTwenty > 1) {
            timePassedTwenty = 0;
            spawner.spawnTwentyPointers();
        }
    }

    public Player getPlayer() {
        return player;
    }

}
