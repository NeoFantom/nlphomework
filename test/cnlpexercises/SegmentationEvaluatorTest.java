package cnlpexercises;

import cnlp.Constants;
import cnlpexercises.algorithm.PosTaggerTest;
import cnlpexercises.algorithm.Segmenter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.*;

public class SegmentationEvaluatorTest {

    /**
     * Test 13
     * Be patient, this part is easy but takes some time.
     * <p>
     * To pass this test, make the assertion successful.
     * When you pass this test, take a look of the file
     * {@link Constants#DIFFERENT_PHRASES_JSON_PATH}.
     * <p>
     * Test 14 is here: {@link PosTaggerTest#performPosTagging()}
     */
    @Test
    public void evaluate() {
        Assertions.assertDoesNotThrow(() -> {
            new SegmentationEvaluator(Segmenter.WORD_DELIMITER,
                    TEST_SEGMENTED_DATA_PATH,
                    TEST_STANDARD_SEGMENTED_DATA_PATH,
                    DIFFERENT_PHRASES_JSON_PATH
            ).evaluate();
        });
    }
}