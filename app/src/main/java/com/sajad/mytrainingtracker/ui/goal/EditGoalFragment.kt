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
import androidx.navigation.fragment.navArgs
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.databinding.FragmentEditGoalBinding
import com.sajad.mytrainingtracker.viewModel.GoalViewModel


class EditGoalFragment : Fragment() {

    private var _binding: FragmentEditGoalBinding? = null
    private val binding get() = _binding!!

    private lateinit var editView: View
    private lateinit var currentGoal: Goal
    private lateinit var goalViewModel: GoalViewModel

    private val args: EditGoalFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalViewModel = ViewModelProvider(requireActivity())[GoalViewModel::class.java]
        currentGoal = args.goal
        editView = view

        binding.etGoalDescription.setText(currentGoal.description)
        setupSpinners()
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
                currentGoal.id,
                goalDescription,
                priority,
                status,
                currentGoal.userId
            )

            goalViewModel.updateGoal(newGoal)
            Toast.makeText(requireContext(), getString(R.string.goal_updated_successfully), Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }

        binding.btnDelete.setOnClickListener {
            goalViewModel.deleteGoal(currentGoal)
            Toast.makeText(requireContext(), getString(R.string.goal_deleted), Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
    }

    private fun setupSpinners() {
        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.goal_priority,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = priorityAdapter

        binding.spinnerPriority.setSelection(currentGoal.priority)

        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.goal_status,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = statusAdapter

        binding.spinnerStatus.setSelection(currentGoal.isCompleted)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}