package com.chessailab.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class UI {

    static final int UP = 0;
    static final int DOWN = 1;
    static final int RIGHT = 2;
    static final int LEFT = 3;

    ShapeRenderer shapes;
    SpriteBatch batch;
    BitmapFont font;

    int spotWidth;
    int pieceRadius;

    int selX;
    int selY;
    int moveC; // move cursor
    int moveX;
    int moveY;
    boolean selected;

    int maxX;
    int maxY;

    int rectWidth;

    private CheckersController cc;

    public UI(CheckersController cc) {
        this.shapes = new ShapeRenderer();
        this.shapes.setAutoShapeType(true);

        this.batch = new SpriteBatch();
        this.font = new BitmapFont();

        spotWidth = 40;
        pieceRadius = 16;

        // selection cursor position
        selX = 0;
        selY = 0;

        // movement selection cursor position
        moveX = 0;
        moveY = 0;
        moveC = 0;

        selected = false;

        // selection cursor bounds
        maxX = 4;
        maxY = 8;

        rectWidth = 3;

        this.cc = cc;
    }

    public void render() {
        ScreenUtils.clear(Color.DARK_GRAY);
        showBoard();
        showPieces();
        showSelection();
        showMoveOptions();
        drawLog();
        checkJumpState();
    }

    //moves cursor
    public void moveSelection(int code) {
        if (!selected) {
            switch (code) {
                case UP: {
                    if (selY < maxY - 1) {
                        selY++;
                    }
                    return;
                }
                case DOWN: {
                    if (selY > 0) {
                        selY--;
                    }
                    return;
                }
                case RIGHT: {
                    if (selX < maxX - 1) {
                        selX++;
                    }
                    return;
                }
                case LEFT: {
                    if (selX > 0) {
                        selX--;
                    }
                    return;
                }
            }
        }
    }


    public void moveMoveSelection(int code) {
        if (selected) {
            ArrayList<int[]> moves = getMoves();
            int max = moves.size();
            switch(code) {
                case RIGHT: {
                    if(moveC == max - 1) {
                        moveC = 0;
                    } else {
                        moveC++;
                    }
                    break;
                }
                case LEFT: {
                    if (moveC == 0) {
                        moveC = max - 1;
                    } else {
                        moveC--;
                    }
                    break;
                }
            }
            if (!moves.isEmpty()) {
                moveX = moves.get(moveC)[0];
                moveY = moves.get(moveC)[1];
            } else {
                selected = false;
            }
            return;
        }
    }

    public void select() {
        if (!cc.isEmpty(selX, selY)) {
            selected = true;
            ArrayList<int[]> moves = getMoves();
            if (!moves.isEmpty()) {
                moveX = moves.get(0)[0];
                moveY = moves.get(0)[1];
                moveC = 0;
            }
        }
    }

    public void deselect() {
        selected = false;
    }

    public void enterMove() {
        if(selected) {
            cc.move(selX, selY, moveX, moveY);
            deselect();
        }
    }

    private ArrayList<int[]> getMoves() {
        ArrayList<int[]> moves;
        if (cc.isJumped()) {
            moves = cc.getJumps(selX, selY);
        } else {
            moves = cc.getAllMoves(selX, selY);
        }
        return moves;
    }

    private void showBoard() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        int spots = 8;
        for(int i = 0; i < spots; i++) {
            for (int j = 0; j < spots; j++) {
                int xpos = i * spotWidth;
                int ypos = j * spotWidth;
                Color c = new Color();
                if (j % 2 == 0) {
                    if (i % 2 == 0) {
                        c.set(Color.RED);
                    } else {
                        c.set(Color.WHITE);
                    }
                } else {
                    if (i % 2 == 1) {
                        c.set(Color.RED);
                    } else {
                        c.set(Color.WHITE);
                    }
                }
                shapes.setColor(c);
                shapes.rect(xpos, ypos, spotWidth, spotWidth);
            }
        }
        shapes.end();
    }

    private void showPieces() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                int xpos = i * spotWidth * 2 + (spotWidth / 2);
                int ypos = j * spotWidth + (spotWidth / 2);
                Color c = new Color();
                Board b = cc.getGameState().getBoard();
                if(!b.getSpot(i, j).isEmpty()) {
                   Piece p = b.getSpot(i, j).getPiece();
                   if (p.getPlayer().equals(cc.getPlayer1())) {
                       c.set(Color.RED);
                   } else {
                       c.set(Color.BLACK);
                   }
                   // alter position
                   if (j % 2 == 0) {
                       xpos += spotWidth;
                   }

                   shapes.setColor(c);
                   shapes.circle(xpos, ypos, pieceRadius);
                   if (p.isKinged()) {
                       shapes.setColor(Color.GOLD);
                       shapes.circle(xpos, ypos, pieceRadius / 2);
                   }
                }
            }
        }
        shapes.end();
    }

    private void showSelection() {
        int xpos;
        int ypos = selY * spotWidth;
        if (selY % 2 == 1) {
            xpos = selX * spotWidth * 2;
        } else {
            xpos = (selX * spotWidth * 2) + spotWidth;
        }
        //int lineWidth = 3;
        Color c;
        if (selected) {
            c = new Color(Color.CORAL);
        } else {
            c = new Color(Color.BLUE);
        }

        drawThickRect(xpos, ypos, xpos + spotWidth, ypos + spotWidth, rectWidth, c);
    }

    private void showMoveOptions() {
        ArrayList<int[]> moves = getMoves();
        drawMoves(moves);

    }

    private void drawMoves(ArrayList<int[]> moves) {
        Color c;
        for(int i = 0; i < moves.size(); i++) {
            int[] m = moves.get(i);
            int x = m[0];
            int y = m[1];
            if (selected && x == moveX && y == moveY) {
                c = new Color(Color.MAROON);
            } else {
                c = new Color(Color.GOLD);
            }
            x *= spotWidth * 2;
            if (y % 2 == 0) {
                x += spotWidth;
            }
            y *= spotWidth;
            drawThickRect(x,y, x + spotWidth, y + spotWidth, rectWidth, c);
        }
    }

    private void drawLog() {
        Color c = new Color(Color.WHITE);
        int x = 10;
        int y = (spotWidth * 8) + spotWidth;
        String log = cc.getLog();

        batch.begin();
        font.draw(batch, log, x, y);
        batch.end();
    }

    private void drawThickRect(int x1, int y1, int x2, int y2, int w, Color c) {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(c);
        shapes.rectLine(x1, y1, x2, y1, w);
        shapes.rectLine(x2, y1, x2, y2, w);
        shapes.rectLine(x1, y2, x2, y2, w);
        shapes.rectLine(x1, y1, x1, y2, w);
        shapes.end();
    }

    private void checkJumpState() {
        if(cc.isJumped()) {
            if (!selected) {
                selX = moveX;
                selY = moveY;
            }
            select();
        }
    }
}
