package com.example.covid19_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.AbsListView
import androidx.core.view.get
import androidx.work.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header, list, false))

        fetchResults()
        swipeToRefresh.setOnRefreshListener {
            fetchResults()
        }

        list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (list.getChildAt(0) != null) {
                    swipeToRefresh.isEnabled =
                        list.firstVisiblePosition === 0 && list.getChildAt(0).getTop() === 0
                }
            }
        })

        list.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, StateActivity::class.java)
            intent.putExtra("SID", position)
            startActivity(intent)
        }


    }

    private fun fetchResults() {
// GlobalScope se coroutines ka global scope start ho jata hai
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }
// This is the network call. It'll return you json object. Your task is to parse it.
            if (response.isSuccessful) {
//                Log.i("info", response.body?.string()!!)
                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])// To obtain first value of state-wise key from json
                    bindStateWiseDate(data.statewise.subList(1, data.statewise.size))
                }
            }
        }
    }

    private fun bindStateWiseDate(subList: List<StatewiseItem>) {
        stateAdapter = StateAdapter(subList)
        list.adapter = stateAdapter
    }

    private fun bindCombinedData(data: StatewiseItem) {
        // Last updated time
        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        tvLastUpdatedTime.text =
            "Last Updated\n ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

        // To populate the HeaderView
        tvConfirmed.text = data.confirmed
        tvActive.text = data.active
        tvRecovered.text = data.recovered
        tvDeceased.text = data.deaths
    }

    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hour ${minutes % 60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
            }
        }
    }

}