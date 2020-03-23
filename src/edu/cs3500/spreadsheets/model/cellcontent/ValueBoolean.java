package edu.cs3500.spreadsheets.model.cellcontent;

/**
 * Represents a Boolean Value.
 */
public class ValueBoolean implements IValue<Boolean> {

  private boolean b;

  /**
   * Constructs a Boolean Value.
   *
   * @param b a boolean
   */
  public ValueBoolean(boolean b) {
    this.b = b;
  }

  @Override
  public Boolean getValue() {
    return this.b;
  }

  @Override
  public String toString() {
    return Boolean.toString(b);
  }
}
