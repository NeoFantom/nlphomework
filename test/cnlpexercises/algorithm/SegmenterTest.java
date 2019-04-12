package cnlpexercises.algorithm;

import cnlp.Constants;
import cnlpexercises.SegmentationEvaluatorTest;
import cnlpexercises.model.StatisticsBox;
import cnlpexercises.model.Vocabulary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.TEST_SEGMENTED_DATA_PATH;
import static cnlp.Constants.TEST_UNSEGMENTED_DATA_PATH;
import static cnlpexercises.Constants.SAVED_STATISTICS_BOX_PATH;
import static cnlpexercises.Constants.TRAIN_DATA_PATH;

public class SegmenterTest {

    /**
     * Test 12 (hard)
     * To pass this test, make the assertion successful.
     * <p>
     * When you pass this test, take a look of the files
     * {@link Constants#TEST_UNSEGMENTED_DATA_PATH}
     * and {@link Constants#TEST_SEGMENTED_DATA_PATH}.
     * <p>
     * Test 13 is here: {@link SegmentationEvaluatorTest#evaluate()}
     */
    @Test
    public void segment() {
        Assertions.assertDoesNotThrow(() -> {
            Segmenter segmenter = new Segmenter(
                    new Vocabulary(StatisticsBox.fromTrainData(TRAIN_DATA_PATH).getWordList()));
            segmenter.letsDoIt(TEST_UNSEGMENTED_DATA_PATH, TEST_SEGMENTED_DATA_PATH);
        });
    }
}