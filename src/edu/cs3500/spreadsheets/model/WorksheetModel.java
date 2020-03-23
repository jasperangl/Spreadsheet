package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.cellcontent.functions.Evaluate;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.HashMap;

/**
 * Represents a grid worksheet. It is made up of cells that are able to refer to each other
 * represent Values and can perform Functions.
 */
public class WorksheetModel implements IWorksheetModel {

  private HashMap<Coord, ICell> sheet;

  /**
   * Constructs a Worksheet using the builder pattern.
   *
   * @param builder A worksheet builder.
   */
  public WorksheetModel(Builder builder) {
    if (builder == null) {
      throw new IllegalArgumentException("Builder cannot be null");
    }
    this.sheet = builder.sheet;
  }

  @Override
  public HashMap<Coord, ICell> getSheet() {
    return this.sheet;
  }

  @Override
  public String evaluate(Coord coord) throws IllegalArgumentException {
    if (containsCell(coord)) {
      // Evaluate takes in the coord to add it to the illegal reference Coordinates.
      String output = "";
      output += this.getCellAt(coord).getSexp().accept(new Evaluate(this, this.getCellAt(coord)));
      this.clearDependencies();
      return output;
    }
    throw new IllegalArgumentException("Cell trying to be evaluated doesn't exist");
  }

  @Override
  public ICell getCellAt(Coord coord) throws IllegalArgumentException {
    if (!this.containsCell(coord)) {
      throw new IllegalArgumentException("#DNA");
    }
    return sheet.get(coord);
  }

  @Override
  public int getMaxColumn() {
    int max = 10;
    for (Coord coord : sheet.keySet()) {
      int check = coord.col;
      max = Math.max(max, check);
    }
    return max;
  }

  @Override
  public int getMaxRow() {
    int max = 10;
    for (Coord coord : sheet.keySet()) {
      int check = coord.row;
      max = Math.max(max, check);
    }
    return max;
  }

  @Override
  public void changeCellContent(Coord coord, String contents) {
      Cell temp;
      try {
        if (contents.equals("")) {
          temp = new Cell(null, coord);
          temp.setContent(contents);
          sheet.put(coord, temp);
        }
        else if (contents.substring(0, 1).equals("=")) {
          Sexp exp = Parser.parse(contents.substring(1));
          temp = new Cell(exp, coord);
          temp.setContent(contents);
          sheet.put(coord, temp);
        } else {
          Sexp exp = Parser.parse(contents);
          temp = new Cell(exp, coord);
          temp.setContent(contents);
          sheet.put(coord, temp);
        }
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("#InvalInput");
      }
  }

  @Override
  public void clearDependencies() {
    for (ICell cell : sheet.values()) {
      cell.clearDependencies();
    }
  }

  @Override
  public boolean containsCell(Coord coord) {
    return sheet.containsKey(coord);
  }


  /**
   * Represents a Builder for a Worksheet.
   */
  public static final class Builder implements WorksheetBuilder<IWorksheetModel> {

    private final HashMap<Coord, ICell> sheet;

    /**
     * Constructs a builder used to instantiate a Worksheet.
     */
    public Builder() {
      this.sheet = new HashMap<>();
    }

    @Override
    public WorksheetBuilder<IWorksheetModel> createCell(int col, int row, String contents) {
      Cell temp;
      Coord currC = new Coord(col, row);
      if (contents.substring(0, 1).equals("=")) {
        Sexp exp = Parser.parse(contents.substring(1));
        // Formula Cell
        temp = new Cell(exp, currC);
        temp.setContent(contents);
        sheet.put(currC, temp);
      } else {
        Sexp exp = Parser.parse(contents);
        temp = new Cell(exp, currC);
        temp.setContent(contents);
        sheet.put(currC, temp);
      }
      return this;
    }

    @Override
    public IWorksheetModel createWorksheet() {
      return new WorksheetModel(this);
    }
  }


}
