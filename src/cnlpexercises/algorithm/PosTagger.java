package cnlpexercises.algorithm;

import cnlpexercises.io.DataManager;
import cnlpexercises.model.StatisticsBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import static cnlp.Constants.END_OF_SENTENCE_STRINGS;
import static cnlpexercises.TodoException.todo;

public class PosTagger {

    public static final char WORD_DELIMITER = ' ';

    public static final char WORD_TAG_SEPARATOR = '/';

    public static final int OUT_OF_VOCABULARY = 0;

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
                    // code above (in comments) are needed.
                    String[] words = reader.readLine().split(Pattern.quote("" + WORD_DELIMITER));

                    //______________________________________________________________________________
                    // We put all the tagged strings into this StringBuilder. Using StringBuilder
                    // is much faster than concatenating lots of strings.
                    // Complete this line. Use default constructor to initialize taggedText.
                    todo();
                    StringBuilder taggedText = null;
                    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

                    // For printing info.
                    int sentencesCount = 0;

                    // The start of the text is always the start of a sentence, so
                    // we set startOfSentence to 0 as default.
                    int startOfSentence = 0;

                    for (int i = 1; i <= words.length; i++) {

                        // When we identify a sentence, tag it. Otherwise do nothing and
                        // wait until a whole sentence is found.
                        String lastWord = words[i - 1];
                        //______________________________________________________________________________
                        // This predicate is incomplete. Because we have to check if lastWord is an
                        // EOS(end-of-sentence). Fill the second condition after && using
                        // Constants.END_OF_SENTENCE_STRINGS.contains()
                        todo();
                        if (!lastWord.isEmpty() && true) {
                            sentencesCount++;

                            // Here we want to tag a sentence, then append it to taggedText. The
                            // sentence should be a subarray of words between the interval
                            // [startOfSentence, i).
                            // Your task: Fill in the parameter of taggedSentence() using
                            // Arrays.copyOfRange(). (Think of this function as getSubarray())
                            todo();
                            taggedText.append(
                                    taggedSentence(
                                            null));

                            // Now this word is the start of sentence and we should update
                            // variable startOfSentence. Figure out the new value for it.
                            todo();

                            System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
                                    + "Tagged sentences: " + sentencesCount);
                        }
                        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    }

                    //__________________________________________________________________________________
                    // This if-condition is when your test text doesn't end with EOS words, which
                    // makes some words untagged at last.
                    // How to check if there are still words remaining to be tagged?
                    // Hint: if the text DOES end with an EOS word, startOfSentence would be updated.
                    todo();
                    if (true)
                    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    {
                        sentencesCount++;
                        taggedText.append(
                                taggedSentence(
                                        Arrays.copyOfRange(words, startOfSentence, words.length)));
                        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
                                + "Tagged sentences: " + sentencesCount);
                    }
                    System.out.println();

                    //__________________________________________________________________________________
                    // Write taggedText use writer.write().
                    todo();
                    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                }
        );
    }

    private String taggedSentence(String[] untaggedWords) {

        int wordCount = untaggedWords.length;

        // Prepare the word indices for computation.
        int[] wordIndicesOfSentence = new int[wordCount];
        for (int i = 0; i < wordCount; i++) {
            wordIndicesOfSentence[i] = word2IndexMap.getOrDefault(untaggedWords[i], OUT_OF_VOCABULARY);
        }

        //____________________________________________________________________________________________
        // Now you have wordIndicesOfSentence, you want to predict tags, i.e. tagIndicesOfSentence.
        // Hint: Use Viterbi.compute()
        todo();
        int[] tagIndicesOfSentence = null;
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        StringBuilder taggedWords = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            taggedWords.append(untaggedWords[i]);
            taggedWords.append(WORD_TAG_SEPARATOR);
            taggedWords.append(tagList.get(tagIndicesOfSentence[i]));
            taggedWords.append(WORD_DELIMITER);
        }

        return taggedWords.toString();
    }
}
