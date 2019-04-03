package cnlp;

import cnlp.algorithm.Segmenter;
import cnlp.io.DataManager;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.*;

class DataManagerTest {

    @Test
    void divideRawData() {
        DataManager.divideRawData(
                 RAW_DATA_PATH,
                 TRAIN_DATA_PATH,
                 TEST_DATA_PATH,
                 TRAIN_DATA_RATIO);
        DataManager.prepareTestDataForSegmentation(
                 TEST_DATA_PATH,
                 TEST_STANDARD_SEGMENTED_DATA_PATH,
                 TEST_UNSEGMENTED_DATA_PATH,
                Segmenter.WORD_DELIMITER
        );
    }
}