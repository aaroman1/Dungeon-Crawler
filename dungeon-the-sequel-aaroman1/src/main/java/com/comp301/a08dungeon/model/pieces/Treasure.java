package com.comp301.a08dungeon.model.pieces;

public class Treasure extends APiece {
  private final int value;

  public Treasure() {
    super("Treasure","images/treasure.png");
    this.value = 100;
  }

  public int getValue() {
    return this.value;
  }
}
