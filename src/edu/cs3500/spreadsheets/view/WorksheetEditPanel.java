package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * Represents a worksheet edit panel that draws the graphic for an updatable and editable
 * spreadsheet.
 */
public class WorksheetEditPanel extends WorksheetPanel implements MouseListener {

  private int mouseX;
  private int mouseY;
  private int coordCol;
  private int coordRow;
  private Graphics2D g2d;
  private IEditableView view;
  private Coord initCoord;
  private Coord endCoord;


  /**
   * Constructs a WorksheetPanel that creates a graphic of the spreadsheet that is used in
   * WorksheetVisualView.
   *
   * @param model the worksheet model only having access to non-mutating methods
   * @param width dedicated width of the cell
   * @param height dedicated height of the cell
   */
  public WorksheetEditPanel(IEditableView view, IWorksheetGetters model, int width,
      int height) {
    super(model, width, height);
    this.view = view;
    addMouseListener(this);
    mouseX = -1;
    mouseY = -1;
  }

  /**
   * Overridden built in method that draws the graphic.
   *
   * @param g graphic that is built upon
   */
  @Override
  protected void paintComponent(Graphics g) {
    g2d = (Graphics2D) g;
    g2d.clearRect(0, 0, rectWidth * width, rectHeight * height);
    super.drawBorder(g2d);
    drawClickedCell();
    super.drawGrid(g2d);
  }

  /**
   * On right arrow key press, moves the mouse position towards the right and updates the
   * spreadsheet.
   */
  protected void moveRight() {
    mouseX = mouseX + rectWidth;
    this.setCoord(mouseX, mouseY);
    this.repaint();
  }

  /**
   * On up arrow key press, moves the mouse position upwards and updates the spreadsheet.
   */
  protected void moveUp() {
    mouseY = mouseY - rectHeight;
    this.setCoord(mouseX, mouseY);
    this.repaint();
  }

  /**
   * On left arrow key press, moves the mouse position towards the left and updates the
   * spreadsheet.
   */
  protected void moveLeft() {
    mouseX = mouseX - rectWidth;
    this.setCoord(mouseX, mouseY);
    this.repaint();
  }

  /**
   * On down arrow key press, moves the mouse position downwards and updates the spreadsheet.
   */
  protected void moveDown() {
    mouseY = mouseY + rectHeight;
    this.setCoord(mouseX, mouseY);
    this.repaint();
  }

  /**
   * Highlights the selected cell.
   */
  private void drawClickedCell() {
    if (mouseX != -1 && mouseY != -1) {
      if (initCoord.equals(endCoord)) {
        g2d.setColor(new Color(0, 140, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(coordCol * rectWidth + 2, coordRow * rectHeight + 1, rectWidth - 3,
            rectHeight - 2);
        g2d.setStroke(new BasicStroke(1));
        //g2d.fillRect(coordCol * rectWidth, coordRow * rectHeight, rectWidth, rectHeight);
      }
      else {
        g2d.setColor(new Color(0, 140, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(initCoord.col * rectWidth + 2, initCoord.row * rectHeight + 1, (endCoord.col - initCoord.col + 1) * rectWidth - 3,
            (endCoord.row - initCoord.row + 1) * rectHeight - 2);
        g2d.setStroke(new BasicStroke(1));
      }
    }
  }

  /**
   * Uses the mouse x and y coordinates and sets the column and row coordinates on the spreadsheet.
   */
  private void setCoord(int x, int y) {
    if (mouseY < rectHeight) {
      mouseY += rectHeight / 2;
    }
    if (mouseX < rectHeight) {
      mouseX += rectWidth / 2;
    } else {
      int col = ((x - rectWidth) / rectWidth) + 1;
      int row = ((y - rectHeight) / rectHeight) + 1;
      coordCol = col;
      coordRow = row;
    }
  }

  /**
   * Gets the current coordinate.
   * @return the current coordinate
   */
  public Coord getCoord() {
    setCoord(mouseX, mouseY);
    if (coordRow == 0 || coordCol == 0) {
      return new Coord(1, 1);
    } else {
      return new Coord(coordCol, coordRow);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
//    this.repaint();
//    if (e.getY() > rectHeight && e.getX() > rectWidth) {
//      mouseX = e.getX();
//      mouseY = e.getY();
//      this.setCoord(mouseX, mouseY);
//      view.setText(this.getCoord());
//      this.repaint();
//    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getY() > rectHeight && e.getX() > rectWidth) {
      mouseX = e.getX();
      mouseY = e.getY();
      this.setCoord(mouseX, mouseY);
      initCoord = this.getCoord();
      view.setText(this.getCoord());
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getY() > rectHeight && e.getX() > rectWidth) {
      mouseX = e.getX();
      mouseY = e.getY();
      this.setCoord(mouseX, mouseY);
      endCoord = this.getCoord();
      view.setText(this.getCoord());
      this.repaint();
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // we have to override this but mouseClicked does all functionality.
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // we have to override this but mouseClicked does all functionality.
  }

  @Override
  public void initHighlight() {
    mouseY = -1;
    mouseX = -1;
  }
}