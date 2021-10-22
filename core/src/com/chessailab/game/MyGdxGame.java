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
		String p1 = "red";
		String p2 = "black";

		cc = new CheckersController(p1, p2);
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
