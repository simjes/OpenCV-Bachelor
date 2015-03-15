package com.simjessimsol.simcvgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.simjessimsol.scoreobjects.PointIncreaser;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private BitmapFont scoreFont;

    private float timePassed = 0;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;

    private PointIncreaser pointIncreaser;

    private ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();
        pointIncreaser = new PointIncreaser(gdxWidth / 2, 500, 50, 50, 10, 10);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        pointIncreaser.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        timePassed += Gdx.graphics.getDeltaTime();
        scoreFont.draw(batch, "Score: " + player.getScore(), gdxWidth / 2, gdxHeight - 20);
        batch.draw(player.getTexture(), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        player.update();
        if (pointIncreaser != null) {
            batch.draw(pointIncreaser.getTexture(), pointIncreaser.getX(), pointIncreaser.getY(), pointIncreaser.getWidth(), pointIncreaser.getHeight());
            pointIncreaser.update();
            if (pointIncreaser.collides(player)) {
                player.addToScore(pointIncreaser.getScoreModifier());
                pointIncreaser = null;
            }

        }

        batch.end();


        /*//TODO: debug stuff
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        Rectangle hitbox = player.getHitbox();
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        //shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        shapeRenderer.end();
*/

    }

    public Player getPlayer() {
        return player;
    }

}
