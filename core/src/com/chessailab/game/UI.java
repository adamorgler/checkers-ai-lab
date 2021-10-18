package com.chessailab.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class UI {

    ShapeRenderer shapes;

    int spotWidth;
    int pieceRadius;

    private CheckersController cc;

    public UI(CheckersController cc) {
        this.shapes = new ShapeRenderer();
        this.shapes.setAutoShapeType(true);

        spotWidth = 40;
        pieceRadius = 16;

        this.cc = cc;
    }

    public void render() {
        ScreenUtils.clear(Color.DARK_GRAY);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        showBoard();
        showPieces();
        shapes.end();
    }

    private void showBoard() {
        int spots = 8;
        for(int i = 0; i < spots; i++) {
            for (int j = 0; j < spots; j++) {
                int xpos = i * spotWidth;
                int ypos = j * spotWidth;
                Color c = new Color();
                if (j % 2 == 0) {
                    if (i % 2 == 0) {
                        c.set(Color.RED);
                    } else {
                        c.set(Color.WHITE);
                    }
                } else {
                    if (i % 2 == 1) {
                        c.set(Color.RED);
                    } else {
                        c.set(Color.WHITE);
                    }
                }
                shapes.setColor(c);
                shapes.rect(xpos, ypos, spotWidth, spotWidth);
            }
        }
    }

    private void showPieces() {
        int xspots = 4;
        int yspots = 8;
        for(int i = 0; i < xspots; i++) {
            for (int j = 0; j < yspots; j++) {
                int xpos = i * spotWidth * 2 + (spotWidth / 2);
                int ypos = j * spotWidth + (spotWidth / 2);
                Color c = new Color();
                BoardSpot[][] board = cc.getBoard();
                if(!board[i][j].isEmpty()) {
                   Piece p = board[i][j].getPiece();
                   if (p.getPlayer().equals(cc.getPlayer1())) {
                       c.set(Color.RED);
                   } else {
                       c.set(Color.BLACK);
                   }
                   // alter position
                   if (j % 2 == 0) {
                       xpos += spotWidth;
                   }

                   shapes.setColor(c);
                   shapes.circle(xpos, ypos, pieceRadius);
                }
            }
        }
    }

    private void showSelection() {

    }

    private void showMoveOptions() {

    }
}
