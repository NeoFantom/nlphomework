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

        MarkableString unmarkSelf() {
            this.mark = 0;
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

    public SegmentationEvaluator(char WORD_DELIMITER, String segmented, String standardSegmented) {
        this.WORD_DELIMITER = WORD_DELIMITER;
        myWordsList = readMarkableWordList(segmented);
        standardWordsList = readMarkableWordList(standardSegmented);
    }

    private void reportError(
            int standardPosition, String standardPhrase, int myPosition, String myPhrase) {
        System.err.println("Malformed segmentation data!");
        System.err.println(standardPosition + "-th word in standard segmentation file is:");
        System.err.println(standardPhrase);
        System.err.println(myPosition + "-th word your segmentation file is:");
        System.err.println(myPhrase);
    }

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

    private StringBuilder standardPhrase = new StringBuilder();
    private StringBuilder myPhrase = new StringBuilder();

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
            if (!standardPhrase.toString().equals(myPhrase.toString())) {
                reportError(
                        s, standardPhrase.toString(),
                        m, myPhrase.toString());
            }
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

    public void evaluate(String differentPhrasesPath) {

        // Step 1 Write differently segmented phrases of my segmentation and standard segmentation
        ArrayList<String> standardDifferentPhrases = extractMarkedPhrases(standardWordsList);
        ArrayList<String> myDifferentPhrases = extractMarkedPhrases(myWordsList);

        if (standardDifferentPhrases.size() != myDifferentPhrases.size()) {
            throw new RuntimeException(
                    "Wrong evaluation! standardDifferentPhrases.size()!= myDifferentPhrases.size()");
        }

        ArrayList<String[]> comparisons = new ArrayList<>(standardDifferentPhrases.size() + 1);
        comparisons.add(new String[]{"Example: standard", "Example: yours"});
        for (int i = 0; i < standardDifferentPhrases.size(); i++) {
            comparisons.add(
                    new String[]{standardDifferentPhrases.get(i), myDifferentPhrases.get(i)}
            );
        }
        DataManager.writeObjectToJson(
                differentPhrasesPath,
                comparisons);

        // Step 2 Summarize differences and print segmentation performance
        for (int s = 0, m = 0;
             s < standardWordsList.size() && m < myWordsList.size();
             s++, m++) {
            if (!standardWordsList.get(s).equals(myWordsList.get(m))) {
                int[] sm = forwardUntilEqual(s, m);
                s = sm[0];
                m = sm[1];
            }
        }
        int totalCharacters = 0;
        for (MarkableString word : standardWordsList)
            totalCharacters += word.length();

        int[] standardSummary = summarize(standardWordsList);
        int[] mySummary = summarize(myWordsList);

        if (standardSummary[0] != mySummary[0]) {
            System.err.println("Wrong evaluation! standardSummary[0] != mySummary[0]");
            exit(1);
        }

        int common = standardSummary[0];
        int onlyInStandard = standardSummary[1];
        int onlyInMy = mySummary[1];
        printEvaluation(common, onlyInStandard, onlyInMy, totalCharacters);
    }

//    // Bad sophisticated implementation
//    public void _evaluate(String segmented, String standardSegmented, char delimiter) {
//
//        try (BufferedReader myReader = new BufferedReader(new FileReader(segmented));
//             BufferedReader standardReader = new BufferedReader(new FileReader(standardSegmented))) {
//
//            ArrayList<String> myPhraseList = new ArrayList<>();
//            StringBuilder myPhrase = new StringBuilder();
//            ArrayList<String> standardPhraseList = new ArrayList<>();
//            StringBuilder standardPhrase = new StringBuilder();
//
//            int myFlag = myReader.read();
//            while (myFlag == delimiter) {
//                myFlag = myReader.read();
//            }
//
//            int standardFlag = standardReader.read();
//            while (standardFlag == delimiter) {
//                standardFlag = standardReader.read();
//            }
//
//            char my, standard;
//            while (true) {
//
//                // Assign int to char if it's not end-of-file.
//                if (myFlag == -1 || standardFlag == -1) {
//                    while (myFlag != -1) {
//                        if (myFlag != delimiter) {
//                            reportError();
//                        }
//                        myFlag = myReader.read();
//                    }
//                    while (standardFlag != -1) {
//                        if (standardFlag != delimiter) {
//                            reportError();
//                        }
//                        standardFlag = standardReader.read();
//                    }
//                    break;
//                } else {
//                    my = (char) myFlag;
//                    standard = (char) standardFlag;
//                }
//
//
//                if (my == delimiter && standard == delimiter) {
//                    common++;
//                    inMy++;
//                    inStandard++;
//                    myCount++;
//                    standardCount++;
//
//                    totalCharacters++;
//                    myFlag = myReader.read();
//                    standardFlag = standardReader.read();
//                } else if (my == delimiter) {
//                    inMy++;
//                    myCount++;
//                    myFlag = myReader.read();
//                } else if (standard == delimiter) {
//                    inStandard++;
//                    standardCount++;
//
//                    totalCharacters++;
//                    standardFlag = standardReader.read();
//                } else if (my != standard) {
//                    reportError();
//                    exit(1);
//                } else {
//                    myCount++;
//                    standardCount++;
//
//                    totalCharacters++;
//                    myFlag = myReader.read();
//                    standardFlag = standardReader.read();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            exit(1);
//        }
//
//        double TP = common;
//        double FP = inMy - common;
//        double FN = inStandard - common;
//        double TN = totalCharacters - TP - FP - FN;
//        double accuracy = (TP + TN) / totalCharacters;
//        double recall = TP / (TP + FN);
//        System.out.println("=========================================================");
//        System.out.println("                        Finished                         ");
//        System.out.println("Number of words in your segmentation:     y=" + inMy);
//        System.out.println("Number of words in standard segmentation: s=" + inStandard);
//        System.out.println("Number of words in common:                c=" + common);
//        System.out.println("Number of characters totally:             t=" + totalCharacters);
//        System.out.println("=========================================================");
//        System.out.println("                     Evaluation Table                    ");
//        System.out.println("---------------------------------------------------------");
//        System.out.println("Predicted:             Yes                No             ");
//        System.out.printf("Indeed Yes:        TP=%7.0f        FN=%7.0f\n", TP, FN);
//        System.out.printf("Indeed No :        FP=%7.0f        TN=%7.0f\n", FP, TN);
//        System.out.println();
//        System.out.println("Accuracy:    " + accuracy);
//        System.out.println("Recall:      " + recall);
//        System.out.println("F1 measure:  " + 2 * recall * accuracy / (recall + accuracy));
//    }
}
