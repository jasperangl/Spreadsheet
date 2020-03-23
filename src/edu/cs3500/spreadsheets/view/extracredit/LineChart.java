package edu.cs3500.spreadsheets.view.extracredit;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart extends PieChart {

  private String chartTitle;
  private String xLabel;
  private String yLabel;
  private IWorksheetGetters model;
  private String inputRegion;
  private List<ICell> cells;

  public LineChart(String title, String xLabel, String yLabel, String range,
      IWorksheetGetters model) {
    super(title, range, model);
    this.chartTitle = title;
    this.model = model;
    this.inputRegion = range;
    this.xLabel = xLabel;
    this.yLabel = yLabel;
    this.cells = super.getCells();
    JFreeChart lineChart;
    try {
      lineChart = ChartFactory
          .createLineChart(title, xLabel, yLabel, this.createDataSet(),
              PlotOrientation.VERTICAL, true, true, false);
    } catch (NumberFormatException e) {
      lineChart = ChartFactory
          .createLineChart("Y-Input has to be numeric!", xLabel, yLabel, new DefaultCategoryDataset(),
              PlotOrientation.VERTICAL, true, true, false);
    }
    ChartPanel chartPanel = new ChartPanel(lineChart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    setContentPane(chartPanel);
    pack();
    setVisible(true);
  }

  /**
   * Updates the data of a Chart.
   *
   * @param model The model used to update the chart content
   */
  public void updateData(IWorksheetGetters model) {
    this.model = model;
    JFreeChart lineChart;
    try {
      lineChart = ChartFactory
          .createLineChart(chartTitle, xLabel, yLabel, this.createDataSet(),
              PlotOrientation.VERTICAL, true, true, false);
    } catch (NumberFormatException e) {
      lineChart = ChartFactory
          .createLineChart("Y-Input has to be numeric!", xLabel, yLabel, new DefaultCategoryDataset(),
              PlotOrientation.VERTICAL, true, true, false);
    }
    ChartPanel chartPanel = new ChartPanel(lineChart);
    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    this.setContentPane(chartPanel);
    this.pack();
  }

  private CategoryDataset createDataSet() {
    DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    List<Double> yValues = new ArrayList<>();
    List<String> xValues = new ArrayList<>();
    for (int col = minCol; col <= maxCol; col++) {
      for (int row = minRow; row <= maxRow; row++) {
        Coord c = new Coord(col, row);
        String content = model.evaluate(c);
        if (model.containsCell(c)) {
          if (col == maxCol) {
            yValues.add(Double.parseDouble(content));
          }
          if (col == minCol) {
            xValues.add(content);
          }
        }
      }
    }
    for (int i = 0; i < xValues.size(); i++) {
      dataSet.addValue(yValues.get(i), yLabel, xValues.get(i));
    }
    return dataSet;
  }
}
