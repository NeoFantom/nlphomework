package cnlp.model;

import java.util.*;

/**
 * Models a vocabulary with trie (i.e. prefix tree).
 *
 * This implementation is specially designed for Chinese trie.
 *
 * There are two steps to use this class:
 * 1. Construct a vocabulary using a {@link Collection} of words.
 * 2. Check if a given word is in this vocabulary.
 *
 * Step 1 is implemented in {@link Vocabulary#Vocabulary}.
 * Step 2 is implemented in {@link Vocabulary#longestMatchLength}
 */
public class Vocabulary {

    private class Node {

        private char key;
        private HashMap<Character, Node> childrenMap;
        private boolean endOfWord;

        Node(char key) {
            this.key = key;
            this.childrenMap = new HashMap<>();
            this.endOfWord = false;
        }

        public char getKey() {
            return key;
        }

        boolean hasChild(char childKey) {
            return childrenMap.containsKey(childKey);
        }

        Node getChild(char childKey) {
            return childrenMap.get(childKey);
        }

        void addChildIfAbsent(char childKey) {
            Node newNode = new Node(childKey);
            childrenMap.putIfAbsent(childKey, newNode);
        }

        void markAsEndOfWord() {
            endOfWord = true;
        }

        boolean isEndOfWord() {
            return endOfWord;
        }
    }

    private Node root;

    private int maxWordLength;

    public Vocabulary(Collection<String> wordList) {
        root = new Node('$');
        // Key of root node, i.e. '$' is not contained in the word.
        // It's just used as a placeholder for convenience.

        maxWordLength = 0;
        for (String word : wordList) {
            addWord(word);
            maxWordLength = Math.max(maxWordLength, word.length());
        }
    }

    private void addWord(String word) {
        Node node = root;
        for (char c : word.toCharArray()) {
            node.addChildIfAbsent(c);
            node = node.getChild(c);
        }
        // Now node contains the last character of the word.
        // Let's mark it as the end of the word.
        node.markAsEndOfWord();
    }

    /**
     * Match the given {@code charsToMath} as long as possible.
     *
     * @param charsToMatch characters that you want to match.
     * @return maximum number of characters in {@code charsToMatch} that match
     * some word in this vocabulary.
     */
    public int longestMatchLength(char[] charsToMatch) {
        int matchLength = 0;
        int depth = 0;
        Node here = root;
        for (char c : charsToMatch) {
            here = here.getChild(c);
            if (here != null) { // The node has child c
                depth++;
                if (here.isEndOfWord()) {
                    matchLength = depth;
                }
            } else {
                break;
            }
        }
        return matchLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }
}