package cnlp.algorithm;

import cnlp.io.DataManager;
import cnlp.model.StatisticsBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import static cnlp.Constants.END_OF_SENTENCE_STRINGS;

public class PosTagger {

    public static final char WORD_DELIMITER = ' ';

    public static final char WORD_TAG_SEPARATOR = '/';

    public static final int OUT_OUT_VOCABULARY = 0;

    private ArrayList<String> tagList;

    private HashMap<String, Integer> word2IndexMap;

    private double[] startProbability;

    private double[][] transitionProbability;

    private double[][] emissionProbability;

    public PosTagger(StatisticsBox statisticsBox) {
        this.tagList = statisticsBox.getTagList();
        this.word2IndexMap = statisticsBox.getWord2IndexMap();
        this.startProbability = statisticsBox.getStartProbability();
        this.transitionProbability = statisticsBox.getTransitionProbability();
        this.emissionProbability = statisticsBox.getEmissionProbability();
    }

    public void roll(String inputPath, String outputPath) {
        System.out.println("Start tagging...");
        DataManager.readAndWrite(
                inputPath,
                outputPath,
                (reader, writer) -> {
//                    StringBuilder untaggedText = new StringBuilder();
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        untaggedText.append(line.trim());
//                        untaggedText.append(WORD_DELIMITER);
//                    }
//                    String[] words = untaggedText.toString().split(Pattern.quote(WORD_DELIMITER));

                    // Assuming test data properly prepared with only one line. Otherwise the
                    // code above are needed.


                    String[] words = reader.readLine().split(Pattern.quote("" + WORD_DELIMITER));

                    // We put all the segmented strings into this StringBuilder.
                    // Using StringBuilder is much faster than concatenating lots
                    // of strings.
                    StringBuilder taggedText = new StringBuilder();

                    // For printing info.
                    int sentencesCount = 0;

                    // The start of the text is always the start of a sentence, so
                    // we set startOfSentence to 0 as default.
                    int startOfSentence = 0;

                    for (int i = 1; i <= words.length; i++) {

                        // When we identify a sentence, tag it. Otherwise do nothing and
                        // wait until a whole sentence is found.
                        String lastWord = words[i - 1];
                        if (!lastWord.isEmpty() && END_OF_SENTENCE_STRINGS.contains(lastWord)) {
                            sentencesCount++;
                            taggedText.append(
                                    taggedSentence(
                                            Arrays.copyOfRange(words, startOfSentence, i)));
                            startOfSentence = i;

                            System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
                                    + "Tagged sentences: " + sentencesCount);
                        }
                    }

                    // This if-condition is when your test text doesn't end with end-of-sentence
                    // characters, which makes some words untagged at last.
                    if (startOfSentence != words.length) {
                        sentencesCount++;
                        taggedText.append(
                                taggedSentence(
                                        Arrays.copyOfRange(words, startOfSentence, words.length)));
                        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
                                + "Tagged sentences: " + sentencesCount);
                    }
                    System.out.println();

                    // Write tagging result.
                    writer.write(taggedText.toString());
                }
        );
    }

    private String taggedSentence(String[] untaggedWords) {

        int wordCount = untaggedWords.length;

        // Prepare the word indices for computation.
        int[] wordIndicesOfSentence = new int[wordCount];
        for (int i = 0; i < wordCount; i++) {
            wordIndicesOfSentence[i] = word2IndexMap.getOrDefault(untaggedWords[i], OUT_OUT_VOCABULARY);
        }

        int[] tagIdsOfSentence = Viterbi.compute(
                wordIndicesOfSentence,
                startProbability,
                transitionProbability,
                emissionProbability
        );

        StringBuilder taggedWords = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            taggedWords.append(untaggedWords[i]);
            taggedWords.append(WORD_TAG_SEPARATOR);
            taggedWords.append(tagList.get(tagIdsOfSentence[i]));
            taggedWords.append(WORD_DELIMITER);
        }

        return taggedWords.toString();
    }
}
