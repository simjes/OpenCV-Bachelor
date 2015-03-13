package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private float timePassed = 0;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;

    @Override
    public void create() {
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();
        playerScaleWidth = gdxWidth / 15;
        player = new Player(50, 20, playerScaleWidth, playerScaleWidth * 2f);
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
        timePassed += Gdx.graphics.getDeltaTime();
        batch.draw(player.getCharacterAnimation().getKeyFrame(timePassed, true), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        batch.end();
    }

    public Player getPlayer() {
        return player;
    }
}
