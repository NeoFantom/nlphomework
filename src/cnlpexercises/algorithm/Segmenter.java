package cnlpexercises.algorithm;

import cnlpexercises.io.DataManager;
import cnlpexercises.model.Vocabulary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import static cnlpexercises.TodoException.todo;

public class Segmenter {

    public static final char WORD_DELIMITER = ' ';

    private final Vocabulary vocabulary;

    public Segmenter(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    /**
     * This function performs segmentation on the text from {@param inputPath}.
     * It's based on maximum-match algorithm.
     * <p>
     * Algorithm specification:
     * <p>
     * For efficiency, we don't read the whole string of the text. Instead, we
     * read a small part each step.
     * <p>
     * Every time we want to segment next word, we try to fill m characters into
     * {@code charsToMatch} from test file , where m={@code maxWordLength}
     * is the maximum word length in {@code vocabulary}, and the file is
     * accessible through lambda parameter {@code reader}. If, at most, the
     * first l characters matches a word in the vocabulary (here
     * l={@code longestMatchLength}), then mark these l characters as a word
     * and write to output file through lambda parameter {@code writer},
     * then read another (m-l) characters from {@code reader} into our
     * buffer {@code charsToMatch}. Do the procedure repeatedly until all
     * text are segmented, i.e. end-of-file is reached.
     *
     * @param inputPath  unsegmented text file path
     * @param outputPath file path where segmented text will be written to
     */
    public void letsDoIt(String inputPath, String outputPath) {

        DataManager.readAndWrite(
                inputPath,
                outputPath,
                (reader, writer) -> {

                    // Initialize variables
                    final int maxWordLength = vocabulary.getMaxWordLength();
                    final char[] charsToMatch = new char[maxWordLength];
                    int longestMatchLength = maxWordLength;
                    // ATTENTION: you may find this assignment weird, but it's done
                    // so that maxWordLength-longestMatchLength==0 at the beginning.
                    // See the comment starting with $1 to understand.


                    while (true) {
                        //____________________________________________________________________
                        // Read characters into array charsToMatch. Fill the first parameter.
                        todo();
                        int actualReadLength = reader.read(
                                null, maxWordLength - longestMatchLength, longestMatchLength);
                        // $1 (See ATTENTION above)
                        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

                        // If there's not enough characters to read, end-of-file
                        // has been reached. Let's take care of it.
                        if (actualReadLength != longestMatchLength) {
                            //_________________________________________________________________
                            // Optional: Read this part about how to cope with end-of-file.
                            // (It's very tricky.)
                            todo();
                            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

                            // actualReadLength == -1 when no characters can be read.
                            actualReadLength = actualReadLength == -1 ? 0 : actualReadLength;

                            // Note that longestMatchLength is the last longest match, length
                            // of which we should have filled if not reaching end-of-file.
                            longestMatchLength -= actualReadLength;

                            // To hold the remaining characters
                            char[] remainingChars = new char[maxWordLength];

                            // Fill the remainingChars.
                            // Note that we move the remaining characters to the end of array,
                            // because there won't be new characters fed into this array and
                            // we only need to discard the matched characters at the beginning
                            // of remaining characters, which means it'll be easier if the
                            // characters are stacked at the end of the array.
                            System.arraycopy(
                                    charsToMatch, 0,
                                    remainingChars, longestMatchLength,
                                    maxWordLength - longestMatchLength);

                            // When remainingChars.length == longestMatchLength, we know it's
                            // done, since all the remaining characters are one word.
                            while (remainingChars.length > longestMatchLength) {

                                // Discard matched characters at the beginning of the buffer.
                                remainingChars = Arrays.copyOfRange(
                                        remainingChars,
                                        longestMatchLength, remainingChars.length);

                                longestMatchLength = matchAndWriteWord(remainingChars, writer);
                            }

                            break; // ATTENTION: break outer loop!
                        }

                        //____________________________________________________________________
                        // Implement matchAndWriteWord() and use it to match a longest word
                        // and update variable longestMatchLength.
                        todo();
                        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

                        //____________________________________________________________________
                        // Move the un-matched characters to the beginning.
                        // Rewrite this for-loop using System.arraycopy() function.
                        todo();
                        int remainingLength = charsToMatch.length - longestMatchLength;
                        for (int i = 0; i < remainingLength; i++) {
                            charsToMatch[i] = charsToMatch[longestMatchLength + i];
                        }
                        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    }
                }
        );
    }

    private int matchAndWriteWord(char[] charsToMatch, BufferedWriter writer) throws IOException {
        //________________________________________________________________________________
        // Use vocabulary to find out longestMatchLength.
        todo();
        int longestMatchLength = 0;
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        // If longestMatchLength == 0, i.e. the character is out-of-vocabulary,
        // just consider this single character as a word.
        longestMatchLength =
                longestMatchLength == 0 ?
                        1 : longestMatchLength;

        writer.write(charsToMatch, 0, longestMatchLength);
        writer.write(WORD_DELIMITER);

        return longestMatchLength;
    }
}
