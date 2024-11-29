package com.example.todoir.peresentaion.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.FragmentProfileBinding
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val mutableLiveData = MutableLiveData<String>()



    @Inject
    lateinit var sp: Sp



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mutableLiveData.value = sp.fetch("username").toString()

chart()
        setName()
        showTask()


    }


    private fun initPieChart() {
        //using percentage as values instead of amount
        binding.pieChart.setUsePercentValues(true)

        //remove the description label on the lower left corner, default true if not set
        binding.pieChart.getDescription().setEnabled(false)

        //enabling the user to rotate the chart, default true
        binding.pieChart.setRotationEnabled(true)
        //adding friction when rotating the pie chart
        binding.pieChart.setDragDecelerationFrictionCoef(0.9f)
        //setting the first entry start from right hand side, default starting from top
        binding.pieChart.setRotationAngle(0F)

        //highlight the entry when it is tapped, default true if not set
        binding.pieChart.setHighlightPerTapEnabled(true)
        //adding animation so the entries pop up from 0 degree
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
        //setting the color of the hole in the middle, default white
        binding.pieChart.setHoleColor(Color.parseColor("#000000"))
        binding.pieChart.setEntryLabelColor(Color.parseColor("#FFFFFFFF"))
        binding.pieChart.setCenterTextColor(Color.parseColor("#FFFFFFFF"))


    }

    private fun chart() {
        initPieChart()
        val pieEntries = ArrayList<PieEntry>()
        val label = "type"

        viewModel.getCategoryChartTask().observe(viewLifecycleOwner){
            Log.i("TAG", "chart: "+it)
            val typeAmountMap: MutableMap<String, Int> = HashMap()
            it.forEach { categoryCount ->
                typeAmountMap[categoryCount.categoryName] = categoryCount.count
            }


            //initializing colors for the entries
            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#890567"))
            colors.add(Color.parseColor("#304567"))
            colors.add(Color.parseColor("#309967"))
            colors.add(Color.parseColor("#476567"))
            colors.add(Color.parseColor("#a35567"))
            colors.add(Color.parseColor("#ff5f67"))
            colors.add(Color.parseColor("#3ca567"))


            //input data and fit data into pie chart entry
            for (type in typeAmountMap.keys) {
                pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
            }


            //collecting the entries with label name
            val pieDataSet = PieDataSet(pieEntries, label)

            //setting text size of the value
            pieDataSet.valueTextSize = 12f

            //providing color list for coloring different entries
            pieDataSet.colors = colors

            //grouping the data set from entry to chart
            val pieData = PieData(pieDataSet)

            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true)

            binding.pieChart.setData(pieData)
            binding.pieChart.invalidate()

        }



    }



    private fun setName() {
        mutableLiveData.observe(viewLifecycleOwner) {
            binding.tvUserNameProfile.text = it.toString()
        }

    }


    private fun showTask() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getTask().observe(viewLifecycleOwner) {
                val doneList = it.map { it.isCompleted }
                var a = 0
                var b = 0
                doneList.forEach {
                    if (it) {
                        a += 1
                    } else {
                        b += 1
                    }
                    binding.tvDoneTask.text = "$a Task Done"
                    binding.tvLeftTask.text = "$b Task Left"
                }

            }
        }


    }

}