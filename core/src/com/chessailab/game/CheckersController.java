package com.chessailab.game;

import java.util.ArrayList;

public class CheckersController {

    final int NORTHWEST = 0;
    final int NORTHEAST = 1;
    final int SOUTHWEST = 2;
    final int SOUTHEAST = 3;

    private GameState gameState;

    private String player1;

    private String player2;

    private boolean jumped;

    private String log;

    private AIController ai1;

    private AIController ai2;

    public CheckersController(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.jumped = false;
        newGame();
        this.ai1 = new AIController(player2, this);
        this.ai1.setDepth(7);
        // for AI vs AI game
        this.ai2 = new AIController(player1, this);
        this.ai2.setDepth(5);
    }

    public CheckersController(String player1, String player2, GameState gameState) {
        this.player1 = player1;
        this.player2 = player2;
        this.jumped = false;
        this.gameState = gameState;
    }

    public GameState checkMove(int x, int y, int moveX, int moveY) {
        Board b = new Board(player1, player2, gameState.getBoard().getBoard());
        GameState gs = new GameState(b, gameState.getTurn());
        if (validMove(x, y, moveX, moveY, gs)) {
            if (checkJumpPos(x, y, moveX, moveY)) {
                int[] jumped = jumpedPos(x, y, moveX, moveY);
                gs.getBoard().getSpot(jumped).removePiece();
                b.getSpot(moveX, moveY).setPiece(b.getSpot(x, y).getPiece());
                b.getSpot(x, y).removePiece();
                if (!hasJumps(moveX, moveY, gs)) {
                    gs.nextTurn();
                }
            } else {
                b.getSpot(moveX, moveY).setPiece(b.getSpot(x, y).getPiece());
                b.getSpot(x, y).removePiece();
                gs.nextTurn();
            }
            return gs;
        }
        return null;
    }

    public boolean move(int x, int y, int moveX, int moveY) {
        Board b = gameState.getBoard();
        if (validMove(x, y, moveX, moveY)) {
            String p = b.getSpot(x, y).getPiece().getPlayer();
            if (checkMyTurn(p)) {
                if (checkJumpPos(x, y, moveX, moveY)) {
                    int[] jumped = jumpedPos(x, y, moveX, moveY);
                    gameState.getBoard().getSpot(jumped).removePiece();
                    b.getSpot(moveX, moveY).setPiece(b.getSpot(x, y).getPiece());
                    b.getSpot(x, y).removePiece();
                    if (hasJumps(moveX, moveY)) {
                        this.jumped = true;
                    } else {
                        gameState.nextTurn();
                        this.jumped = false;
                    }
                } else {
                    b.getSpot(moveX, moveY).setPiece(b.getSpot(x, y).getPiece());
                    b.getSpot(x, y).removePiece();
                    gameState.nextTurn();
                }
                checkDraw();
                checkEndgame();
                if (isRunning()) {
                    logTurn();
                    checkKings();
                    if (!isJumped()) {
                        checkAITurn();
                    }
                }
                return true;
            }
        }
        return false;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return  player2;
    }

    public ArrayList<int[]> getPieces(String player) {
        ArrayList<int[]> pieces = new ArrayList<>();
        Board b = gameState.getBoard();
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 8; j++) {
                Spot bs = b.getSpot(i, j);
                if (!bs.isEmpty()) {
                    Piece p = bs.getPiece();
                    if (p.getPlayer().equals(player)) {
                        pieces.add(new int[]{i, j});
                    }
                }
            }
        }
        return pieces;
    }

    public ArrayList<int[]> getMoves(int x, int y) {
        ArrayList<int[]> moves = new ArrayList<>();
        if (posExists(x, y) && !isEmpty(x, y)) {
            Piece p = getPiece(x, y);
            int sX = shiftX(x, y);
            int [] nw = {stillX(sX - 1, y + 1), y + 1};
            int [] ne = {stillX(sX + 1, y + 1), y + 1};
            int [] sw = {stillX(sX - 1, y - 1), y - 1};
            int [] se = {stillX(sX + 1, y - 1), y - 1};
            if (posExists(nw)) {
                if (isEmpty(nw)) {
                    if (p.getPlayer().equals(player1) || p.isKinged()) {
                        moves.add(nw);
                    }
                }
            }
            if (posExists(ne)) {
                if (isEmpty(ne)) {
                    if (p.getPlayer().equals(player1) || p.isKinged()) {
                        moves.add(ne);
                    }
                }
            }
            if (posExists(sw)) {
                if (isEmpty(sw)) {
                    if (p.getPlayer().equals(player2) || p.isKinged()) {
                        moves.add(sw);
                    }
                }
            }
            if (posExists(se)) {
                if (isEmpty(se)) {
                    if (p.getPlayer().equals(player2) || p.isKinged()) {
                        moves.add(se);
                    }
                }
            }
        }
        return moves;
    }

    public ArrayList<int[]> getJumps(int x, int y) {
        ArrayList<int[]> jumps = new ArrayList<>();
        if (posExists(x, y) && !isEmpty(x, y)) {
            Piece p = getPiece(x, y);
            int sX = shiftX(x, y);
            int [] nw = {stillX(sX - 2, y + 2), y + 2};
            int [] nwm = {stillX(sX - 1, y + 1), y + 1};
            int [] ne = {stillX(sX + 2, y + 2), y + 2};
            int [] nem = {stillX(sX + 1, y + 1), y + 1};
            int [] sw = {stillX(sX - 2, y - 2), y - 2};
            int [] swm = {stillX(sX - 1, y - 1), y - 1};
            int [] se = {stillX(sX + 2, y - 2), y - 2};
            int [] sem = {stillX(sX + 1, y - 1), y - 1};
            if(checkJump(p, player1, nw, nwm)) {
                jumps.add(nw);
            }
            if(checkJump(p, player1, ne, nem)) {
                jumps.add(ne);
            }
            if(checkJump(p, player2, sw, swm)) {
                jumps.add(sw);
            }
            if(checkJump(p, player2, se, sem)) {
                jumps.add(se);
            }

        }

        return jumps;
    }

    public ArrayList<int[]> getJumps(int x, int y, GameState gs) {
        ArrayList<int[]> jumps = new ArrayList<>();
        if (posExists(x, y) && !isEmpty(x, y, gs)) {
            Piece p = getPiece(x, y, gs);
            int sX = shiftX(x, y);
            int [] nw = {stillX(sX - 2, y + 2), y + 2};
            int [] nwm = {stillX(sX - 1, y + 1), y + 1};
            int [] ne = {stillX(sX + 2, y + 2), y + 2};
            int [] nem = {stillX(sX + 1, y + 1), y + 1};
            int [] sw = {stillX(sX - 2, y - 2), y - 2};
            int [] swm = {stillX(sX - 1, y - 1), y - 1};
            int [] se = {stillX(sX + 2, y - 2), y - 2};
            int [] sem = {stillX(sX + 1, y - 1), y - 1};
            if(checkJump(p, player1, nw, nwm, gs)) {
                jumps.add(nw);
            }
            if(checkJump(p, player1, ne, nem, gs)) {
                jumps.add(ne);
            }
            if(checkJump(p, player2, sw, swm, gs)) {
                jumps.add(sw);
            }
            if(checkJump(p, player2, se, sem, gs)) {
                jumps.add(se);
            }

        }
        return jumps;
    }

    public ArrayList<int[]> getAllMoves(int x, int y) {
        ArrayList<int[]> moves = getMoves(x, y);
        ArrayList<int[]> jumps = getJumps(x, y);
        moves.addAll(jumps);
        return moves;
    }

    public ArrayList<int[]> getPlayerMoves (String player) {
        ArrayList<int[]> playerMoves = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                if (!isEmpty(i, j)) {
                    Spot s = gameState.getBoard().getSpot(i, j);
                    Piece p = s.getPiece();
                    String pl = p.getPlayer();
                    if (pl.equals(player)) {
                        if (hasMoves(i, j) || hasJumps(i, j)) {
                            ArrayList<int[]> moves = getAllMoves(i, j);
                            for (int [] m : moves) {
                                int[] next = {i, j, m[0], m[1]};
                                playerMoves.add(next);
                            }
                        }
                    }
                }
            }
        }
        return playerMoves;
    }

    public boolean checkPlayerHasMoves(String player) {
        ArrayList<int[]> playerMoves = getPlayerMoves(player);
        if (playerMoves.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean hasMoves(int x, int y) {
        if (posExists(x, y)) {
            if (!isEmpty(x,  y)) {
                ArrayList<int[]> moves = getAllMoves(x, y);
                if (moves.size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasJumps(int x, int y) {
        if (posExists(x, y)) {
            if (!isEmpty(x,  y)) {
                ArrayList<int[]> jumps = getJumps(x, y);
                if (jumps.size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasJumps(int x, int y, GameState gs) {
        if (posExists(x, y)) {
            if (!isEmpty(x, y, gs)) {
                ArrayList<int[]> jumps = getJumps(x, y, gs);
                if (jumps.size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkJump(Piece p, String player, int[] land, int[] jumped) {
        if(posExists(land)) { // check if jump position exists
            if(isEmpty(land) && !isEmpty(jumped)) { // check if there is a player to jump and the jump position is empty
                if(p.getPlayer().equals(player) || p.isKinged()) { // check movement rules
                    if (!getPiece(jumped).getPlayer().equals(p.getPlayer())) { // check if jumped piece is opposing team
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkJump(Piece p, String player, int[] land, int[] jumped, GameState gs) {
        if(posExists(land)) { // check if jump position exists
            if(isEmpty(land, gs) && !isEmpty(jumped, gs)) { // check if there is a player to jump and the jump position is empty
                if(p.getPlayer().equals(player) || p.isKinged()) { // check movement rules
                    if (!getPiece(jumped).getPlayer().equals(p.getPlayer())) { // check if jumped piece is opposing team
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Piece getPiece(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        return getPiece(x, y);
    }

    public Piece getPiece(int[] pos, GameState gs) {
        int x = pos[0];
        int y = pos[1];
        return getPiece(x, y, gs);
    }

    public Piece getPiece(int x, int y) {
        Board b = gameState.getBoard();
        Piece p = b.getSpot(x, y).getPiece();
        return p;
    }

    public Piece getPiece(int x, int y, GameState gs) {
        Board b = gs.getBoard();
        Piece p = b.getSpot(x, y).getPiece();
        return p;
    }

    public boolean move(int[] pos, int[] move) {
        int x = pos[0];
        int y = pos[1];
        int mX = move[0];
        int mY = move[1];
        return move(x, y, mX, mY);
    }

    public boolean isEmpty(int x, int y) {
        Board b = gameState.getBoard();
        return b.getSpot(x, y).isEmpty();
    }

    public boolean isEmpty(int[] pos) {
        return isEmpty(pos[0], pos[1]);
    }

    public boolean isEmpty(int[] pos, GameState gs) {
        return isEmpty(pos[0], pos[1], gs);
    }

    public boolean isEmpty(int x, int y, GameState gs) {
        Board b = gs.getBoard();
        return b.getSpot(x, y).isEmpty();
    }

    public boolean posExists(int x, int y) {
        if (x >= 0 && x < 4 && y >= 0 && y < 8) {
            return true;
        }
        return false;
    }

    public boolean posExists(int[] pos) {
        if(pos[0] >= 0 && pos[0] < 4 && pos[1] >= 0 && pos[1] < 8) {
            return true;
        }
        return false;
    }

    public boolean validMove(int x, int y, int moveX, int moveY) {
        if (posExists(x, y) && posExists(moveX, moveY) && isEmpty(moveX, moveY)) {
            return true;
        }
        return false;
    }

    public boolean validMove(int x, int y, int moveX, int moveY, GameState gs) {
        if (posExists(x, y) && posExists(moveX, moveY) && isEmpty(moveX, moveY, gs)) {
            return true;
        }
        return false;
    }

    public void king(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        king(x, y);
    }

    public void king(int x, int y) {
        if(posExists(x, y)) {
            if (!isEmpty(x, y)) {
                getPiece(x, y).king();
            }
        }
    }

    public void setAIMove(GameState gameState) {
        this.gameState = gameState;
        checkDraw();
        checkEndgame();
        if (isRunning()) {
            logTurn();
            checkKings();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean checkMyTurn(String player) {
        int turn = gameState.getTurn();
        if(turn % 2 == 0 && player.equals(player1)) {
            log(player1 + "'s turn");
            return true;
        } else if (turn % 2 == 1 && player.equals(player2)) {
            log(player2 + "'s turn");
            return true;
        }
        return false;
    }

    public boolean isRunning() {
        return gameState.isRunning();
    }

    public boolean isJumped() {
        return jumped;
    }

    public void newGame() {
        this.gameState = new GameState(this);
        log("New game! " + player1 + " vs " + player2 + "! " + player1 + "'s turn!");
    }

    public void log(String message) {
        this.log = message;
    }

    public String getLog() {
        return log;
    }

    // shifts xpos to a more manageble coordinate.
    // removes "zigzagging" in matrix allowing for simple cardinal move translation
    private int shiftX(int x, int y) {
        if (y % 2 == 0) {
            return (x * 2) + 1;
        }
        return (x * 2);
    }

    // returns x to the origional board position
    private int stillX(int x, int y) {
        if (y % 2 == 0) {
            return (x - 1) / 2;
        }
        return x / 2;
    }

    private boolean checkJumpPos(int x, int y, int moveX, int moveY) {
        if (Math.abs(y - moveY) == 2) {
            return true;
        }
        return false;
    }

    private int[] jumpedPos(int x, int y, int moveX, int moveY) {
        int[] jumped = new int[2];
        jumped[1] = (y + moveY) / 2;
        if (y % 2 == 0) {
            if (x > moveX) {
                jumped[0] = x;
            } else {
                jumped[0] = moveX;
            }
        } else {
            if (x > moveX) {
                jumped[0] = moveX;
            } else {
                jumped[0] = x;
            }
        }
        return jumped;
    }

    private void checkEndgame() {
        if (getPieces(player1).size() == 0) {
            log(player2 + " wins!");
        } else if (getPieces(player2).size() == 0) {
            log(player1 + " wins!");
        }  else {
            return;
        }
        gameState.endGame();
    }

    private void checkDraw() {
        if (!checkPlayerHasMoves(player1) && !checkPlayerHasMoves(player2)) {
            log("Draw!");
            gameState.endGame();
        }
    }

    private void checkKings() {
        Board b = gameState.getBoard();
        for (int i = 0; i < 4; i++) {
            Spot top = b.getSpot(i, 7);
            if (!top.isEmpty() && top.getPiece().getPlayer().equals(player1)) {
                top.getPiece().king();
            }
            Spot bot = b.getSpot(i, 0);
            if (!bot.isEmpty() && bot.getPiece().getPlayer().equals(player2)) {
                bot.getPiece().king();
            }
        }
    }

    private void logTurn() {
        int turn = gameState.getTurn();
        if(turn % 2 == 0) {
            log(player1 + "'s turn");
        } else if (turn % 2 == 1) {
            log(player2 + "'s turn");
        }
    }

    private void checkAITurn() {
        Thread t = new Thread(ai1);
        t.start();
    }

}
