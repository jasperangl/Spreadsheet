package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Concatenates all the cells' Values. Defaults to an empty string.
 */
public class Concat extends AFunction<String> {

  /**
   * Constructs a Concat Function.
   *
   * @param model a worksheet model
   * @param cell the current cell we are working on
   */
  public Concat(IWorksheetModel model, ICell cell) {
    super(model, cell);
  }

  @Override
  public String visitBoolean(boolean b) {
    return Boolean.toString(b);
  }

  @Override
  public String visitNumber(double d) {
    return Double.toString(d);
  }

  @Override
  public String visitSList(List<Sexp> l) {
    Sexp exp = super.sListHelper(l);
    return exp.accept(this);
  }

  @Override
  public String visitSymbol(String s) {
    Sexp exp = sSymbolHelper(s);
    return exp.accept(this);
  }

  @Override
  public String visitString(String s) {
    return s;
  }

  @Override
  public String eval(List<Coord> coords) {
    String finalStr = "";
    for (int i = coords.size() - 1; i >= 0; i--) {
      currCell = model.getCellAt(coords.get(i));
      finalStr += model.getCellAt(coords.get(i)).getSexp().accept(this);
    }
    return finalStr;
  }
}
