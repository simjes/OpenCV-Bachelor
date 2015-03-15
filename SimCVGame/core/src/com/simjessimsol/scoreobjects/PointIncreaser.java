package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.simjessimsol.simcvgame.Player;

public class PointIncreaser {

    private Vector2 position;
    private int fallspeed;
    private Rectangle hitbox;
    private Texture texture;
    private int scoreModifier;
    private int width;
    private int height;

    public PointIncreaser(float x, float y, int width, int height, int fallspeed, int scoreModifier) {
        position = new Vector2(x, y);
        hitbox = new Rectangle(x, y, width, height);
        this.height = height;
        this.width = width;
        this.fallspeed = fallspeed;
        this.scoreModifier = scoreModifier;
        texture = new Texture(Gdx.files.internal("scoremodifier" + scoreModifier + ".png"));
    }

    public void update() {
        position.y -= fallspeed;
        hitbox.set(position.x, position.y, hitbox.width, hitbox.height);
        if (position.y < 0) {
            position.y = 500;
        }
    }

    public boolean collides(Player player) {
        return Intersector.overlaps(hitbox, player.getHitbox());
    }

    public Texture getTexture() {
        return texture;
    }

    public int getScoreModifier() {
        return scoreModifier;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void dispose() {
        texture.dispose();
    }
}
