package com.chessailab.game;

public class BoardSpot {

    private Piece piece;

    public BoardSpot() {
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece removePiece() {
        Piece temp = this.piece.copy();
        this.piece = null;
        return temp;
    }

    public boolean isEmpty() {
        if (piece == null) {
            return true;
        }
        return false;
    }
}
