package com.amar.mimoapp.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amar.mimoapp.R
import com.amar.mimoapp.db.DataState
import com.amar.mimoapp.model.Lesson
import com.amar.mimoapp.viewmodel.LessonViewModel
import kotlinx.android.synthetic.main.activity_lessons.*

class LessonsActivity : AppCompatActivity() {

    lateinit var lessonViewModel: LessonViewModel
    private var lessonList : ArrayList<Lesson> = arrayListOf()
    lateinit var dialog: Dialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons)

        lessonViewModel = ViewModelProvider(this).get(LessonViewModel::class.java)

        subscribeObservers()
        lessonViewModel.getUnFinishedLessons()

        buttonNext.setOnClickListener {
            lessonViewModel.makeLessonCompleted(lessonList.get(0))
        }

    }

    private fun subscribeObservers(){
        lessonViewModel.dataState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<List<Lesson>> -> {
                    lessonList = dataState.data as ArrayList<Lesson>

                    if (lessonList.size > 0) {
                        var lesson = lessonList.get(0)

                        showLesson(lesson)
                    }
                    else {
                        finish()
                    }
                }
                is DataState.Error -> {

                }
                is DataState.Loading -> {

                }
            }
        })

        lessonViewModel.dataStateLesson.observe(this, {
            dataState ->
            when (dataState) {
                is DataState.Success<Int> -> {

                    lessonViewModel.getUnFinishedLessons()

                }
                is DataState.Error -> {

                }
                is DataState.Loading -> {

                }
            }
        })
    }

    private fun showLesson(lesson: Lesson) {

        if (lesson.input == null) {
            buttonNext.isEnabled = true
            questionTV.setText(lesson.getFullQuestion())
        }
        else {
            buttonNext.isEnabled = false

            var fullQuestionString = SpannableString(lesson.getFullQuestion())
            val inputSize: Int = lesson.input?.endIndex?.minus(lesson.input?.startIndex ?: 0) ?: 0

            var replacmentString = "";
            for (i in 0..inputSize-1) {
                replacmentString = replacmentString.plus("_")
            }

            val correctAnswer: String = fullQuestionString.substring(lesson.input?.startIndex ?: 0, lesson.input?.endIndex ?: 0)

            val string =  fullQuestionString.replaceRange(lesson.input?.startIndex ?: 0, lesson.input?.endIndex ?: 0, replacmentString)
            fullQuestionString = SpannableString(string)
            val clickableSpan: ClickableSpan = object : ClickableSpan() {

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = resources.getColor(android.R.color.background_dark)
                }

                override fun onClick(widget: View) {
                    showDialog(correctAnswer, lesson.getFullQuestion())
                }
            }

            fullQuestionString.setSpan(clickableSpan, lesson.input?.startIndex ?: 0, lesson.input?.endIndex ?: 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            questionTV.setText(fullQuestionString)
            questionTV.setMovementMethod(LinkMovementMethod.getInstance())
        }
    }

    private fun showDialog(correctAnswer: String, fullAnswer: String) {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_enter_answer_layout)

        val buttonSubmit = dialog.findViewById(R.id.buttonSubmit) as Button
        val answerET = dialog.findViewById(R.id.answerET) as EditText

        answerET.requestFocus()

        buttonSubmit.setOnClickListener {

            if (answerET.text.toString().trim().equals(correctAnswer)) {
                dialog.dismiss()
                Toast.makeText(applicationContext, R.string.correct_answer, Toast.LENGTH_SHORT).show()
                questionTV.setText(fullAnswer)
                buttonNext.isEnabled = true
            }
            else {
                Toast.makeText(applicationContext, R.string.incorrect_answer, Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LessonsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }
}