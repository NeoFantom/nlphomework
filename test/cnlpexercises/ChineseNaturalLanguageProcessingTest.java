package cnlpexercises;

import cnlp.Constants;
import cnlpexercises.algorithm.PosTagger;
import cnlpexercises.algorithm.Segmenter;
import cnlpexercises.io.DataManager;
import cnlpexercises.model.StatisticsBox;
import cnlpexercises.model.Vocabulary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.*;
import static cnlpexercises.TodoException.todo;

class ChineseNaturalLanguageProcessingTest {

    /**
     * Test 16
     * Let's get real!
     * To pass this test, implement {@link ChineseNaturalLanguageProcessingTest#run}.
     * Important: Please look into this test function to see guidance.
     * <p>
     * Test 17 is here: (→_→) Just joking (←_←)
     * <p>
     * This is the final part! Finishing this, you have done all the key parts of this
     * program! Hope you enjoyed it! Hopefully after these exercises, you'll feel that
     * programming with Java and NLP tasks are not that hard. After all, you've done it!
     */
    @Test
    void main() {
        Assertions.assertDoesNotThrow(() -> {
            // If you do it right, when run this test, you should see 2 exactly same evaluations both
            // on segmentation and POS tagging (because we are using the same train data
            // and test data!). To see if they are totally the same, use Ctrl+F in
            // console to find out.
            System.out.println("=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>");
            System.out.println("First run.");
            System.out.println("Running program with useDataFromLastRun = false.");
            run(false);
            System.out.println("=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>");
            System.out.println("Second run.");
            System.out.println("Running program with useDataFromLastRun = true.");
            run(true);
        });
    }

    /**
     * Run segmentation and POS tagging program once for each.
     *
     * @param useDataFromLastRun if true, we won't prepare and analyze the data. Instead, we just
     *                           use the StatisticsBox from last run.
     */
    private static void run(boolean useDataFromLastRun) {
        // Part 0:
        // Prepare data and construct a StatisticsBox. Copy the code inside
        // ChineseNaturalLanguageProcessing.run() // Uncomment this line and Ctrl-click to jump there.
        // You have to drop the last two lines where it calls two start() functions.
        todo();

        // Part 1:
        // Do segmentation. Copy the code inside
        // SegmentationTask.start() // Uncomment this line and Ctrl-click to jump there.
        todo();

        // Part 2:
        // Do POS tagging. Copy the code inside
        // PosTaggingTask.start() // Uncomment this line and Ctrl-click to jump there.
        todo();
    }
}