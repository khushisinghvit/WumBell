package com.example.wumbell.screens.workout.mainPage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.wumbell.R
import com.example.wumbell.data.BodyPart
import com.example.wumbell.data.ExerciseData
import com.example.wumbell.data.ExerciseData.Companion.bodypartList
import com.example.wumbell.data.ExerciseData.Companion.equipmentList
import com.example.wumbell.databinding.WorkoutMainPageBinding


class MainPageFragment : Fragment() {
    private lateinit var exerciseAdapter:ExerciseAdapter
    private lateinit var filter:MutableList<BodyPart>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding= DataBindingUtil.inflate<WorkoutMainPageBinding>(inflater,R.layout.workout_main_page, container, false)
        updateUI(binding)
        return binding.root
    }

    private fun updateUI(binding: WorkoutMainPageBinding) {
        val exerciseList=binding.recyclerView

        val spinnerArgs=MainPageFragmentArgs.fromBundle(requireArguments())
        val equipmentSpinner =binding.equipmentSpinner
        val bodySpinner=binding.bodyPartSpinner

        equipmentSpinner.adapter=
            ArrayAdapter(requireContext(), R.layout.spinner_layout,equipmentList)
        bodySpinner.adapter=
            ArrayAdapter(requireContext(), R.layout.spinner_layout,bodypartList)
        equipmentSpinner.setSelection(spinnerArgs.equipment)
        bodySpinner.setSelection(spinnerArgs.body)


        var selectedEquip= equipmentList[spinnerArgs.equipment]
        exerciseAdapter=ExerciseAdapter(ClickListener { index ->
            view?.findNavController()?.navigate(MainPageFragmentDirections.actionMainPageFragmentToMenuPageFragment(index,selectedEquip))
        })
        exerciseList.adapter=exerciseAdapter

        submitList("")

        bodySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?, position: Int, id: Long) {
                if(position==0)
                    submitList("")
                selectedEquip= equipmentList[position]
                if(position==1 || position==2)
                    submitList(bodypartList[position])
                else if(position!=0)
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }
    fun submitList(name: String?) {
        if(name.isNullOrEmpty()) {
            filter=ExerciseData.data
        }
        else{
            filter= mutableListOf<BodyPart>()
            ExerciseData.data.forEach {
                if(it.name.toLowerCase().contains(name.toLowerCase()))
                    filter.add(it)
            }
        }
        exerciseAdapter.submitList(filter)
        exerciseAdapter.notifyDataSetChanged()
    }
}