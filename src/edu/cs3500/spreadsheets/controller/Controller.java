package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Represents the controller of an interactive spreadsheet. It enable the functionalities to modify
 * cells of current spreadsheet, deleting a cell from the model, loading in a new .txt file
 * representing a spreadsheet and saving a spreadsheet masterpiece.
 */
public class Controller implements IController {

  private IWorksheetModel model;
  private WorksheetBuilder<IWorksheetModel> builder;

  /**
   * Constructs a spreadsheet controller with the ability to modify a worksheet model and save and
   * open new Files.
   *
   * @param model the worksheet model passed in and to be modified
   * @param builder a Builder enabling to create new Cells for a spreadsheet
   */
  public Controller(IWorksheetModel model, WorksheetBuilder<IWorksheetModel> builder) {
    if (builder == null || model == null) {
      throw new IllegalArgumentException("Inputs cannot be null");
    }
    this.builder = builder;
    this.model = model;
  }

  @Override
  public void deleteCell(Coord c) {
    if (model.containsCell(c)) {
      model.getSheet().remove(c);
    }
  }

  @Override
  public void changeFile(String filename) {
    if (filename.contains(".txt")) {
      Readable cFile = null;
      try {
        cFile = new FileReader(filename);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      this.model.getSheet().clear();
      this.model = WorksheetReader.read(this.builder, cFile);
    }
  }

  @Override
  public void saveFile(String filename) {
    if (filename.contains(".txt")) {
      PrintWriter writer;
      try {
        writer = new PrintWriter(filename);
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("Cannot find file");
      }
      WorksheetView view = new WorksheetTextualView(model, writer);
      view.render();
      writer.close();
    }
  }

  @Override
  public void changeContent(Coord c, String s) {
    model.changeCellContent(c,s);
  }
}
