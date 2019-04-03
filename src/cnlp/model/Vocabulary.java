package cnlp.model;

import java.util.*;

/**
 * Models a vocabulary with trie (i.e. prefix tree).
 * A {@link Vocabulary} is basically a trie, but named as "Vocabulary".
 * This implementation is specially designed for Chinese trie.
 */
public class Vocabulary {

    private class Node {

        private char key;
        //private Node parent;
        private HashMap<Character, Node> childrenMap;
        private boolean endOfWord;

        Node(char key) {
            this.key = key;
//            parent = null;
            this.childrenMap = new HashMap<>();
            this.endOfWord = false;
        }

//        public void setParent(Node parent) {
//            this.parent = parent;
//        }

        public char getKey() {
            return key;
        }

//        public Node getParent() {
//            return parent;
//        }

        boolean hasChild(char childKey) {
            return childrenMap.containsKey(childKey);
        }

        Node getChild(char childKey) {
            return childrenMap.get(childKey);
        }

        void addChildIfAbsent(char childKey) {
            Node newNode = new Node(childKey);
//            newNode.setParent(Node.this);
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
        // It's just used as a placeholder for convenient.

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
     * @param charsToMatch characters that you want to match. It could
     *                     be an array, a {@link java.util.List},
     *                     a {@link java.util.Set}, etc. As long as it
     *                     implements {@link Iterable} interface.
     * @return number of characters in {@code charsToMatch} that match
     * some word in the vocabulary.
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