package bean;

public class DBWordBean {
    private int _id;
    private String word;
    private String accent;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    private String mean_cn;
    private String sentence;
    private String sentence_trans;
    private int isCollection;
}
