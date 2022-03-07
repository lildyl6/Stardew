package com.idk;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Item
{
    public String name;
    public Rectangle boundingbox;
    public Texture itemTexture;
    public Texture inventoryTexture;


    public Item(String name, float x, float y, Texture itemTexture, Texture inventoryTexture)
    {
        this.name = name;
        this.boundingbox = new Rectangle(x, y, 16, 16);
        this.itemTexture = itemTexture;
        this.inventoryTexture = inventoryTexture;
    }

    public void draw(Batch batch)
    {
        batch.draw(itemTexture, boundingbox.x, boundingbox.y, 12, 12);
    }


}
