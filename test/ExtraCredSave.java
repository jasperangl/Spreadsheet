import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Test;

/**
 * Testing class for saving the edited spreadsheet in the round trip format. We perform one
 * exhaustive test, that modifies the model1 from super class so that it differs from original, then
 * we read that file back in and make sure that every cell is the exact same as the mutated model1
 * from super class. When trying to run the test make sure the resources/ExtraCredSaveOut.txt is
 * empty so that it is visible that this test actually saves the file. I will submit the file with
 * the text so that in case you can't run it yourself you will see that it saved the sample model
 * with the changes made. Our method also doesn't allow for any other file type than .txt If someone
 * would try to open a .jpg or other non .txt file type nothing will happen.
 */
public class ExtraCredSave extends TestController {

  @Test
  public void roundTrip() throws FileNotFoundException {
    controller1.deleteCell(new Coord(1, 1));
    controller1.changeContent(new Coord(3, 3), "=(SUM 3 A3)");
    controller1.changeContent(new Coord(10, 15), "129");

    controller1.saveFile("resources/BigFileBase.JPG");
    controller1.saveFile("resources/ExtraCredSaveOut.txt");

    WorksheetReader.WorksheetBuilder<IWorksheetModel> builder2 = new WorksheetModel.Builder();
    Readable file = new FileReader("resources/ExtraCredSaveOut.txt");
    IWorksheetModel model2 = WorksheetReader.read(builder2, file);

    for (Coord coord : model1.getSheet().keySet()) {
      assertEquals(model2.getCellAt(coord).getContent(), model1.getCellAt(coord).getContent());
      assertEquals(model2.getCellAt(coord).getSexp(), model1.getCellAt(coord).getSexp());
      assertEquals(model2.getCellAt(coord).getDependencies(),
          model1.getCellAt(coord).getDependencies());
      assertEquals(model2.getCellAt(coord).getCoord(), model1.getCellAt(coord).getCoord());
    }
  }
}
