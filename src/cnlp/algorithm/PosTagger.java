package cnlp.algorithm;

import cnlp.io.DataManager;
import cnlp.model.StatisticsBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

public class PosTagger {

    public static final char WORD_DELIMITER = ' ';

    public static final char WORD_TAG_SEPARATOR = '/';

    public static final int OUT_OUT_VOCABULARY = 0;

    public static final HashSet<String> END_OF_SENTENCE_SET =
            new HashSet<>(Arrays.asList(
                    "，", "。", "？", "！", "；"
            ));

    private ArrayList<String> tagList;

    private HashMap<String, Integer> word2IdMap;

    private double[] startProbability;

    private double[][] transitionProbability;

    private double[][] emissionProbability;

    public PosTagger(StatisticsBox statisticsBox) {
        this.tagList = statisticsBox.getTagList();
        this.word2IdMap = statisticsBox.getWord2IdMap();
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

                    // Assuming test data properly prepared. Otherwise the
                    // code above are needed.
                    String[] words = reader.readLine().split(Pattern.quote("" + WORD_DELIMITER));

                    StringBuilder taggedText = new StringBuilder();

                    int sentencesCount = 0;

                    int startOfSentence = 0;
                    for (int i = 1; i <= words.length; i++) {
                        String lastWord = words[i - 1];
                        if (!lastWord.isEmpty() && END_OF_SENTENCE_SET.contains(lastWord)) {
                            sentencesCount++;
                            taggedText.append(taggedSentence(Arrays.copyOfRange(words, startOfSentence, i)));
                            System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
                                    + "Tagged sentences: " + sentencesCount);
                            startOfSentence = i;
                        }
                    }
                    if (startOfSentence != words.length) {
                        // Tag the last words if it doesn't end with end-of-sentence characters
                        sentencesCount++;
                        taggedText.append(taggedSentence(Arrays.copyOfRange(words, startOfSentence, words.length)));
                        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
                                + "Tagged sentences: " + sentencesCount);
                    }
                    System.out.println();
                    writer.write(taggedText.toString());
                }
        );
    }

    private String taggedSentence(String[] untaggedWords) {

        int wordCount = untaggedWords.length;

        int[] wordIdsOfSentence = new int[wordCount];
        for (int i = 0; i < wordCount; i++) {
            wordIdsOfSentence[i] = word2IdMap.getOrDefault(untaggedWords[i], OUT_OUT_VOCABULARY);
        }

        int[] tagIdsOfSentence = Viterbi.compute(
                wordIdsOfSentence,
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
