package com.comp301.a08dungeon.model.pieces;

public class Hero extends APiece implements MovablePiece {
  public Hero() {
    super("Hero","images/hero.png");
  }

  @Override
  public CollisionResult collide(Piece other) {
    // 1. Nothing → CONTINUE, no points
    if (other == null) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    // 2. Treasure → CONTINUE, +treasure value
    if (other instanceof Treasure) {
      int pts = ((Treasure) other).getValue();
      return new CollisionResult(pts, CollisionResult.Result.CONTINUE);
    }
    // 3. Enemy → GAME_OVER
    if (other instanceof Enemy) {
      return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
    }
    // 4. Exit → NEXT_LEVEL
    if (other instanceof Exit) {
      return new CollisionResult(0, CollisionResult.Result.NEXT_LEVEL);
    }
    // Anything else (e.g. Wall) → just continue
    if (other instanceof Wall){
      throw new IllegalArgumentException("Hero cannot collide with a wall");
    }
    throw new IllegalArgumentException("Hero cannot collide with other pieces.");
  }
}
