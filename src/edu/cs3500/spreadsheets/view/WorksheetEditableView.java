package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.IController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetGetters;
import edu.cs3500.spreadsheets.view.extracredit.LineChart;
import edu.cs3500.spreadsheets.view.extracredit.PieChart;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

/**
 * Represents an editable view with the ability to modify cells contents, deleting cells, loading in
 * different spreadsheets and saving the current spreadsheet.
 */
public class WorksheetEditableView extends JFrame implements IEditableView, ActionListener {

  private final String BUTTON_1 = "B1";
  private final String BUTTON_2 = "B2";
  private final String BUTTON_3 = "Open";
  private final String BUTTON_4 = "Save";
  private int cellWidth = 100;
  private int cellHeight = 40;
  private int windowWidth = 800;
  private int windowHeight = 500;
  private JPanel toolBarPanel;
  private JButton b1;
  private JButton b2;
  private JButton b3;
  private JButton b4;
  private JButton b5;
  private JTextField textField;
  private WorksheetEditPanel worksheetPanel;
  private IWorksheetGetters model;
  private IController controller;
  private JScrollPane scrollPane;
  private JMenuBar chartBar;
  private JMenuBar colorBar;
  private PieChart pc;
  private LineChart lc;
  private JTextField cellRangeTf;
  private JTextField chartNameTf;
  private JFrame popupMenu;
  private String chartName;

  /**
   * Construct a visual view of our worksheet.
   *
   * @param model the model that gets visually represented as a sheet
   */
  public WorksheetEditableView(IWorksheetGetters model, IController controller) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    this.controller = controller;

    int sheetXSize = cellWidth * (1 + model.getMaxColumn());
    int sheetYSize = cellHeight * (1 + model.getMaxRow());

    this.initButtonAndTextField();
    this.initChartChooser();
    this.initColorChooser();
    this.initToolBar();

    this.setTitle("Worksheet");
    this.setSize(sheetXSize, sheetYSize);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    worksheetPanel = new WorksheetEditPanel(this, model, cellWidth, cellHeight);
    worksheetPanel.setPreferredSize(new Dimension(sheetXSize, sheetYSize));
    this.addMouseListener(worksheetPanel);
    this.addKeyListener(new MyKeyListener());
    this.setFocusable(true);
    this.initScrollBar();
  }

  @Override
  public void render() {
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void setText(Coord c) {
    String content;
    try {
      content = model.getCellAt(c).getContent();
      this.textField.setText(content);
    } catch (IllegalArgumentException e) {
      this.textField.setText("...");
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    Coord c = this.worksheetPanel.getCoord();
    switch (command) {
      case BUTTON_1:
        try {
          controller.changeContent(c, textField.getText());
        } catch (IllegalArgumentException exc) {
          textField.setText(exc.getMessage());
        }
        if (pc != null) {
          pc.updateData(model);
        }
        if (lc != null) {
          lc.updateData(model);
        }
        this.repaint();
        this.requestFocus();
        break;
      case BUTTON_2:
        this.setText(c);
        if (model.containsCell(c)) {
          this.textField.setText(model.getCellAt(c).getContent());
          this.repaint();
        }
        this.requestFocus();
        break;
      case BUTTON_3:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new TextFileFilter());
        fileChooser.setCurrentDirectory(new java.io.File("."));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          controller.changeFile(selectedFile.getName());
          worksheetPanel.initHighlight();
          this.repaint();
        }
        break;
      case BUTTON_4:
        JFileChooser fileChooserSave = new JFileChooser();
        fileChooserSave.setCurrentDirectory(new java.io.File("."));
        fileChooserSave.addChoosableFileFilter(new TextFileFilter());
        int returnVal = fileChooserSave.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooserSave.getSelectedFile();
          controller.saveFile(selectedFile.getName());
        }
        break;
      case "PieChart":
      case "BarChart":
      case "LineChart":
        this.popUpWindow(command);
        break;
      case "Draw PieChart":
        pc = new PieChart(chartNameTf.getText(), cellRangeTf.getText(), model);
        popupMenu.dispatchEvent(new WindowEvent(popupMenu, WindowEvent.WINDOW_CLOSING));
        break;
      case "Draw LineChart":
        lc = new LineChart(chartNameTf.getText(), "X-Axis", "Y-Axis", cellRangeTf.getText(),
            model);
        popupMenu.dispatchEvent(new WindowEvent(popupMenu, WindowEvent.WINDOW_CLOSING));
        break;
      case "Red":
        this.setCellColor(new Color(250, 143, 91));
        this.repaint();
        break;
      case "Blue":
        this.setCellColor(new Color(100, 170, 250));
        this.repaint();
        break;
      case "Light Green":
        this.setCellColor(new Color(148, 220, 120));
        this.repaint();
        break;
      case "Blank":
        this.setCellColor(Color.WHITE);
        this.repaint();
        break;
      default:
        throw new IllegalArgumentException("Illegal ActionCommand");
    }

  }

  private void initScrollBar() {
    scrollPane = new JScrollPane(worksheetPanel,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(windowWidth, windowHeight));
    scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    ChangeListener verticalListener = e -> {
      JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
      if (scrollBar.getValue() + scrollBar.getVisibleAmount() == scrollBar.getMaximum()) {
        worksheetPanel.updateHeight();
      }
    };
    ChangeListener horizontallistener = e -> {
      JScrollBar scrollBar = scrollPane.getHorizontalScrollBar();
      if (scrollBar.getValue() + scrollBar.getVisibleAmount() == scrollBar.getMaximum()) {
        worksheetPanel.updateWidth();
      }
    };
    scrollPane.getViewport().addChangeListener(horizontallistener);
    scrollPane.getViewport().addChangeListener(verticalListener);
    this.add(scrollPane);
  }

  private void popUpWindow(String command) {
    popupMenu = new JFrame(command);
    JToolBar cellRange = new JToolBar();
    JToolBar cellTitle = new JToolBar();
    cellRangeTf = new JTextField("Enter cell range");
    chartNameTf = new JTextField("Enter chart title");
    b5 = new JButton("Draw");
    b5.setActionCommand("Draw " + command);
    b5.addActionListener(this);
    popupMenu.setPreferredSize(new Dimension(200, 220));
    cellRange.add(cellRangeTf);
    cellTitle.add(chartNameTf);
    cellRange.setPreferredSize(new Dimension(200, 60));
    cellTitle.setPreferredSize(new Dimension(200, 60));
    b5.setPreferredSize(new Dimension(200, 60));
    popupMenu.add(b5, BorderLayout.SOUTH);
    popupMenu.add(cellTitle, BorderLayout.CENTER);
    popupMenu.add(cellRange, BorderLayout.NORTH);
    popupMenu.setVisible(true);
    popupMenu.pack();

  }

  private void initButtonAndTextField() {
    b1 = new JButton("Edit");
    b2 = new JButton("Restore");
    b3 = new JButton("Open File");
    b4 = new JButton("Save File");
    b1.setActionCommand(BUTTON_1);
    b1.addActionListener(this);
    b2.setActionCommand(BUTTON_2);
    b2.addActionListener(this);
    b3.setActionCommand(BUTTON_3);
    b3.addActionListener(this);
    b4.setActionCommand(BUTTON_4);
    b4.addActionListener(this);
    textField = new JTextField("...");
  }

  private void initToolBar() {
    toolBarPanel = new JPanel();
    toolBarPanel.add(b3);
    toolBarPanel.add(b4);
    toolBarPanel.add(b1);
    toolBarPanel.add(b2);
    JToolBar tb = new JToolBar("Toolbar");
    tb.add(toolBarPanel);
    tb.add(textField);
    tb.add(colorBar);
    tb.add(chartBar);
    add(tb, BorderLayout.NORTH);
  }

  private void initChartChooser() {
    chartBar = new JMenuBar();
    JMenu menu = new JMenu("Chart");
    JMenuItem item = new JMenuItem("Pie Chart");
    item.setActionCommand("PieChart");
    item.addActionListener(this);
    JMenuItem item2 = new JMenuItem("Line Chart");
    item2.setActionCommand("LineChart");
    item2.addActionListener(this);
    JMenuItem item3 = new JMenuItem("Bar Chart");
    item3.setActionCommand("BarChart");
    item3.addActionListener(this);
    menu.add(item);
    menu.add(item3);
    menu.add(item2);
    chartBar.add(menu);
  }

  private void initColorChooser() {
    colorBar = new JMenuBar();
    JMenu menu = new JMenu("Color");
    JMenuItem item = new JMenuItem("Red");
    item.setActionCommand("Red");
    item.addActionListener(this);
    JMenuItem item2 = new JMenuItem("Light Blue");
    item2.setActionCommand("Blue");
    item2.addActionListener(this);
    JMenuItem item3 = new JMenuItem("Light Green");
    item3.setActionCommand("Light Green");
    item3.addActionListener(this);
    JMenuItem item4 = new JMenuItem("Blank");
    item4.setActionCommand("Blank");
    item4.addActionListener(this);
    menu.add(item);
    menu.add(item2);
    menu.add(item3);
    menu.add(item4);
    colorBar.add(menu);
  }


  /**
   * Sets the background color of the current cell.
   * If color is white it removes the background color.
   * If cell to have color doesn't exist yet it gets created.
   *
   * @param c The color the cell will change to
   */
  private void setCellColor(Color c) {
   Coord coord = worksheetPanel.getCoord();
      if (!model.containsCell(coord)) {
        controller.changeContent(coord, "");
      }
      if (c.equals(Color.WHITE)) {
        model.getCellAt(coord).setColor(null);
      } else {
        model.getCellAt(coord).setColor(c);
      }
  }


  private class MyKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      // all operations on keyPressed.
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_DELETE) {
        Coord c = worksheetPanel.getCoord();
        controller.deleteCell(c);
        textField.setText("...");
        repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        worksheetPanel.moveLeft();
        Coord c = worksheetPanel.getCoord();
        setText(c);
        repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        worksheetPanel.moveRight();
        Coord c = worksheetPanel.getCoord();
        setText(c);
        repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_UP) {
        worksheetPanel.moveUp();
        Coord c = worksheetPanel.getCoord();
        setText(c);
        repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        worksheetPanel.moveDown();
        Coord c = worksheetPanel.getCoord();
        setText(c);
        repaint();
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      // all operations on keyPressed.
    }
  }

  // Makes it so that only textFiles are allowed to be accessed through the File chooser when that
  // option is selected.
  private class TextFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
      return f.getName().contains(".txt");
    }

    @Override
    public String getDescription() {
      return "TextFileFilter";
    }
  }


}