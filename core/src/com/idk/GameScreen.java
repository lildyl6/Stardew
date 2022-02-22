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
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class GameScreen implements Screen
{
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private final int WORLD_WIDTH = 640;
    private final int WORLD_HEIGHT = 640;

    private Player player;
    //private Bat bat;
    public Array<Bat> bats;

    private int hardCodeLeft = 0;
    private int hardCodeRight = 0;
    private int hardCodeUp = 0;
    private int hardCodeDown = 0;

    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;
    private TiledMapTileLayer collisionLayer;

    Iterator<TiledMapTile> tiles;

    Array<StaticTiledMapTile> streamTilesH;
    Array<StaticTiledMapTile> streamTilesV;
    Array<StaticTiledMapTile> streamTilesUL;
    Array<StaticTiledMapTile> streamTilesUR;
    Array<StaticTiledMapTile> streamTilesDL;
    Array<StaticTiledMapTile> streamTilesDR;
    Array<StaticTiledMapTile> shrubTiles;

    AnimatedTiledMapTile animatedStreamTileH;
    AnimatedTiledMapTile animatedStreamTileV;
    AnimatedTiledMapTile animatedStreamTileUL;
    AnimatedTiledMapTile animatedStreamTileUR;
    AnimatedTiledMapTile animatedStreamTileDL;
    AnimatedTiledMapTile animatedStreamTileDR;
    AnimatedTiledMapTile animatedShrubTile;


    public GameScreen()
    {
        camera = new OrthographicCamera(10, 10);
        viewport = new StretchViewport(200,200,camera);

        player = new Player(300, 300, 16,32, 70, 2);

        bats = new Array<Bat>();
        //bat = new Bat(400, 400, 16, 16, 1);
        //bats.add(bat);

        batch = new SpriteBatch();

        map = new TmxMapLoader().load("ShrubMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(map, batch);
        collisionLayer = (TiledMapTileLayer)map.getLayers().get(0);

    }



    @Override
    public void render(float deltaTime)
    {
        camera.position.x = player.boundingBox.x + player.boundingBox.width/2;
        camera.position.y = player.boundingBox.y + player.boundingBox.height/2;
        camera.update();

        animateTiles();

        tmr.render();

        batch.begin();

        player.draw(batch, deltaTime, player.movementSpeed);

        doBats(deltaTime);

        doHealthBar();

        tmr.setView(camera);

        detectInput(deltaTime);

        checkForShrubs();

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        }
    }

    private void doHealthBar()
    {
        Texture heart = new Texture("Heart.png");
        int i = 0;
        while (i < player.health)
        {
            batch.draw(heart, player.boundingBox.x - 93 +(i*11),
                player.boundingBox.y + 100, 16, 16);
            i++;
        }
        if (player.hitCoolDown > 0)
        {
            player.hitCoolDown--;
        }
    }

    private void doBats(float deltaTime)
    {
        int index = 0;
        for (Bat b : bats)
        {
            b.handle(batch, deltaTime, player);
            detectBatKill(b, index);
            index++;
        }
    }

    private void detectBatKill(Bat bat, int index)
    {
        Rectangle man = new Rectangle(player.boundingBox.x, player.boundingBox.y,
                player.boundingBox.width, player.boundingBox.height - 20);
        Rectangle top = new Rectangle(player.boundingBox.x - player.boundingBox.width,
                player.boundingBox.y + man.height, 48, 16);
        Rectangle right = new Rectangle(player.boundingBox.x + player.boundingBox.width,
                player.boundingBox.y - (player.boundingBox.height/2), 16, 64);
        Rectangle bottom = new Rectangle(player.boundingBox.x - player.boundingBox.width,
                player.boundingBox.y - (player.boundingBox.height/2), 48, 16);
        Rectangle left = new Rectangle(player.boundingBox.x - player.boundingBox.width,
                player.boundingBox.y - (player.boundingBox.height/2), 16, 64);

        if (bat.boundinBox.overlaps(man))
        {
            player.handleHit();
            if (player.health == 0)
            {
                player = new Player(300, 300, 16,32, 70, 2);
            }
        }
        if (player.facing == 0)
        {
            if (bat.boundinBox.overlaps(top) && player.swingFrames == 10)
            {
                bat.vec = new Vector2(0,6);
                bat.health--;
            }
        }
        if (player.facing == 1)
        {
            if (bat.boundinBox.overlaps(right) && player.swingFrames == 10)
            {
                bat.vec = new Vector2(6,0);
                bat.health--;
            }
        }
        if (player.facing == 2)
        {
            if (bat.boundinBox.overlaps(bottom) && player.swingFrames == 10)
            {
                bat.vec = new Vector2(0,-6);
                bat.health--;
            }
        }
        if (player.facing == 3)
        {
            if (bat.boundinBox.overlaps(left) && player.swingFrames == 10)
            {
                bat.vec = new Vector2(-6,0);
                bat.health--;
            }
        }

        if (bat.health == 0)
        {
            bats.removeIndex(index);
        }
    }

    private void checkForShrubs()
    {
        int x = (int)(player.boundingBox.x + player.boundingBox.width/2)/16;
        int y = (int)(player.boundingBox.y + player.boundingBox.height/3)/16;
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        if (layer.getCell(x, y).getTile().getProperties().containsKey("Animation"))
        {
            if (layer.getCell(x, y).getTile().getProperties().get("Animation").equals("Shrub")
                && player.jumpFrames < 1)
            {
                final TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                cell.setTile(shrubTiles.get(1));

                Timer timer = new Timer();
                timer.scheduleTask(new Timer.Task()
                {
                    @Override
                    public void run()
                    {
                        cell.setTile(shrubTiles.get(0));
                    }
                }, .5f);

            }
        }
    }


    private void animateTiles()
    {
        streamTilesH = new Array<StaticTiledMapTile>(2);
        streamTilesV = new Array<StaticTiledMapTile>(2);
        streamTilesUL = new Array<StaticTiledMapTile>(2);
        streamTilesUR = new Array<StaticTiledMapTile>(2);
        streamTilesDL = new Array<StaticTiledMapTile>(2);
        streamTilesDR = new Array<StaticTiledMapTile>(2);
        shrubTiles = new Array<StaticTiledMapTile>(2);



        tiles = map.getTileSets().getTileSet(0).iterator();
        while (tiles.hasNext())
        {
            TiledMapTile tile = tiles.next();

            if (tile.getProperties().containsKey("Animation"))
            {
                if (tile.getProperties().get("Animation", String.class).equals("StreamH"))
                {
                    streamTilesH.add((StaticTiledMapTile)tile);
                }
                else if (tile.getProperties().get("Animation", String.class).equals("StreamV"))
                {
                    streamTilesV.add((StaticTiledMapTile)tile);
                }
                else if (tile.getProperties().get("Animation", String.class).equals("StreamUL"))
                {
                    streamTilesUL.add((StaticTiledMapTile)tile);
                }
                else if (tile.getProperties().get("Animation", String.class).equals("StreamUR"))
                {
                    streamTilesUR.add((StaticTiledMapTile)tile);
                }
                else if (tile.getProperties().get("Animation", String.class).equals("StreamDL"))
                {
                    streamTilesDL.add((StaticTiledMapTile)tile);
                }
                else if (tile.getProperties().get("Animation", String.class).equals("StreamDR"))
                {
                    streamTilesDR.add((StaticTiledMapTile)tile);
                }
                else if (tile.getProperties().get("Animation", String.class).equals("Shrub"))
                {
                    shrubTiles.add((StaticTiledMapTile)tile);
                }
            }
        }

        streamTilesUL.reverse();
        streamTilesUR.reverse();
        streamTilesDL.reverse();
        streamTilesDR.reverse();

        animatedStreamTileH = new AnimatedTiledMapTile(1/3f, streamTilesH);
        animatedStreamTileV = new AnimatedTiledMapTile(1/3f, streamTilesV);
        animatedStreamTileUL = new AnimatedTiledMapTile(1/3f, streamTilesUL);
        animatedStreamTileUR = new AnimatedTiledMapTile(1/3f, streamTilesUR);
        animatedStreamTileDL = new AnimatedTiledMapTile(1/3f, streamTilesDL);
        animatedStreamTileDR = new AnimatedTiledMapTile(1/3f, streamTilesDR);
        animatedShrubTile = new AnimatedTiledMapTile(1/3f, shrubTiles);

        animatedStreamTileH.getProperties().put("Blocked", 0);
        animatedStreamTileV.getProperties().put("Blocked", 0);
        animatedStreamTileUL.getProperties().put("Blocked", 0);
        animatedStreamTileUR.getProperties().put("Blocked", 0);
        animatedStreamTileDL.getProperties().put("Blocked", 0);
        animatedStreamTileDR.getProperties().put("Blocked", 0);

        animatedStreamTileV.getProperties().put("Jumpable", 0);
        animatedStreamTileH.getProperties().put("Jumpable", 0);


        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);

        for (int x = 0; x < layer.getWidth(); x++)
        {
            for (int y = 0; y < layer.getHeight(); y++)
            {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell.getTile().getProperties().containsKey("Animation"))
                {
                    if (cell.getTile().getProperties().get("Animation",
                            String.class).equals("StreamH"))
                    {
                        cell.setTile(animatedStreamTileH);
                    }
                    else if (cell.getTile().getProperties().get("Animation",
                            String.class).equals("StreamV"))
                    {
                        cell.setTile(animatedStreamTileV);
                    }
                    else if (cell.getTile().getProperties().get("Animation",
                            String.class).equals("StreamUL"))
                    {
                        cell.setTile(animatedStreamTileUL);
                    }
                    else if (cell.getTile().getProperties().get("Animation",
                            String.class).equals("StreamUR"))
                    {
                        cell.setTile(animatedStreamTileUR);
                    }
                    else if (cell.getTile().getProperties().get("Animation",
                            String.class).equals("StreamDL"))
                    {
                        cell.setTile(animatedStreamTileDL);
                    }
                    else if (cell.getTile().getProperties().get("Animation",
                            String.class).equals("StreamDR"))
                    {
                        cell.setTile(animatedStreamTileDR);
                    }
                }
            }
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
            TiledMapTileLayer.Cell topLeft = collisionLayer.getCell((xleft + 1)/16, (yup + 1)/16);
            TiledMapTileLayer.Cell topRight = collisionLayer.getCell(xright/16, (yup + 1)/16);

            if (topLeft.getTile().getProperties().containsKey("Jumpable")
                    && topRight.getTile().getProperties().containsKey("Jumpable")
                    && player.getJumpFrames() > 25)
            {
                hardCodeUp = 25;
                return true;
            }
            else if (topLeft.getTile().getProperties().containsKey("Blocked")
                || topRight.getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }
        else if (facing == 1)
        {
            TiledMapTileLayer.Cell topRight = collisionLayer.getCell((xright + 1)/16, yup/16);
            TiledMapTileLayer.Cell bottomRight = collisionLayer.getCell((xright + 1)/16, (ydown + 1)/16);

            if (topRight.getTile().getProperties().containsKey("Jumpable")
                && bottomRight.getTile().getProperties().containsKey("Jumpable")
                    && player.getJumpFrames() > 25)
            {
                hardCodeRight = 25;
                return true;
            }
            else if (topRight.getTile().getProperties().containsKey("Blocked")
                || bottomRight.getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }
        else if (facing == 2)
        {
            TiledMapTileLayer.Cell bottomLeft = collisionLayer.getCell((xleft + 1)/16, (ydown - 0)/16);
            TiledMapTileLayer.Cell bottomRight = collisionLayer.getCell(xright/16, (ydown - 0)/16);

            if (bottomLeft.getTile().getProperties().containsKey("Jumpable")
                && bottomRight.getTile().getProperties().containsKey("Jumpable")
                && player.getJumpFrames() > 25)
            {
                hardCodeDown = 25;
                return true;
            }
            else if (bottomLeft.getTile().getProperties().containsKey("Blocked")
                || bottomRight.getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }
        }
        else // facing == 3
        {
            TiledMapTileLayer.Cell topLeft = collisionLayer.getCell((xleft + 0)/16, yup/16);
            TiledMapTileLayer.Cell bottomLeft = collisionLayer.getCell((xleft + 0)/16, (ydown + 1)/16);

            if (topLeft.getTile().getProperties().containsKey("Jumpable")
                    && bottomLeft.getTile().getProperties().containsKey("Jumpable")
                    && player.getJumpFrames() > 25)
            {
                hardCodeLeft = 25;
                return true;
            }
            else if (topLeft.getTile().getProperties().containsKey("Blocked")
                || bottomLeft.getTile().getProperties().containsKey("Blocked"))
            {
                return false;
            }

        }

        return true;
    }

    private void detectInput(float deltaTime)
    {
        if (player.swingFrames > 0)
        {
            // dont move
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            Bat b = new Bat(400, 400, 16, 16, 1);
            bats.add(b);
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.A) && canMove(3))
            || hardCodeLeft > 0)
        {
            if (hardCodeLeft >  0)
            {
                float xchange = player.movementSpeed * deltaTime;
                player.translate(-xchange, 0f);
                updateCamera(-xchange, 0f);
                hardCodeLeft--;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W) && canMove(0))
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
        else if ((Gdx.input.isKeyPressed(Input.Keys.D) && canMove(1))
                    || hardCodeRight > 0)
        {
            if (hardCodeRight >  0)
            {
                float xchange = player.movementSpeed * deltaTime;
                player.translate(xchange, 0f);
                updateCamera(xchange, 0f);
                hardCodeRight--;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W) && canMove(0))
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
        else if ((Gdx.input.isKeyPressed(Input.Keys.W) && canMove(0))
                    || hardCodeUp > 0)
        {
            if (hardCodeUp >  0)
            {
                float ychange = player.movementSpeed * deltaTime;
                player.translate(0f, ychange);
                updateCamera(0f, ychange);
                hardCodeUp--;
            }
            else
            {
                float yChange = player.movementSpeed * deltaTime;
                player.translate(0f, yChange);
                updateCamera(0f, yChange);
            }
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.S) && canMove(2))
                    || hardCodeDown > 0)
        {
            if (hardCodeDown > 0)
            {
                float yChange = player.movementSpeed * deltaTime;
                player.translate(0f, -yChange);
                updateCamera(0f, -yChange);
                hardCodeDown--;
            }
            else
            {
                float yChange = player.movementSpeed * deltaTime;
                player.translate(0f, -yChange);
                updateCamera(0f, -yChange);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
        {
            player.movementSpeed = 100;
        }
        else
        {
            player.movementSpeed = 70;
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
