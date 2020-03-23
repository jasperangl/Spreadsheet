package edu.cs3500.spreadsheets.model.cellcontent;

/**
 * Represents a Double Value.
 */
public class ValueDouble implements IValue<Double> {

  private double num;

  /**
   * Constructs a Double Value.
   *
   * @param number a Double
   */
  public ValueDouble(double number) {
    this.num = number;
  }

  @Override
  public Double getValue() {
    return this.num;
  }

  @Override
  public String toString() {
    return Double.toString(num);
  }
}
