package edu.cs3500.spreadsheets.model.cellcontent;

/**
 * Represents a String Value.
 */
public class ValueString implements IValue<String> {

  private String s;

  /**
   * Constructs a String Value.
   *
   * @param s a String
   */
  public ValueString(String s) {
    this.s = s;
  }

  @Override
  public String getValue() {
    return this.s;
  }

  @Override
  public String toString() {
    return s;
  }
}
