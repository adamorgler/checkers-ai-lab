package com.chessailab.game;

import java.util.ArrayList;

public class CheckersController {

    final int NORTHWEST = 0;
    final int NORTHEAST = 1;
    final int SOUTHWEST = 2;
    final int SOUTHEAST = 3;

    private BoardSpot[][] board;

    private String player1;

    private String player2;

    public CheckersController(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        newGame();
    }

    public BoardSpot[][] getBoard() {
        return board;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return  player2;
    }

    public ArrayList<int[]> getPieces(String player) {
        ArrayList<int[]> pieces = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 8; j++) {
                BoardSpot bs = board[i][j];
                if (!bs.isEmpty()) {
                    Piece p = bs.getPiece();
                    if (p.getPlayer().equals(player)) {
                        pieces.add(new int[]{i, j});
                    }
                }
            }
        }
        return pieces;
    }

    public int[][] getMoves(int x, int y) {
        int[][] moves = new int[4][2];
        if (posExists(x, y)) {
            int sX = shiftX(x, y);
            int [] nw = {stillX(sX - 1, y + 1), y + 1};
            int [] ne = {stillX(sX + 1, y + 1), y + 1};
            int [] sw = {stillX(sX - 1, y - 1), y - 1};
            int [] se = {stillX(sX + 1, y - 1), y - 1};
            if (isEmpty(nw) && posExists(nw)) {
                moves[NORTHWEST] = nw;
            }
            if (isEmpty(ne) && posExists(ne)) {
                moves[NORTHEAST] = ne;
            }
            if (isEmpty(sw) && posExists(sw)) {
                moves[SOUTHWEST] = sw;
            }
            if (isEmpty(se) && posExists(se)) {
                moves[SOUTHEAST] = se;
            }
        }
        return moves;
    }

    public int[][] getJumps(int x, int y) {
        return null;
    }

    public boolean move(int x, int y, int moveX, int moveY) {
        if (validMove(x, y, moveX, moveY)) {
            board[moveX][moveY].setPiece(board[x][y].getPiece());
            board[x][y].removePiece();
            return true;
        }
        return false;
    }
    public boolean move(int[] pos, int[] move) {
        int x = pos[0];
        int y = pos[1];
        int mX = move[0];
        int mY = move[1];
        return move(x, y, mX, mY);
    }

    public boolean isEmpty(int x, int y) {
        return board[x][y].isEmpty();
    }

    public boolean isEmpty(int[] pos) {
        return board[pos[0]][pos[1]].isEmpty();
    }

    public boolean posExists(int x, int y) {
        if (x >= 0 && x < 4 && y >= 0 && y < 8) {
            return true;
        }
        return false;
    }

    public boolean posExists(int[] pos) {
        if(pos[0] >= 0 && pos[0] < 4 && pos[1] >= 0 && pos[1] < 8) {
            return true;
        }
        return false;
    }

    public boolean validMove(int x, int y, int moveX, int moveY) {
        if (posExists(x, y) && posExists(moveX, moveY) && isEmpty(moveX, moveY)) {
            return true;
        }
        return false;
    }

    // shifts xpos to a more manageble coordinate.
    // removes "zigzagging" in matrix allowing for simple cardinal move translation
    private int shiftX(int x, int y) {
        if (y % 2 == 0) {
            return (x * 2) + 1;
        }
        return (x * 2);
    }

    // returns x to the origional board position
    private int stillX(int x, int y) {
        if (y % 2 == 0) {
            return (x - 1) / 2;
        }
        return x / 2;
    }
    private void newGame() {
        int xwidth = 4;
        int ywidth = 8;
        this.board = new BoardSpot[xwidth][ywidth];
        for(int i = 0; i < xwidth; i++){
            for(int j = 0; j < ywidth; j++) {
                BoardSpot bs = new BoardSpot();
                if (j < 3) {
                    bs.setPiece(new Piece(player1));
                } else if (j >= 5) {
                    bs.setPiece(new Piece(player2));
                }
                this.board[i][j] = bs;
            }
        }
    }
}
