package cnlp;

import cnlp.algorithm.Segmenter;
import cnlp.io.DataManager;
import cnlp.model.StatisticsBox;
import cnlp.model.Vocabulary;

import static cnlp.Constants.*;

public class SegmentationTask {

    private static void runSingleTask(boolean useDataFromLastRun) {
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
            box = StatisticsBox.fromTrainData(TRAIN_DATA_PATH);
            box.saveStatistics(SAVED_STATISTICS_BOX_PATH);
        }
        start(box);
    }

    public static void main(String[] args) {
        System.out.println(SegmentationTask.class.getName() + " Running...");

        runSingleTask(false);
    }

    public static void start(StatisticsBox box) {
        Vocabulary vocabulary = new Vocabulary(box.getWord2FrequencyMap().keySet());
        DataManager.writeObjectToJson(VOCABULARY_JSON_PATH, vocabulary);

        Segmenter segmenter = new Segmenter(vocabulary);
        segmenter.letsDoIt(TEST_UNSEGMENTED_DATA_PATH, TEST_SEGMENTED_DATA_PATH);
        new SegmentationEvaluator(Segmenter.WORD_DELIMITER,
                TEST_SEGMENTED_DATA_PATH,
                TEST_STANDARD_SEGMENTED_DATA_PATH).evaluate(DIFFERENT_PHRASES_JSON_PATH);
    }
}