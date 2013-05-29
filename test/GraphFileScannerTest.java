import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;

import java.io.File;
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
}
