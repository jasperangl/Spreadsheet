package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.awt.Color;
import java.util.List;

/**
 * Represents an interface of a Cell.
 */
public interface ICell {

  /**
   * Gets the coordinate of a Cell.
   *
   * @return the Coordinate of a Cell
   */
  Coord getCoord();

  /**
   * Gets the S-expression of a Cell representing the content.
   *
   * @return S-Expression of Cell
   */
  Sexp getSexp();

  /**
   * Gets the unformatted string content of a Cell.
   *
   * @return the unformatted string content of a Cell
   */
  String getContent();

  /**
   * Sets the unformatted string content of a Cell to the given one.
   */
  void setContent(String content);


  /**
   * Sets a Cells S-expression to the given Sexp.
   *
   * @param exp a S-Expression
   */
  void setSexp(Sexp exp);

  /**
   * Adds the coordinate of dependent cell.
   *
   * @param coord coordinate of dependent cell
   */
  void addDependency(Coord coord);

  /**
   * Gets the coordinate of dependent cell.
   */
  List<Coord> getDependencies();


  /**
   * Determines whether this Cell is part or contains a cyclic reference.
   *
   * @return whether there exists a cycle
   */
  boolean containsCycle();

  /**
   * Removes all the added dependencies from this cell.
   */
  void clearDependencies();

  boolean hasColor();

  Color getColor();

  void setColor(Color c);

}
