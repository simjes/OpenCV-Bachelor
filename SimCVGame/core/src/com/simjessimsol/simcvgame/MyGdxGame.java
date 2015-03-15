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
    private float timePassed = 0;
    private float timeUsed = 0;
    private BitmapFont timerFont;

    private int gdxWidth;
    private int gdxHeight;
    private int playerScaleWidth;
    private ShapeRenderer shapeRenderer;
    private ArrayList<Wall> walls;


    @Override
    public void create() {
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
        batch = new SpriteBatch();
        playerScaleWidth = gdxWidth / 18;
        player = new Player(100f, 100f, playerScaleWidth, playerScaleWidth * 1.7f);
        timerFont = new BitmapFont();
        timerFont.setColor(Color.BLACK);
        timerFont.setScale(3);
        shapeRenderer = new ShapeRenderer();

        //Wall testWall = new Wall(600, 600, 500, 500);
        Wall topWall = new Wall(0, gdxHeight - 30, gdxWidth, 30);
        Wall bottomWall = new Wall(0, 0, gdxWidth, 30);
        Wall leftWall = new Wall(0, 0, 30, gdxHeight);
        Wall rightWall = new Wall(gdxWidth - 30, 0, 30, gdxHeight);
        walls = new ArrayList<Wall>();
        walls.add(topWall);
        walls.add(bottomWall);
        walls.add(leftWall);
        walls.add(rightWall);
        //walls.add(testWall);
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

        batch.draw(player.getCharacterAnimation().getKeyFrame(timePassed, true), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        player.update();
        timerFont.draw(batch, "Time: " + timeUsed, gdxWidth / 2, gdxHeight - 20);
        /*
        for (Wall w: WallsArray) {
                if (wall.collides(player) {
                timeUsed += 10;
            }
        }
        */
        for (Wall w : walls) {
            batch.draw(w.getTexture(), w.getX(), w.getY(), w.getWidth(), w.getHeight());
        }
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

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void addPenaltyTime() {
        timeUsed += 5;
    }
}
