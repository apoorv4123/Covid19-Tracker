package com.example.covid19_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class StateAdapter(val list: List<StatewiseItem>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_list, parent, false)
        val item =
            list[position]// List mein jiss position pe hum item set kar rhe hain, uss position ka data, hum list mein se le rhe hain

        // Populate the views in list
        view.tvState.text = item.state
        view.tvCnfmd.text = item.confirmed
        view.tvActv.text = item.active
        view.tvRcvrd.text = item.recovered
        view.tvDcsd.text = item.deaths
        return view
    }
}