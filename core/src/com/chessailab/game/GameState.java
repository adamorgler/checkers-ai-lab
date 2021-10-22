package com.chessailab.game;

import com.sun.tools.javac.comp.Check;

public class GameState {

    private Board board;

    private int turn;

    private boolean running;

    public GameState(CheckersController cc) {
        this.board = new Board(cc.getPlayer1(), cc.getPlayer2());
        this.turn = 0;
        this.running = true;
    }

    public GameState(Board b, int turn) {
        this.board = b;
        this.turn = turn;
        this.running = true;
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
        running = true;
    }

    public void endGame() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
