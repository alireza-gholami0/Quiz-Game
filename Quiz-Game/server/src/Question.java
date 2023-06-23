import java.util.ArrayList;

public class Question {
    String question;
    String[] options = null;
    int answer;
    public Question(String question, String[] options, int answer){
        this.question = question;
        this.answer = answer;
        this.options = options;
    }
}
