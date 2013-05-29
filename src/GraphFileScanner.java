import java.util.List;
import java.util.Arrays;
import java.util.NoSuchElementException;

import java.util.zip.GZIPInputStream;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class GraphFileScanner {
	private BufferedReader reader;
	private List<String> nextLineTokens;

	/**
	 * Open file pointer to compressed graph file
	 * @param file
	 * @throws IOException
	 */
	public GraphFileScanner(File file) throws IOException {
		reader = new BufferedReader(
			new InputStreamReader(
				new GZIPInputStream(new FileInputStream(file)),
				"UTF-8"));
	}

	/**
	 * Return true if there is an unscanned line in the file,
	 * false otherwise.
	 * @return whether there is an unscanned line
	 */
	public boolean hasNextLine() {
		while (nextLineTokens == null) {
			try {
				String nextLineStr = reader.readLine();
				if (nextLineStr == null) break; // end-of-file
				nextLineStr = nextLineStr.trim();
				if (nextLineStr.length() == 0) continue; // ignore empty lines
				nextLineTokens = Arrays.asList(nextLineStr.split("\t"));
			} catch (IOException ex) {
				break; // file read error
			}
		}

		return nextLineTokens != null;
	}

	/**
	 * Return next unscanned line in file.
	 * @return line represented as a list of tokens (which are
	 *         delimited by tab characters in the graph file)
	 * @throws NoSuchElementException if there are no more lines
	 */
	public List<String> nextLine() throws NoSuchElementException {
		if (! hasNextLine())
			throw new NoSuchElementException();

		List<String> lineTokens = nextLineTokens;
		nextLineTokens = null;

		return lineTokens;
	}
}
