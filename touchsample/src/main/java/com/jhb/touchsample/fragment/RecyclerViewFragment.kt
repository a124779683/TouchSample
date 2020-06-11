package com.jhb.demo.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jhb.touchsample.R
import com.jhb.touchsample.fragment.NestedScrollChildView
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 * @author jianghongbo
 * @version 1.0
 * @file RecyclerViewFragment.java
 * @brief
 * @date 2020/6/10
 * Copyright (c) 2020, 北京小药药人工智能
 * All rights reserved.
 */
class RecyclerViewFragment : Fragment(), NestedScrollChildView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_recycler, container, false)
        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_framgent.layoutManager = LinearLayoutManager(this@RecyclerViewFragment.context).apply {
            this.orientation = RecyclerView.VERTICAL
        }
        val arrayList = ArrayList<String>()
        for (i in 0..100) {
            arrayList.add("position".plus(i))
        }
        rv_framgent.adapter = Adapter(arrayList)

        //设置加载监听
        rv_framgent.setOnLoadingListener {
            Handler().postDelayed({
                rv_framgent.loadFinished()
            }, 1000)
        }
    }

    inner class Adapter(val list: List<String>) : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemCount(): Int = list.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val textView = TextView(this@RecyclerViewFragment.context)
            textView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 700)
            return ViewHolder(textView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.view.text = list[position]
        }
    }

    class ViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun getNestedView() = this!!.rv_framgent
}