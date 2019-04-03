package cnlp.model;

import cnlp.io.DataManager;
import cnlp.algorithm.PosTagger;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cnlp.Constants.RAW_DATA_DELIMITER;

public class StatisticsBox implements Serializable {

    private ArrayList<String> wordList = new ArrayList<>();

    private ArrayList<String> tagList = new ArrayList<>();

    private HashMap<String, Integer> word2IdMap = new HashMap<>();

    private HashMap<String, Integer> tag2IdMap = new HashMap<>();

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

    public StatisticsBox(
            ArrayList<String> wordList,
            ArrayList<String> tagList,
            HashMap<String, Integer> word2IdMap,
            HashMap<String, Integer> tag2IdMap,
            double[] startProbability,
            double[][] transitionProbability,
            double[][] emissionProbability,
            HashMap<String, Integer> word2FrequencyMap
    ) {
        this.wordList = wordList;
        this.tagList = tagList;
        this.word2IdMap = word2IdMap;
        this.tag2IdMap = tag2IdMap;
        this.startProbability = startProbability;
        this.transitionProbability = transitionProbability;
        this.emissionProbability = emissionProbability;
        this.word2FrequencyMap = word2FrequencyMap;
    }

    private void rememberWordAndTag(WordAndTag wordAndTag) {
        String word = wordAndTag.getWord();
        String tag = wordAndTag.getTag();
        if (word2IdMap.putIfAbsent(word, wordList.size()) == null) {
            wordList.add(word);
        }
        if (tag2IdMap.putIfAbsent(tag, tagList.size()) == null) {
            tagList.add(tag);
        }
    }

    private String lastWord = "。";
    private int lastTagId = -1;

    private void doStatistics(WordAndTag wordAndTag) {
        String word = wordAndTag.getWord();
        String tag = wordAndTag.getTag();
        int wordId = this.word2IdMap.get(word);
        int tagId = this.tag2IdMap.get(tag);

        this.word2FrequencyMap.put(
                word,
                this.word2FrequencyMap.getOrDefault(word, 0) + 1);

        if (PosTagger.END_OF_SENTENCE_SET.contains(lastWord)) {
            this.startProbability[tagId]++;
        } else {
            this.transitionProbability[lastTagId][tagId]++;
        }
        lastWord = word;
        lastTagId = tagId;

        emissionProbability[tagId][wordId]++;
    }

    private void normalizeVector(double[] vector) {
        double s = Arrays.stream(vector).sum();
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= s;
        }
    }

    private void normalizeProbabilities() {
        normalizeVector(startProbability);
        Arrays.stream(transitionProbability).forEach(this::normalizeVector);
        Arrays.stream(emissionProbability).forEach(this::normalizeVector);
    }

    public static StatisticsBox fromTrainData(String path) {
        System.out.println("Constructing " + StatisticsBox.class.getName() + " from train data.");
        StatisticsBox box = new StatisticsBox();

        box.wordList.add(""); // Refers to out-of-vocabulary words

        System.out.println("Extracting words ang tags...");
        DataManager.parseWordAndTags(path, RAW_DATA_DELIMITER, box::rememberWordAndTag);

        int tagCount = box.tagList.size();
        int wordCount = box.wordList.size();
        box.startProbability = new double[tagCount];
        box.transitionProbability = new double[tagCount][tagCount];
        box.emissionProbability = new double[tagCount][wordCount + 1];
        box.word2FrequencyMap = new HashMap<>(wordCount);

        // We consider the emission probability of
        // out-of-vocabulary word given any tag as 1
        for (int i = 0; i < tagCount; i++) {
            box.emissionProbability[i][0] = 1;
        }

        System.out.println("Doing statistics.");
        DataManager.parseWordAndTags(path, RAW_DATA_DELIMITER, box::doStatistics);

        box.normalizeProbabilities();
        return box;
    }

    public static StatisticsBox fromSavedFile(String path) {
        System.out.println("Recovering " + StatisticsBox.class.getName() + " from saved file.");
        return (StatisticsBox) DataManager.readObjectsFromBinary(path).get(0);
//        Naive implementation
//        List<Serializable> list = DataManager.readObjectsFromBinary(path);
//        return new StatisticsBox(
//                (ArrayList<String>) list.get(1),
//                (ArrayList<String>) list.get(2),
//                (HashMap<String, Integer>) list.get(3),
//                (HashMap<String, Integer>) list.get(4),
//                (double[]) list.get(5),
//                (double[][]) list.get(6),
//                (double[][]) list.get(7),
//                (HashMap<String, Integer>) list.get(8));
    }

    public void saveStatistics(String path) {
        DataManager.writeObjectsToBinary(
                path,
                this);
//        Naive implementation
//        DataManager.writeObjectsToBinary(
//                path,
//                wordList,
//                tagList,
//                word2IdMap,
//                tag2IdMap,
//                startProbability,
//                transitionProbability,
//                emissionProbability,
//                word2FrequencyMap);
    }

    public void saveWord2FrequencyMapAsJson(String path) {
        List<WordWithFrequency> wordWithFrequencyList =
                new ArrayList<>(getWord2FrequencyMap().entrySet())
                        .stream()
                        .map(entry -> new WordWithFrequency(entry))
                        .sorted(Comparator.comparingInt(WordWithFrequency::getFrequency))
                        .collect(Collectors.toList());
        DataManager.writeObjectToJson(path, wordWithFrequencyList);
    }

    public HashMap<String, Integer> getWord2FrequencyMap() {
        return word2FrequencyMap;
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public HashMap<String, Integer> getWord2IdMap() {
        return word2IdMap;
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
}
