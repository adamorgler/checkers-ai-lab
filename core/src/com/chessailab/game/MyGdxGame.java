package com.chessailab.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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

// test code for double jumps
//		Board b = new Board(cc.getPlayer1(), cc.getPlayer2());
//		b.emptyBoard();
//		b.getSpot(1, 1).setPiece(new Piece(cc.getPlayer1()));
//		b.getSpot(1, 2).setPiece(new Piece(cc.getPlayer1()));
//		b.getSpot(3, 3).setPiece(new Piece(cc.getPlayer1()));
//		b.getSpot(3, 2).setPiece(new Piece(cc.getPlayer1()));
//		b.getSpot(2, 4).setPiece(new Piece(cc.getPlayer2()));
//		GameState gs = new GameState(b, 0);
//		cc.setAIMove(gs);

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
