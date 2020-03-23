
import edu.cs3500.spreadsheets.controller.IController;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.MouseEvent;

import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.view.WorksheetEditPanel;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;

import static org.junit.Assert.assertEquals;

/**
 * Tests class for a EditPanel. It was difficult to make testable methods since most methods rely on
 * mouse clicks and positions. However, we were still able to simulate a mouse click behavior.
 */
public class TestWorksheetEditPanel {

  private WorksheetModel.Builder builder;
  private WorksheetEditPanel wep;
  private MouseEvent me1;
  private MouseEvent me2;
  private MouseEvent me3;
  private WorksheetEditableView ev;

  @Before
  public void init() {
    builder = new WorksheetModel.Builder();
    IWorksheetModel model = new WorksheetModel(builder);
    IController c = new Controller(model, builder);
    ev = new WorksheetEditableView(model, c);
    wep = new WorksheetEditPanel(ev, model, 100, 40);
    me1 = new MouseEvent(wep, MouseEvent.MOUSE_CLICKED, 0, 0, 110, 300, 1, false);
    me2 = new MouseEvent(wep, MouseEvent.MOUSE_CLICKED, 0, 0, 110, 70, 1, false);
    me3 = new MouseEvent(wep, MouseEvent.MOUSE_CLICKED, 0, 0, 530, 140, 1, false);
  }

  @Test
  public void testInitHighlight() {
    wep.initHighlight();
    assertEquals(new Coord(1, 1), wep.getCoord());
  }

  @Test
  public void testGetCoord() {
    assertEquals(new Coord(1, 1), wep.getCoord());
    wep.mouseClicked(me1);
    assertEquals(new Coord(1, 7), wep.getCoord());
    wep.mouseClicked(me2);
    assertEquals(new Coord(1, 1), wep.getCoord());
    wep.mouseClicked(me3);
    assertEquals(new Coord(5, 3), wep.getCoord());
  }


}
