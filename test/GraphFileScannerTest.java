import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;

import java.util.zip.GZIPOutputStream;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class GraphFileScannerTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testBadFile() throws IOException {
		File file = new File("/does/not/exist.txt");
		for (int i = 0; file.exists(); ++i)
			file = new File("/does/not/exist" + i + ".txt");

		thrown.expect(IOException.class);
        new GraphFileScanner(file);
    }

    @Test
    public void testEmptyFile() throws IOException {
		File file = File.createTempFile(
			GraphFileScannerTest.class.getName(),
			".graph.gz");
		file.deleteOnExit();

		Writer writer = new OutputStreamWriter(
			new GZIPOutputStream(new FileOutputStream(file)),
			"UTF-8");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertFalse(scanner.hasNextLine());
    }
}
