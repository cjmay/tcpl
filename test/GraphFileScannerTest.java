import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.Arrays;

import java.util.zip.GZIPOutputStream;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    @Test
    public void testBlankLineFile() throws IOException {
		writer.write(" \t \n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testBlankLinesFile() throws IOException {
		writer.write("\n\t\n\t\n \t \n \n\n \t \n\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testOneLineFile() throws IOException {
		writer.write("hello\tworld\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hello", "world"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testOneLineFileOneToken() throws IOException {
		writer.write("hello,world\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hello,world"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testOneLineFileWithSpace() throws IOException {
		writer.write("hell o\tworld\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hell o", "world"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testOneLineFileWithExtraNewlines() throws IOException {
		writer.write("\nhello\tworld\n\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hello", "world"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testOneLineFileWithConsecutiveTabs() throws IOException {
		writer.write("hello\t\tworld\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hello", "", "world"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testTwoLineFile() throws IOException {
		writer.write("hello\tworld\nfoo\tbar\tbaz\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hello", "world"}),
			scanner.nextLine());
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"foo", "bar", "baz"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }

    @Test
    public void testTwoLineFileWithExtraWhitespace() throws IOException {
		writer.write("\n\nhello\tworld\n\nfoo\tbar\tbaz\n\n");
		writer.close();

        GraphFileScanner scanner = new GraphFileScanner(file);
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"hello", "world"}),
			scanner.nextLine());
		assertTrue(scanner.hasNextLine());
		assertEquals(Arrays.asList(new String[] {"foo", "bar", "baz"}),
			scanner.nextLine());
		assertFalse(scanner.hasNextLine());
    }
}
