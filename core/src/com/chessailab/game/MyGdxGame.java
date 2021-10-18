package com.chessailab.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	CheckersController cc;
	UI ui;
	
	@Override
	public void create () {
		cc = new CheckersController("p1", "p2");
		int[] pos = {1, 2};
		int[][] moves = cc.getMoves(pos[0], pos[1]);
		cc.move(pos, moves[cc.NORTHWEST]);
		ui = new UI(cc);
	}

	@Override
	public void render () {
		ui.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
