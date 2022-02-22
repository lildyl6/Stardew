package com.idk;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bat
{
    Rectangle boundinBox;
    float movementSpeed;
    int facing;
    TextureAtlas batAtlas;
    Animation<TextureRegion> batAnimation;
    private float elapsedTime = 0f;
    private float frameSpeed = 4f;
    public Vector2 vec;
    public int health;

    public Bat(float xCenter, float yCenter, float width, float height,
               int facing/*, TextureAtlas batAtlas, Animation<TextureRegion> batAnimation*/)
    {
        this.boundinBox = new Rectangle(xCenter - width/2, yCenter - height/2, width, height);
        this.movementSpeed = 60;
        this.facing = facing;
        vec = new Vector2(0,0);
        this.health = 3;
    }

    public void handle(Batch batch, float deltaTime, Player player)
    {
        updatePosition(player);
        draw(batch, deltaTime);
    }

    public void draw(Batch batch, float deltaTime)
    {
        elapsedTime += deltaTime;
        batAtlas = new TextureAtlas("batR.atlas");
        batAnimation = new Animation<TextureRegion>(1f / 30f, batAtlas.getRegions());
        batch.draw(batAnimation.getKeyFrame(elapsedTime / frameSpeed, true),
                boundinBox.x, boundinBox.y);
    }

    public void updatePosition(Player player)
    {
        float dx = player.boundingBox.x - this.boundinBox.x;
        float dy = player.boundingBox.y - this.boundinBox.y;
        Vector2 newVec = new Vector2(dx, dy);
        newVec.nor();
        Vector2 v = new Vector2((.95f*vec.x) + (.05f*newVec.x),
                (.95f*vec.y) + (.05f*newVec.y));
        translate(v.x, v.y);
        this.vec = v;
    }

    public void translate(float xChange, float yChange)
    {
        float speed = this.movementSpeed/70;
        this.boundinBox.setPosition(this.boundinBox.x + xChange*speed, this.boundinBox.y + yChange*speed);
    }
}
