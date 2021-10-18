package com.chessailab.game;

public class Piece {

    private final String player;

    private boolean kinged;

    public Piece(String player) {
        this.player = player;
        this.kinged = false;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isKinged() {
        return kinged;
    }

    public void king() {
        kinged = true;
    }

    public Piece copy() {
        Piece p = new Piece(this.player);
        if (this.kinged) {
            p.king();
        }
        return p;
    }


}
