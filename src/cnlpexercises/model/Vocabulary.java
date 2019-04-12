package cnlpexercises.model;

import java.util.Collection;
import java.util.HashMap;

import static cnlpexercises.TodoException.todo;

/**
 * Models a vocabulary with trie (i.e. prefix tree).
 * <p>
 * This implementation is specially designed for Chinese trie.
 * <p>
 * There are two steps to use this class:
 * 1. Construct a vocabulary using a {@link Collection} of words.
 * 2. Check if a given word is in this vocabulary.
 * <p>
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
            //______________________________________________________________________
            // Return the child which is related to childKey.
            // Hint: use childrenMap.get().
            todo();
            return null;
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        }

        void addChildIfAbsent(char childKey) {
            // Construct a new node.
            Node newNode = new Node(childKey);
            //______________________________________________________________________
            // Add the new node to this node's children and associate it with childKey.
            // Hint: use childrenMap.putIfAbsent().
            todo();
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
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
        // Key of root node is '$', i.e. root node is not contained in the word.
        // It's just used as a placeholder for convenience.

        maxWordLength = 0;
        for (String word : wordList) {
            // For each word, we add it into our vocabulary.
            addWord(word);
            //_______________________________________________________________________
            // Update maxWordLength.
            // Hint: Use Math.max(int a, int b)
            todo();
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
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
        //___________________________________________________________________________
        // Think: Why must we mark end of a word characters above?
        // Suppose, if we add "beauty" into our vocabulary. Now we meet a word
        // "beautiful" in test data. Without end-of-word mark, we'll match a string
        // "beaut", which is not a word. Not what we want!
        todo();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    }

    /**
     * Match the given {@code charsToMath} as long as possible.
     * <p>
     * For example. If {@code charsToMath} is "homework", and we have a word
     * "home" in vocabulary but don't have "homework" in vocabulary. Then this
     * function should return 4, because the longest matched word is "home",
     * whose length==4.
     *
     * @param charsToMatch characters that you want to match.
     * @return length of the longest matched word.
     */
    public int longestMatchLength(char[] charsToMatch) {
        int matchLength = 0;
        int depth = 0;
        Node here = root;
        for (char c : charsToMatch) {
            here = here.getChild(c);
            //________________________________________________________________________________________
            // Look above: getChild() returns null if a node doesn't contain a child with this key.
            // Complete this part yourself.
            // Don't forget to update matchLength!
            // Hint: Use here.isEndOfWord() to check if it's the end of a word.
            todo();
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        }
        return matchLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }
}