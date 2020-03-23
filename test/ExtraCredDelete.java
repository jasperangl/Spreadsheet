import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import org.junit.Test;

/**
 * Testing class for deleting a Cell. We ensure that we can delete existing cells and that nothing
 * happens when we try to delete empty cells.
 */
public class ExtraCredDelete extends TestController {

  @Test
  public void testDeleteNormal() {
    assertEquals("23", c1.getContent());
    assertEquals(new SNumber(23), c1.getSexp());

    assertTrue(model1.getSheet().containsKey(new Coord(1, 1)));
    assertTrue(model1.getSheet().containsKey(new Coord(1, 3)));
    assertTrue(model1.getSheet().containsKey(new Coord(10, 15)));
    controller1.deleteCell(new Coord(1, 1));
    controller1.deleteCell(new Coord(1, 3));
    controller1.deleteCell(new Coord(10, 15));
    assertFalse(model1.getSheet().containsKey(new Coord(1, 1)));
    assertFalse(model1.getSheet().containsKey(new Coord(1, 3)));
    assertFalse(model1.getSheet().containsKey(new Coord(10, 15)));
  }

  @Test
  public void testDeleteMTCell() {
    assertEquals("=A1", c2.getContent());
    assertEquals(new SSymbol("A1"), c2.getSexp());
    assertEquals(6, model1.getSheet().size());
    // doesn't exist
    // model is supposed to not throw exception or mutate anything
    controller1.deleteCell(new Coord(3, 1));

    assertEquals(6, model1.getSheet().size());
    assertEquals("=A1", c2.getContent());
    assertEquals(new SSymbol("A1"), c2.getSexp());
  }


}
