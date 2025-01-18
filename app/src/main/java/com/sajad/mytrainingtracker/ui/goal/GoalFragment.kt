package com.sajad.mytrainingtracker.ui.goal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.adapter.GoalAdapter
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.databinding.FragmentGoalBinding
import com.sajad.mytrainingtracker.viewModel.GoalViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModel

class GoalFragment : Fragment() {

    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!

    private lateinit var goalViewModel: GoalViewModel
    private lateinit var goalAdapter: GoalAdapter

    private lateinit var userViewModel: UserViewModel

    private var sortByPriority = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalViewModel = ViewModelProvider(requireActivity())[GoalViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.btnSort.setOnClickListener {
            sortByPriority = !sortByPriority
            sortGoals()
        }

        attachUiListeners()
        setupRecyclerView()
    }

    private fun sortGoals() {
        getCurrentUser { userId ->
            if (userId != 0) {
                goalViewModel.getGoalsByUserId(userId).observe(viewLifecycleOwner) { goals ->
                    if (goals.isNotEmpty()) {
                        val sortedGoals = if (sortByPriority) {
                            goals.sortedByDescending { it.priority }
                        } else {
                            goals.sortedByDescending { it.isCompleted }
                        }
                        goalAdapter.differ.submitList(sortedGoals)
                        binding.goalRecyclerView.smoothScrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun attachUiListeners() {
        binding.btnNewGoal.setOnClickListener { view ->
            getCurrentUser { userId ->
                if (userId == 0) {
                    view.findNavController().navigate(R.id.navigation_profile)
                } else {
                    val action = GoalFragmentDirections.actionNavigationGoalToNavigationAddGoal(userId)
                    view.findNavController().navigate(action)
                }
            }
        }
    }

    private fun getCurrentUser(callback: (Int) -> Unit) {
        activity?.let {
            userViewModel.getLoggedInUser().observe(viewLifecycleOwner) { user ->
                callback(user?.id ?: 0)
            }
        }
    }

    private fun setupRecyclerView() {
        goalAdapter = GoalAdapter(
            onBtnEditClick = { goal ->
                val action = GoalFragmentDirections.actionNavigationGoalToEditGoalFragment(goal)
                view?.findNavController()?.navigate(action)
            },
            onBtnPriorityClick = { goal ->
                updatePriority(goal)
            },
            onBtnStatusClick = { goal ->
                updateStatus(goal)
            }
        )

        binding.goalRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = goalAdapter
        }

        getCurrentUser { userId ->
            if (userId != 0) {
                goalViewModel.getGoalsByUserId(userId).observe(viewLifecycleOwner) { goals ->
                    if (goals.isNotEmpty()) {
                        goalAdapter.differ.submitList(goals)
                        updateUi(true)
                    } else {
                        goalAdapter.differ.submitList(emptyList())
                        updateUi(false)
                    }
                }
            } else {
                goalAdapter.differ.submitList(emptyList())
                updateUi(false)
            }
        }
    }

    private fun updateStatus(goal: Goal) {
        val nextStatus = when (goal.isCompleted) {
            1 -> 2
            2 -> 3
            else -> 1
        }

        val updatedGoal = goal.copy(isCompleted = nextStatus)
        goalViewModel.updateGoal(updatedGoal)
    }

    private fun updatePriority(goal: Goal) {
        val nextPriority = when (goal.priority) {
            1 -> 2
            2 -> 3
            else -> 1
        }

        val updatedGoal = goal.copy(priority = nextPriority)
        goalViewModel.updateGoal(updatedGoal)
    }

    private fun updateUi(hasGoal: Boolean) {
        if (hasGoal) {
            binding.pageImage.visibility = View.GONE
            binding.goalRecyclerView.visibility = View.VISIBLE

            binding.btnSort.isEnabled = true
        } else {
            binding.pageImage.visibility = View.VISIBLE
            binding.goalRecyclerView.visibility = View.GONE

            binding.btnSort.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}