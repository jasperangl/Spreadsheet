import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Test;

/**
 * Testing class for opening a spreadsheet. We will use the resources/ExtraCredOpen.txt file. Our
 * method also doesn't allow for any other file type than .txt If someone would try to open a .jpg
 * or other non .txt file type nothing will happen.
 */
public class ExtraCredOpen extends TestController {

  @Test
  public void openExistingFile() throws FileNotFoundException {
    controller1.changeFile("resources/BigFileBase.JPG");
    controller1.changeFile("resources/ExtraCredOpen.txt");

    WorksheetReader.WorksheetBuilder<IWorksheetModel> builder2 = new WorksheetModel.Builder();
    Readable file = new FileReader("resources/ExtraCredOpen.txt");
    IWorksheetModel model2 = WorksheetReader.read(builder2, file);

    assertEquals("56.7", model2.getCellAt(new Coord(1, 1)).getContent());
    assertEquals(new SNumber(56.7), model2.getCellAt(new Coord(1, 1)).getSexp());

    assertEquals("=A1", model2.getCellAt(new Coord(1, 2)).getContent());
    assertEquals(new SSymbol("A1"), model2.getCellAt(new Coord(1, 2)).getSexp());

    assertEquals("false", model2.getCellAt(new Coord(2, 3)).getContent());
    assertEquals(new SBoolean(false), model2.getCellAt(new Coord(2, 3)).getSexp());

    assertEquals("=(SUM A1 A2)", model2.getCellAt(new Coord(9, 12)).getContent());
    assertEquals(new SList(new SSymbol("SUM"), new SSymbol("A1"), new SSymbol("A2")),
        model2.getCellAt(new Coord(9, 12)).getSexp());
  }
}
