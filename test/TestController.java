import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.controller.IController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel.Builder;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A class to test the main Controller capabilities.
 */
public class TestController {

  protected IController controller1;
  protected IWorksheetModel model1;
  private WorksheetEditableView viewEditable;
  private Builder builder;
  protected ICell c1;
  protected ICell c2;
  private ICell c3;
  private ICell c4;
  private ICell c5;
  private ICell c6;


  @Before
  public void init() {
    builder = new Builder();
    builder.createCell(1, 1, "23");
    builder.createCell(1, 2, "=A1");
    builder.createCell(1, 3, "\"Hello\"");
    builder.createCell(4, 6, "=(SUM A2 4)");
    builder.createCell(2, 4, "A3");
    builder.createCell(10, 15, "=(< 4 A1)");

    model1 = new WorksheetModel(builder);
    controller1 = new Controller(model1, builder);
    viewEditable = new WorksheetEditableView(model1, controller1);


    c1 = model1.getCellAt(new Coord(1, 1));
    c2 = model1.getCellAt(new Coord(1, 2));
    c3 = model1.getCellAt(new Coord(1, 3));
    c4 = model1.getCellAt(new Coord(4, 6));
    c5 = model1.getCellAt(new Coord(2, 4));
    c6 = model1.getCellAt(new Coord(10, 15));
  }

  // Constructor Tests

  @Test(expected = IllegalArgumentException.class)
  public void constructorMTModel() {
    controller1 = new Controller(null, builder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorMTBuilder() {
    controller1 = new Controller(model1, null);
  }

  // Test change content

  @Test
  public void changeContentExistingCells() {

    assertEquals("23", c1.getContent());
    assertEquals(new SNumber(23), c1.getSexp());

    assertEquals("=A1", c2.getContent());
    assertEquals(new SSymbol("A1"), c2.getSexp());

    assertEquals("=(< 4 A1)", c6.getContent());
    assertEquals(new SList(new SSymbol("<"), new SNumber(4), new SSymbol("A1")),
        c6.getSexp());

    controller1.changeContent(new Coord(1, 1), "\"Peter\"");
    controller1.changeContent(new Coord(1, 2), "Peter");
    controller1.changeContent(new Coord(10, 15), "=(SUM A2 4)");

    assertEquals("\"Peter\"", c1.getContent());
    assertEquals(new SString("Peter"), c1.getSexp());

    assertEquals("Peter",c2.getContent());
    assertEquals(new SSymbol("Peter"), c2.getSexp());

    assertEquals("=(SUM A2 4)", c6.getContent());
    assertEquals(new SList(new SSymbol("SUM"), new SSymbol("A2"), new SNumber(4)),
        c6.getSexp());
  }

  @Test
  public void changeContentNewCells() {
    // Can't get a null cell from my model because it doesn't exist

    controller1.changeContent(new Coord(1, 5), "\"Bonjour\"");
    controller1.changeContent(new Coord(5, 5), "=(PRODUCT A1 4)");

    assertEquals("\"Bonjour\"", model1.getCellAt(new Coord(1, 5)).getContent());
    assertEquals(new SString("Bonjour"), model1.getCellAt(new Coord(1, 5)).getSexp());

    assertEquals("=(PRODUCT A1 4)", model1.getCellAt(new Coord(5, 5)).getContent());
    assertEquals(new SList(new SSymbol("PRODUCT"), new SSymbol("A1"), new SNumber(4)),
        model1.getCellAt(new Coord(5, 5)).getSexp());
  }



}
