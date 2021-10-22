package com.chessailab.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ActionListener {

    CheckersController cc;

    UI ui;

    public ActionListener(CheckersController cc, UI ui) {
        this.cc = cc;
        this.ui = ui;
    }

    public void listen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (cc.checkMyTurn(cc.getPlayer1())) {
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
                cc.newGame();
            }

        }

    }
}
