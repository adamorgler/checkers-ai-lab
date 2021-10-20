package com.chessailab.game;

import com.sun.tools.javac.comp.Check;

public class AIController implements Runnable{

    private CheckersController cc;

    private String player;

    public AIController(String player, CheckersController cc) {
        this.cc = cc;
        this.player = player;
    }
    @Override
    public void run() {

    }
}
