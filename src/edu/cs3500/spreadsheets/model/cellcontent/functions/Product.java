package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Calculates the product of all given cells. Non-numeric content is regarded as 1.
 */
public class Product extends AFunction<Double> {

  /**
   * Constructs a Product formula.
   *
   * @param model a worksheet model
   * @param cell the current Cell
   */
  public Product(IWorksheetModel model, ICell cell) {
    super(model,cell);
  }

  @Override
  public Double visitBoolean(boolean b) {
    return 1.0;
  }

  @Override
  public Double visitNumber(double d) {
    return d;
  }

  // only accessed
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
    return 1.0;
  }

  @Override
  public Double eval(List<Coord> coords) {
    double finalProduct = 1.0;
    for (Coord c : coords) {
      currCell = model.getCellAt(c);
      finalProduct *= model.getCellAt(c).getSexp().accept(this);
    }
    return finalProduct;
  }
}