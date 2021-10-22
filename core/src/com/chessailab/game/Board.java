package com.chessailab.game;

import com.sun.tools.javac.comp.Check;

public class Board {

    private Spot[][] board;

    private String player1;

    private String player2;

    public Board(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        newGame();
    }

    public Board(String player1, String player2, Spot[][] board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
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
                    bs.setPiece(new Piece(player1));
                } else if (j >= 5) {
                    bs.setPiece(new Piece(player2));
                }
                this.board[i][j] = bs;
            }
        }
    }

    public void emptyBoard() {
        int xwidth = 4;
        int ywidth = 8;
        this.board = new Spot[xwidth][ywidth];
        for(int i = 0; i < xwidth; i++){
            for(int j = 0; j < ywidth; j++) {
                Spot bs = new Spot();
                this.board[i][j] = bs;
            }
        }
    }

    public Spot[][] getBoard() {
        int xwidth = 4;
        int ywidth = 8;
        Spot[][] b = new Spot[4][8];
        for(int i = 0; i < xwidth; i++) {
            for (int j = 0; j < ywidth; j++) {
                Spot bs = new Spot();
                bs.setPiece(board[i][j].getPiece());
                b[i][j] = bs;
            }
        }
        return b;
    }

    public Spot getSpot(int x, int y) {
        return board[x][y];
    }

    public Spot getSpot(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        return getSpot(x, y);
    }
}
