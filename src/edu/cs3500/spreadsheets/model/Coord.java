package edu.cs3500.spreadsheets.model;

import java.util.Objects;
import java.util.Set;

/**
 * A value type representing coordinates in a BasicWorksheetCell.
 */
public class Coord implements Comparable<Coord> {
  /**
   * Obtain the spanning coordinate from some provided list of coordinates.
   * @param options The options from which the max will be derived.
   * @return The max spanning coordinate.
   */
  public static Coord spanning(Set<Coord> options) {
    int maxWidth = 1;
    int maxHeight = 1;
    for (Coord c : options) {
      maxWidth = Math.max(c.col, maxWidth);
      maxHeight = Math.max(c.row, maxHeight);
    }
    return new Coord(maxWidth, maxHeight);
  }

  public final int row;
  public final int col;

  /**
   * Representing a coordinate in string form (e.g. A1)
   * @param representation Representation of the coordinate.
   */
  public Coord(String representation) {
    if (representation == null) {
      throw new IllegalArgumentException("Representation cannot be null!");
    }

    int separatorIndex = 0;
    while (separatorIndex < representation.length()
        && !Character.isDigit(representation.charAt(separatorIndex))) {
      if (!Character.isAlphabetic(representation.charAt(separatorIndex))) {
        throw new IllegalArgumentException("Malformed coordinate " + representation + "!");
      }
      separatorIndex++;
    }
    if (separatorIndex == representation.length() || separatorIndex == 0) {
      throw new IllegalArgumentException("Malformed coordinate " + representation + "!");
    }

    int col = colNameToIndex(representation.substring(0, separatorIndex));
    int row;
    try {
      row = Integer.parseInt(representation.substring(separatorIndex));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Expected number for row!");
    }

    this.row = row;
    this.col = col;
  }

  /**
   * A Coord instance to represent a row and column.
   * @param col  The column being represented.
   * @param row  The row being represented.
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
        && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public int compareTo(Coord other) {
    int rowBased = Integer.compare(this.row, other.row);
    if (rowBased == 0) {
      return Integer.compare(this.col, other.col);
    } else {
      return rowBased;
    }
  }
}
