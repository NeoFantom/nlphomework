package cnlpexercises;

import cnlpexercises.algorithm.PosTagger;
import cnlpexercises.algorithm.Segmenter;
import cnlpexercises.io.DataManager;
import cnlpexercises.model.StatisticsBox;

import static cnlp.Constants.*;

public class ChineseNaturalLanguageProcessing {

    public static void main(String[] args) {
        System.out.println(ChineseNaturalLanguageProcessing.class.getName() + " is running...");
        run(false);
    }

    private static void run(boolean useDataFromLastRun) {
        StatisticsBox box;

        if (useDataFromLastRun) {
            box = StatisticsBox.fromSavedFile(SAVED_STATISTICS_BOX_PATH);
        } else {
            DataManager.divideRawData(RAW_DATA_PATH, TRAIN_DATA_PATH, TEST_DATA_PATH, TRAIN_DATA_RATIO);
            DataManager.prepareTestDataForSegmentation(
                    TEST_DATA_PATH,
                    TEST_STANDARD_SEGMENTED_DATA_PATH,
                    TEST_UNSEGMENTED_DATA_PATH,
                    Segmenter.WORD_DELIMITER);
            DataManager.prepareTestDataForPosTagging(
                    TEST_DATA_PATH,
                    TEST_STANDARD_TAGGED_DATA_PATH,
                    TEST_UNTAGGED_DATA_PATH,
                    PosTagger.WORD_DELIMITER);
            box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
            box.saveStatistics(SAVED_STATISTICS_BOX_PATH);
        }

        SegmentationTask.start(box);
        PosTaggingTask.start(box);
    }
}

