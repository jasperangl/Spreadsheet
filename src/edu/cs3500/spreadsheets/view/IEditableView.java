package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Representing views that visually support editing of spreadsheets.
 */
public interface IEditableView extends WorksheetView {

  /**
   * Modifies the displayed content in a text area to the content of the cell at the given
   * coordinate.
   * @param c Coordinate of a Cell we display the content of
   */
  void setText(Coord c);

}
