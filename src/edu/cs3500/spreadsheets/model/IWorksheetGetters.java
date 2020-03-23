package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

/**
 * Represents an immutable worksheet containing only getters.
 */
public interface IWorksheetGetters {

  /**
   * Gets the spreadsheet of a WorksheetModel.
   *
   * @return the sheet of a WorksheetModel
   */
  HashMap<Coord, ICell> getSheet();

  /**
   * Evaluates a Cells content and returns the representational String.
   *
   * @param coord the Coordinate of the Cell that needs to be evaluated
   * @return the String representing a Cell
   */
  String evaluate(Coord coord);

  /**
   * Gets cell at the given coordinate.
   *
   * @param coord The Coordinates of the cell
   * @return the cell at the given coordinates
   * @throws IllegalArgumentException if given Coordinates are bigger than rectWidth or rectHeight
   *         of sheet
   */
  ICell getCellAt(Coord coord);

  /**
   * Gets the current largest column index or 10 if the max column is smaller than that.
   *
   * @return An integer value that represents the max column
   */
  int getMaxColumn();

  /**
   * Gets the current largest row index or 10 if the max row is smaller than that.
   *
   * @return An integer value that represents the max row
   */
  int getMaxRow();

  /**
   * Determines whether the Spreadsheet contains a Cell at the given Coordinates.
   *
   * @param coord Coordinates of a a possible cell
   * @return true or false whether the spreadsheet contains a Cell at given Coordinates
   */
  boolean containsCell(Coord coord);
}
