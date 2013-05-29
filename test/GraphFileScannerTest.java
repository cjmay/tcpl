import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.junit.After;
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

	private File file;
	private Writer writer;

	@Before
	public void setUp() throws IOException {
		file = File.createTempFile(
			GraphFileScannerTest.class.getName(),
			".graph.gz");
		file.deleteOnExit();

		writer = new OutputStreamWriter(
			new GZIPOutputStream(new FileOutputStream(file)),
			"UTF-8");
	}

	@After
	public void tearDown() throws IOException {
		writer.close();
	}

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
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertFalse(scanner.hasNextLine());
    }
}
