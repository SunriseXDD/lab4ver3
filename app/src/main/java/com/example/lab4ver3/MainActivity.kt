package com.example.lab4ver3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    private var correctAnswers = 0
    private var answeredQuestions = 0
    private val totalQuestions = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)


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
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        nextButton.setOnClickListener {
            if (currentIndex < questionBank.size - 1) {
                currentIndex++
                trueButton.visibility = View.VISIBLE
                falseButton.visibility = View.VISIBLE
                updateQuestion()
            }
        }

        previousButton.setOnClickListener {
            val messageResId = R.string.error_toast
            if (currentIndex > 0) {
                currentIndex--
                trueButton.visibility = View.VISIBLE
                falseButton.visibility = View.VISIBLE
                updateQuestion()
            } else {
                Toast.makeText(this, R.string.error_toast, Toast.LENGTH_SHORT).show()
            }
        }
        updateQuestion()
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
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            correctAnswers++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
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




