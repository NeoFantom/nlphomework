package cnlpexercises.algorithm;

import cnlp.Constants;
import cnlpexercises.PosTaggingEvaluatorTest;
import cnlpexercises.model.StatisticsBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.*;

public class PosTaggerTest {

    /**
     * Test 14
     * Be patient, this part is easy but takes some time.
     * <p>
     * To pass this test, make the assertion successful.
     * When you pass this test, take a look at the files
     * {@link Constants#TEST_UNTAGGED_DATA_PATH}.
     * and {@link Constants#TEST_TAGGED_DATA_PATH}.
     * <p>
     * Test 15 is here: {@link PosTaggingEvaluatorTest#evaluate()}
     */
    @Test
    public void performPosTagging() {
        Assertions.assertDoesNotThrow(() -> {
            PosTagger posTagger = new PosTagger(StatisticsBox.fromTrainData(TRAIN_DATA_PATH));
            posTagger.roll(TEST_UNTAGGED_DATA_PATH, TEST_TAGGED_DATA_PATH);
        });
    }
}