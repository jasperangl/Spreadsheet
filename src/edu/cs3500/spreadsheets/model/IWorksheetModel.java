package edu.cs3500.spreadsheets.model;

/**
 * Represents the interface of a Worksheet Model with mutable methods.
 */
public interface IWorksheetModel extends IWorksheetGetters {

  /**
   * Changes a Cells content and Sexp at the given Coordinate or creates a new one if
   * no cell existed at that coordinate before.
   *
   * @param coord A coordinate
   * @param contents a String representing the content that will change
   */
  void changeCellContent(Coord coord, String contents);

  /**
   * Removes all dependencies of the Cells in our Spreadsheet.
   */
  void clearDependencies();
}
