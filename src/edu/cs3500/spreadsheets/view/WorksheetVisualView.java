package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

/**
 * Represents a Visual view of our worksheet. It is visualized in cells. White Cells represents the
 * content cells and green cells represents a column or row index.
 */
public class WorksheetVisualView extends JFrame implements WorksheetView {

  private int cellWidth = 100;
  private int cellHeight = 40;
  private int windowWidth = 1100;
  private int windowHeight = 700;

  /**
   * Construct a visual view of our worksheet.
   *
   * @param model the model that gets visually represented as a sheet
   */
  public WorksheetVisualView(IWorksheetGetters model) {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    int sheetXSize = cellWidth * (1 + model.getMaxColumn());
    int sheetYSize = cellHeight * (1 + model.getMaxRow());

    this.setTitle("Worksheet");
    this.setSize(sheetXSize, sheetYSize);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    WorksheetPanel worksheetPanel = new WorksheetPanel(model, cellWidth, cellHeight);
    worksheetPanel.setPreferredSize(new Dimension(sheetXSize, sheetYSize));
    JScrollPane scrollPane = new JScrollPane(worksheetPanel,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(windowWidth, windowHeight));
    scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    this.add(scrollPane);
    this.pack();
  }

  @Override
  public void render() {
    this.setVisible(true);
  }
}