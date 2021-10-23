package com.chessailab.game;

import java.util.concurrent.TimeUnit;

public class AIGame implements Runnable{

    private AIController ai1;

    private AIController ai2;

    private CheckersController cc;

    public AIGame(AIController ai1, AIController ai2, CheckersController cc) {
        this.ai1 = ai1;
        this.ai2 = ai2;
        this.cc = cc;
        this.ai1.setAiOnly();
        this.ai2.setAiOnly();
        this.ai1.setEvFunction(0);
        this.ai1.setEvFunction(0);
    }

    @Override
    public void run() {
        while(cc.isRunning()) {
            if (cc.checkMyTurn(cc.getPlayer1())) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    ai1.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    ai2.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
