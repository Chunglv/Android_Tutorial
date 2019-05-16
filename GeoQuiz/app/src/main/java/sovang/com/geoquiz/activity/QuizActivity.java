package sovang.com.geoquiz.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sovang.com.geoquiz.R;
import sovang.com.geoquiz.models.Question;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Button trueButton = null;
    private Button falseButton = null;
    private TextView questionText = null;
    private Button nextButton = null;
    private Button previousButton = null;
    private int currentQuestion = 0;
    private Question[] questions = new Question [] {
            new Question(R.string.capital_Australia, true),
            new Question(R.string.capital_VietNam, true),
            new Question(R.string.capital_Laos, false),
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setOnClickListener();
    }

    private void setOnClickListener() {
        trueButton = findViewById(R.id.trueButton);
        if (trueButton != null) {
            trueButton.setOnClickListener(this);
        }

        falseButton = findViewById(R.id.falseButton);
        if (falseButton != null) {
            falseButton.setOnClickListener(this);
        }

        questionText = findViewById(R.id.questionText);
        questionText.setOnClickListener(this);
        updateQuestion();

        nextButton = findViewById(R.id.nextButton);
        if (nextButton  != null) {
            nextButton.setOnClickListener(this);
        }
        previousButton = findViewById(R.id.previousButton);
        if (previousButton != null) {
            previousButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trueButton: {
                checkAnswer(true);
                break;
            }
            case R.id.falseButton: {
                checkAnswer(false);
                break;
            }
            case R.id.nextButton: {
                currentQuestion = (currentQuestion + 1)% questions.length;
                updateQuestion();
                break;
            }
            case R.id.questionText: {
                currentQuestion = (currentQuestion + 1)% questions.length;
                updateQuestion();
                break;
            }
            case R.id.previousButton: {
                currentQuestion = ((currentQuestion - 1) % questions.length) & questions.length -1;
                updateQuestion();
            }

        }
    }

    private void checkAnswer(boolean isPressTrue) {
        Boolean answer = questions[currentQuestion].isAnswerTrue();
        int messageResId;
        if (isPressTrue == answer) {
            messageResId = R.string.correct_answer;
        }
        else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
        if (questionText != null) {
            int textRestId = questions[currentQuestion].getTextResId();
            questionText.setText(textRestId);
        }
    }
}
