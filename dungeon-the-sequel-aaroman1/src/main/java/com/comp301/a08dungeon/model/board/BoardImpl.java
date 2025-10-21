package com.comp301.a08dungeon.model.board;

import com.comp301.a08dungeon.model.pieces.*;

import java.util.*;

public class BoardImpl implements Board {
  private final int width;
  private final int height;
  private final Piece[][] board;
  private Hero hero;
  private final List<Enemy> enemies = new ArrayList<>();
  private Exit exit;
  private final Random rng = new Random();

  /** ctor: empty board of given size */
  public BoardImpl(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Board dimensions must be positive");
    }
    this.width = width;
    this.height = height;
    this.board = new Piece[height][width];
    // start all positions as null
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        board[r][c] = null;
      }
    }
  }

  /** ctor: wrap an existing grid */
  public BoardImpl(Piece[][] preset) {
    if (preset == null || preset.length == 0 || preset[0].length == 0) {
      throw new IllegalArgumentException("Board array can't be null or empty");
    }
    this.height = preset.length;
    this.width = preset[0].length;
    this.board = new Piece[height][width];
    for (int r = 0; r < height; r++) {
      if (preset[r].length != width) {
        throw new IllegalArgumentException("Uneven board array.");
      }
      for (int c = 0; c < width; c++) {
        board[r][c] = preset[r][c];
        if (board[r][c] instanceof Hero) {
          hero = (Hero) board[r][c];
          hero.setPosn(new Posn(r, c));
        } else if (board[r][c] instanceof Exit) {
          exit = (Exit) board[r][c];
          exit.setPosn(new Posn(r, c));
        } else if (board[r][c] instanceof Enemy) {
          Enemy e = (Enemy) board[r][c];
          e.setPosn(new Posn(r, c));
          enemies.add(e);
        }
      }
    }
  }

  @Override public int getWidth()  { return width; }
  @Override public int getHeight() { return height; }

  @Override
  public Piece get(Posn pos) {
    return board[pos.getRow()][pos.getCol()];
  }

  @Override
  public void set(Piece p, Posn newPos) {
    if(p == null || newPos == null){
      throw new IllegalArgumentException("Piece and Posn cannot be null.");
    }
    int r = newPos.getRow();
    int c = newPos.getCol();
    if(r<0 || r>= height || c<0 || c>=width){
      throw new IllegalArgumentException("Posn out of bounds.");
    }
    board[r][c] = p;
    p.setPosn(newPos);
    if(p instanceof Hero){
      this.hero = (Hero) p;
    }else if(p instanceof Enemy){
      this.enemies.add((Enemy)p);
    }else if(p instanceof Exit){
      this.exit = (Exit) p;
    }
  }

  @Override
  public void init(int numEnemies, int numTreasures, int numWalls) {
    // 1) clear board & reset lists
    for (int r = 0; r < height; r++) {
      Arrays.fill(board[r], null);
    }
    enemies.clear();
    hero = null;
    exit = null;

    int spots = width * height;
    if (1 + 1 + numEnemies + numTreasures + numWalls > spots) {
      throw new IllegalArgumentException("Too many pieces for board size");
    }


    hero = new Hero();
    Posn p = getRandomEmptyPosn();
    placePiece(hero, p);


    exit = new Exit();
    p = getRandomEmptyPosn();
    placePiece(exit, p);


    for (int i = 0; i < numEnemies; i++) {
      Enemy e = new Enemy();
      p = getRandomEmptyPosn();
      placePiece(e, p);
      enemies.add(e);
    }


    for (int i = 0; i < numTreasures; i++) {
      Treasure t = new Treasure();
      p = getRandomEmptyPosn();
      placePiece(t, p);
    }


    for (int i = 0; i < numWalls; i++) {
      Wall w = new Wall();
      p = getRandomEmptyPosn();
      placePiece(w, p);
    }
  }

  @Override
  public CollisionResult moveHero(int drow, int dcol) {
    Posn old = hero.getPosn();
    Posn dest = new Posn(old.getRow() + drow, old.getCol() + dcol);

    if (!inBounds(dest) || get(dest) instanceof Wall) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }

    Piece other = get(dest);
    CollisionResult heroRes = hero.collide(other);
    int pts = heroRes.getPoints();

    clearPosition(old);
    placePiece(hero, dest);

    if (heroRes.getResults() != CollisionResult.Result.CONTINUE) {
      return new CollisionResult(pts, heroRes.getResults());
    }

    boolean heroEaten = false;
    for (Enemy e : new ArrayList<>(enemies)) {
      CollisionResult er = moveEnemy(e);
      if (er.getResults() == CollisionResult.Result.GAME_OVER) {
        heroEaten = true;
      }
    }
    if (heroEaten) {
      return new CollisionResult(pts, CollisionResult.Result.GAME_OVER);
    }

    return new CollisionResult(pts, CollisionResult.Result.CONTINUE);
  }

  private CollisionResult moveEnemy(Enemy e) {
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    Collections.shuffle(Arrays.asList(dirs), rng);
    Posn old = e.getPosn();

    for (int[] d : dirs) {
      Posn dest = new Posn(old.getRow()+d[0], old.getCol()+d[1]);
      if (!inBounds(dest)) continue;
      Piece occ = get(dest);
      if (occ instanceof Wall || occ instanceof Exit || occ instanceof Enemy) continue;

      clearPosition(old);
      placePiece(e, dest);
      CollisionResult cr = e.collide(occ);
      if (cr.getResults() == CollisionResult.Result.GAME_OVER) {
        return cr;
      }
      break;
    }
    return new CollisionResult(0, CollisionResult.Result.CONTINUE);
  }

  private boolean inBounds(Posn p) {
    return p.getRow() >= 0 && p.getRow() < height
            && p.getCol() >= 0 && p.getCol() < width;
  }

  private Posn getRandomEmptyPosn() {
    Posn p;
    do {
      int r = rng.nextInt(height);
      int c = rng.nextInt(width);
      p = new Posn(r, c);
    } while (get(p) != null);
    return p;
  }

  private void placePiece(Piece piece, Posn p) {
    piece.setPosn(p);
    board[p.getRow()][p.getCol()] = piece;
  }

  private void clearPosition(Posn p) {
    board[p.getRow()][p.getCol()] = null;
  }
}