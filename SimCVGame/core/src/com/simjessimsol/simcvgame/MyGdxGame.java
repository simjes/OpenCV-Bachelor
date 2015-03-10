package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private float timePassed = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(100, 100);
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
        batch.draw(player.getCharacterAnimation().getKeyFrame(timePassed, true), 100, 100);
        batch.end();
    }
}
