package com.chessailab.game;

public class GameState {

    private CheckersController cc;

    private Board board;

    private int turn;

    public GameState(CheckersController cc) {
        this.cc = cc;
        this.board = new Board(cc);
        this.turn = 0;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void nextTurn() {
        turn++;
    }

    public void newGame() {
        turn = 0;
        board.newGame();
    }
}
