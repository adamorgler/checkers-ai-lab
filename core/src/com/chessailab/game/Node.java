package com.chessailab.game;

public class Node {

    private GameState gameState;

    private final int x;

    private final int y;

    public Node(GameState gameState, int x, int y) {
        this.gameState = gameState;
        this.x = x;
        this.y = y;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
