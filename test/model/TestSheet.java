package model;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import java.util.HashMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Represents the tester class for all tests.
 */
public class TestSheet {

  private WorksheetModel.Builder builder = new WorksheetModel.Builder();
  private IWorksheetModel worksheet = new WorksheetModel(builder);

  // Test Constructor

  @Test (expected = IllegalArgumentException.class)
  public void testNullBuilder() {
    worksheet = new WorksheetModel(null);
  }


  // Test getCellAt

  @Test (expected = IllegalArgumentException.class)
  public void getCellAtNotInModel() {
    builder.createCell(1,1,"Hello");
    worksheet.getCellAt(new Coord(1,2));
  }

  @Test
  public void getCellAtNormal() {
    builder.createCell(1, 1, "=A2");
    Coord retcoord = new Coord(1, 1);
    ICell ret = new Cell(new SSymbol("A2"), retcoord);
    ret.setContent("=A2");
    assertEquals(ret.getCoord(), worksheet.getCellAt(new Coord(1, 1)).getCoord());
    assertEquals(ret.getContent(), worksheet.getCellAt(new Coord(1, 1)).getContent());
    assertEquals(ret.getSexp(), worksheet.getCellAt(new Coord(1, 1)).getSexp());
    assertEquals(ret.getDependencies(), worksheet.getCellAt(new Coord(1, 1)).getDependencies());
  }

  // Test max row & column

  @Test
  public void maxRowCol() {
    assertEquals(10, worksheet.getMaxColumn());
    assertEquals(10, worksheet.getMaxRow());
    builder.createCell(8,5,"false");
    assertEquals(10, worksheet.getMaxColumn());
    assertEquals(10, worksheet.getMaxRow());
    builder.createCell(11, 14, "(SUM 34 true)");
    assertEquals(11, worksheet.getMaxColumn());
    assertEquals(14, worksheet.getMaxRow());
  }

  @Test
  public void testEmptyWorksheet() {
    assertEquals(new HashMap<Coord, ICell>(), worksheet.getSheet());
    int i = 0;
    for (Coord coord : worksheet.getSheet().keySet()) {
      i++;
    }
    assertEquals(0, i);
  }

  // Reference Fails

  @Test(expected = IllegalArgumentException.class)
  public void testDirectSelfReference() {
    builder.createCell(1, 1, "=A1");
    worksheet.evaluate(new Coord(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIndirectSelfReference() {
    builder.createCell(1, 1, "=B2");
    builder.createCell(2, 2, "=A4");
    builder.createCell(1, 4, "=(SUM A1 5)");
    worksheet.evaluate(new Coord(1, 4));
  }

  // Testing Values

  @Test
  public void testValueBoolean() {
    builder.createCell(1, 1, "false");
    assertEquals("false", worksheet.evaluate(new Coord(1, 1)));
  }

  @Test
  public void testValueString() {
    builder.createCell(1, 1, "\"Hello Peter\"");
    assertEquals("Hello Peter", worksheet.evaluate(new Coord(1, 1)));
  }

  @Test
  public void testValueDouble() {
    builder.createCell(1, 1, "3.23435");
    assertEquals("3.23435", worksheet.evaluate(new Coord(1, 1)));
  }

  @Test
  public void testStoredBlank() {
    builder.createCell(1, 3, "=(SUM A1 A2)");
    assertEquals("0.0", worksheet.evaluate(new Coord(1, 3)));
  }

  // Testing Formulas

  @Test
  public void sumNormal() {
    builder.createCell(1, 1, "=(SUM true 3 \"Hello\" 4.7 A3)");
    assertEquals("7.7", worksheet.evaluate(new Coord(1, 1)));
  }

  @Test
  public void sumRegion() {
    builder.createCell(1, 1, "=4");
    builder.createCell(1, 2, "=1");
    builder.createCell(1, 5, "=(SUM A1:A4 B4 3.5)");
    assertEquals("8.5", worksheet.evaluate(new Coord(1, 5)));
  }

  @Test
  public void productNormal() {
    builder.createCell(1, 1, "=(PRODUCT true 3 \"Hello\" 4.5 A3)");
    assertEquals("13.5", worksheet.evaluate(new Coord(1, 1)));
  }

  @Test
  public void productRegion() {
    builder.createCell(1, 1, "=4");
    builder.createCell(1, 2, "=1");
    builder.createCell(1, 5, "=(PRODUCT A1:A4 B4 3.5)");
    assertEquals("14.0", worksheet.evaluate(new Coord(1, 5)));
  }

  @Test
  public void concatNormal() {
    builder.createCell(1, 1, "hello");
    builder.createCell(1, 2, "howdy");
    builder.createCell(1, 3, "=(CONCAT A1 A2)");
    assertEquals("\"hellohowdy\"", worksheet.evaluate(new Coord(1, 3)));
  }

  @Test
  public void concatRegion() {
    builder.createCell(1, 1, "Hello");
    builder.createCell(1, 2, "How");
    builder.createCell(1, 3, "You");
    builder.createCell(2, 1, "Partner");
    builder.createCell(2, 2, "Are");
    builder.createCell(2, 3, "Today");
    builder.createCell(1, 4, "=(CONCAT A1:B3)");
    builder.createCell(1, 5, "=(CONCAT B3:A1)");
    assertEquals("\"HelloPartnerHowAreYouToday\"", worksheet.evaluate(new Coord(1, 4)));
  }

  @Test
  public void lessThanNormal() {
    builder.createCell(1, 1, "=(< 1.8 1.9)");
    builder.createCell(1, 2, "=(< 3 1)");
    // creating an invalid cell doesn't throw exception only when it tries to be evaluated.
    builder.createCell(1, 3, "=(< 3 1 7)");
    assertEquals("true", worksheet.evaluate(new Coord(1, 1)));
    assertEquals("false", worksheet.evaluate(new Coord(1, 2)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void lessThanMoreThan2() {
    builder.createCell(1, 1, "=(< 1 3 5)");
    worksheet.evaluate(new Coord(1,1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void lessThanLessThan2() {
    builder.createCell(1, 1, "=(< 1)");
    worksheet.evaluate(new Coord(1,1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void lessThanInvalInput() {
    builder.createCell(1, 1, "=(< 2 \"Hello\")");
    worksheet.evaluate(new Coord(1,1));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testStoredStringFail() {
    builder.createCell(1, 1, "=hello");
    builder.createCell(1, 2, "=howdy");
    builder.createCell(1, 3, "=(< A1:A2)");
    assertEquals(new IllegalArgumentException(), worksheet.evaluate(new Coord(1, 3)));
  }

  @Test
  public void testStoredBoolean() {
    builder.createCell(1, 1, "3");
    builder.createCell(1, 2, "9");
    builder.createCell(1, 3, "=(< A1 A2)");
    assertEquals("true", worksheet.evaluate(new Coord(1, 3)));
  }
}
