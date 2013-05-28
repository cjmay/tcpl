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
	private List<String> nextLine;

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
		if (nextLine == null) {
			try {
				String nextLineUnsplit = reader.readLine();
				if (nextLineUnsplit != null) {
					nextLineUnsplit = nextLineUnsplit.trim();
					if (nextLineUnsplit.length() > 0) {
						nextLine = Arrays.asList(nextLineUnsplit.split("\t"));
					}
				}
			} catch (IOException ex) {
				// just leave nextLine as null (end-of-file behavior)
			}
		}

		return nextLine != null;
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

		return nextLine;
	}
}
