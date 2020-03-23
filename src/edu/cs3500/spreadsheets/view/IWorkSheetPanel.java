package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import java.util.List;

/**
 * Represents a worksheet panel that creates a basic spreadsheet graphic.
 */
public interface IWorkSheetPanel {

  /**
   * Updates the height of the current spreadsheet by adding two rows. Updates the dimensions to
   * accommodate for the bigger spreadsheet. Occurs when the scroll bar reaches the max.
   */
  void updateHeight();

  /**
   * Updates the width of the current spreadsheet by adding two columns. Updates the dimensions to
   * accommodate for the bigger spreadsheet. Occurs when the scroll bar reaches the max.
   */
  void updateWidth();

  /**
   * Sets the mouse coordinates to (-1, -1) so that the highlight box is not displayed.
   */
  void initHighlight();

  /**
   * Gets the current height of the spreadsheet.
   * @return the height
   */
  int getterHeight();

  /**
   * Gets the current width of the spreadsheet.
   * @return the width
   */
  int getterWidth();
}
