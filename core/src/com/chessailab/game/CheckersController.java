package com.chessailab.game;

import java.util.ArrayList;

public class CheckersController {

    final int NORTHWEST = 0;
    final int NORTHEAST = 1;
    final int SOUTHWEST = 2;
    final int SOUTHEAST = 3;

    private GameState gameState;

    private String player1;

    private String player2;

    public CheckersController(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        newGame();
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return  player2;
    }

    public ArrayList<int[]> getPieces(String player) {
        ArrayList<int[]> pieces = new ArrayList<>();
        Board b = gameState.getBoard();
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 8; j++) {
                Spot bs = b.getSpot(i, j);
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

    public ArrayList<int[]> getMoves(int x, int y) {
        ArrayList<int[]> moves = new ArrayList<>();
        if (posExists(x, y) && !isEmpty(x, y)) {
            Piece p = getPiece(x, y);
            int sX = shiftX(x, y);
            int [] nw = {stillX(sX - 1, y + 1), y + 1};
            int [] ne = {stillX(sX + 1, y + 1), y + 1};
            int [] sw = {stillX(sX - 1, y - 1), y - 1};
            int [] se = {stillX(sX + 1, y - 1), y - 1};
            if (posExists(nw)) {
                if (isEmpty(nw)) {
                    if (p.getPlayer().equals(player1) || p.isKinged()) {
                        moves.add(nw);
                    }
                }
            }
            if (posExists(ne)) {
                if (isEmpty(ne)) {
                    if (p.getPlayer().equals(player1) || p.isKinged()) {
                        moves.add(ne);
                    }
                }
            }
            if (posExists(sw)) {
                if (isEmpty(sw)) {
                    if (p.getPlayer().equals(player2) || p.isKinged()) {
                        moves.add(sw);
                    }
                }
            }
            if (posExists(se)) {
                if (isEmpty(se)) {
                    if (p.getPlayer().equals(player2) || p.isKinged()) {
                        moves.add(se);
                    }
                }
            }
        }
        return moves;
    }

    public ArrayList<int[]> getJumps(int x, int y) {
        ArrayList<int[]> jumps = new ArrayList<>();
        if (posExists(x, y) && !isEmpty(x, y)) {
            Piece p = getPiece(x, y);
            int sX = shiftX(x, y);
            int [] nw = {stillX(sX - 2, y + 2), y + 2};
            int [] nwm = {stillX(sX - 1, y + 1), y + 1};
            int [] ne = {stillX(sX + 2, y + 2), y + 2};
            int [] nem = {stillX(sX + 1, y + 1), y + 1};
            int [] sw = {stillX(sX - 2, y - 2), y - 2};
            int [] swm = {stillX(sX - 1, y - 1), y - 1};
            int [] se = {stillX(sX + 2, y - 2), y - 2};
            int [] sem = {stillX(sX + 1, y - 1), y - 1};
            if(checkJump(p, player1, nw, nwm)) {
                jumps.add(nw);
            }
            if(checkJump(p, player1, ne, nem)) {
                jumps.add(ne);
            }
            if(checkJump(p, player2, sw, swm)) {
                jumps.add(sw);
            }
            if(checkJump(p, player2, se, sem)) {
                jumps.add(se);
            }

        }

        return jumps;
    }

    private boolean checkJump(Piece p, String player, int[] land, int[] jumped) {
        if(posExists(land)) { // check if jump position exists
            if(isEmpty(land) && !isEmpty(jumped)) { // check if there is a player to jump and the jump position is empty
                if(p.getPlayer().equals(player) || p.isKinged()) { // check movement rules
                    if (!getPiece(jumped).getPlayer().equals(p.getPlayer())) { // check if jumped piece is opposing team
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Piece getPiece(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        return getPiece(x, y);
    }

    public Piece getPiece(int x, int y) {
        Board b = gameState.getBoard();
        Piece p = b.getSpot(x, y).getPiece();
        return p;
    }

    public boolean move(int x, int y, int moveX, int moveY) {
        Board b = gameState.getBoard();
        if (validMove(x, y, moveX, moveY)) {
            b.getSpot(moveX, moveY).setPiece(b.getSpot(x, y).getPiece());
            b.getSpot(x, y).removePiece();
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
        Board b = gameState.getBoard();
        return b.getSpot(x, y).isEmpty();
    }

    public boolean isEmpty(int[] pos) {
        return isEmpty(pos[0], pos[1]);
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

    public void king(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        king(x, y);
    }

    public void king(int x, int y) {
        if(posExists(x, y)) {
            if (!isEmpty(x, y)) {
                getPiece(x, y).king();
            }
        }
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
        this.gameState = new GameState(this);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
