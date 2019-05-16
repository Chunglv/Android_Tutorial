package sovang.com.geoquiz.models;

public class Question {
    private int textResId;
    private boolean answerTrue;
    public Question(int textid, boolean answerTrue) {
        textResId = textid;
        this.answerTrue = answerTrue;
    }

    public int getTextResId() {
        return textResId;
    }

    public void setTextResId(int textResId) {
        this.textResId = textResId;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }
}
