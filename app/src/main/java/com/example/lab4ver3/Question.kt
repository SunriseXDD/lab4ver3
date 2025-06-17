package com.example.lab4ver3

import androidx.annotation.StringRes

data class Question(@StringRes val textResId:
                    Int, val answer: Boolean)