package com.comp301.a08dungeon.model.pieces;

public interface MovablePiece extends Piece {
  CollisionResult collide(Piece other);
}
