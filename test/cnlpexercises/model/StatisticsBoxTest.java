package cnlpexercises.model;

import cnlpexercises.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlpexercises.Constants.*;


public class StatisticsBoxTest {

    private StatisticsBox box;

    /**
     * Test 7
     * Be patient. You don't need to do much to pass this test, but there's
     * a lot for you to digest.
     * <p>
     * To pass this test, make the assertion successful.
     * <p>
     * Test 8 is here: {@link StatisticsBoxTest#statisticsCachingSystemTest()}
     */
    @Test
    public void fromTrainData() {
        Assertions.assertDoesNotThrow(() -> {
            box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
        });
    }

    /**
     * Test 8
     * To pass this test, implement this function:
     * {@link StatisticsBox#fromSavedFile}
     * <p>
     * Test 9 is here: {@link StatisticsBoxTest#saveWordFrequencyAsJson()}
     */
    @Test
    public void statisticsCachingSystemTest() {
        box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
        box.saveStatistics(SAVED_STATISTICS_BOX_PATH);
        StatisticsBox box1 = StatisticsBox.fromSavedFile(SAVED_STATISTICS_BOX_PATH);

        Assertions.assertNotNull(box1);
        Assertions.assertEquals(box.getWordList(), box1.getWordList());
        Assertions.assertEquals(box.getTagList(), box1.getTagList());
        Assertions.assertEquals(box.getWord2FrequencyMap(), box1.getWord2FrequencyMap());
        Assertions.assertArrayEquals(box.getStartProbability(), box1.getStartProbability());
        Assertions.assertArrayEquals(box.getTransitionProbability()[0], box1.getTransitionProbability()[0]);
        Assertions.assertArrayEquals(box.getEmissionProbability()[0], box1.getEmissionProbability()[0]);
    }

    /**
     * Test 9
     * To pass this test, implement {@link StatisticsBox#saveWordFrequencyAsJson}.
     * <p>
     * When you finish this test, take a look at the file
     * {@link Constants#WORD_FREQUENCY_JSON_PATH}.
     * <p>
     * Test 10 is here: {@link VocabularyTest#constructVocabulary()}
     */
    @Test
    public void saveWordFrequencyAsJson() {
        Assertions.assertDoesNotThrow(() -> {
            box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
            box.saveWordFrequencyAsJson(WORD_FREQUENCY_JSON_PATH);
        });
    }
}