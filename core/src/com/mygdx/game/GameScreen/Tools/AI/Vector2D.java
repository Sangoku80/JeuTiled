package com.mygdx.game.GameScreen.Tools.AI;

import com.badlogic.gdx.math.Vector2;

public class Vector2D {
    public float x;
    public float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D subtract(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D multiply(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        double length = length();
        return new Vector2D((float) (this.x / length), (float) (this.y / length));
    }

    public Vector2 getVector2()
    {
        return new Vector2(x, y);
    }
}
