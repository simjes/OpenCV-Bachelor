package com.simjessimsol.simcvgame;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Wall {
    private Vector2 position;
    private int width;
    private int height;
    private Rectangle hitbox;

    public Wall(float x, float y, int width, int height) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        hitbox = new Rectangle(x, y, width, height);
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean collides(Player player) {
        //TODO: needs fix?
        if (position.x + width < player.getX() + player.getWidth()) {
            return (Intersector.overlaps(player.getHitbox(), hitbox));
        }
        return false;
    }
}
