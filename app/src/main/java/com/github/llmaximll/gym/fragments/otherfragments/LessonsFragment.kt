package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

    private var lessons: List<Lessons>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_lessons, container, false)

        tabs = view.findViewById(R.id.tabs)
        viewPager2 = view.findViewById(R.id.viewPager2)

        viewPager2.adapter = PagerAdapter(listOf())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
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
                        viewPager2.adapter = PagerAdapter(it)
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
            when (lessonsList[0].category) {
                "hands" -> imageView.setImageResource(R.mipmap.hands)
                "spine" -> imageView.setImageResource(R.mipmap.spine)
                "torso" -> imageView.setImageResource(R.mipmap.torso)
                "legs" -> imageView.setImageResource(R.mipmap.legs)
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
            val handsLessons = mutableListOf<Lessons>()
            val spineLessons = mutableListOf<Lessons>()
            val torsoLessons = mutableListOf<Lessons>()
            val legsLessons = mutableListOf<Lessons>()
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
            log(TAG, "lessons=$lessonsList")
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