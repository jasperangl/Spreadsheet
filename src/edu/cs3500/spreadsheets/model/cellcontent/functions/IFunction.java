package edu.cs3500.spreadsheets.model.cellcontent.functions;

import edu.cs3500.spreadsheets.model.cellcontent.IFormula;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.List;

/**
 * Represents the interface of a function.
 *
 * @param <T> return type of a Function. Either boolean, double or String.
 */
public interface IFunction<T> extends IFormula, SexpVisitor<T> {

  /**
   * Evaluates a function on all the given Coordinates of cells.
   *
   * @param coords Coordinates of cells
   * @return specified return Type for each function
   */
  T eval(List<Coord> coords);


}
