package com.chessailab.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ActionListener {

    CheckersController cc;

    UI ui;

    int wait;
    int waitCount;

    public ActionListener(CheckersController cc, UI ui) {
        this.cc = cc;
        this.ui = ui;
        int wait = 10;
        int waitCount = 0;
    }

    public void listen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            // UP
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                ui.moveSelection(UI.UP);
                ui.moveMoveSelection(UI.UP);
            }
            // DOWN
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                ui.moveSelection(UI.DOWN);
                ui.moveMoveSelection(UI.DOWN);
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
                ui.select();
            }
            // BACKSPACE
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                ui.deselect();
            }

        }

    }
}
