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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

    private final int WORLD_WIDTH = 640;
    private final int WORLD_HEIGHT = 640;

    private Player player;

    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;
    private TiledMapTileLayer collisionLayer;


    public GameScreen()
    {
        camera = new OrthographicCamera(10, 10);
        viewport = new StretchViewport(200,200,camera);

        player = new Player(100, 100, 16,32, 70, 2);

        batch = new SpriteBatch();

        map = new TmxMapLoader().load("MegaMapCollision.tmx");
        tmr = new OrthogonalTiledMapRenderer(map, batch);
        collisionLayer = (TiledMapTileLayer)map.getLayers().get(0);
    }


    @Override
    public void render(float deltaTime)
    {
        tmr.render();

        batch.begin();

        player.draw(batch, deltaTime);

        tmr.setView(camera);

        detectInput(deltaTime);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }
    }

    private boolean canMove(int facing)
    {
        int xright = (int)player.boundingBox.x + (int)player.boundingBox.width - 1;
        int xleft = (int)player.boundingBox.x;
        int yup = (int)player.boundingBox.y + (int)player.boundingBox.height/2;
        int ydown = (int)player.boundingBox.y + 2;

        if (facing == 0)
        {
            if (collisionLayer.getCell(xright/16,
                    (yup + 1)/16).getTile().getProperties().containsKey("Blocked")
                || collisionLayer.getCell((xleft + 1)/16,
                    (yup + 1)/16).getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }
        else if (facing == 1)
        {
            if (collisionLayer.getCell((xright+ 1)/16,
                    yup/16).getTile().getProperties().containsKey("Blocked")
                || collisionLayer.getCell((xright + 1)/16,
                    (ydown + 1)/16).getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }
        else if (facing == 2)
        {
            if (collisionLayer.getCell((xleft + 1)/16,
                    ydown/16).getTile().getProperties().containsKey("Blocked")
                || collisionLayer.getCell(xright/16,
                    ydown/16).getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }
        else // facing == 3
        {
            if (collisionLayer.getCell(xleft/16,
                    yup/16).getTile().getProperties().containsKey("Blocked")
                || collisionLayer.getCell(xleft/16,
                    (ydown + 1)/16).getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }

        return true;
    }

    private void detectInput(float deltaTime)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.A) && canMove(3))
        {
            if (Gdx.input.isKeyPressed(Input.Keys.W) && canMove(0))
            {
                float bothChange = player.movementSpeed * deltaTime/1.414f;
                player.translate(-bothChange, bothChange);
                updateCamera(-bothChange, bothChange);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S) && canMove(2))
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
        else if (Gdx.input.isKeyPressed(Input.Keys.D) && canMove(1))
        {
            if (Gdx.input.isKeyPressed(Input.Keys.W) && canMove(0))
            {
                float bothChange = player.movementSpeed * deltaTime/1.414f;
                player.translate(bothChange, bothChange);
                updateCamera(bothChange, bothChange);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S) && canMove(2))
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
        else if (Gdx.input.isKeyPressed(Input.Keys.W) && canMove(0))
        {
            if (canMove(0))
            {
                float yChange = player.movementSpeed * deltaTime;
                player.translate(0f, yChange);
                updateCamera(0f, yChange);
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && canMove(2))
        {
            float yChange = player.movementSpeed * deltaTime;
            player.translate(0f, -yChange);
            updateCamera(0f, -yChange);
        }
    }

    private void updateCamera(float xChange, float yChange)
    {
            camera.translate(xChange, yChange);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
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
        map.dispose();
        tmr.dispose();
        batch.dispose();
    }
}
