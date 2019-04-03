package cnlp;

import cnlp.io.DataManager;
import cnlp.model.WordAndTag;

import java.util.ArrayList;
import java.util.regex.Pattern;

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

    private class TaggingComparison {
        boolean different;
        String standardTagging;
        String yourTagging;

        public TaggingComparison(boolean different, String standardTagging, String yourTagging) {
            this.different = different;
            this.standardTagging = standardTagging;
            this.yourTagging = yourTagging;
        }
    }

    public void evaluate() {
        System.out.println("Evaluating POS tagging task...");
        ArrayList<WordAndTag> standardTagging = readWordAndTags(standardTaggedPath);
        ArrayList<WordAndTag> myTagging = readWordAndTags(taggedPath);

        if (standardTagging.size() != myTagging.size()) {
            throw new RuntimeException("POS Tagging Error!" +
                    "\nstandardTagging.size()=" + standardTagging.size() +
                    "\nmyTagging.size()=" + myTagging.size());
        }
        int size = standardTagging.size();


        ArrayList<TaggingComparison> comparisons = new ArrayList<>(size);

        int countCommon = 0;
        for (int i = 0; i < size; i++) {
            if (standardTagging.get(i).toString()
                    .equals(
                            myTagging.get(i).toString())) {
                countCommon++;
                comparisons.add(new TaggingComparison(
                        false, standardTagging.get(i).toString(), myTagging.get(i).toString()));
            } else {
                comparisons.add(new TaggingComparison(
                        true, standardTagging.get(i).toString(), myTagging.get(i).toString()));
            }
        }

        DataManager.writeObjectToJson(taggingComparisonPath, comparisons);

        System.out.println("Evaluation finished.");
        System.out.println("======================================================");
        System.out.println("                 POS Tagging Evaluation               ");
        System.out.println("Total words:            " + size);
        System.out.println("Correctly tagged words: " + countCommon);
        System.out.println("Accuracy:               " + countCommon / (double) size);
        System.out.println("---------------------------------------------------------");
    }

    private ArrayList<WordAndTag> readWordAndTags(String path) {
        ArrayList<WordAndTag> wordAndTags = new ArrayList<>();
        DataManager.parseWordAndTags(
                path,
                Pattern.quote("" + WORD_DELIMITER),
                wordAndTag -> wordAndTags.add(wordAndTag)
        );
        return wordAndTags;
    }
}
