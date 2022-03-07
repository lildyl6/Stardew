package com.idk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;


public class Inventory implements InputProcessor
{
    private Texture inventoryTexture;
    private Texture inventorySelectedTexture;
    public Array<Item> inv;
    public int selected;



    public Inventory()
    {
        inventoryTexture = new Texture("Inventory.png");
        inventorySelectedTexture = new Texture("InventorySelected.png");
        inv = new Array<Item>();
        selected = 0;
        Gdx.input.setInputProcessor(this);

        Texture t = new Texture("Blank.png");
        Item blank = new Item("Blank",16,16,t,t);
        for (int i = 0; i < 9; i ++)
        {
            inv.add(blank);
        }
    }

    public String getSelected()
    {
        if (selected > inv.size - 1)
        {
            return "Invalid";
        }
        else
        {
            return inv.get(selected).name;
        }
    }

    public void remove()
    {
        Texture b = new Texture("Blank.png");
        Item blank = new Item("Blank", 16, 16,b,b);
        inv.set(selected, blank);
    }

    public void draw(Batch batch, float x, float y)
    {
        batch.draw(inventoryTexture, x, y);
        batch.draw(inventorySelectedTexture, x + (selected*17), y);

        int index = 0;
        for (Item item : inv)
        {
            batch.draw(item.inventoryTexture, x + 1 + (index*17), y + 1);
            index++;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY == 1)
        {
            selected++;
            if (selected > 8)
            {
                selected = 0;
            }
        }
        else if (amountY == -1)
        {
            selected--;
            if (selected < 0)
            {
                selected = 8;
            }
        }

        return false;
    }
}
