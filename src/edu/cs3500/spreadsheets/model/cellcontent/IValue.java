package edu.cs3500.spreadsheets.model.cellcontent;


/**
 * Represents the interface of a possible spreadsheet value.
 *
 * @param <T> is one of String, Boolean or Double
 */
public interface IValue<T> extends IFormula {

  /**
   * Gives the value for a Cell.
   *
   * @return Value of a Cell. One of String, Boolean or Double
   */
  public T getValue();

}
