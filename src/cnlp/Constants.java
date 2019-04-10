package cnlp;

import java.util.Arrays;
import java.util.HashSet;

public class Constants {

    public static final HashSet<String> END_OF_SENTENCE_STRINGS =
            new HashSet<>(Arrays.asList(
                    "，", "。", "？", "！", "；"
            ));

    public static final double TRAIN_DATA_RATIO = 0.95;

    public static final String RAW_DATA_DELIMITER = "\\s+";

    public static final String RAW_DATA_PATH = "data/199801.txt";

    public static final String TEMP_PATH = "data/temp.txt";

    public static final String TRAIN_DATA_PATH = "data/trainData.txt";

    public static final String TEST_DATA_PATH = "data/testData.txt";

    public static final String SAVED_STATISTICS_BOX_PATH = "data/savedStatisticsBox";

    public static final String SEGMENTATION_DIR_PATH = "data/segmentationTest/";

    public static final String TEST_UNSEGMENTED_DATA_PATH = SEGMENTATION_DIR_PATH + "unsegmentedData.txt";

    public static final String TEST_SEGMENTED_DATA_PATH = SEGMENTATION_DIR_PATH + "segmentedData.txt";

    public static final String TEST_STANDARD_SEGMENTED_DATA_PATH = SEGMENTATION_DIR_PATH + "standardSegmentedData.txt";

    public static final String DIFFERENT_PHRASES_JSON_PATH = SEGMENTATION_DIR_PATH + "differentPhrases.json";

    public static final String WORD_FREQUENCY_JSON_PATH = SEGMENTATION_DIR_PATH + "wordFrequencyStatistics.json";

    public static final String VOCABULARY_JSON_PATH = SEGMENTATION_DIR_PATH + "vocabulary.json";

    public static final String POS_TAGGING_DIR_PATH = "data/posTaggingTest/";

    public static final String TEST_UNTAGGED_DATA_PATH = POS_TAGGING_DIR_PATH + "untaggedData.txt";

    public static final String TEST_TAGGED_DATA_PATH = POS_TAGGING_DIR_PATH + "taggedData.txt";

    public static final String TEST_STANDARD_TAGGED_DATA_PATH = POS_TAGGING_DIR_PATH + "standardTaggedData.txt";

    public static final String TAGGING_COMPARISONS_JSON_PATH = POS_TAGGING_DIR_PATH + "taggingComparisons.json";
}
