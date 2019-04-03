package cnlp.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cnlp.Constants;
import cnlp.model.WordAndTag;
import cnlp.model.WordAndTagProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * This class manages all the data. Except for some light-weight task, Every
 * time you operate on a file (especially writing to), you should do it
 * through this class.
 */
public class DataManager {

    /**
     * Simple help function.
     *
     * @param path file path
     * @return a {@link BufferedReader} with access to the file specified by {@param path}
     * @throws FileNotFoundException see {@link FileReader#FileReader(String)}
     */
    private static BufferedReader bReader(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }

    /**
     * Simple help function.
     *
     * @param path file path
     * @return a {@link BufferedWriter} with access to the file specified by {@param path}
     * @throws IOException see {@link FileWriter#FileWriter(String)}
     */
    private static BufferedWriter bWriter(String path) throws IOException {
        return new BufferedWriter(new FileWriter(path));
    }

    public static void divideRawData(
            String rawDataPath,
            String trainDataPath,
            String testDataPath,
            double trainDataRatio
    ) {
        try (BufferedReader raw = bReader(rawDataPath);
             BufferedWriter train = bWriter(trainDataPath);
             BufferedWriter test = bWriter(testDataPath)) {

            System.out.println("Dividing data at " + rawDataPath + " into train data and test data ...");

            int trainLines = 0, testLines = 0;

            String line = raw.readLine();
            while (line != null) {
                // Pre-processing raw data.
                line = line
                        // 1. Remove dates.
                        .replaceFirst("\\d{8}-\\d{2}-\\d{3}-\\d{3}/m\\s+", "")
                        // 2. Remove group tags. See (line 6, column 84) in raw data file.
                        .replace('[', ' ')
                        .replaceAll("]\\w+", "");

                if (!line.isEmpty()) {
                    if (Math.random() < trainDataRatio) {
                        train.write(line);
                        trainLines++;
                    } else {
                        test.write(line);
                        testLines++;
                    }
                    System.out.print(
                            "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" // \b is backspace
                                    + (trainLines + testLines) + " valid lines. ");
                }
                line = raw.readLine();
            }
            System.out.println();

            double totalLines = trainLines + testLines;
            System.out.printf(
                    "Dividing finished. %d lines in train data, %d lines in test data.\n" +
                            "Actual train/test ratio: %.3f/%.3f\n",
                    trainLines, testLines,
                    trainLines / totalLines, testLines / totalLines);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public static void prepareTestDataForSegmentation(
            String testDataPath,
            String standardSegmentedPath,
            String unsegmentedPath,
            char delimiter
    ) {
        try (BufferedReader original = bReader(testDataPath);
             BufferedWriter standardSegmented = bWriter(standardSegmentedPath);
             BufferedWriter unsegmented = bWriter(unsegmentedPath)) {

            System.out.println("Preparing test data at " + testDataPath + " ...");

            String line = original.readLine();
            while (line != null) {

                for (String pair : line.split(Constants.RAW_DATA_DELIMITER)) {

                    String word = pair.split("/")[0];
                    if (!word.trim().isEmpty()) {
                        unsegmented.write(word);
                        standardSegmented.write(word);
                        standardSegmented.write(delimiter);
                    }
                }

                line = original.readLine();
            }

            System.out.println("Preparing test data finished.");
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public static void prepareTestDataForPosTagging(
            String testDataPath,
            String standardTaggedPath,
            String untaggedPath,
            char delimiter
    ) {
        try (BufferedReader original = bReader(testDataPath);
             BufferedWriter standardTagged = bWriter(standardTaggedPath);
             BufferedWriter untagged = bWriter(untaggedPath)) {

            System.out.println("Preparing test data at " + testDataPath + " ...");

            String line = original.readLine();
            while (line != null) {

                for (String pair : line.split(Constants.RAW_DATA_DELIMITER)) {

                    String word = pair.split("/")[0];
                    if (!word.trim().isEmpty()) {
                        untagged.write(word);
                        untagged.write(delimiter);
                        standardTagged.write(pair);
                        standardTagged.write(delimiter);
                    }
                }

                line = original.readLine();
            }

            System.out.println("Preparing test data finished.");
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public static void parseWordAndTags(String path, String delimiterRegex,
                                        WordAndTagProcessor processor) {

        try (BufferedReader reader = bReader(path)) {
            System.out.println("Reading raw data from " + path + " ...");

            int total = 0;
            String line = reader.readLine();
            while (line != null) {

                for (String pair : line.split(delimiterRegex)) {

                    String[] strings = pair.split("/");
                    if (!strings[0].trim().isEmpty() && !strings[0].trim().isEmpty()) {
                        WordAndTag wordAndTag = new WordAndTag(strings[0], strings[1]);
                        processor.process(wordAndTag);
                        total++;
                    }
                }
                line = reader.readLine();
            }

            System.out.println("Read " + total + " words.");
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public static void writeObjectsToBinary(String path, Serializable... objects) {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(path))) {

            System.out.println("Writing objects to " + path + " ...");
            for (Object o : objects) {
                objectOutputStream.writeObject(o);
            }
            System.out.println("Wrote " + objects.length + " objects.");
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public static List<Serializable> readObjectsFromBinary(String path) {

        List<Serializable> objectList = new ArrayList<>();

        try (ObjectInputStream objectInputStream =
                     new ObjectInputStream(new FileInputStream(path))) {

            System.out.println("Reading objects from " + path + " ...");
            while (true) {
                try {
                    objectList.add((Serializable) objectInputStream.readObject());
                } catch (EOFException e) {
                    // EndOfFileException
                    break;
                }
            }
            System.out.println("Read " + objectList.size() + " objects.");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            exit(1);
        }
        return objectList;
    }

    public static void writeObjectToJson(String path, Object object) {
        try (BufferedWriter writer = bWriter(path)) {

            System.out.println("Writing json data to " + path + " ...");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(object));
            System.out.println("Writing json finished.");
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public static interface ReadAndWriteProcessor {
        public void process(BufferedReader reader, BufferedWriter writer) throws IOException;
    }

    public static void readAndWrite(
            String readPath,
            String writePath,
            ReadAndWriteProcessor processor
    ) {
        try (BufferedReader reader = new BufferedReader(new FileReader(readPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(writePath))) {
            processor.process(reader, writer);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }
}
