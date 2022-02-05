package com.idk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player
{
    Rectangle boundingBox;
    float movementSpeed;
    int facing; // 0 N, 1 E, 2 S, 3 W
    Texture playerTexture;
    TextureAtlas boyAtlas;
    Animation<TextureRegion> playerAnimation;

    private float elapsedTime = 0f;


    public Player(float xCenter, float yCenter, float width, float height, float movementSpeed,
                  int facing)
    {
        this.boundingBox = new Rectangle(xCenter - width/2, yCenter - height/2, width, height);
        this.movementSpeed = movementSpeed;
        this.facing = facing;
    }

    public void draw(Batch batch, float deltaTime)
    {
        elapsedTime += deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            facing = 3;
            boyAtlas = new TextureAtlas("ManWalkLeft.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                    boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            facing = 1;
            boyAtlas = new TextureAtlas("ManWalkRight.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            facing = 2;
            boyAtlas = new TextureAtlas("ManWalkDown.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                    boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            facing = 0;
            boyAtlas = new TextureAtlas("ManWalkUp.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                    boundingBox.x, boundingBox.y);
        }
        else
        {
            if (facing == 0)
            {
                playerTexture = new Texture("ManFaceUp.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
            else if (facing == 1)
            {
                playerTexture = new Texture("ManFaceRight.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
            else if (facing == 2)
            {
                playerTexture = new Texture("ManFaceDown.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
            else if (facing == 3)
            {
                playerTexture = new Texture("ManFaceLeft.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
        }

    }

    public void translate(float xChange, float yChange)
    {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }
}
