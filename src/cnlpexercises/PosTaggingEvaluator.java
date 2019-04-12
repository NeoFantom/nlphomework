package cnlpexercises;

import cnlpexercises.io.DataManager;
import cnlpexercises.model.WordAndTag;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static cnlpexercises.TodoException.todo;

public class PosTaggingEvaluator {

    private String taggedPath;

    private String standardTaggedPath;

    private String taggingComparisonPath;

    private char WORD_DELIMITER;

    public PosTaggingEvaluator(
            String taggedPath, String standardTaggedPath, String taggingComparisonPath, char WORD_DELIMITER) {
        this.taggedPath = taggedPath;
        this.standardTaggedPath = standardTaggedPath;
        this.taggingComparisonPath = taggingComparisonPath;
        this.WORD_DELIMITER = WORD_DELIMITER;
    }

    /**
     * For convenience of writing tagging results.
     */
    private class TaggingComparison {
        boolean different;
        String standardTagging;
        String yourTagging;

        TaggingComparison(boolean different, String standardTagging, String yourTagging) {
            this.different = different;
            this.standardTagging = standardTagging;
            this.yourTagging = yourTagging;
        }
    }

    private ArrayList<WordAndTag> readWordAndTags(String path) {
        ArrayList<WordAndTag> wordAndTags = new ArrayList<>();
        DataManager.parseWordAndTags(
                path,
                Pattern.quote("" + WORD_DELIMITER),
                wordAndTags::add
        );
        return wordAndTags;
    }

    public void evaluate() {
        System.out.println("Evaluating POS tagging task...");
        //_______________________________________________________________________________________
        // Initialize myTagging similarly.
        todo();
        ArrayList<WordAndTag> standardTagging = readWordAndTags(standardTaggedPath);
        ArrayList<WordAndTag> myTagging = null;

        // Make sure standardTagging.size() == myTagging.size(). If not, some code must be wrong!
        // Hint: Use assert.
        todo();
        int size = standardTagging.size();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        ArrayList<TaggingComparison> comparisons = new ArrayList<>();

        // Number of correct tags
        int countCommon = 0;

        for (int i = 0; i < size; i++) {
            WordAndTag standardWordAndTag = standardTagging.get(i);
            WordAndTag myWordAndTag = myTagging.get(i);

            //___________________________________________________________________________________________
            // Check if standard tag is equal to my tag (given by previous tagging program). And add a
            // comparison between them accordingly.
            // Hint: Use String.equals() instead of s1 == s2 (because this compares variable addresses.
            // And use WordAndTag.getTag() to extract the tag.
            todo();
            if (true)
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            {
                countCommon++;
                comparisons.add(new TaggingComparison(
                        false, standardWordAndTag.toString(), myWordAndTag.toString()));
            } else {
                //_______________________________________________________________________________________
                // What should you add to comparison here?
                todo();
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            }
        }

        //_______________________________________________________________________________________________
        // Use DataManager.writeObjectToJson() to write comparisons into taggingComparisonPath as json file.
        todo();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        System.out.println("Evaluation finished.");
        System.out.println("=========================================================");
        System.out.println("                 POS Tagging Evaluation                  ");
        System.out.println("Total words:            " + size);
        System.out.println("Correctly tagged words: " + countCommon);
        System.out.println("Accuracy:               " + countCommon / (double) size);
        System.out.println("---------------------------------------------------------");
    }
}
