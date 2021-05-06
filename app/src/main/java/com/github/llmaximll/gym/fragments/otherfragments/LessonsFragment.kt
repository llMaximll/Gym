package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.dataclasses.Lessons
import com.github.llmaximll.gym.vm.LessonsVM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "LessonsFragment"

class LessonsFragment : Fragment() {

    private lateinit var viewModel: LessonsVM
    private lateinit var tabs: TabLayout
    private lateinit var viewPager2: ViewPager2

    private var adapter = PagerAdapter(listOf())

    private var bitmap: Bitmap? = null
    private var countHands = 0
    private var countSpine = 0
    private var countTorso = 0
    private var countLegs = 0
    private val bitmapHands = mutableListOf<Bitmap>()
    private val bitmapSpine = mutableListOf<Bitmap>()
    private val bitmapTorso = mutableListOf<Bitmap>()
    private val bitmapLegs = mutableListOf<Bitmap>()
    private val handsLessons = mutableListOf<Lessons>()
    private val spineLessons = mutableListOf<Lessons>()
    private val torsoLessons = mutableListOf<Lessons>()
    private val legsLessons = mutableListOf<Lessons>()

    private var lessons: List<Lessons>? = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_lessons, container, false)

        tabs = view.findViewById(R.id.tabs)
        viewPager2 = view.findViewById(R.id.viewPager2)

        viewPager2.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.downloadImages()
        viewModel.getLessons()
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Hands"
                }
                1 -> {
                    tab.text = "Spine"
                }
                2 -> {
                    tab.text = "Torso"
                }
                3 -> {
                    tab.text = "Legs"
                }
            }
            viewPager2.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun initObservers() {
        viewModel = ViewModelProvider(this).get(LessonsVM::class.java)
        viewModel.cosmeticView.lessons.observe(
                viewLifecycleOwner,
                {
                    lessons = it
                    if (it != null) {
                        adapter = PagerAdapter(it)
                        viewPager2.adapter = adapter
                    }
                }
        )
        viewModel.imageLD.observe(
                viewLifecycleOwner,
                { image ->
                    bitmap = image.bitmap
                    when (image.category) {
                        "hands" -> bitmapHands.add(image.bitmap)
                        "spine" -> bitmapSpine.add(image.bitmap)
                        "torso" -> bitmapTorso.add(image.bitmap)
                        "legs" -> bitmapLegs.add(image.bitmap)
                    }
                    if (bitmapHands.size >= 3 && bitmapSpine.size >= 3 && bitmapTorso.size >= 3 && bitmapLegs.size >= 3) {
                        adapter = PagerAdapter(lessons!!)
                        viewPager2.adapter = adapter
                        log(TAG, "update adapter")
                    }
                }
        )
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    private fun animateView(view: View, reverse: Boolean) {
        if (!reverse) {
            val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f)
            val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f)
            AnimatorSet().apply {
                playTogether(animatorX, animatorY)
                duration = 150
                start()
            }
        } else {
            val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f)
            val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f)
            AnimatorSet().apply {
                playTogether(animatorX, animatorY)
                duration = 150
                start()
            }
        }
    }

    private inner class LessonsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener {

        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnTouchListener(this)
        }

        fun bind(lessonsList: List<Lessons>) {
            if (bitmapHands.size >= 3) {
                when (lessonsList[0].category) {
                    "hands" -> {
                        when (countHands) {
                            0 -> imageView.setImageBitmap(bitmapHands[0])
                            1 -> imageView.setImageBitmap(bitmapHands[1])
                            2 -> imageView.setImageBitmap(bitmapHands[2])
                        }
                        countHands++
                    }
                    "spine" -> {
                        when (countSpine) {
                            0 -> imageView.setImageBitmap(bitmapSpine[0])
                            1 -> imageView.setImageBitmap(bitmapSpine[1])
                            2 -> imageView.setImageBitmap(bitmapSpine[2])
                        }
                        countSpine++
                    }
                    "torso" -> {
                        when (countTorso) {
                            0 -> imageView.setImageBitmap(bitmapTorso[0])
                            1 -> imageView.setImageBitmap(bitmapTorso[1])
                            2 -> imageView.setImageBitmap(bitmapTorso[2])
                        }
                        countTorso++
                    }
                    "legs" -> {
                        when (countLegs) {
                            0 -> imageView.setImageBitmap(bitmapLegs[0])
                            1 -> imageView.setImageBitmap(bitmapLegs[1])
                            2 -> imageView.setImageBitmap(bitmapLegs[2])
                        }
                        countLegs++
                    }
                }
            }
        }

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(itemView, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(itemView, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(itemView, true)
                    itemView.performClick()
                }
            }
            return true
        }
    }

    private inner class LessonsAdapter(val lessonsList: List<Lessons>) : RecyclerView.Adapter<LessonsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsHolder {
            val view = layoutInflater.inflate(R.layout.item_lessons_recycler, parent, false)
            return LessonsHolder(view)
        }
        override fun onBindViewHolder(holder: LessonsHolder, position: Int) {
            holder.bind(lessonsList)
        }
        override fun getItemCount(): Int = lessonsList.size
    }

    private inner class PagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)

        fun bind(position: Int, lessonsList: List<Lessons>) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            lessonsList.forEach { lesson ->
                when (lesson.category) {
                    "hands" -> handsLessons.add(lesson)
                    "spine" -> spineLessons.add(lesson)
                    "torso" -> torsoLessons.add(lesson)
                    "legs" -> legsLessons.add(lesson)
                }
            }
            when (position) {
                0 -> {
                    recyclerView.adapter = LessonsAdapter(handsLessons)
                }
                1 -> {
                    recyclerView.adapter = LessonsAdapter(spineLessons)
                }
                2 -> {
                    recyclerView.adapter = LessonsAdapter(torsoLessons)
                }
                3 -> {
                    recyclerView.adapter = LessonsAdapter(legsLessons)
                }
            }
        }
    }

    private inner class PagerAdapter(val lessonsList: List<Lessons>) : RecyclerView.Adapter<PagerHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerHolder {
            val view = layoutInflater.inflate(R.layout.item_lessons_pager, parent, false)
            return PagerHolder(view)
        }
        override fun onBindViewHolder(holder: PagerHolder, position: Int) {
            holder.bind(position, lessonsList)
        }
        override fun getItemCount(): Int = 4
    }

    companion object {
        fun newInstance(): LessonsFragment = LessonsFragment()
    }
}