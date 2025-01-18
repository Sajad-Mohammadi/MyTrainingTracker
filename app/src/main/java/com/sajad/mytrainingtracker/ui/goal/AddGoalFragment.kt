package com.sajad.mytrainingtracker.ui.goal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.databinding.FragmentAddGoalBinding
import com.sajad.mytrainingtracker.viewModel.GoalViewModel


class AddGoalFragment : Fragment() {

    private var _binding: FragmentAddGoalBinding? = null
    private val binding get() = _binding!!

    private lateinit var goalViewModel: GoalViewModel
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt("userId", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalViewModel = ViewModelProvider(requireActivity())[GoalViewModel::class.java]

        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.goal_priority,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = priorityAdapter
        binding.spinnerPriority.setSelection(0)

        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.goal_status,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = statusAdapter
        binding.spinnerStatus.setSelection(0)

        attachUiListeners()
    }

    private fun attachUiListeners() {
        binding.btnSave.setOnClickListener {

            val goalDescription = binding.etGoalDescription.text.toString().trim()
            val selectedPriority = binding.spinnerPriority.selectedItem.toString()
            val priority: Int = when (selectedPriority) {
                "Low" -> 1
                "Medium" -> 2
                "High" -> 3
                else -> 0
            }
            val selectedStatus = binding.spinnerStatus.selectedItem.toString()
            val status: Int = when (selectedStatus) {
                "Planned" -> 1
                "In progress" -> 2
                "Completed" -> 3
                else -> 0
            }

            if (goalDescription.isEmpty()) {
                binding.etGoalDescription.error = getString(R.string.please_enter_a_description)
                binding.etGoalDescription.requestFocus()
                return@setOnClickListener
            }
            if (selectedPriority == "Priority" || selectedStatus == "Status") {
                Toast.makeText(requireContext(),
                    getString(R.string.please_select_valid_priority_and_status), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newGoal = Goal(
                0,
                goalDescription,
                priority,
                status,
                userId
            )

            goalViewModel.insertGoal(newGoal)
            Toast.makeText(requireContext(),
                getString(R.string.goal_added_successfully), Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
    }
}