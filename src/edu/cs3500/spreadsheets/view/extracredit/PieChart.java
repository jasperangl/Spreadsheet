package edu.cs3500.spreadsheets.view.extracredit;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import edu.cs3500.spreadsheets.sexp.Parser;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


public class PieChart extends JFrame {

  private static final long serialVersionUID = 1L;
  private int x;
  private String chartTitle;
  private IWorksheetGetters model;
  private String inputRegion;
  private List<ICell> cells;
  protected int minCol;
  protected int maxCol;
  protected int minRow;
  protected int maxRow;

  public PieChart(String chartTitle, String range, IWorksheetGetters model) {
    super("Pie Chart");
    this.chartTitle = chartTitle;
    this.model = model;
    this.inputRegion = range;
    this.cells = getCells();
    PieDataset dataset = createDataset();
    JFreeChart chart = createChart(dataset, chartTitle);
    ChartPanel chartPanel = new ChartPanel(chart);
    // default size
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    // add it to our application
    setContentPane(chartPanel);
    this.pack();
    this.setVisible(true);
  }

  /**
   * Updates the data of a Chart.
   * @param model The model used to update the chart content
   */
  public void updateData(IWorksheetGetters model) {
    this.model = model;
    ChartPanel chartPanel = new ChartPanel(createChart(createDataset(), chartTitle));
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    this.setContentPane(chartPanel);
    this.pack();
  }

  /**
   * Gets all the Cells given in a rectangular region.
   * @return all the cells of a region
   * TODO: Maybe find a way to parse String inputs like "A1, C4, D12" and not only "A1:C4"
   */
  protected List<ICell> getCells() {
    Coord c1;
    Coord c2;
    if (inputRegion.contains(":")) {
      String[] los = inputRegion.split(":");
      if (los.length != 2) {
        throw new IllegalArgumentException("#InvalRef");
      }
      String[] firstCell = los[0].split("(?=\\d)(?<!\\d)");
      String[] secondCell = los[1].split("(?=\\d)(?<!\\d)");
      c1 = new Coord(Coord.colNameToIndex(firstCell[0]),
          Integer.parseInt(firstCell[1]));
      c2 = new Coord(Coord.colNameToIndex(secondCell[0]),
          Integer.parseInt(secondCell[1]));
      return rectRegion(c1,c2);
    }
    return new ArrayList<>();
  }

  private List<ICell> rectRegion(Coord c1, Coord c2) {
    List<ICell> loc = new ArrayList<>();
    minCol = Math.min(c1.col, c2.col);
    maxCol = Math.max(c1.col, c2.col);
    minRow = Math.min(c1.row, c2.row);
    maxRow = Math.max(c1.row, c2.row);
    for (int row = maxRow; row >= minRow; row--) {
      for (int col = maxCol; col >= minCol; col--) {
        Coord coord = new Coord(col, row);
        if (model.containsCell(coord)) {
          loc.add(model.getCellAt(coord));
        }
      }
    }
    return loc;
  }

  /**
   * Creates a Data set.
   * @return a Data Set
   * @throws IllegalArgumentException if evaluated cell content is illegal or invalid.
   */
  private PieDataset createDataset() throws IllegalArgumentException{
    DefaultPieDataset result = new DefaultPieDataset();
    try {
      for (ICell cell : cells) {
        String content = model.evaluate(cell.getCoord());
        int count = 0;
        for (ICell c : cells) {
          String cellCont = model.evaluate(c.getCoord());
          if (content.equals(cellCont)) {
            count++;
          }
        }
        result.setValue(content, count);
      }
    } catch (IllegalArgumentException e) {
      result.clear();
      result.setValue("Cannot draw a " + e.getMessage(), 1);
    }
    return result;
  }

  /**
   * Creates a chart
   */
  private JFreeChart createChart(PieDataset dataset, String title) {

    JFreeChart chart = ChartFactory.createPieChart3D(
        title,                  // chart title
        dataset,                // data
        true,                   // include legend
        true,
        false
    );

    PiePlot3D plot = (PiePlot3D) chart.getPlot();
    plot.setStartAngle(290);
    plot.setForegroundAlpha(0.5f);
    return chart;

  }
}