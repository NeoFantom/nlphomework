package cnlp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static cnlp.Constants.*;

class SegmentationTaskTest {
    BufferedReader getReader(String path) throws IOException {
        return new BufferedReader(new FileReader(path));
    }

    BufferedWriter getWriter(String path) throws IOException {
        return new BufferedWriter(new FileWriter(path));
    }

    /**
     * Test 1 (There is no Test 0 ^_^)
     * To pass the test, make sure all the paths are valid.
     * Note that you have to manually create directories that the program needs.
     * Test 2 is here: {@link SegmentationTaskTest#encodingCorrect()}
     */
    @Test
    void allPathsValid() {
        Assertions.assertDoesNotThrow(() -> getReader( RAW_DATA_PATH));

        Assertions.assertDoesNotThrow(() -> getWriter( TRAIN_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( TEST_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( TEST_UNSEGMENTED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( TEST_STANDARD_SEGMENTED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( TEST_SEGMENTED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( SAVED_STATISTICS_BOX_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( WORD_FREQUENCY_JSON_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter( TEMP_PATH));
    }

    /**
     * Test 2
     * To pass the test, change the {@link Constants#RAW_DATA_PATH} 's
     * file encoding to utf8.
     * <p>
     * While we do the test, we also extract 100 lines to
     * {@link Constants#RAW_DATA_PATH} for following tests.
     * Test 3 is here: {@link}
     */
    @Test
    void encodingCorrect() {
        Assertions.assertDoesNotThrow(() -> {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream( RAW_DATA_PATH), StandardCharsets.UTF_8));
            String line = reader.readLine();
            Assertions.assertEquals(
                    "19980101-01-001-001/m  迈向/v  充满/v  希望/n  的/u  新/a  世纪/n  ——/w  一九九八年/t  新年/t  讲话/n  （/w  附/v  图片/n  １/m  张/q  ）/w  ",
                    line);

            // Tests finished. Now prepare temp file for tests.
            BufferedWriter writer = getWriter( TEMP_PATH);
            int count = 0;
            while (line != null && count < 100) {
                if (!line.isEmpty()) {
                    writer.write(line);
                    writer.write('\n');
                    count++;
                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        });
    }
}