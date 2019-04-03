package cnlp.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.TEMP_PATH;

class VocabularyTest {

    Vocabulary vocabulary;

    @BeforeEach
    void setUp() {
        StatisticsBox box = StatisticsBox.fromTrainData(TEMP_PATH);
        vocabulary = new Vocabulary(box.getWord2FrequencyMap().keySet());
    }

    @Test
    void longestMatchLength() {
        char[] chars = "19980101-01-005-002".toCharArray();
        System.out.println(vocabulary.getMaxWordLength());
        Assertions.assertEquals(1, vocabulary.longestMatchLength(chars));
    }
}