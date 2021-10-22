package com.chessailab.game;

import com.badlogic.gdx.utils.Queue;

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
        minimaxStart(5);
    }



    private void alphaBeta() {
        GameState gs = cc.getGameState();

    }

    private void minimaxStart(int depth) {
        GameState gs = cc.getGameState();
        ArrayList<int[]> moves = cc.getPlayerMoves(player);
        ArrayList<GameState> states = nextStates(moves, gs);
        GameState dec = checkMove(moves.get(0), cc);
        int v = Integer.MIN_VALUE;
        for(GameState s : states) {
            int w = minimax(depth, s, true);
            if (w > v) {
                dec = s;
            }
            v = Math.max(w, v);
        }
        cc.setAIMove(dec);
    }

    private int minimax(int depth, GameState gs, boolean max) {
        if (depth == 0) {
            return evNumCapturedOpp(gs);
        }
        depth--;
        int v;
        CheckersController newCC = newCC(gs);
        ArrayList<int[]> moves;
        ArrayList<GameState> states;
        if (max) {
            v = Integer.MIN_VALUE;
            moves = newCC.getPlayerMoves(cc.getPlayer2());
            states = nextStates(moves, gs);
            for (GameState s : states) {
                v = Math.max(v, minimax(depth, s, false));
            }
        } else {
            v = Integer.MAX_VALUE;
            moves = newCC.getPlayerMoves(cc.getPlayer1());
            states = nextStates(moves, gs);
            for (GameState s : states) {
                v = Math.min(v, minimax(depth, s, true));
            }
        }
        return v;
    }

    // ai picks a random move
    private void random() {
        GameState gs = cc.getGameState();
        ArrayList<int[]> moves = cc.getPlayerMoves(player);
        ArrayList<GameState> states = nextStates(moves, gs);
        for (int[] m : moves) {
            GameState next = checkMove(m, cc);
            states.add(next);
        }
        Random rand = new Random(System.nanoTime());
        int i = rand.nextInt(states.size());
        cc.setAIMove(states.get(i));
    }

    private ArrayList<GameState> nextStates(ArrayList<int[]> moves, GameState gs) {
        ArrayList<GameState> fixed = new ArrayList<>();
        CheckersController newCC = newCC(gs);
        for(int[] m : moves) {
            GameState next = checkMove(m, newCC);
            if (gs.getTurn() == next.getTurn()) {
                Queue<Node> q = new Queue<>();
                q.addLast(new Node(next, m[2], m[3]));
                while(!q.isEmpty()) {
                    Node popped = q.removeFirst();
                    CheckersController ccc = newCC(popped.getGameState());
                    int x = popped.getX();
                    int y = popped.getY();
                    ArrayList<int[]> jumps = ccc.getJumps(x, y);
                    for (int[] j : jumps) {
                        int moveX = j[0];
                        int moveY = j[1];
                        GameState g = ccc.checkMove(x, y, moveX, moveY);
                        if (g.getTurn() == next.getTurn()) {
                            Node n = new Node(g, moveX, moveY);
                            q.addLast(n);
                        } else {
                            fixed.add(g);
                        }
                    }
                }
            } else {
                fixed.add(next);
            }
        }
        return fixed;
    }

    private int evNumCapturedOpp(GameState gs) {
        CheckersController newCC = new CheckersController(cc.getPlayer1(), cc.getPlayer2(), gs);
        ArrayList<int[]> oppPieces = newCC.getPieces(cc.getPlayer1());
        return 12 - oppPieces.size();
    }

    private GameState checkMove(int[] m, CheckersController cc) {
        int x = m[0];
        int y = m[1];
        int moveX = m[2];
        int moveY = m[3];
        return cc.checkMove(x, y, moveX, moveY);
    }

    private CheckersController newCC(GameState gs) {
        return new CheckersController(cc.getPlayer1(), cc.getPlayer2(), gs);
    }
}
