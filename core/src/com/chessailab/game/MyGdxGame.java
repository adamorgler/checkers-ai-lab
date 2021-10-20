package com.chessailab.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.*;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	CheckersController cc;
	UI ui;
	ActionListener al;
	
	@Override
	public void create () {
		cc = new CheckersController("red", "black");
		cc.move(0, 2, 0, 3);
		cc.move(1, 5, 0, 4);
		cc.move(1, 2, 1, 3);
		ui = new UI(cc);
		al = new ActionListener(cc, ui);
	}

	@Override
	public void render () {
		al.listen();
		ui.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
