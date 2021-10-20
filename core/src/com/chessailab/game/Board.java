package com.chessailab.game;

import com.sun.tools.javac.comp.Check;

public class Board {

    private Spot[][] board;

    private CheckersController cc;

    public Board(CheckersController cc) {
        this.cc = cc;
        newGame();
    }

    public Spot[][] getBoard() {
        return board;
    }

    public void setBoard(Spot[][] board) {
        this.board = board;
    }

    public void newGame() {
        int xwidth = 4;
        int ywidth = 8;
        this.board = new Spot[xwidth][ywidth];
        for(int i = 0; i < xwidth; i++){
            for(int j = 0; j < ywidth; j++) {
                Spot bs = new Spot();
                if (j < 3) {
                    bs.setPiece(new Piece(cc.getPlayer1()));
                } else if (j >= 5) {
                    bs.setPiece(new Piece(cc.getPlayer2()));
                }
                this.board[i][j] = bs;
            }
        }
    }

    public Spot getSpot(int x, int y) {
        return board[x][y];
    }
}
