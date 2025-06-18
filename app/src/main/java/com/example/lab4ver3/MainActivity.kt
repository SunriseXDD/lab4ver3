package com.example.lab4ver3

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import android.os.Build


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : ComponentActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button


    private var correctAnswers = 0
    private var answeredQuestions = 0
    private val totalQuestions = 6
    private val quizViewModel: QuizViewModel by viewModels()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0

        quizViewModel.currentIndex = currentIndex
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)
        updateCheatButtonText()

        trueButton.setOnClickListener {
            checkAnswer(true)
            it.visibility = View.INVISIBLE
            falseButton.visibility = View.INVISIBLE
            answeredQuestions++
            checkTestCompletion()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            it.visibility = View.INVISIBLE
            trueButton.visibility = View.INVISIBLE
            answeredQuestions++
            checkTestCompletion()
        }

        questionTextView.setOnClickListener { view: View ->
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            updateQuestion()
        }

        nextButton.setOnClickListener {
            if (quizViewModel.currentIndex < quizViewModel.questionBank.size - 1) {
                quizViewModel.moveToNext()
                updateQuestion()
                trueButton.visibility = View.VISIBLE
                falseButton.visibility = View.VISIBLE
            }
        }

        previousButton.setOnClickListener {
            if (quizViewModel.questionBank.size > 0) {
                quizViewModel.currentIndex--
                trueButton.visibility = View.VISIBLE
                falseButton.visibility = View.VISIBLE
                updateQuestion()
            } else {
                Toast.makeText(this, R.string.error_toast, Toast.LENGTH_SHORT).show()
            }
        }
        cheatButton.setOnClickListener { view ->
            if (quizViewModel.cheatCount > 0) {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options =
                        ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                    startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                } else {
                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }
                quizViewModel.cheatCount--
                updateCheatButtonText()
            } else {
                Toast.makeText(this, "No more cheats left!", Toast.LENGTH_SHORT).show()
            }
        }
        updateQuestion()
    }
    private fun updateCheatButtonText() {
        cheatButton.text = "Cheat (${quizViewModel.cheatCount} left)"
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT)
        {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater ->
                R.string.judgment_toast
            userAnswer == correctAnswer ->
                R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun checkTestCompletion() {
        if (answeredQuestions >= totalQuestions) {
            showFinalResult()
        }
    }

    private fun showFinalResult() {
        val percentage = (correctAnswers.toFloat() / totalQuestions) * 100
        val resultMessage = "Correct ansswers: \n$correctAnswers из $totalQuestions (${"%.1f".format(percentage)}%)"

        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()

        trueButton.visibility = View.GONE
        falseButton.visibility = View.GONE
        nextButton.visibility = View.GONE
        previousButton.visibility = View.GONE
    }
}






