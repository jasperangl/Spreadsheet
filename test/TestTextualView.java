import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Represents the testing class for the textual view.
 */
public class TestTextualView {

  private WorksheetModel.Builder builder2;
  private IWorksheetModel worksheet;

  @Before
  public void init() {
    WorksheetModel.Builder builder = new WorksheetModel.Builder();
    builder2 = new WorksheetModel.Builder();
    worksheet = new WorksheetModel(builder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMTModel() throws FileNotFoundException {
    Appendable file;
    try {
      file = new PrintWriter("TextViewOutput.txt");
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("File not found");
    }
    new WorksheetTextualView(null, file);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMTAppendable() throws FileNotFoundException {
    Appendable file;
    try {
      file = new PrintWriter("TextViewOutput.txt");
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("File not found");
    }
    new WorksheetTextualView(worksheet, null);
  }

  @Test
  public void testTextualRepr() {
    WorksheetReader.WorksheetBuilder<IWorksheetModel> builder = new WorksheetModel.Builder();
    Readable file;
    PrintWriter writer;
    try {
      file = new FileReader("TextViewInput.txt");
      writer = new PrintWriter("TextViewOutput.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    WorksheetTextualView textualView = new WorksheetTextualView(model, writer);
    textualView.render();
    assertEquals("A2 =3.5\n"
        + "B3 \"\\\"Hello\\\"\"\n"
        + "A1 5\n"
        + "B2 WhatsUp\n"
        + "B1 \"Hello\"\n"
        + "C2 A2\n"
        + "D3 =(SUM A1:A4)\n"
        + "C1 =A1\n"
        + "D2 (CONCAT A1 B2 C2)\n"
        + "D1 =(SUM A1 B1 C2)\n"
        + "B6 =true\n"
        + "B5 true\n"
        + "A3 =7.2345\n"
        + "B4 false\n", textualView.toString());
  }


  @Test
  public void BigVsOutputEquivalenceTest() throws FileNotFoundException {
    WorksheetReader.WorksheetBuilder<IWorksheetModel> builder = new WorksheetModel.Builder();
    Readable file;
    PrintWriter writer;
    try {
      file = new FileReader("TextViewInput.txt");
      writer = new PrintWriter("TextViewOutput.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    WorksheetTextualView textualView = new WorksheetTextualView(model, writer);
    textualView.render();
    writer.close();
    Readable file2 = new FileReader("TextViewOutput.txt");
    IWorksheetModel model2 = WorksheetReader.read(builder2, file2);
    for (Coord coord : model.getSheet().keySet()) {
      assertEquals(model2.getCellAt(coord).getContent(), model.getCellAt(coord).getContent());
      assertEquals(model2.getCellAt(coord).getSexp(), model.getCellAt(coord).getSexp());
      assertEquals(model2.getCellAt(coord).getDependencies(),
          model.getCellAt(coord).getDependencies());
      assertEquals(model2.getCellAt(coord).getCoord(), model.getCellAt(coord).getCoord());
    }
  }

  @Test
  public void RoundTripTest2() throws FileNotFoundException {
    WorksheetReader.WorksheetBuilder<IWorksheetModel> builder = new WorksheetModel.Builder();
    Readable file;
    PrintWriter writer;
    try {
      file = new FileReader("BigVisual.txt");
      writer = new PrintWriter("empty1.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    }
    IWorksheetModel model = WorksheetReader.read(builder, file);
    WorksheetTextualView textualView = new WorksheetTextualView(model, writer);
    textualView.render();
    FileReader fw = new FileReader("empty1.txt");
    PrintWriter pw = new PrintWriter("empty2.txt");
    pw.print(textualView.toString());
    IWorksheetModel model2 = WorksheetReader.read(builder, fw);
    WorksheetTextualView tv2 = new WorksheetTextualView(model2, pw);
    assertEquals(tv2.toString(), textualView.toString());
  }


}