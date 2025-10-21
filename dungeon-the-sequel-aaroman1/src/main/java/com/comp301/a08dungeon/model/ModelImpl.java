package com.comp301.a08dungeon.model;

import com.comp301.a08dungeon.model.board.Board;
import com.comp301.a08dungeon.model.board.BoardImpl;
import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.CollisionResult;
import com.comp301.a08dungeon.model.pieces.Piece;
import java.util.ArrayList;
import java.util.List;

import static com.comp301.a08dungeon.model.pieces.CollisionResult.Result.*;

public class ModelImpl implements Model {
    private final Board board;
    private int curScore;
    private int highScore;
    private int level;
    private STATUS status;
    private final List<Observer> observers = new ArrayList<>();


    public ModelImpl(int width, int height) {
        this.board = new BoardImpl(width, height);
        resetState();
    }


    public ModelImpl(Board board) {
        this.board = board;
        resetState();
    }


    private void resetState() {
        this.curScore = 0;
        this.level = 1;
        this.status = STATUS.END_GAME;
    }

    //---- Model getters ----
    @Override
    public int getWidth(){
        return board.getWidth();
    }
    @Override
    public int getHeight(){
        return board.getHeight();
    }


    @Override
    public Piece get(Posn p){
        return board.get(p);
    }
    @Override
    public int getCurScore(){
        return curScore;
    }
    @Override
    public int getHighScore(){
        return highScore;
    }
    @Override
    public int getLevel(){
        return level;
    }
    @Override
    public STATUS getStatus(){
        return status;
    }

    //---- Observer methods ----
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    private void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

    //---- Game control ----
    @Override
    public void startGame() {
        this.curScore = 0;
        this.level = 1;
        status = STATUS.IN_PROGRESS;
        board.init(level + 1, 2, 2);
        notifyObservers();
    }

    @Override
    public void endGame() {
        status = STATUS.END_GAME;
        if (this.curScore > highScore){
            this.highScore = curScore;
        }
        notifyObservers();
    }


    @Override
    public void moveUp(){
        CollisionResult r = board.moveHero(-1,0);
        updateScore(r);
        notifyObservers();
    }
    @Override
    public void moveDown(){
        CollisionResult r = board.moveHero(1,0);
        updateScore(r);
        notifyObservers();
    }
    @Override
    public void moveLeft(){
        CollisionResult r = board.moveHero(0,-1);
        updateScore(r);
        notifyObservers();
    }
    @Override
    public void moveRight(){
        CollisionResult r = board.moveHero(0,1);
        updateScore(r);
        notifyObservers();
    }


    private void updateScore(CollisionResult r) {
        curScore += r.getPoints();
        switch (r.getResults()) {
            case NEXT_LEVEL:
                level++;
                board.init(level + 1, 2, 2);
                notifyObservers();
                break;
            case GAME_OVER:
                endGame();
                break;
            case CONTINUE:
                notifyObservers();
                break;
                }
        }
    }

