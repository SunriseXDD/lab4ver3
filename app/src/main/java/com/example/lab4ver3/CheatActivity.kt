package com.example.lab4ver3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.os.Build


private const val EXTRA_ANSWER_IS_TRUE = "com.example.lab4ver3.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.lab4ver3.answer_shown"


class CheatActivity : ComponentActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        val androidVersionTextView = findViewById<TextView>(R.id.android_version_text)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
        }

        val versionName = "Android ${Build.VERSION.RELEASE}"
        val versionCode = Build.VERSION.SDK_INT
        androidVersionTextView.text = "Версия системы: $versionName (API $versionCode)"
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}


