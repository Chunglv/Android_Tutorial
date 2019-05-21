package sovang.com.geoquiz.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sovang.com.geoquiz.R;
import sovang.com.geoquiz.models.Question;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String tag  = "GeoQuiz";
    private static final String indexKey = "index";
    public static final String answerIsTrueKey = "answerIsTrue";
    public static final String questionsCheaterKey = "questionsCheaterKey";
    private static final int requestCodeCheat = 0;
    private Button trueButton = null;
    private Button falseButton = null;
    private TextView questionText = null;
    private Button nextButton = null;
    private Button previousButton = null;
    private Button cheatButton = null;
    private int currentQuestion = 0;
    private boolean isCheater;
    private Question[] questions = new Question [] {
            new Question(R.string.capital_Australia, true),
            new Question(R.string.capital_VietNam, true),
            new Question(R.string.capital_Laos, false),
    };

    private boolean[] questionsCheat = new boolean[questions.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState != null) {
            currentQuestion = savedInstanceState.getInt(indexKey);
            questionsCheat = savedInstanceState.getBooleanArray(questionsCheaterKey);
            isCheater = questionsCheat[currentQuestion];
        }
        setOnClickListener();
        Log.d(tag, "onCreate(Bundle savedInstanceState) called");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "onStart() called");
    }

    @Override
    protected void onStop() {
        Log.d(tag, "onStop() called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(tag, "onDestroy() called");
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        Log.d(tag, "onPause() called");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "onResume() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(indexKey, currentQuestion);
        outState.putBooleanArray(questionsCheaterKey, questionsCheat);
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
        cheatButton = findViewById(R.id.cheatButton);
        if (cheatButton != null) {
            cheatButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case requestCodeCheat: {
                if (data == null) {
                    return;
                }
                isCheater = CheatActivity.wasAnswerShown(data);
                questionsCheat[currentQuestion] = isCheater;
                break;
            }
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
                isCheater = isQuestionCheater(currentQuestion);
                updateQuestion();
                break;
            }
            case R.id.questionText: {
                currentQuestion = (currentQuestion + 1)% questions.length;
                isCheater = isQuestionCheater(currentQuestion);
                updateQuestion();
                break;
            }
            case R.id.previousButton: {
                currentQuestion = ((currentQuestion - 1) % questions.length) & questions.length -1;
                isCheater = isQuestionCheater(currentQuestion);
                updateQuestion();
                break;
            }
            case R.id.cheatButton: {
                boolean answerIsTrue = questions[currentQuestion].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(this, answerIsTrue);
                startActivityForResult(intent, requestCodeCheat);
            }
        }
    }

    private boolean isQuestionCheater(int index) {
        return questionsCheat[index];
    }

    private void checkAnswer(boolean isPressTrue) {
        Boolean answer = questions[currentQuestion].isAnswerTrue();
        int messageResId;
        if (isCheater) {
            messageResId = R.string.judgeToast;
        }
        else {
            if (isPressTrue == answer) {
                messageResId = R.string.correct_answer;
            }
            else {
                messageResId = R.string.incorrect_toast;
            }
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
