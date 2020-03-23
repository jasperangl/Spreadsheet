import edu.cs3500.spreadsheets.model.WorksheetModel;
import org.junit.Test;

import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.view.WorksheetPanel;

import static org.junit.Assert.assertEquals;

/**
 * Represents the testing class for a Basic Worksheet panel.
 */
public class TestWorksheetPanel {

  WorksheetModel.Builder builder = new WorksheetModel.Builder();
  IWorksheetModel model = new WorksheetModel(builder);
  WorksheetPanel wp = new WorksheetPanel(model, 100, 40);

  @Test
  public void testUpdateHeight() {
    assertEquals(10, wp.getterHeight());
    wp.updateHeight();
    assertEquals(12, wp.getterHeight());
  }

  @Test
  public void testUpdateWidth() {
    assertEquals(10, wp.getterWidth());
    wp.updateWidth();
    assertEquals(12, wp.getterWidth());
  }
}
