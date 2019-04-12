package cnlpexercises;

import cnlp.Constants;
import cnlpexercises.algorithm.PosTagger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cnlp.Constants.*;

public class PosTaggingEvaluatorTest {

    /**
     * Test 15
     * To pass this test, implement {@link PosTaggingEvaluator#evaluate()}.
     * When you pass this test, take a look at the file
     * {@link Constants#TAGGING_COMPARISONS_JSON_PATH}.
     * <p>
     * Test 16 is here: {@link ChineseNaturalLanguageProcessingTest#main()}
     */
    @Test
    public void evaluate() {
        Assertions.assertDoesNotThrow(() -> {
            new PosTaggingEvaluator(
                    TEST_TAGGED_DATA_PATH,
                    TEST_STANDARD_TAGGED_DATA_PATH,
                    TAGGING_COMPARISONS_JSON_PATH,
                    PosTagger.WORD_DELIMITER
            ).evaluate();
        });
    }
}