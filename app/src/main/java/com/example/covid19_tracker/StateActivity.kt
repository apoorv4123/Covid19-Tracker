package com.example.covid19_tracker

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eazegraph.lib.models.PieModel

val states = arrayOf(
    State("TT", R.drawable.ind),
    State("AN", R.drawable.ind_andamanandnicobar),
    State("AP", R.drawable.ind_andhrapradesh),
    State("AR", R.drawable.ind_arunachalpradesh),
    State("AS", R.drawable.ind_assam),
    State("BR", R.drawable.ind_bihar),
    State("CH", R.drawable.ind_chandigarh),
    State("CT", R.drawable.ind_chhattisgarh),
    State("DN", R.drawable.ind_ddu),
    State("DL", R.drawable.ind_nctofdelhi),
    State("GA", R.drawable.ind_goa),
    State("GJ", R.drawable.ind_gujarat),
    State("HR", R.drawable.ind_haryana),
    State("HP", R.drawable.ind_himachalpradesh),
    State("JK", R.drawable.ind_jammuandkashmir),
    State("JH", R.drawable.ind_jharkhand),
    State("KA", R.drawable.ind_karnataka),
    State("KL", R.drawable.ind_kerala),
    State("LA", R.drawable.ind_jammuandkashmir),
    State("LD", R.drawable.ind_lakshadweep),
    State("MP", R.drawable.ind_madhyapradesh),
    State("MH", R.drawable.ind_maharashtra),
    State("MN", R.drawable.ind_manipur),
    State("ML", R.drawable.ind_meghalaya),
    State("MZ", R.drawable.ind_mizoram),
    State("NL", R.drawable.ind_nagaland),
    State("OR", R.drawable.ind_odisha),
    State("PY", R.drawable.ind_puducherry),
    State("PB", R.drawable.ind_punjab),
    State("RJ", R.drawable.ind_rajasthan),
    State("SK", R.drawable.ind_sikkim),
    State("UN", R.drawable.ic_virus),
    State("TN", R.drawable.ind_tamilnadu),
    State("TG", R.drawable.ind_telangana),
    State("TR", R.drawable.ind_tripura),
    State("UP", R.drawable.ind_uttarpradesh),
    State("UT", R.drawable.ind_uttarakhand),
    State("WB", R.drawable.ind_westbengal)
)

class StateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        val position = intent.getIntExtra("SID", 0)

        // Set up back button in toolbar
        toolBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolBar.setNavigationOnClickListener {
            finish()
        }

        ivState.setImageResource(states[position].mapId)
        fetchResults(position)
    }

    private fun fetchResults(position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
//            if(!Client.api.isExecuted())
//            Client.api.cancel()
            val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
            if (response.isSuccessful) {
                val data = Gson().fromJson(response.body?.string(), Response::class.java)

                data.let {
                    toolBar.title = it.statewise[position].state

                    tvActvState.text = it.statewise[position].active
                    tvConfState.text = it.statewise[position].confirmed
                    tvRcvrdState.text = it.statewise[position].recovered
                    tvDcsdState.text = it.statewise[position].deaths

                    pieChart.addPieSlice(
                        PieModel(
                            "Confirmed",
                            Integer.parseInt(tvConfState.text.toString()).toFloat(),
                            Color.parseColor("#D32F2F")
                        )
                    )
                    pieChart.addPieSlice(
                        PieModel(
                            "Active",
                            Integer.parseInt(tvActvState.text.toString()).toFloat(),
                            Color.parseColor("#1976D2")
                        )
                    )
                    pieChart.addPieSlice(
                        PieModel(
                            "Recovered",
                            Integer.parseInt(tvRcvrdState.text.toString()).toFloat(),
                            Color.parseColor("#388E3C")
                        )
                    )
                    pieChart.addPieSlice(
                        PieModel(
                            "Deceased",
                            Integer.parseInt(tvDcsdState.text.toString()).toFloat(),
                            Color.parseColor("#FBC02D")
                        )
                    )
                    pieChart.startAnimation()
                }
            }
        }
    }

}