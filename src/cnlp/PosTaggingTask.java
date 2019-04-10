package cnlp;

import cnlp.algorithm.PosTagger;
import cnlp.io.DataManager;
import cnlp.model.StatisticsBox;

import static cnlp.Constants.*;

public class PosTaggingTask {

    private static void runSingleTask(boolean useDataFromLastRun) {
        StatisticsBox box;
        if (useDataFromLastRun) {
            box = StatisticsBox.fromSavedFile(SAVED_STATISTICS_BOX_PATH);
        } else {
            DataManager.divideRawData(RAW_DATA_PATH, TRAIN_DATA_PATH, TEST_DATA_PATH, TRAIN_DATA_RATIO);
            DataManager.prepareTestDataForPosTagging(
                    TEST_DATA_PATH,
                    TEST_STANDARD_TAGGED_DATA_PATH,
                    TEST_UNTAGGED_DATA_PATH,
                    PosTagger.WORD_DELIMITER);
            box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
            box.saveStatistics(SAVED_STATISTICS_BOX_PATH);
        }
        start(box);
    }

    public static void main(String[] args) {
        System.out.println(PosTaggingTask.class.getName() + " is running...");

        runSingleTask(false);
    }

    public static void start(StatisticsBox box) {
        PosTagger posTagger = new PosTagger(box);
        posTagger.roll(TEST_UNTAGGED_DATA_PATH, TEST_TAGGED_DATA_PATH);

        new PosTaggingEvaluator(
                TEST_TAGGED_DATA_PATH,
                TEST_STANDARD_TAGGED_DATA_PATH,
                TAGGING_COMPARISONS_JSON_PATH,
                PosTagger.WORD_DELIMITER
        ).evaluate();
    }
}
