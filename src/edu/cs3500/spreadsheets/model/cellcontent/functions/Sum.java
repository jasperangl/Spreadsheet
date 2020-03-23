package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Calculates the sum of all the given cells. Non-numeric content is regarded as 0.
 */
public class Sum extends AFunction<Double> {


  /**
   * Constructs a Sum formula.
   *
   * @param model a worksheet model
   * @param cell the current cell we are working on
   */
  public Sum(IWorksheetModel model, ICell cell) {
    super(model, cell);
  }

  @Override
  public Double eval(List<Coord> coords) {
    double finalSum = 0.0;
    for (Coord c : coords) {
      currCell = model.getCellAt(c);
      finalSum += model.getCellAt(c).getSexp().accept(this);

    }
    return finalSum;
  }

  @Override
  public Double visitBoolean(boolean b) {
    return 0.0;
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
    return 0.0;
  }

}
