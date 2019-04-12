package cnlpexercises.model;

import cnlpexercises.io.DataManager;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cnlp.Constants.END_OF_SENTENCE_STRINGS;
import static cnlp.Constants.RAW_DATA_DELIMITER;
import static cnlpexercises.TodoException.todo;

public class StatisticsBox implements Serializable {

    private ArrayList<String> wordList = new ArrayList<>();

    private ArrayList<String> tagList = new ArrayList<>();

    private HashMap<String, Integer> word2IndexMap = new HashMap<>();

    private HashMap<String, Integer> tag2IndexMap = new HashMap<>();

    private double[] startProbability;

    private double[][] transitionProbability;

    private double[][] emissionProbability;

    private HashMap<String, Integer> word2FrequencyMap;

    private class WordWithFrequency {
        private String word;
        private int frequency;

        WordWithFrequency(Map.Entry<String, Integer> word2FrequencyMapEntry) {
            word = word2FrequencyMapEntry.getKey();
            frequency = word2FrequencyMapEntry.getValue();
        }

        String getWord() {
            return word;
        }

        int getFrequency() {
            return frequency;
        }
    }

    private StatisticsBox() {
    }

    /**
     * This method is used to construct:
     * {@link StatisticsBox#word2IndexMap}
     * {@link StatisticsBox#wordList}
     * {@link StatisticsBox#tag2IndexMap}
     * {@link StatisticsBox#tagList}
     * <p>
     * After calling this method, these 4 properties should be all set.
     *
     * @param wordAndTag the item to be used
     */
    private void rememberWordAndTag(WordAndTag wordAndTag) {
        String word = wordAndTag.getWord();
        String tag = wordAndTag.getTag();
        if (word2IndexMap.putIfAbsent(word, wordList.size()) == null) {
            wordList.add(word);
        }
        if (tag2IndexMap.putIfAbsent(tag, tagList.size()) == null) {
            tagList.add(tag);
        }
    }

    // Properties used as temporary variables for efficiency.
    private String lastWord = "。";
    private int lastTagId = -1;

    /**
     * This method constructs properties:
     * {@link StatisticsBox#word2FrequencyMap}
     * {@link StatisticsBox#startProbability}
     * {@link StatisticsBox#transitionProbability}
     * {@link StatisticsBox#emissionProbability}
     * <p>
     * After calling this method, these 4 properties should be all set.
     *
     * @param wordAndTag the item to be used
     */
    private void doStatistics(WordAndTag wordAndTag) {
        //___________________________________________________________________________________
        // Read this entire function. Don't forget the doc comment above.
        todo();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        // Extract information of variable wordAndTag.
        String word = wordAndTag.getWord();
        String tag = wordAndTag.getTag();
        int wordId = this.word2IndexMap.get(word);
        int tagId = this.tag2IndexMap.get(tag);

        // Update word frequency statistics.
        this.word2FrequencyMap.put(
                word,
                this.word2FrequencyMap.getOrDefault(word, 0) + 1);

        // Check if last word is end of sentence, i.e. this word is the start of sentence.
        // Update start probability if it's start of sentence.
        if (END_OF_SENTENCE_STRINGS.contains(lastWord)) {
            this.startProbability[tagId]++;
        }
        // Or update transition probability if not.
        // transitionProbability[lastTagId][tagId] is the probability: P(tag | lastTag)
        else {
            this.transitionProbability[lastTagId][tagId]++;
        }
        lastWord = word;
        lastTagId = tagId;

        // The probability of emitting a word given a tag, or P(word | tag)
        emissionProbability[tagId][wordId]++;
    }

    /**
     * After normalization, all components of {@param vector} add
     * up to 1. If the {@param vector} is the frequency distribution,
     * then after normalization it is just the probability distribution
     * of those frequencies.
     *
     * @param vector to be normalised
     */
    private static void normalizeVector(double[] vector) {
        double s = Arrays.stream(vector).sum();
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= s;
        }
    }

    private void normalizeProbabilities() {
        normalizeVector(startProbability);
        Arrays.stream(transitionProbability).forEach(StatisticsBox::normalizeVector);
        //________________________________________________________________________________
        // Normalize emissionProbability similarly as transitionProbability.
        todo();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

    public static StatisticsBox fromTrainData(String path) {
        System.out.println("Constructing " + StatisticsBox.class.getName() + " from train data.");
        StatisticsBox box = new StatisticsBox();

        //________________________________________________________________________________________
        // What if there is an OOV(out-of-vocabulary) word? To deal with them, we add an
        // empty String as a placeholder before reading any train data, and map all the
        // OOV words to this one.
        // Your task: Add an empty string to box.wordList.
        todo();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        System.out.println("Extracting words ang tags...");
        //________________________________________________________________________________________
        // Try to understand this line of code. Read all the comments of rememberWordAndTag().
        // Especially doc comments which are above function name.
        todo();
        DataManager.parseWordAndTags(path, RAW_DATA_DELIMITER, box::rememberWordAndTag);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        int tagCount = box.tagList.size();
        int wordCount = box.wordList.size();
        box.startProbability = new double[tagCount];
        box.transitionProbability = new double[tagCount][tagCount];
        box.emissionProbability = new double[tagCount][wordCount + 1]; // Note the +1 here for OOV
        box.word2FrequencyMap = new HashMap<>(wordCount);

        // We must give the emission probability of out-of-vocabulary words
        // given any tag, i.e. P_emi(OOV | some_tag). We simply set
        // all of them to 1. NOT ZERO because all zeros add up to 0, which
        // means total frequency is 0, which means the probability of any
        // tag given an OOV word is 0 (think about Bayes equation), impossible.
        for (int i = 0; i < tagCount; i++) {
            //____________________________________________________________________________________
            // Read the comments above. Here P_emi(OOV | tag_i) is emissionProbability[i][0] in code.
            // Your task: assign box.emissionProbability[i][0] to 1 for every i.
            todo();
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        }

        System.out.println("Doing statistics...");
        //________________________________________________________________________________________
        // Try to understand this line of code. Go inside function doStatistics().
        todo();
        DataManager.parseWordAndTags(path, RAW_DATA_DELIMITER, box::doStatistics);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        box.normalizeProbabilities();

        System.out.println("Statistics done.");
        return box;
    }

    public static StatisticsBox fromSavedFile(String path) {
        System.out.println("Recovering " + StatisticsBox.class.getName() + " from saved file.");
        //_______________________________________________________________________________
        // Read a StatisticsBox from path by imitating saveStatistics(String path) function.
        // Hint: use get(i) to get the i-th element of a list.
        todo();
        return null;
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

    public void saveStatistics(String path) {
        DataManager.writeObjectsToBinary(path, this);
    }

    public void saveWordFrequencyAsJson(String path) {
        // We build a word list with their frequencies then write it to parameter path.
        List<WordWithFrequency> wordWithFrequencyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : getWord2FrequencyMap().entrySet()) {
            //_______________________________________________________________________________
            // Add a WordWithFrequency object to list using this entry.
            todo();
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        }
        //___________________________________________________________________________________
        // Sort this list by frequency, increasing. Just replace argument null by:
        // (o1, o2) -> o1.getFrequency() - o2.getFrequency()
        todo();
        wordWithFrequencyList.sort(null);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        //___________________________________________________________________________________
        // Write this list to path using DataManager.writeObjectToJson().
        // Then compare your implementation with a better Java 8 implementation in answer.
        todo();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public HashMap<String, Integer> getWord2IndexMap() {
        return word2IndexMap;
    }

    public HashMap<String, Integer> getTag2IndexMap() {
        return tag2IndexMap;
    }

    public double[] getStartProbability() {
        return startProbability;
    }

    public double[][] getTransitionProbability() {
        return transitionProbability;
    }

    public double[][] getEmissionProbability() {
        return emissionProbability;
    }

    public HashMap<String, Integer> getWord2FrequencyMap() {
        return word2FrequencyMap;
    }
}
