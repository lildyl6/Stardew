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
    TextureAtlas playerAtlas;
    Animation<TextureRegion> playerAnimation;
    public int health;

    private float elapsedTime = 0f;
    public int jumpFrames = 0;
    public int swingFrames = 0;
    public int hitCoolDown = 0;
    public boolean canSwing = false;
    public boolean canEat = false;


    public Player(float xCenter, float yCenter, float width, float height, float movementSpeed,
                  int facing)
    {
        this.boundingBox = new Rectangle(xCenter - width/2, yCenter - height/2, width, height);
        this.movementSpeed = movementSpeed;
        this.facing = facing;
        health = 5;
    }

    public void doAction(String action)
    {
        if (action.equals("Jump"))
        {
            jumpFrames = 40;
        }
        else if (action.equals("Sword"))
        {
            swingFrames = 20;
        }
        else if (action.equals("Apple"))
        {
            if (health < 5)
            {
                this.health++;
            }
        }
    }

    public void handleHit()
    {
        if (hitCoolDown == 0)
        {
            this.health--;
            hitCoolDown = 100;
        }
    }



    public void draw(Batch batch, float deltaTime, float speed)
    {
        elapsedTime += deltaTime;
        float frameSpeed = (60/speed)*4;

        if (jumpFrames > 0)
        {
            drawJump(batch, jumpFrames, this.facing);
            jumpFrames--;
        }
        else if (swingFrames > 0)
        {
            drawSwing(batch, swingFrames, this.facing);
            swingFrames--;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            facing = 3;
            playerAtlas = new TextureAtlas("ManWalkLeft.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, playerAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / frameSpeed, true),
                    boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            facing = 1;
            playerAtlas = new TextureAtlas("ManWalkRight.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, playerAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / frameSpeed, true),
                boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            facing = 2;
            playerAtlas = new TextureAtlas("ManWalkDown.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, playerAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / frameSpeed, true),
                    boundingBox.x, boundingBox.y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            facing = 0;
            playerAtlas = new TextureAtlas("ManWalkUp.atlas");
            playerAnimation = new Animation<TextureRegion>(1f/30f, playerAtlas.getRegions());
            batch.draw(playerAnimation.getKeyFrame(elapsedTime / frameSpeed, true),
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

    public void drawSwing(Batch batch, int swingFrames, int facing)
    {
        float swordx = 0;
        float swordy = 0;
        Texture sword = new Texture("SwordL.png");
        boolean swordBehind = false;

        if (facing == 0)
        {
            playerAtlas = new TextureAtlas("ManSU.atlas");
            if (swingFrames <= 10)
            {
                Texture slash = new Texture("SlashU.png");
                batch.draw(slash, boundingBox.x - (1.0f*this.boundingBox.width),
                        boundingBox.y + (.4f*this.boundingBox.height));

                sword = new Texture("SwordL.png");
                swordx = -13;
                swordy = 4;
                swordBehind = true;
            }
            else
            {
                sword = new Texture("SwordR.png");
                swordx = 6;
                swordy = 6;
                swordBehind = true;
            }
        }
        else if (facing == 1)
        {
            playerAtlas = new TextureAtlas("ManSR.atlas");
            if (swingFrames <= 10)
            {
                Texture slash = new Texture("SlashR.png");
                batch.draw(slash, boundingBox.x + (.5f*this.boundingBox.width),
                        boundingBox.y - (.3f*this.boundingBox.height));

                sword = new Texture("SwordU.png");
                swordx = 0;
                swordy = 20;
                swordBehind = true;
            }
            else
            {
                sword = new Texture("SwordL.png");
                swordx = -4;
                swordy = 5;
            }
        }
        else if (facing == 2)
        {
            playerAtlas = new TextureAtlas("ManSD.atlas");
            if (swingFrames <= 10)
            {
                Texture slash = new Texture("SlashD.png");
                batch.draw(slash, boundingBox.x - (1f*this.boundingBox.width),
                        boundingBox.y - (.6f*this.boundingBox.height));

                sword = new Texture("SwordR.png");
                swordx = 13;
                swordy = 4;
            }
            else
            {
                sword = new Texture("SwordL.png");
                swordx = -5;
                swordy = 5;
            }
        }
        else // facing == 3
        {
            playerAtlas = new TextureAtlas("ManSL.atlas");
            if (swingFrames <= 10)
            {
                Texture slash = new Texture("SlashL.png");
                batch.draw(slash, boundingBox.x - (1.5f*this.boundingBox.width),
                        boundingBox.y - (.3f*this.boundingBox.height));

                sword = new Texture("SwordU.png");
                swordx = 0;
                swordy = 20;
                swordBehind = true;
            }
            else
            {
                sword = new Texture("SwordR.png");
                swordx = 4;
                swordy = 5;
            }
        }

        playerAnimation = new Animation<TextureRegion>(3, playerAtlas.getRegions());

        if (swordBehind)
        {
            batch.draw(sword, boundingBox.x + swordx, boundingBox.y + swordy);
            batch.draw(playerAnimation.getKeyFrame(swingFrames/4, true),
                    boundingBox.x, boundingBox.y);
        }
        else
        {
            batch.draw(playerAnimation.getKeyFrame(swingFrames / 4, true),
                    boundingBox.x, boundingBox.y);
            batch.draw(sword, boundingBox.x + swordx, boundingBox.y + swordy);
        }
    }

    public void drawJump(Batch batch, int jumpFrames, int facing)
    {
        if (facing == 0)
        {
            playerAtlas = new TextureAtlas("ManJU.atlas");
        }
        else if (facing == 1)
        {
            playerAtlas = new TextureAtlas("ManJR.atlas");
        }
        else if (facing == 2)
        {
            playerAtlas = new TextureAtlas("ManJD.atlas");
        }
        else // facing == 3
        {
            playerAtlas = new TextureAtlas("ManJL.atlas");
        }

        playerAnimation = new Animation<TextureRegion>(1, playerAtlas.getRegions());
        batch.draw(playerAnimation.getKeyFrame(jumpFrames/4, true),
                boundingBox.x, boundingBox.y);
    }

    public void translate(float xChange, float yChange)
    {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }
}
