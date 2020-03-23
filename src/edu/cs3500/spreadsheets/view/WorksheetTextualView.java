package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import java.io.IOException;

/**
 * Represents a textual view of a worksheet. It is used to output a new file copying the old one but
 * also evaluates every cells content.
 */
public class WorksheetTextualView implements WorksheetView {

  private final IWorksheetGetters model;
  private Appendable out;

  /**
   * Constructs a Textual View meant to output a text file identical to the given text file to the
   * model.
   *
   * @param model the worksheet model only having access to non-mutating methods
   */
  public WorksheetTextualView(IWorksheetGetters model, Appendable ap) {
    if (model == null || ap == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    this.out = ap;
  }

  @Override
  public void render() {
    try {
      this.out.append(toString());
    } catch (IOException e) {
      throw new IllegalArgumentException("IO Exception");
    }
  }

  @Override
  public String toString() {
    String temp = "";
    for (ICell cell : this.model.getSheet().values()) {
      String s = cell.getContent();
      try {
        temp += String.format("%f", Double.parseDouble(temp)) + "\n";
      } catch (NumberFormatException ignored) {
      }
      temp += cell.getCoord() + " " + s + "\n";
    }

    return temp;
  }
}
