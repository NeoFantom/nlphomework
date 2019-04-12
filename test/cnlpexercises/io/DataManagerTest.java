package cnlpexercises.io;

import cnlpexercises.Constants;
import cnlpexercises.algorithm.PosTagger;
import cnlpexercises.algorithm.Segmenter;
import cnlpexercises.model.StatisticsBoxTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.TEST_DATA_PATH;
import static cnlp.Constants.TEST_STANDARD_SEGMENTED_DATA_PATH;
import static cnlp.Constants.TEST_STANDARD_TAGGED_DATA_PATH;
import static cnlp.Constants.TEST_UNSEGMENTED_DATA_PATH;
import static cnlp.Constants.TEST_UNTAGGED_DATA_PATH;
import static cnlpexercises.Constants.*;
import static cnlpexercises.TodoException.todo;

public class DataManagerTest {

    private static void printWhenPositive(int n) {
        System.out.println("Don't modify outside //___ and //^^^ boundaries.");
        //________________________________________________
        todo();
        // Change < to > below.
        if (n < 0)
            System.out.println("Congratulations! ヽ(✿ﾟ▽ﾟ)ノ");
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

    /**
     * Test 3
     * Run this test first.
     * <p>
     * Go to the second level of TodoException stacktrace
     * (which is {@link DataManagerTest#printWhenPositive}).
     * To pass the test, delete the todo()
     * and modify the code between //___ and //^^^
     * so that the info will be printed.
     * <p>
     * Trick: Use Ctrl+click on variables, functions, classes
     * and anything to jump into any code.
     * <p>
     * Test 4 is here: {@link DataManagerTest#divideRawData()}
     */
    @Test
    public void learnAboutTodo() {
        Assertions.assertDoesNotThrow(() -> printWhenPositive(1));
    }

    /**
     * Test 4
     * To pass the test, you have to make the actual train/test ratio
     * (printed in console) close to {@link Constants#TRAIN_DATA_RATIO},
     * which is 0.9 by default.
     * <p>
     * Test 5 is here: {@link DataManagerTest#prepareTestDataForSegmentation()}
     */
    @Test
    public void divideRawData() {
        Assertions.assertDoesNotThrow(() -> DataManager.divideRawData(
                TEMP_PATH,
                TRAIN_DATA_PATH,
                TEST_DATA_PATH,
                TRAIN_DATA_RATIO));
    }

    /**
     * Test 5
     * To pass this test, make the assertion successful.
     * <p>
     * Test 6 is here: {@link DataManagerTest#prepareTestDataForPosTagging()}
     */
    @Test
    public void prepareTestDataForSegmentation() {
        Assertions.assertDoesNotThrow(() -> DataManager.prepareTestDataForSegmentation(
                TEST_DATA_PATH,
                TEST_STANDARD_SEGMENTED_DATA_PATH,
                TEST_UNSEGMENTED_DATA_PATH,
                Segmenter.WORD_DELIMITER));
    }

    /**
     * Test 6
     * To pass this test, make the assertion successful.
     * <p>
     * Test 7 is here: {@link StatisticsBoxTest#fromTrainData()}
     */
    @Test
    public void prepareTestDataForPosTagging() {
        Assertions.assertDoesNotThrow(() -> DataManager.prepareTestDataForPosTagging(
                TEST_DATA_PATH,
                TEST_STANDARD_TAGGED_DATA_PATH,
                TEST_UNTAGGED_DATA_PATH,
                PosTagger.WORD_DELIMITER));
    }
}