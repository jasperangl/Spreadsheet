package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Takes two cells numeric values and determines whether the first one is smaller than the second.
 */
public class LessThan extends AFunction<Double> {

  /**
   * Constructs a LessThan Function.
   *
   * @param model a worksheet model
   * @param cell the current cell we are working on
   */
  public LessThan(IWorksheetModel model, ICell cell) {
    super(model, cell);
  }

  @Override
  public Double visitBoolean(boolean b) {
    throw new IllegalArgumentException("#InvalInput<");
  }

  @Override
  public Double visitNumber(double d) {
    return d;
  }

  @Override
  public Double visitSList(List<Sexp> l) {
    Sexp exp = super.sListHelper(l);
    return exp.accept(this);
  }

  @Override
  public Double visitSymbol(String s) {
    Sexp exp = sSymbolHelper(s);
    return exp.accept(this);
  }

  @Override
  public Double visitString(String s) {
    throw new IllegalArgumentException("#InvalInput<");
  }


  @Override
  public Double eval(List<Coord> coords) {
    double lessThan = 0.0;
    if (coords.size() > 1) {
      throw new IllegalArgumentException("#NoRectOn<");
    }
    for (Coord c : coords) {
      currCell = model.getCellAt(c);
      lessThan = model.getCellAt(c).getSexp().accept(this);
    }
    return lessThan;
  }
}
