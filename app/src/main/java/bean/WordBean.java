package bean;

public class WordBean {
    public WordBean(String word, String accent, String mean_cn, String sentence, String sentence_trans) {
        this.word = word;
        this.accent = accent;
        this.mean_cn = mean_cn;
        this.sentence = sentence;
        this.sentence_trans = sentence_trans;
    }

    public WordBean() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getMean_cn() {
        return mean_cn;
    }

    public void setMean_cn(String mean_cn) {
        this.mean_cn = mean_cn;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence_trans() {
        return sentence_trans;
    }

    public void setSentence_trans(String sentence_trans) {
        this.sentence_trans = sentence_trans;
    }

    private String word;
    private String accent;
    private String mean_cn;
    private String sentence;
    private String sentence_trans;

    @Override
    public String toString() {
        return "WordBean{" +
                "word='" + word + '\'' +
                ", accent='" + accent + '\'' +
                ", mean_cn='" + mean_cn + '\'' +
                ", sentence='" + sentence + '\'' +
                ", sentence_trans='" + sentence_trans + '\'' +
                '}';
    }
}
