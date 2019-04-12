package cnlpexercises;

import cnlpexercises.io.DataManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static cnlpexercises.Constants.*;
import static cnlpexercises.HelpFunctions.getReader;
import static cnlpexercises.HelpFunctions.getWriter;

class ConstantsTest {

    /**
     * Test 1 (There is no Test 0 ^_^ )
     * Important: Never read any test function UNLESS you're told to do so.
     * To pass the test, make sure all the paths are valid.
     * Note that you have to manually create directories that the program needs.
     * <p>
     * Test 2 is here: {@link ConstantsTest#encodingCorrect()}
     */
    @Test
    void allPathsValid() {
        Assertions.assertDoesNotThrow(() -> getReader(RAW_DATA_PATH));

        Assertions.assertDoesNotThrow(() -> getWriter(TEMP_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TRAIN_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(SAVED_STATISTICS_BOX_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_UNSEGMENTED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_SEGMENTED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_STANDARD_SEGMENTED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(DIFFERENT_PHRASES_JSON_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(WORD_FREQUENCY_JSON_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(VOCABULARY_JSON_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_UNTAGGED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_TAGGED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TEST_STANDARD_TAGGED_DATA_PATH));
        Assertions.assertDoesNotThrow(() -> getWriter(TAGGING_COMPARISONS_JSON_PATH));
    }

    /**
     * Test 2
     * To pass the test, change the encoding of file at
     * {@link cnlpexercises.Constants#RAW_DATA_PATH} to utf8.
     * <p>
     * Test 3 is here: {@link DataManagerTest#learnAboutTodo()}
     */
    @Test
    void encodingCorrect() {
        Assertions.assertDoesNotThrow(() -> {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(RAW_DATA_PATH), StandardCharsets.UTF_8));
            String line = reader.readLine();
            Assertions.assertEquals(
                    "19980101-01-001-001/m  迈向/v  充满/v  希望/n  的/u  新/a  世纪/n  ——/w  一九九八年/t  新年/t  讲话/n  （/w  附/v  图片/n  １/m  张/q  ）/w  ",
                    line);

            // Tests finished. Now prepare temp file for tests.
            prepareForOtherTests(reader);
        });
    }

    private void prepareForOtherTests(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        BufferedWriter writer = getWriter(TEMP_PATH);
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
    }
}