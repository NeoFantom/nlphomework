package cnlp.model;

import java.io.Serializable;

public class WordAndTag implements Serializable {

    private String word;

    private String tag;

    @Override
    public String toString() {
        return word + "/" + tag;
    }

    public WordAndTag(String word, String tag) {
        this.word = word;
        this.tag = tag;
    }

    public String getWord() {
        return word;
    }

    public String getTag() {
        return tag;
    }
}
