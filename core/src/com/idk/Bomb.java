package com.idk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bomb
{
    Rectangle boundingBox;
    float width;
    float height;
    Texture bombTexture;
    float movementSpeed;
    float fuse;
    int facing;

    TextureAtlas bombAtlas;
    Animation<TextureRegion> bombAnimation;
    private float elapsedTime = 0f;

    public Bomb(float xCenter, float yCenter, int facing)
    {
        this.width = 16;
        this.height = 16;
        fuse = 100;
        this.facing = facing;
        this.movementSpeed = 1;
        //this.bombTexture = new Texture("Bomb.png");
        this.boundingBox = new Rectangle(xCenter, yCenter, width, height);
    }

    public void draw(Batch batch, float deltaTime, int i)
    {
        elapsedTime += deltaTime;

        if (i < 20)
        {
            bombAtlas = new TextureAtlas("explosion.atlas");
            bombAnimation = new Animation<TextureRegion>(1f/30f, bombAtlas.getRegions());
            batch.draw(bombAnimation.getKeyFrame(elapsedTime/4, true),
                    boundingBox.x, boundingBox.y, this.width, this.height);
        }
        else if (this.facing == 0)
        {
            bombAtlas = new TextureAtlas("bombUpp.atlas");
            bombAnimation = new Animation<TextureRegion>(1f/30f, bombAtlas.getRegions());
            this.boundingBox.y += movementSpeed;
            batch.draw(bombAnimation.getKeyFrame(elapsedTime/4, true),
                    boundingBox.x, boundingBox.y, this.width, this.height);
        }
        else if (this.facing == 1)
        {
            bombAtlas = new TextureAtlas("bombRightt.atlas");
            bombAnimation = new Animation<TextureRegion>(1f/30f, bombAtlas.getRegions());
            this.boundingBox.x += movementSpeed;
            batch.draw(bombAnimation.getKeyFrame(elapsedTime/4, true),
                    boundingBox.x, boundingBox.y, this.width, this.height);
        }
        else if (this.facing == 2)
        {
            bombAtlas = new TextureAtlas("bombDown.atlas");
            bombAnimation = new Animation<TextureRegion>(1f/30f, bombAtlas.getRegions());
            this.boundingBox.y -= movementSpeed;
            batch.draw(bombAnimation.getKeyFrame(elapsedTime/4, true),
                    boundingBox.x, boundingBox.y, this.width, this.height);
        }
        else //facing == 3
        {
            bombAtlas = new TextureAtlas("bombLeft.atlas");
            bombAnimation = new Animation<TextureRegion>(1f/30f, bombAtlas.getRegions());
            this.boundingBox.x -= movementSpeed;
            batch.draw(bombAnimation.getKeyFrame(elapsedTime/4, true),
                    boundingBox.x, boundingBox.y, this.width, this.height);
        }
    }




}
