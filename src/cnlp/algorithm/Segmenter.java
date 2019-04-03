package cnlp.algorithm;

import cnlp.io.DataManager;
import cnlp.model.Vocabulary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

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
     * For efficiency, we don't read the whole string of the text. Instead,
     * every time we want to segment next word, we try to fill characters into
     * {@code charsToMatch} from test file , where n={@code longestMatchLength}
     * is the maximum word length in {@code vocabulary}, and the file is
     * accessible through lambda parameter {@code reader}. If, at most, the
     * first m characters matches a word in the vocabulary (here
     * m={@code actualReadLength}), then mark these m characters as a word
     * and write to output file through lambda parameter {@code writer},
     * and read another (n-m) characters from {@code reader} into our
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
                    final int maxWordLength = vocabulary.getMaxWordLength();
                    final char[] charsToMatch = new char[maxWordLength];
                    int longestMatchLength = maxWordLength;

                    while (true) {
                        int actualReadLength = reader.read(
                                charsToMatch, maxWordLength - longestMatchLength, longestMatchLength);

                        if (actualReadLength != longestMatchLength) {
                            // There's not enough characters to read, so end-of-file
                            // has been reached. Let's take care of it.

                            actualReadLength = actualReadLength == -1 ? 0 : actualReadLength;
                            longestMatchLength -= actualReadLength;
                            char[] remainingChars = new char[maxWordLength];
                            System.arraycopy(
                                    charsToMatch, 0,
                                    remainingChars, longestMatchLength,
                                    maxWordLength - longestMatchLength);

                            while (remainingChars.length > longestMatchLength) {
                                // We still need to segment the remaining text in remainingChars.
                                // When remainingChars.length == longestMatchLength, we know it's
                                // done, since all the remaining characters are one word.

                                // Shrink the buffer. We only need characters of interval
                                // [longestMatchLength, remainingChars.length)
                                remainingChars = Arrays.copyOfRange(
                                        remainingChars,
                                        longestMatchLength, remainingChars.length);

                                longestMatchLength = matchAndWriteWord(remainingChars, writer);
                            }

                            break; // ATTENTION: break outer loop!
                        }

                        longestMatchLength = matchAndWriteWord(charsToMatch, writer);

                        // Move the un-matched characters to the beginning.
                        System.arraycopy(
                                charsToMatch, longestMatchLength,
                                charsToMatch, 0,
                                charsToMatch.length - longestMatchLength);
                    }
                }
        );
    }

    private int matchAndWriteWord(char[] charsToMatch, BufferedWriter writer) throws IOException {
        int longestMatchLength = vocabulary.longestMatchLength(charsToMatch);

        // If longestMatchLength == 0, i.e. the character is out-of-vocabulary,
        // just consider this single character as a word.
        longestMatchLength =
                longestMatchLength == 0 ?
                        1 : longestMatchLength;
//for debug============================================================
//        System.out.println(longestMatchLength);
//        System.out.println(charsToMatch);
//======================================================================
        writer.write(charsToMatch, 0, longestMatchLength);
        writer.write(WORD_DELIMITER);

        return longestMatchLength;
    }
}
