package cnlp;

import cnlp.io.DataManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

public class SegmentationEvaluator {

    private final char WORD_DELIMITER;

    private ArrayList<MarkableString> myWordsList;

    private ArrayList<MarkableString> standardWordsList;

    private String differentPhrasesPath;

    /**
     * For special use of this program.
     * <p>
     * When evaluating, mark the words that are segmented differently.
     * By default, a word is unmarked on initialization.
     */
    private class MarkableString {
        private final String string;
        private int mark;

        MarkableString(String string) {
            this.string = string;
            this.mark = 0;
        }

        MarkableString markSelf() {
            this.mark = 1;
            return this;
        }

        boolean isMarked() {
            return this.mark != 0;
        }

        boolean isUnmarked() {
            return this.mark == 0;
        }

        @Override
        public String toString() {
            return string;
        }

        public boolean equals(MarkableString markableString) {
            return this.string.equals(markableString.toString());
        }

        int length() {
            return string.length();
        }
    }

    /**
     * For convenience of writing evaluation results.
     */
    private class SegmentationComparison{
        String standardSegmentation;
        String mySegmentaion;

        public SegmentationComparison(String standardSegmentation, String mySegmentaion) {
            this.standardSegmentation = standardSegmentation;
            this.mySegmentaion = mySegmentaion;
        }
    }

    public SegmentationEvaluator(
            char WORD_DELIMITER,
            String segmented,
            String standardSegmented,
            String differentPhrasesPath) {
        this.WORD_DELIMITER = WORD_DELIMITER;
        this.myWordsList = readMarkableWordList(segmented);
        this.standardWordsList = readMarkableWordList(standardSegmented);
        this.differentPhrasesPath = differentPhrasesPath;
    }

    /**
     * Called when segmentation files are malformed.
     *
     * @param standardPosition word position of bad data in standard segmentation.
     * @param standardPhrase   bad phrase of the standard segmentation.
     * @param myPosition       word position of bad data in my segmentation.
     * @param myPhrase         bad phrase of the my segmentation.
     */
    private void reportError(
            int standardPosition, String standardPhrase, int myPosition, String myPhrase) {
        throw new RuntimeException(
                "Malformed segmentation data!"+
                standardPosition + "-th word in standard segmentation file is:"+
                standardPhrase +
                myPosition + "-th word your segmentation file is:"+
                myPhrase);
    }

    /**
     * Read segmented word list and mark the different ones.
     *
     * @param path file path to be read.
     * @return a {@link ArrayList<MarkableString>} read from path.
     */
    private ArrayList<MarkableString> readMarkableWordList(String path) {
        ArrayList<MarkableString> list = new ArrayList<>();
        StringBuilder word = new StringBuilder(); // Empty string

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            int charValue = reader.read();
            while (charValue != -1) {

                char c = (char) charValue;

                if (c != WORD_DELIMITER) {
                    word.append(c);
                } else {
                    if (word.length() != 0) {
                        list.add(new MarkableString(word.toString()));
                        word.delete(0, word.length());
                    }
                }
                charValue = reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        return list;
    }

    // Properties used as temporary variables for efficiency.
    private StringBuilder standardPhrase = new StringBuilder();
    private StringBuilder myPhrase = new StringBuilder();

    /**
     * Starting from position {@param s} and {@param m}, of standard and my segmentation
     * respectively, keep moving to newS and newM, such that the two phrases
     * of standardWordsList[s:newS] and myWordsList[m:newM] are equal.
     *
     * @param s beginning position of standardWordsList.
     * @param m beginning position of myWordsList.
     * @return new int[]{newS, newM}.
     */
    private int[] forwardUntilEqual(int s, int m) {

        try {
            standardPhrase.append(standardWordsList.get(s).markSelf());
            myPhrase.append(myWordsList.get(m).markSelf());

            while (standardPhrase.length() != myPhrase.length()) {

                if (standardPhrase.length() < myPhrase.length()) {
                    standardPhrase.append(standardWordsList.get(++s).markSelf());
                } else {
                    myPhrase.append(myWordsList.get(++m).markSelf());
                }
            }

            // Now standardPhrase.length()==myPhrase.length(). These two
            // strings should be totally equal otherwise it's malformed data.
            if (!standardPhrase.toString().equals(myPhrase.toString())) {
                reportError(
                        s, standardPhrase.toString(),
                        m, myPhrase.toString());
            }

            // Clear StringBuilders for next use.
            standardPhrase.delete(0, standardPhrase.length());
            myPhrase.delete(0, myPhrase.length());
        } catch (IndexOutOfBoundsException e) {
            if (s == 0 && m == 0) {
                reportError(
                        s, standardPhrase.toString(),
                        m, myPhrase.toString());
            }
        }

        return new int[]{s, m};
    }

    /**
     * Summarize the number of unmarked(different from the other segmentation)
     * and marked(oppositely) words.
     *
     * @param wordList to be summarized.
     * @return new int[]{unmarked, marked}.
     */
    private int[] summarize(List<MarkableString> wordList) {
        int unmarked = 0, marked = 0;
        for (MarkableString markableString : wordList)
            if (markableString.isUnmarked())
                unmarked++;
            else
                marked++;
        return new int[]{unmarked, marked};
    }

    private void printEvaluation(int common, int onlyInStandard, int onlyInMy, int totalCharacters) {
        double TP = common;
        double FP = onlyInMy;
        double FN = onlyInStandard;
        double TN = totalCharacters - TP - FP - FN;
        double accuracy = (TP + TN) / totalCharacters;
        double recall = TP / (TP + FN);
        System.out.println("=========================================================");
        System.out.println("            Segmentation Evaluation Finished             ");
        System.out.println("Number of words in your segmentation:     y=" + onlyInMy + common);
        System.out.println("Number of words in standard segmentation: s=" + onlyInStandard + common);
        System.out.println("Number of words in common:                c=" + common);
        System.out.println("Number of characters totally:             t=" + totalCharacters);
        System.out.println("---------------------------------------------------------");
        System.out.println("              Segmentation Evaluation Table              ");
        System.out.println("---------------------------------------------------------");
        System.out.println("Predicted:             Yes                No             ");
        System.out.printf("Indeed Yes:        TP=%7.0f        FN=%7.0f\n", TP, FN);
        System.out.printf("Indeed No :        FP=%7.0f        TN=%7.0f\n", FP, TN);
        System.out.println();
        System.out.println("Accuracy:               (TP + TN) / (TP + TN + FP + FN)=" + accuracy);
        System.out.println("Recall:                                  TP / (TP + FN)=" + recall);
        System.out.println("F1 measure: 2 * recall * accuracy / (recall + accuracy)=" + 2 * recall * accuracy / (recall + accuracy));
        System.out.println("---------------------------------------------------------");
    }

    /**
     * @param markableWordList {@link List<MarkableString>}
     * @return an {@link ArrayList<String>} each of which is a phrase, composed of
     * the words that has been marked, which means they are different from the
     * other segmentation.
     */
    private ArrayList<String> extractMarkedPhrases(List<MarkableString> markableWordList) {

        ArrayList<String> differentPhrases = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < markableWordList.size(); i++) {

            if (markableWordList.get(i).isMarked()) {

                for (; i < markableWordList.size(); i++) {
                    if (markableWordList.get(i).isUnmarked())
                        break;
                    builder.append(markableWordList.get(i));
                    builder.append(WORD_DELIMITER);
                }
                differentPhrases.add(builder.toString());
                builder.delete(0, builder.length());
            }
        }
        return differentPhrases;
    }

    public void evaluate() {

        // Step 1 Write differently segmented phrases of my segmentation and standard segmentation
        ArrayList<String> standardDifferentPhrases = extractMarkedPhrases(standardWordsList);
        ArrayList<String> myDifferentPhrases = extractMarkedPhrases(myWordsList);

        assert standardDifferentPhrases.size() == myDifferentPhrases.size();
        int differentPhrasesSize = standardDifferentPhrases.size();

        ArrayList<SegmentationComparison> comparisons = new ArrayList<>(differentPhrasesSize);

        for (int i = 0; i < differentPhrasesSize; i++) {
            comparisons.add(new SegmentationComparison(
                    standardDifferentPhrases.get(i), myDifferentPhrases.get(i)));
        }
        DataManager.writeObjectToJson(differentPhrasesPath, comparisons);

        // Step 2 Summarize number of differences and print segmentation performance
        for (int s = 0, m = 0; s < differentPhrasesSize && m < differentPhrasesSize; s++, m++) {
            if (!standardWordsList.get(s).equals(myWordsList.get(m))) {
                int[] sm = forwardUntilEqual(s, m);
                s = sm[0];
                m = sm[1];
            }
        }
        int totalCharacters = standardWordsList.stream().mapToInt(MarkableString::length).sum();

        int[] standardSummary = summarize(standardWordsList);
        int[] mySummary = summarize(myWordsList);

        assert standardSummary[0] == mySummary[0];
        int common = standardSummary[0];
        int onlyInStandard = standardSummary[1];
        int onlyInMy = mySummary[1];
        printEvaluation(common, onlyInStandard, onlyInMy, totalCharacters);
    }
}
