package com.jhb.touchsample

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jhb.demo.fragment.RecyclerViewFragment
import kotlinx.android.synthetic.main.activity_main_touch.*

/**
 * ScrollView+Viewpager+Fragment+RecyclerView
 */
class MainTouchActivity : AppCompatActivity() {

    val fragmentList: List<Fragment> = arrayListOf<Fragment>().let {
        it.add(RecyclerViewFragment())
        it.add(RecyclerViewFragment())
        it.add(RecyclerViewFragment())
        it.add(RecyclerViewFragment())
        it
    }
    val strings = arrayOf("A", "B", "C", "D")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_touch)

        scrollview.headerContent = tv_content
        viewpager.adapter = FragmentAdapter(supportFragmentManager)
        tablayout.setupWithViewPager(viewpager)
        viewpager.post {
            val srHeight = scrollview.measuredHeight
            val tabHeight = tablayout.measuredHeight
            viewpager.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, srHeight - tabHeight)
        }

        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
        }
    }

    inner class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return strings[position]
        }
    }
}
