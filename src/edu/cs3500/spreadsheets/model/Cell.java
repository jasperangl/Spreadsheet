package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Cell of a Spreadsheet.
 */
public class Cell implements ICell {

  private final Coord coord;
  private Sexp expression;
  private List<Coord> dependencies;
  private String contents;
  private Color color;

  /**
   * Constructs a Cell.
   *
   * @param sexp A cells S-Expression
   * @param coord A cells coordinate
   */
  public Cell(Sexp sexp, Coord coord) {
    this.expression = sexp;
    this.coord = coord;
    this.dependencies = new ArrayList<>(Collections.singletonList(coord));
    //A list of Coordinates that are not allowed to be accessed because they would
    // cause cyclic references.
  }

  @Override
  public Coord getCoord() {
    return this.coord;
  }

  @Override
  public String getContent() {
    return this.contents;
  }

  @Override
  public void setContent(String content) {
    this.contents = content;
  }

  @Override
  public Sexp getSexp() {
//    if (this.expression == null) {
//      throw new IllegalArgumentException("Tried to get Cells Sexp before evaluating it");
//    }
    return this.expression;
  }

  @Override
  public void setSexp(Sexp exp) {
    this.expression = exp;
  }

  @Override
  public void addDependency(Coord c) {
    this.dependencies.add(c);
  }

  @Override
  public List<Coord> getDependencies() {
    return this.dependencies;
  }

  @Override
  public boolean containsCycle() {
    Set<Coord> dependent = new HashSet<>(this.dependencies);
    return this.dependencies.size() > dependent.size();
  }

  @Override
  public void clearDependencies() {
    this.dependencies.clear();
    this.dependencies.add(this.coord);
  }

  @Override
  public boolean hasColor() {
    return color != null;
  }

  @Override
  public Color getColor() {
    if (color == null) {
      throw new IllegalArgumentException("Color not assigned");
    }
    return this.color;
  }

  @Override
  public void setColor(Color c) {
    color = c;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Cell)) {
      return false;
    }
    return ((Cell) o).coord == this.coord
        && ((Cell) o).expression == this.expression
        && ((Cell) o).dependencies == this.dependencies
        && ((Cell) o).contents.equals(this.contents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coord,expression,dependencies,contents);
  }

}
