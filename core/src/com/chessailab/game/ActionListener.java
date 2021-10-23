package com.chessailab.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.concurrent.TimeUnit;

public class ActionListener {

    CheckersController cc;

    UI ui;

    Thread t;

    public ActionListener(CheckersController cc, UI ui) {
        this.cc = cc;
        this.ui = ui;
        this.t = new Thread();
    }

    public void listen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (!cc.aiRunning()) {
                // UP
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    ui.moveSelection(UI.UP);
                }
                // DOWN
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    ui.moveSelection(UI.DOWN);
                }
                // RIGHT
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    ui.moveSelection(UI.RIGHT);
                    ui.moveMoveSelection(UI.RIGHT);
                }
                // LEFT
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    ui.moveSelection(UI.LEFT);
                    ui.moveMoveSelection(UI.LEFT);
                }
                // ENTER
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    if (ui.selected) {
                        ui.enterMove();
                    } else {
                        ui.select();
                    }
                }
                // BACKSPACE
                if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                    if (!cc.isJumped()) {
                        ui.deselect();
                    }
                }
            }

            // n
            if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                try {
                    cc.stop();
                    TimeUnit.MILLISECONDS.sleep(100);
                    cc.newGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            // 1
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                cc.newGame();
                AIController ai1 = cc.getAi1();
                AIController ai2 = cc.getAi2();
                AIGame g = new AIGame(ai1, ai2, cc);
                t = new Thread(g);
                t.start();
            }

        }

    }
}
