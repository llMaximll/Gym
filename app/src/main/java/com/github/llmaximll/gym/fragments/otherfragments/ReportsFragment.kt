package com.github.llmaximll.gym.fragments.otherfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.customviews.ChartView

class ReportsFragment : Fragment() {

    private lateinit var chartTrainingView: ChartView
    private lateinit var chartMinutesView: ChartView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_reports, container, false)

        chartTrainingView = view.findViewById(R.id.chartView1)
        chartMinutesView = view.findViewById(R.id.chartView2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        var count = ChartView.STEP_TRAINING
        for (i in 0..29) {
            chartTrainingView.dataItem = count
            count += ChartView.STEP_TRAINING
        }
        count = 20f
        for (i in 0..29) {
            chartMinutesView.dataItem = count
            count += 20f
        }
    }

    companion object {
        fun newInstance(): ReportsFragment = ReportsFragment()
    }
}