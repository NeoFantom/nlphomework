package cnlp.model;

import cnlp.io.DataManager;
import cnlp.algorithm.PosTagger;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cnlp.Constants.END_OF_SENTENCE_STRINGS;
import static cnlp.Constants.RAW_DATA_DELIMITER;

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

    private void doStatistics(WordAndTag wordAndTag) {
        String word = wordAndTag.getWord();
        String tag = wordAndTag.getTag();
        int wordId = this.word2IndexMap.get(word);
        int tagId = this.tag2IndexMap.get(tag);

        this.word2FrequencyMap.put(
                word,
                this.word2FrequencyMap.getOrDefault(word, 0) + 1);

        if (END_OF_SENTENCE_STRINGS.contains(lastWord)) {
            this.startProbability[tagId]++;
        } else {
            this.transitionProbability[lastTagId][tagId]++;
        }
        lastWord = word;
        lastTagId = tagId;

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
        Arrays.stream(emissionProbability).forEach(StatisticsBox::normalizeVector);
    }

    public static StatisticsBox fromTrainData(String path) {
        System.out.println("Constructing " + StatisticsBox.class.getName() + " from train data.");
        StatisticsBox box = new StatisticsBox();

        // What if there is an OOV(out-of-vocabulary) word? To deal with them, we add an
        // empty String as a placeholder before reading any train data, and map all the
        // OOV words to this one.
        box.wordList.add("");

        System.out.println("Extracting words ang tags...");
        DataManager.parseWordAndTags(path, RAW_DATA_DELIMITER, box::rememberWordAndTag);

        int tagCount = box.tagList.size();
        int wordCount = box.wordList.size();
        box.startProbability = new double[tagCount];
        box.transitionProbability = new double[tagCount][tagCount];
        box.emissionProbability = new double[tagCount][wordCount + 1]; // Note the +1 here for OOV
        box.word2FrequencyMap = new HashMap<>(wordCount);

        // We must give the emission probability of out-of-vocabulary words
        // given any tag. We simply set all of them to 1. NOT ZERO because
        // all zeros add up to 0, which means total frequency is 0, which
        // means the probability of any tag given an OOV word is 0,
        // impossible.
        for (int i = 0; i < tagCount; i++) {
            box.emissionProbability[i][0] = 1;
        }

        System.out.println("Doing statistics...");
        DataManager.parseWordAndTags(path, RAW_DATA_DELIMITER, box::doStatistics);

        box.normalizeProbabilities();

        System.out.println("Statistics done.");
        return box;
    }

    public static StatisticsBox fromSavedFile(String path) {
        System.out.println("Recovering " + StatisticsBox.class.getName() + " from saved file.");
        return (StatisticsBox) DataManager.readObjectsFromBinary(path).get(0);
    }

    public void saveStatistics(String path) {
        DataManager.writeObjectsToBinary(
                path,
                this);
    }

    public void saveWordFrequencyAsJson(String path) {
        List<WordWithFrequency> wordWithFrequencyList =
                new ArrayList<>(getWord2FrequencyMap().entrySet())
                        .stream()
                        .map(WordWithFrequency::new)
                        .sorted(Comparator.comparingInt(WordWithFrequency::getFrequency))
                        .collect(Collectors.toList());
        DataManager.writeObjectToJson(path, wordWithFrequencyList);
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
