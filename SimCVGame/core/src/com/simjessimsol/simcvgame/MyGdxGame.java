package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;


public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private BitmapFont scoreFont;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;


    @Override
    public void create() {
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();

        playerScaleWidth = gdxWidth / 12;
        player = new Player(gdxWidth / 2, 20, playerScaleWidth, playerScaleWidth * 0.3f);
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.BLACK);
        scoreFont.setScale(3);
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
        batch.draw(player.getTexture(), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        batch.end();

    }

    public Player getPlayer() {
        return player;
    }

}
