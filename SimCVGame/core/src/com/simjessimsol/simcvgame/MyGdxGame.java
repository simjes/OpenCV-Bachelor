package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.awt.Font;


public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private float timePassed = 0;
    private float timeUsed = 0;
    private BitmapFont timerFont;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;
    private ShapeRenderer shapeRenderer;


    @Override
    public void create() {
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();
        playerScaleWidth = gdxWidth / 18;
        player = new Player(50f, 20f, playerScaleWidth, playerScaleWidth * 1.7f);
        timerFont = new BitmapFont();
        timerFont.setColor(Color.BLACK);
        timerFont.setScale(3);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        timerFont.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        timePassed += Gdx.graphics.getDeltaTime();
        timeUsed += timePassed;
        //TODO: center timer
        //TODO: dont let player go through walls;
        timerFont.draw(batch, "Time: " + timeUsed, gdxWidth / 2, gdxHeight);
        batch.draw(player.getCharacterAnimation().getKeyFrame(timePassed, true), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        player.update();
        /*
        for (Wall w: WallsArray) {
                if (wall.collides(player) {
                timeUsed += 10;
            }
        }
         */
        batch.end();

        /*
        //TODO: debug stuff
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        Rectangle hitbox = player.getHitbox();
        //shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        shapeRenderer.end();
        */
    }

    public Player getPlayer() {
        return player;
    }
}
