package cnlp.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static cnlp.Constants.RAW_DATA_PATH;
import static cnlp.Constants.SAVED_STATISTICS_BOX_PATH;


class StatisticsBoxTest {

    private StatisticsBox box;

    @BeforeEach
    void setUp() {
        box = StatisticsBox.fromTrainData(
                RAW_DATA_PATH);
    }

    /**
     * Test 1 (There is no Test 0 ^_^)
     * To pass the test, the sum of frequencies of all words should equal 100,
     * because we only read 100 words from raw data.
     * <p>
     * Test 2 is here: {@link StatisticsBoxTest#fromRawData()}
     */
    @Test
    void fromPartOfRawData() {


        Collection<Integer> frequencies = box.getWord2FrequencyMap().values();
        int sum = frequencies.stream().reduce((it, last) -> last + it).orElse(0);
        Assertions.assertEquals(100, sum);
    }

    /**
     *
     */
    @Test
    void fromRawData() {
    }


    @Test
    void saveStatistics() {
        box.saveStatistics(SAVED_STATISTICS_BOX_PATH);
        StatisticsBox box1 = StatisticsBox.fromSavedFile(SAVED_STATISTICS_BOX_PATH);

        System.out.println();
        System.out.println("box.getWord2FrequencyMap()");
        System.out.println(box.getWord2FrequencyMap());
        System.out.println("box1.getWord2FrequencyMap()");
        System.out.println(box1.getWord2FrequencyMap());
        System.out.println("box.getWord2TagMap()");
        System.out.println();
    }
}