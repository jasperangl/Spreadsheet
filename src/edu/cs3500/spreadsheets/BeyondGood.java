package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.controller.IController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel.Builder;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;
import edu.cs3500.spreadsheets.view.WorksheetVisualView;


import edu.cs3500.spreadsheets.view.extracredit.PieChart;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The main class for our Spreadsheet.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    WorksheetModel.Builder builder = new WorksheetModel.Builder();
    IWorksheetModel model = new WorksheetModel(builder);
    WorksheetView frame = new WorksheetVisualView(model);

    label:
    switch (args[0]) {
      case "-in":
        switch (args[2]) {
          case "-eval":
            evalCell(args);
            break label;
          case "-save":
            saveNewFile(args);
            break label;
          case "-gui":
            visualizeSheet(args);
            break label;
          case "-edit":
            visualizeEditSheet(args);
            break;
          case "-chart":
            drawChart(args);
            break;
          default:
            throw new IllegalArgumentException("invalid args[2]");
        }
        break;
      case "-gui":
        frame.render();
        break;
      default:
        throw new IllegalArgumentException("invalid args[0]");
    }
  }

  private static void visualizeSheet(String[] args) {
    WorksheetBuilder<IWorksheetModel> builder = new Builder();
    Readable file;
    try {
      file = new FileReader(args[1]);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    WorksheetView frame = new WorksheetVisualView(model);
    frame.render();
  }

  private static void visualizeEditSheet(String[] args) {
    WorksheetBuilder<IWorksheetModel> builder = new Builder();
    Readable file;
    try {
      file = new FileReader(args[1]);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    IController controller = new Controller(model, builder);
    WorksheetView frame = new WorksheetEditableView(model, controller);
    frame.render();
  }

  private static void saveNewFile(String[] args) {
    WorksheetBuilder<IWorksheetModel> builder = new Builder();
    Readable file;
    PrintWriter writer;
    try {
      file = new FileReader(args[1]);
      writer = new PrintWriter(args[3]);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    WorksheetView view = new WorksheetTextualView(model, writer);
    view.render();
    writer.close();
    System.exit(0);
  }

  private static void evalCell(String[] args) {
    Readable file;
    try {
      file = new FileReader(args[1]);
    } catch (IOException e) {
      throw new IllegalStateException("IO Exception");
    }
    WorksheetBuilder<IWorksheetModel> builder = new Builder();
    IWorksheetModel model = WorksheetReader.read(builder, file);
    Coord c = stringToCoord(args[3]);
    String s = model.evaluate(c);
    try {
      s = String.format("%f", Double.parseDouble(s));
    } catch (NumberFormatException ignored) {
    }
    System.exit(0);
  }

  private static void drawChart(String [] args) {
    WorksheetBuilder<IWorksheetModel> builder = new Builder();
    Readable file;
    try {
      file = new FileReader(args[1]);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    PieChart demo = new PieChart("Which operating system are you using?", "A1:A6", model);
  }

  /**
   * Determines a Coordinate from a String.
   *
   * @param s Given String representation of a Coordinate
   * @return a Coordinate
   */
  private static Coord stringToCoord(String s) {
    String[] onlyCell = s.split("(?=\\d)(?<!\\d)");
    return new Coord(Coord.colNameToIndex(onlyCell[0]), Integer.parseInt(onlyCell[1]));
  }

}