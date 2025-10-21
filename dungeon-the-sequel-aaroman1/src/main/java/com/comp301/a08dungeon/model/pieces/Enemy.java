package com.comp301.a08dungeon.model.pieces;

public class Enemy extends APiece implements MovablePiece {

  public Enemy() {
    super("Enemy","images/enemy.png");
  }

  @Override
  public CollisionResult collide(Piece other) {
    // 1. Nothing → CONTINUE
    if (other == null) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    // 2. Treasure → CONTINUE (enemy “eats” it, so no points to player)
    if (other instanceof Treasure) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }
    // 3. Hero → GAME_OVER
    if (other instanceof Hero) {
      return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
    }
    // 4. Exit → enemies can’t leave; treat as CONTINUE
    if (other instanceof Exit) {
      throw new IllegalArgumentException("Enemy cannot exit.");
    }
    // Anything else → CONTINUE
    if(other instanceof Wall){
      throw new IllegalArgumentException("Enemy cannot collide with a wall.");
    }
    throw new IllegalArgumentException("Enemy cannot collide with piece.");
  }
}
