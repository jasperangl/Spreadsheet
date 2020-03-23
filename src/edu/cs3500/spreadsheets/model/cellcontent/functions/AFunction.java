package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an abstract function. Contains the helpers for SList and SSymbol which are utilized by
 * all extending class.
 */
abstract class AFunction<T> implements IFunction<T> {

  protected final IWorksheetModel model;
  private final ICell originCell;
  protected ICell currCell;

  /**
   * Constructs an abstract function.
   *
   * @param model a worksheet model
   */
  public AFunction(IWorksheetModel model, ICell oCell) {
    this.model = model;
    this.originCell = oCell;
    this.currCell = oCell;
  }


  protected Sexp sListHelper(List<Sexp> l) {
    Sexp exp = l.get(0);
    if (exp.toString().equals("SUM")) {
      if (!currCell.getContent().substring(0,1).equals("=")) {
        throw new IllegalArgumentException("#InvalFormula");
      }
      AFunction<Double> sum = new Sum(model, originCell);
      double finalSum = 0.0;
      for (int i = 1; i < l.size(); i++) {
        finalSum += l.get(i).accept(sum);
        sum.currCell = this.originCell;
        model.clearDependencies();
      }
      return new SNumber(finalSum);
    }
    if (exp.toString().equals("<")) {
      double first;
      double second;
      boolean place;
      if (l.size() != 3) {
        throw new IllegalArgumentException("#Only2Comp!");
      } else {
        AFunction<Double> lessThan = new LessThan(model, originCell);
        first = l.get(1).accept(lessThan);
        model.clearDependencies();
        second = l.get(2).accept(lessThan);
        model.clearDependencies();
        lessThan.currCell = this.originCell;
        place = first < second;
      }
      return new SBoolean(place);
    }
    if (exp.toString().equals("PRODUCT")) {
      AFunction<Double> product = new Product(model, originCell);
      double finalSum = 1.0000;
      for (int i = 1; i < l.size(); i++) {
        finalSum *= l.get(i).accept(product);
        product.currCell = this.originCell;
        model.clearDependencies();
      }
      return new SNumber(finalSum);
    }
    if (exp.toString().equals("CONCAT")) {
      AFunction<String> concat = new Concat(model, originCell);
      StringBuilder finalString = new StringBuilder();
      for (int i = 1; i < l.size(); i++) {
        finalString.append(l.get(i).accept(concat));
        concat.currCell = this.currCell;
        model.clearDependencies();
      }
      return new SString(finalString.toString());
    } else {
      throw new IllegalArgumentException("#InvalFunc");
    }
  }

  protected Sexp sSymbolHelper(String s) {
    String firstChar = this.originCell.getContent().substring(0, 1);
    if (firstChar.equals("(")) {
      throw new IllegalArgumentException("#InvalFormula");
    }
    if (firstChar.equals("=")) {
      if (s.contains(":")) {
        String[] los = s.split(":");
        if (los.length != 2) {
          throw new IllegalArgumentException("#InvalRef");
        }
        String[] firstCell = los[0].split("(?=\\d)(?<!\\d)");
        String[] secondCell = los[1].split("(?=\\d)(?<!\\d)");
        Coord coordC1 = new Coord(Coord.colNameToIndex(firstCell[0]),
            Integer.parseInt(firstCell[1]));
        Coord coordC2 = new Coord(Coord.colNameToIndex(secondCell[0]),
            Integer.parseInt(secondCell[1]));
        String value = this.rectangleRegion(coordC1, coordC2).toString();
        return Parser.parse(value);
      }
      String value;
      if (!s.contains(":")) {
        Coord coord;
        try {
          String[] onlyCell = s.split("(?=\\d)(?<!\\d)");
          coord = new Coord(Coord.colNameToIndex(onlyCell[0]), Integer.parseInt(onlyCell[1]));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException n) {
          return new SString(s);
        }
        value = this.singleCell(coord).toString();
        return Parser.parse(value);
      } else {
        throw new IllegalArgumentException("Should have reached one of the above if statements");
      }
    }
    return new SString(s);
  }

  private T singleCell(Coord coord) {
    HashMap<Coord, ICell> sheet = model.getSheet();
    ArrayList<Coord> retVal = new ArrayList<>();
    if (sheet.containsKey(coord)) {
      retVal.add(coord);
      this.currCell.addDependency(coord);
    }
    if (this.currCell.containsCycle()) {
      throw new IllegalArgumentException("#Cycle!");
    }
    if (sheet.containsKey(coord)) {
      this.currCell = model.getCellAt(coord);
    }
    return this.eval(retVal);
  }

  private T rectangleRegion(Coord coord1, Coord coord2) {
    List<Coord> loc = new ArrayList<>();
    int cell1col = coord1.col;
    int cell1row = coord1.row;
    int cell2col = coord2.col;
    int cell2row = coord2.row;
    int minCol = Math.min(cell1col, cell2col);
    int maxCol = Math.max(cell1col, cell2col);
    int minRow = Math.min(cell1row, cell2row);
    int maxRow = Math.max(cell1row, cell2row);
    for (int row = maxRow; row >= minRow; row--) {
      for (int col = maxCol; col >= minCol; col--) {
        Coord coord = new Coord(col, row);
        if (model.containsCell(coord)) {
          loc.add(coord);
          this.currCell.addDependency(coord);
        }
      }
    }
    return eval(loc);
  }
}
