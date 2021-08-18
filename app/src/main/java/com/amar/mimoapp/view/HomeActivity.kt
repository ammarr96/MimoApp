package com.amar.mimoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amar.mimoapp.R
import com.amar.mimoapp.db.DataState
import com.amar.mimoapp.model.Lesson
import com.amar.mimoapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class HomeActivity : AppCompatActivity() {

    private var lessonList : ArrayList<Lesson> = arrayListOf()

    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        buttonStart.visibility = View.GONE
        succesView.visibility = View.GONE

        subscribeObservers()

        buttonStart.setOnClickListener {
            LessonsActivity.start(applicationContext)
        }

        buttonReset.setOnClickListener {
            homeViewModel.resetAllLessons()
        }

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAllLessons()
    }

    private fun subscribeObservers(){
        homeViewModel.dataState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<List<Lesson>> -> {
                    progressBar.visibility = View.GONE
                    homeViewModel.getUnfinishedLessons()
                }
                is DataState.Error -> {
                    progressBar.visibility = View.GONE
                }
                is DataState.Loading -> {
                   progressBar.visibility = View.VISIBLE
                }
            }
        })

        homeViewModel.dataStateUnfinished.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<List<Lesson>> -> {
                    progressBar.visibility = View.GONE
                    lessonList = dataState.data as ArrayList<Lesson>
                    if (lessonList.size == 0) {
                        buttonStart.visibility = View.GONE
                        succesView.visibility = View.VISIBLE
                    }
                    else {
                        buttonStart.visibility = View.VISIBLE
                        succesView.visibility = View.GONE
                    }
                }
                is DataState.Error -> {
                    progressBar.visibility = View.GONE
                }
                is DataState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })

        homeViewModel.dataStateReset.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Int> -> {
                    progressBar.visibility = View.GONE
                    homeViewModel.getAllLessons()
                }
                is DataState.Error -> {
                    progressBar.visibility = View.GONE
                }
                is DataState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}