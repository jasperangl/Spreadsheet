package model;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ICell;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Represents the testing class for ICells.
 */
public class TestCell {

  private WorksheetModel.Builder builder = new WorksheetModel.Builder();
  private ICell c1;
  private ICell c2;
  private ICell c3;
  private ICell c4;


  @Before
  public void init() {
    builder.createCell(1, 1, "23");
    builder.createCell(1, 2, "A1");
    builder.createCell(1, 3, "\"Hello\"");
    builder.createCell(4, 6, "=(SUM A2 4)");

    IWorksheetModel worksheetS = new WorksheetModel(builder);
    c1 = worksheetS.getCellAt(new Coord(1, 1));
    c2 = worksheetS.getCellAt(new Coord(1, 2));
    c3 = worksheetS.getCellAt(new Coord(1, 3));
    c4 = worksheetS.getCellAt(new Coord(4, 6));

  }

  @Test
  public void testGetCoord() {
    assertEquals(new Coord(1, 1), c1.getCoord());
    assertEquals(new Coord(1, 2), c2.getCoord());
    assertEquals(new Coord(1, 3), c3.getCoord());
  }

  @Test
  public void testGetSexp() {
    assertEquals(new SNumber(23), c1.getSexp());
    assertEquals(new SSymbol("A1"), c2.getSexp());
    assertEquals(new SString("Hello"), c3.getSexp());
    assertEquals(new SList(
            new ArrayList<>(Arrays.asList(new SSymbol("SUM"), new SSymbol("A2"), new SNumber(4)))),
        c4.getSexp());
  }

  @Test
  public void testSetSexp() {
    assertEquals(new SNumber(23), c1.getSexp());
    assertEquals(new SList(
            new ArrayList<>(Arrays.asList(new SSymbol("SUM"), new SSymbol("A2"), new SNumber(4)))),
        c4.getSexp());
    c1.setSexp(new SString("Hello"));
    c4.setSexp(new SBoolean(false));
    assertEquals(new SString("Hello"), c1.getSexp());
    assertEquals(new SBoolean(false), c4.getSexp());
  }

  @Test
  public void testGetContent() {
    assertEquals("23", c1.getContent());
    assertEquals("A1", c2.getContent());
    assertEquals("\"Hello\"", c3.getContent());
    assertEquals("=(SUM A2 4)", c4.getContent());
  }

  @Test
  public void testSetContent() {
    assertEquals("23", c1.getContent());
    assertEquals("=(SUM A2 4)", c4.getContent());
    c1.setContent("=(SUM A2 4)");
    c4.setContent("=malformed");
    assertEquals("=(SUM A2 4)", c1.getContent());
    assertEquals("=malformed", c4.getContent());
  }

  @Test
  public void testDependency() {
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 1))), c1.getDependencies());
    c1.addDependency(new Coord(1, 2));
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 1), new Coord(1, 2))),
        c1.getDependencies());
    c1.clearDependencies();
    assertEquals(new ArrayList<>(Arrays.asList(new Coord(1, 1))), c1.getDependencies());
    assertFalse(c1.containsCycle());
    c1.addDependency(new Coord(1, 1));
    assertTrue(c1.containsCycle());
  }
}
