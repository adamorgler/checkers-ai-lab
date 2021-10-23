package com.chessailab.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AIController implements Runnable{

    private CheckersController cc;

    private String player;

    private int depth;

    private boolean aiOnly;

    private int evFunction;

    public AIController(String player, CheckersController cc) {
        this.cc = cc;
        this.player = player;
        this.aiOnly = false;
    }

    @Override
    public void run() {
        alphaBetaStart(depth);
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setAiOnly() {
        this.aiOnly = true;
    }

    public void setEvFunction(int f) {
        evFunction = f;
    }

    private void alphaBetaStart(int depth) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        GameState gs = cc.getGameState();
        ArrayList<int[]> moves = cc.getPlayerMoves(player);
        Collections.shuffle(moves);
        ArrayList<GameState> states = nextStates(moves, gs);
        GameState dec = checkMove(moves.get(0), cc);
        int v = Integer.MIN_VALUE;
        for(GameState s : states) {
            int w = alphaBeta(depth, s, true, alpha, beta);
            if (w >= v) {
                dec = s;
                v = w;
            }
            if(v >= beta) {
                break;
            }
            alpha = Math.max(v, alpha);
        }
        cc.setAIMove(dec);
    }

    private int alphaBeta(int depth, GameState gs, boolean max, int alpha, int beta) {
        if (depth == 0) {
            return evFunction(gs);
        }
        depth--;
        int v;
        CheckersController newCC = newCC(gs);
        ArrayList<int[]> moves;
        ArrayList<GameState> states;
        if (max) {
            v = Integer.MIN_VALUE;
            moves = newCC.getPlayerMoves(player);
            states = nextStates(moves, gs);
            for (GameState s : states) {
                v = Math.max(v, alphaBeta(depth, s, false, alpha, beta));
                if(v >= beta) {
                    return v;
                }
                alpha = Math.max(v, alpha);
            }
        } else {
            v = Integer.MAX_VALUE;
            if (player.equals(cc.getPlayer1())) {
                moves = newCC.getPlayerMoves(cc.getPlayer2());
            } else {
                moves = newCC.getPlayerMoves(cc.getPlayer1());
            }
            states = nextStates(moves, gs);
            for (GameState s : states) {
                v = Math.min(v, alphaBeta(depth, s, true, alpha, beta));
                if(v <= alpha) {
                    return v;
                }
                beta = Math.min(v, beta);
            }
        }
        return v;
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

    private int evFunction(GameState gs) {
        switch(evFunction) {
            case 0: {
                return evBoardValue(gs);
            }
            case 1: {
                return evNumCapturedOpp(gs);
            }
            default: {
                return 0;
            }
        }
    }

    private int evNumCapturedOpp(GameState gs) {
        CheckersController newCC = newCC(gs);
        ArrayList<int[]> pieces;
        if (newCC.checkMyTurn(player)) {
            pieces = newCC.getPieces(cc.getPlayer1());
            return 12 - pieces.size();
        } else {
            pieces = newCC.getPieces(player);
            return pieces.size();
        }
    }

    private int evBoardValue(GameState gs) {
        int value = 0;
        Spot[][] b = gs.getBoard().getBoard();
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = b[i][j].getPiece();
                if (piece != null) {
                    String pl = piece.getPlayer();
                    value += pieceValue(j, pl);
                    if (piece.isKinged()) {
                        value += kingValue(j, pl);
                    }

                }
            }
        }
        return value;
    }
    private int pieceValue(int y, String player) {
        int v = 0;
        int pieceV = 5;
        int halfV = 2;
        if (player.equals(this.player)) {
            v += pieceV;
        } else {
            v -= pieceV;
        }
        if (this.player.equals(cc.getPlayer1())) {
            if(player.equals(cc.getPlayer1()) && y > 3) {
                v += halfV;
            } else if(player.equals(cc.getPlayer2()) && y <= 3) {
                v -= halfV;
            }
        } else {
            if (player.equals(cc.getPlayer2()) && y <= 3) {
                v += halfV;
            } else if(player.equals(cc.getPlayer1()) && y > 3) {
                v -= halfV;
            }
        }
        return v;
    }
    private int kingValue(int y, String player) {
        int v = 0;
        int kingV = 10;
        int halfV = 2;
        if(player.equals(this.player)) {
            v += kingV;
        } else {
            v -= kingV;
        }
        return v;
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
