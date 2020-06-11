package com.jhb.touchsample

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.jhb.demo.fragment.RecyclerViewFragment
import com.jhb.touchsample.fragment.NestedScrollChildView
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

        initUI()
        initBanner()
        initRefreshLayout()
    }

    var screenWidth: Int? = null
    var urls = arrayListOf(
        "http://f.hiphotos.baidu.com/zhidao/pic/item/3b87e950352ac65cbdbeff61fcf2b21193138a6d.jpg",
        "http://c.hiphotos.baidu.com/zhidao/pic/item/562c11dfa9ec8a1359aa88b6f103918fa0ecc030.jpg",
        "http://c.hiphotos.baidu.com/zhidao/pic/item/faf2b2119313b07e6077d3bc0ad7912396dd8cb8.jpg"
    )
    var imgheights: Array<Int?>? = null

    private fun initBanner() {
        screenWidth = resources.displayMetrics.widthPixels

        Glide.with(this).asBitmap().load(urls[0]).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val scale = (resource.height * 1.0f) / (resource.width * 1.0f)
                val defaultheight = (scale * screenWidth!!)
                initViewPager(defaultheight);
            }
        })
    }

    private fun initViewPager(defaultheight: Float) {
        viewpager_loop.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object` as View
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

            override fun getCount(): Int {
                if (imgheights == null || imgheights!!.size != urls.size) {
                    imgheights = arrayOfNulls<Int?>(urls.size)
                }
                return imgheights!!.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val imageView = ImageView(this@MainTouchActivity).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }

                Glide.with(this@MainTouchActivity).asBitmap()
                    .placeholder(R.drawable.ic_launcher_background).load(urls[position]).into(
                        object : ImageViewTarget<Bitmap>(imageView) {
                            override fun setResource(resource: Bitmap?) {
                                if (resource != null) {
                                    val scale = (resource.height * 1.0f) / (resource.width * 1.0f)
                                    imgheights?.set(
                                        position,
                                        (scale * this@MainTouchActivity.screenWidth!!).toInt()
                                    )

                                    imageView.setImageBitmap(resource)
                                }
                            }
                        }
                    )

                container.addView(imageView)
                return imageView
            }
        }

        viewpager_loop.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, defaultheight.toInt())
        viewpager_loop.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == imgheights?.size?.minus(1)) {
                    return
                }

                val height = if (imgheights?.get(position) == 0) {
                    defaultheight
                } else {
                    var h: Float? =
                        if (imgheights?.get(position + 1) == 0) defaultheight else imgheights?.get(
                            position + 1
                        )?.times(positionOffset)?.let {
                            imgheights?.get(position)?.times(1 - positionOffset)?.plus(
                                it
                            )
                        }
                    h
                }
                if (height != null)
                    viewpager_loop.layoutParams =
                        LinearLayout.LayoutParams(MATCH_PARENT, height!!.toInt())
            }
        })
    }


    private fun initRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
        }
    }

    private fun initUI() {
        scrollview.headerContent = viewpager_loop
        tablayout.setupWithViewPager(viewpager)
        //设置ViewPager高度及内容
        viewpager.adapter = FragmentAdapter(supportFragmentManager)
        viewpager.post {
            //第一次渲染完成后设置VP高度
            val srHeight = scrollview.measuredHeight
            val tabHeight = tablayout.measuredHeight
            viewpager.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, srHeight - tabHeight)

            //设置初始CHILD
            val fragment = fragmentList[0]
            if (fragment is NestedScrollChildView) {
                val nestedView = fragment.getNestedView()
                scrollview.canScrollChild = nestedView
            }
        }

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position <= fragmentList.size - 1) {
                    val fragment = fragmentList[position]
                    if (fragment is NestedScrollChildView) {
                        val nestedView = fragment.getNestedView()
                        scrollview.canScrollChild = nestedView
                    }
                }
            }
        })
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
