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
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            facing = 3;
            boyAtlas = new TextureAtlas("walkLeft.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            facing = 1;
            boyAtlas = new TextureAtlas("walkRight.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            facing = 2;
            boyAtlas = new TextureAtlas("walkDown.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                    boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            facing = 0;
            boyAtlas = new TextureAtlas("walkUp.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, boyAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / 4, true),
                    boundingBox.x, boundingBox.y);
        }

        else
        {
            if (facing == 0)
            {
                playerTexture = new Texture("faceUp.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
            else if (facing == 1)
            {
                playerTexture = new Texture("faceRight.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
            else if (facing == 2)
            {
                playerTexture = new Texture("faceDown.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }
            else if (facing == 3)
            {
                playerTexture = new Texture("faceLeft.png");
                batch.draw(playerTexture, boundingBox.x, boundingBox.y);
            }

        }

    }

    public void translate(float xChange, float yChange)
    {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }
}
