package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.cellcontent.ValueBoolean;
import edu.cs3500.spreadsheets.model.cellcontent.ValueDouble;
import edu.cs3500.spreadsheets.model.cellcontent.ValueString;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Represents a Class that is used to Evaluate to Cells S-Expression and either returns a Value or
 * delegates to other methods that handle references and functions.
 */
public class Evaluate extends AFunction<String> {

  /**
   * Constructs an Evaluation.
   *
   * @param model a worksheet model
   * @param cell the current cell we are working on
   */
  public Evaluate(IWorksheetModel model, ICell cell) {
    super(model, cell);
  }

  @Override
  public String visitBoolean(boolean b) {
    return new ValueBoolean(b).toString();
  }

  @Override
  public String visitNumber(double d) {
    return new ValueDouble(d).toString();
  }

  @Override
  public String visitSList(List<Sexp> l) {
    return this.sListHelper(l).toString();
  }

  @Override
  public String visitSymbol(String s) {
    if (currCell.getContent().substring(0,1).equals("=")) {
      return this.sSymbolHelper(s).toString();
    }
    else {
      return new ValueString(s).toString();
    }
  }

  @Override
  public String visitString(String s) {
    return new ValueString(s).toString();
  }

  @Override
  public String eval(List<Coord> coords) {
    String res = "";
    if (coords.size() == 0) {
      return "0";
    }
    for (Coord c : coords) {
      res = res.concat(model.getCellAt(c).getSexp().accept(this));
    }
    return res;
  }
}
