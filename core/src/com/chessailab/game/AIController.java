package com.chessailab.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Queue;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.jdi.LockObject;
import org.graalvm.compiler.nodes.virtual.LockState;

import java.util.ArrayList;
import java.util.Random;

public class AIController implements Runnable{

    private CheckersController cc;

    private String player;

    public AIController(String player, CheckersController cc) {
        this.cc = cc;
        this.player = player;
    }

    @Override
    public void run() {
        random();
    }

    private void random() {
        GameState gs = cc.getGameState();
        ArrayList<int[]> moves = cc.getPlayerMoves(player);
        ArrayList<GameState> states = new ArrayList<>();
        for(int[] m : moves) {
            int x = m[0];
            int y = m[1];
            int moveX = m[2];
            int moveY = m[3];
            GameState next = cc.checkMove(x, y, moveX, moveY);
            if (next.getTurn() == gs.getTurn()) { // search for jump chains to add
                Queue<GameState> jumpQueue = new Queue<>();
                jumpQueue.addLast(next);
                while(!jumpQueue.isEmpty()) {
                    GameState nextJump = jumpQueue.get(0);
                    x = moveX;
                    y = moveY;
                    CheckersController newCC = new CheckersController(cc.getPlayer1(), cc.getPlayer2(), nextJump);
                    ArrayList<int[]> jumps = newCC.getJumps(x, y);
                    for(int[] j : jumps) {
                        moveY = j[0];
                        moveX = j[1];
                        GameState check = newCC.checkMove(x,y, moveX, moveY);
                        if (check.getTurn() == gs.getTurn()) {
                            jumpQueue.addLast(check);
                        } else {
                            states.add(check);
                        }
                    }
                }
            } else {
                states.add(next);
            }
        }
        Random rand = new Random(System.nanoTime());
        int i = rand.nextInt(states.size());
        cc.setAIMove(states.get(i));
    }

    private int evalutaion(GameState gs) {
        int score = 0;
        return 0;
    }
}
