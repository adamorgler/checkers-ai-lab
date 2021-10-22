package com.chessailab.game;

public class Spot {

    private Piece piece;

    public Spot() {
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }

    public boolean isEmpty() {
        if (piece == null) {
            return true;
        }
        return false;
    }
}
