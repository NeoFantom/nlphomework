package cnlpexercises.model;

import cnlpexercises.algorithm.SegmenterTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlpexercises.Constants.*;

public class VocabularyTest {

    /**
     * Test 10
     * To pass this test, modify class {@link Vocabulary} so that
     * we can build an object of this class.
     * <p>
     * Test 11 is here: {@link VocabularyTest#longestMatchLength()}
     */
    @Test
    public void constructVocabulary() {
        Assertions.assertDoesNotThrow(() -> {
            StatisticsBox box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
            new Vocabulary(box.getWordList());
        });
    }

    /**
     * Test 11 (hard)
     * Your first test that's a little bit hard. Have faith!
     * <p>
     * To pass this test, implement {@link Vocabulary#longestMatchLength}
     * <p>
     * Test 12 is here: {@link SegmenterTest#segment()}
     */
    @Test
    public void longestMatchLength() {
        StatisticsBox box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
        Vocabulary vocabulary = new Vocabulary(box.getWordList());
        Assertions.assertEquals(vocabulary.longestMatchLength("".toCharArray()), 0);
        Assertions.assertEquals(vocabulary.longestMatchLength("的".toCharArray()), 1);
        Assertions.assertEquals(vocabulary.longestMatchLength("中国".toCharArray()), 2);
    }
}