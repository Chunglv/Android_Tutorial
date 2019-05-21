package sovang.com.geoquiz.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sovang.com.geoquiz.R;

public class CheatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String wasAnswerShownKey = "wasAnswerShown";
    private static final String answerIsTrueKey = "answerIsTrue";
    private Boolean answerIsTrue;
    private TextView answerTextView;
    private Button showAnswerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        getIntentAndUpdateView();
        setOnClickListener();
        if (savedInstanceState != null) {
            answerIsTrue = savedInstanceState.getBoolean(answerIsTrueKey);
            showAnswerTextView(answerIsTrue);
        }
    }

    private void setOnClickListener() {
        answerTextView = findViewById(R.id.answerText);
        showAnswerButton =findViewById(R.id.showAnswerButton);
        if (showAnswerButton != null) {
            showAnswerButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(answerIsTrueKey, answerIsTrue);
    }

    private void getIntentAndUpdateView() {
        Intent intent = getIntent();
        answerIsTrue = intent.getBooleanExtra(QuizActivity.answerIsTrueKey, false);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(QuizActivity.answerIsTrueKey, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent data) {
        return data.getBooleanExtra(wasAnswerShownKey, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showAnswerButton: {
                showAnswerButton();
                break;
            }
        }
    }

    private void showAnswerButton() {
        showAnswerTextView(answerIsTrue);
        setAnswerShownResult(true);
    }

    private void showAnswerTextView(boolean answerIsTrue) {
        if (answerIsTrue) {
            if (answerTextView != null) {
                answerTextView.setText(R.string.true_button);
            }
        } else {
            if (answerTextView != null) {
                answerTextView.setText(R.string.false_button);
            }
        }
    }

    private void setAnswerShownResult(boolean shownResult) {
        Intent intent = new Intent();
        intent.putExtra(wasAnswerShownKey, shownResult);
        setResult(RESULT_OK, intent);
    }
}
