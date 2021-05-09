package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.vm.PushUpsChoiceVM

private const val TAG = "PushUpsChoiceFragment"
private const val KEY_CATEGORY = "key_category"

class PushUpsChoiceFragment : Fragment() {

    interface Callbacks {
        fun onPushUpsChoiceFragment(nameEx: String, numberEx: Int, scores: Int, isRepetition: Boolean)
    }
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    private lateinit var category: String
    private lateinit var viewModel: PushUpsChoiceVM

    private var countCompItems = 0

    private var callbacks: Callbacks? = null
    private var adapter: StepAdapter = StepAdapter()
    private var countItem = 1
    private var progress: Float = 0f

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString(KEY_CATEGORY, "category") ?: "category"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_pushups_choice, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        progressTextView = view.findViewById(R.id.progress_textView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        initVM()

        return view
    }

    override fun onResume() {
        super.onResume()
        countItem = 1
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun initVM() {
        viewModel = ViewModelProvider(this).get(PushUpsChoiceVM::class.java)
        viewModel.initDB(requireContext())
        countCompItems = viewModel.getCompletedExercises()
        progress = (countCompItems * 100 / 60).toFloat()
        progressBar.progress = progress.toInt()
        progressTextView.text = progress.toString() + "%"
    }

    private fun TextView.setCountItem(countItem: Int) {
        this.text = countItem.toString()
    }

    private fun ImageView.setComItems(countItem: Int, textView: TextView) {
        if (countItem <= countCompItems) {
            this.setBackgroundResource(R.drawable.background_item_recycler_push_ups_choice_on)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun View.setListeners(countItem: Int) {
        if (countItem < countCompItems + 2) {
            this.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        animateView(this, false)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        animateView(this, true)
                    }
                    MotionEvent.ACTION_UP -> {
                        animateView(this, true)
                        log(TAG, "countItem=$countItem")
                        val isRepetition = countItem < countCompItems + 1
                        callbacks?.onPushUpsChoiceFragment(category, countItem, countItem, isRepetition)
                        this.performClick()
                    }
                }
                true
            }
        } else {
            this.setOnClickListener {
                toast("Необходимо пройти предыдущие уроки")
            }
        }
    }

    private fun toast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private fun animateView(view: View, reverse: Boolean) {
        if (!reverse) {
            val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f)
            val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f)
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

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    private inner class StepHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val frameLayout1: FrameLayout = itemView.findViewById(R.id.frameLayout1)
        private val frameLayout2: FrameLayout = itemView.findViewById(R.id.frameLayout2)
        private val frameLayout3: FrameLayout = itemView.findViewById(R.id.frameLayout3)
        private val frameLayout4: FrameLayout = itemView.findViewById(R.id.frameLayout4)
        private val frameLayout5: FrameLayout = itemView.findViewById(R.id.frameLayout5)
        private val frameLayout6: FrameLayout = itemView.findViewById(R.id.frameLayout6)
        private val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
        private val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
        private val imageView3: ImageView = itemView.findViewById(R.id.imageView3)
        private val imageView4: ImageView = itemView.findViewById(R.id.imageView4)
        private val imageView5: ImageView = itemView.findViewById(R.id.imageView5)
        private val imageView6: ImageView = itemView.findViewById(R.id.imageView6)
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)
        private val textView4: TextView = itemView.findViewById(R.id.textView4)
        private val textView5: TextView = itemView.findViewById(R.id.textView5)
        private val textView6: TextView = itemView.findViewById(R.id.textView6)

        fun bind() {
            textView1.setCountItem(countItem)
            imageView1.setComItems(countItem, textView1)
            frameLayout1.setListeners(countItem)
            countItem++

            textView2.setCountItem(countItem)
            imageView2.setComItems(countItem, textView2)
            frameLayout2.setListeners(countItem)
            countItem++

            textView3.setCountItem(countItem)
            imageView3.setComItems(countItem, textView3)
            frameLayout3.setListeners(countItem)
            countItem++

            textView4.setCountItem(countItem)
            imageView4.setComItems(countItem, textView4)
            frameLayout4.setListeners(countItem)
            countItem++

            textView5.setCountItem(countItem)
            imageView5.setComItems(countItem, textView5)
            frameLayout5.setListeners(countItem)
            countItem++

            textView6.setCountItem(countItem)
            imageView6.setComItems(countItem, textView6)
            frameLayout6.setListeners(countItem)
            countItem++

        }
    }

    private inner class StepAdapter() : RecyclerView.Adapter<StepHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepHolder {
            val view = layoutInflater.inflate(R.layout.item_recycler_push_ups_choice, parent, false)
            return StepHolder(view)
        }
        override fun onBindViewHolder(holder: StepHolder, position: Int) {
            holder.bind()
        }
        override fun getItemCount(): Int = 10
    }

    companion object {
        fun newInstance(category: String): PushUpsChoiceFragment {
            val args = Bundle().apply {
                putString(KEY_CATEGORY, category)
            }
            return PushUpsChoiceFragment().apply {
                arguments = args
            }
        }
    }
}