package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A controller representation for a Spreadsheet with abilities to change and delete cells and save
 * and load files.
 */
public interface IController {

  /**
   * If cell exists, changes the content and Sexp of the cell with the given coordinate to the given
   * String.
   *
   * @param c Coordinate of Cell to be changed
   * @param s String that will represents a cells content
   */
  void changeContent(Coord c, String s);

  /**
   * Extra Credit: Deletes a Cell at the given Coordinate or does nothing if no Cell exists.
   */
  void deleteCell(Coord c);

  /**
   * Extra Credit: Changes the models content to the given files content. File can only be a txt
   * file every other file gets ignored.
   *
   * @param filename the filename of the file being loaded into our spreadsheet
   */
  void changeFile(String filename);

  /**
   * Saves the current modified Spreadsheet into a new spreadsheet text file with the given name.
   * Non text files are ignored and nothing happens.
   *
   * @param filename the filename of the file that will be saved.
   */
  void saveFile(String filename);

}
