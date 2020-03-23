package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;


import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;

/**
 * Represents a class painting a spreadsheet based on the given model and cell rectWidth and
 * rectHeight.
 */
public class WorksheetPanel extends JPanel implements IWorkSheetPanel {

  protected IWorksheetGetters model;
  protected final int rectWidth;
  protected final int rectHeight;
  private final int incrementX;
  private final int incrementY;
  protected int width;
  protected int height;

  /**
   * Constructs a WorksheetPanel that creates a graphic of the spreadsheet that is used in
   * WorksheetVisualView.
   *
   * @param model the worksheet model only having access to non-mutating methods
   * @param rectWidth dedicated rectWidth of the cell
   * @param rectHeight dedicated rectHeight of the cell
   */
  public WorksheetPanel(IWorksheetGetters model, int rectWidth, int rectHeight) {
    this.model = model;
    this.rectWidth = rectWidth;
    this.rectHeight = rectHeight;
    this.incrementX = (int) (rectWidth * 0.1);
    this.incrementY = (int) (rectHeight * 0.6);
    this.setFont(new Font("Arial", Font.PLAIN, rectHeight / 3));
    this.width = model.getMaxColumn();
    this.height = model.getMaxRow();
  }

  /**
   * Overridden built in method that draws the graphic.
   *
   * @param g graphic that is built upon
   */
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    drawBorder(g2d);
    drawGrid(g2d);
  }

  /**
   * Draws the green border with the row and column labels.
   *
   * @param g2d the Graphic these cells get added onto
   */
  protected void drawBorder(Graphics2D g2d) {
    int x = 0;
    int y = 0;
    for (int i = 1; i <= width + 1; i++) {
      g2d.setColor(new Color(0, 130, 0));
      g2d.fillRect(x, y, rectWidth, rectHeight);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, rectWidth, rectHeight);
      g2d.setColor(Color.WHITE);
      String s = Coord.colIndexToName(i - 1);
      g2d.drawString(s, x + rectWidth / 2, y + rectHeight / 2);
      x = x + rectWidth;
    }
    x = 0;
    y = rectHeight;
    for (int i = 1; i <= height; i++) {
      g2d.setColor(new Color(0, 130, 0));
      g2d.fillRect(x, y, rectWidth, rectHeight);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(x, y, rectWidth, rectHeight);
      g2d.setColor(Color.WHITE);
      String s = Integer.toString(i);
      g2d.drawString(s, x + rectWidth / 2, y + rectHeight / 2);
      y = y + rectHeight;
    }
  }

  /**
   * Draws a grid of cells representing the spreadsheet graphic. If the cells contain data, the data
   * is displayed and fitted to the cell.
   *
   * @param g2d the Graphic these cells get drawn to
   */
  protected void drawGrid(Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    int x = rectWidth;
    int y = rectHeight;
    for (int i = 1; i <= width; i++) {
      for (int j = 1; j <= height; j++) {
        g2d.setClip(null);
        g2d.clipRect(x, y, rectWidth, rectHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, rectWidth, rectHeight);
        Coord coord = new Coord(i, j);
        String s = "";
        try {
          if (model.containsCell(coord)) {
            if (model.getCellAt(coord).hasColor()) {
              if (model.getCellAt(coord).getSexp() == null) {
                g2d.setColor(model.getCellAt(coord).getColor());
                g2d.fillRect(x + 1, y + 1, rectWidth - 1, rectHeight - 1);
                g2d.setColor(Color.BLACK);
                s = "";
                g2d.setColor(Color.WHITE);
              } else {
                g2d.setColor(model.getCellAt(coord).getColor());
                g2d.fillRect(x + 1, y + 1, rectWidth - 1, rectHeight - 1);
                g2d.setColor(Color.BLACK);
                s = model.evaluate(coord);
                g2d.setColor(Color.WHITE);
              }
            } else {
              ICell cell = model.getCellAt(coord);
              s = model.evaluate(cell.getCoord());
            }
          }
        } catch (IllegalArgumentException e) {
            s = e.getMessage();
          }
        g2d.drawString(s, x + incrementX, y + incrementY);
        g2d.clipRect(x, y, rectWidth, rectHeight);
        y = y + rectHeight;
      }
      x = x + rectWidth;
      y = rectHeight;
    }
  }

  @Override
  public void initHighlight() {
    throw new IllegalArgumentException("Never used");
  }

  @Override
  public void updateWidth() {
    width += 2;
    int w = rectWidth * (1 + width);
    int h = rectHeight * (1 + height);
    this.setPreferredSize(new Dimension(w, h));
  }

  @Override
  public void updateHeight() {
    height += 2;
    int w = rectWidth * (1 + width);
    int h = rectHeight * (1 + height);
    this.setPreferredSize(new Dimension(w, h));
  }

  @Override
  public int getterHeight() {
    int retval = height;
    return retval;
  }

  @Override
  public int getterWidth() {
    int retval = width;
    return retval;
  }

}