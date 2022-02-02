package com.idk;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends Game
{
	GameScreen gameScreen;

	@Override
	public void create()
	{
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}
}
