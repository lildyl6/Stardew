package com.idk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen
{
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture background;

    private final int WORLD_WIDTH = 640;
    private final int WORLD_HEIGHT = 640;

    private Player player;

    // hardcode crap
    boolean bombExists = false;
    boolean bombCreated = false;
    boolean canPlaceBomb = true;
    int i = 100;
    Bomb bomb;


    public GameScreen()
    {
        camera = new OrthographicCamera(20, 20);
        viewport = new StretchViewport(300,300,camera);
        background = new Texture("largeMap.png");

        player = new Player(140, 150, 16,32, 50, 2);

        batch = new SpriteBatch();
    }


    @Override
    public void render(float deltaTime)
    {
        batch.begin();

        //detectInput(deltaTime);



        batch.draw(background, -400, -400);

        player.draw(batch, deltaTime);

        if (bombExists)
        {
            bomb = getBomb(deltaTime);
            bombCreated = true;
            bombExists = false;
            canPlaceBomb = false;
        }
        if (bombCreated && i > 0)
        {
            bomb.draw(batch, deltaTime, i);
            i--;
        }
        else
        {
            i = 100;
            bombCreated = false;
            canPlaceBomb = true;
        }

        detectInput(deltaTime);

        batch.end();
    }

    public Bomb getBomb(float deltaTime)
    {
        Bomb bomb;
        if (player.facing == 0)
        {
            bomb = new Bomb(player.boundingBox.x,
                    player.boundingBox.y + player.boundingBox.height -2, 0);
        }
        else if (player.facing == 1)
        {
            bomb = new Bomb(player.boundingBox.x + player.boundingBox.width -1,
                    player.boundingBox.y, 1);
        }
        else if (player.facing == 2)
        {
            bomb = new Bomb(player.boundingBox.x,
                    player.boundingBox.y - player.boundingBox.height/2, 2);
        }
        else  // player.facing == 3
        {
            bomb = new Bomb(player.boundingBox.x - player.boundingBox.width,
                    player.boundingBox.y, 3);
        }

        //bombExists = false;
        bomb.draw(batch, deltaTime, i);
        return bomb;
    }

    private void detectInput(float deltaTime)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canPlaceBomb)
        {
            bombExists = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
            {
                float bothChange = player.movementSpeed * deltaTime/1.414f;
                player.translate(-bothChange, bothChange);
                updateCamera(-bothChange, bothChange);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            {
                float bothChange = player.movementSpeed * deltaTime/1.414f;
                player.translate(-bothChange, -bothChange);
                updateCamera(-bothChange, -bothChange);
            }
            else
            {
                float xChange = player.movementSpeed * deltaTime;
                player.translate(-xChange, 0f);
                updateCamera(-xChange, 0f);
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
            {
                float bothChange = player.movementSpeed * deltaTime/1.414f;
                player.translate(bothChange, bothChange);
                updateCamera(bothChange, bothChange);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            {
                float bothChange = player.movementSpeed * deltaTime/1.414f;
                player.translate(bothChange, -bothChange);
                updateCamera(bothChange, -bothChange);
            }
            else
            {
                float xChange = player.movementSpeed * deltaTime;
                player.translate(xChange, 0f);
                updateCamera(xChange, 0f);
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
                float yChange = player.movementSpeed * deltaTime;
                player.translate(0f, yChange);
                updateCamera(0f, yChange);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
                float yChange = player.movementSpeed * deltaTime;
                player.translate(0f, -yChange);
                updateCamera(0f, -yChange);
        }
        else
        {

        }
    }

    private void updateCamera(float xChange, float yChange)
    {
            camera.translate(xChange, yChange);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
    }

    public void setBombExists(boolean bool)
    {
        bombExists = bool;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
