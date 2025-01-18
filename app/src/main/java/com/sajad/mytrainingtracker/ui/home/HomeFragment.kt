package com.sajad.mytrainingtracker.ui.home

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
import com.sajad.mytrainingtracker.adapter.TrainingProgramAdapter
import com.sajad.mytrainingtracker.adapter.UserAdapter
import com.sajad.mytrainingtracker.data.entities.Goal
import com.sajad.mytrainingtracker.databinding.FragmentHomeBinding
import com.sajad.mytrainingtracker.ui.goal.GoalFragmentDirections
import com.sajad.mytrainingtracker.viewModel.GoalViewModel
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var trainingProgramViewModel: TrainingProgramViewModel
    private lateinit var trainingProgramAdapter: TrainingProgramAdapter

    private lateinit var goalViewModel: GoalViewModel
    private lateinit var goalAdapter: GoalAdapter

    private lateinit var userViewModel: UserViewModel

    private var hasProgram: Boolean = false
    private var hasGoal: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trainingProgramViewModel =
            ViewModelProvider(requireActivity())[TrainingProgramViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        goalViewModel = ViewModelProvider(requireActivity())[GoalViewModel::class.java]

        attacheUiListeners()
        setupRecyclerView()
        setupGoalRecyclerView()
    }

    private fun attacheUiListeners() {
        binding.btnAddProgram.setOnClickListener { view ->
            getCurrentUser { userId ->
                if (userId == 0) {
                    view.findNavController().navigate(R.id.navigation_profile)
                } else {
                    val bundle = Bundle().apply {
                        putInt("userId", userId)
                    }
                    view.findNavController().navigate(R.id.navigation_add_training_program, bundle)
                }
            }
        }
        binding.btnAddGoal.setOnClickListener { view ->
            getCurrentUser { userId ->
                if (userId == 0) {
                    view.findNavController().navigate(R.id.navigation_profile)
                } else {
                    val bundle = Bundle().apply {
                        putInt("userId", userId)
                    }
                    view.findNavController().navigate(R.id.navigation_add_goal, bundle)
                }
            }
        }
    }

    private fun updateUi(p: Boolean) {
        if (p) {
            hasProgram = true
            binding.programRecyclerView.visibility = View.VISIBLE
            binding.tvNoPrograms.visibility = View.GONE
        } else {
            hasProgram = false
            binding.programRecyclerView.visibility = View.GONE
            binding.tvNoPrograms.visibility = View.VISIBLE
        }
        updatePageImageVisibility()
    }

    private fun updatePageImageVisibility() {
        if (hasProgram || hasGoal) {
            binding.pageImage.visibility = View.GONE
        } else {
            binding.pageImage.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        trainingProgramAdapter = TrainingProgramAdapter(
            onBtnEditClick = { trainingProgram ->
                val action =
                    HomeFragmentDirections.actionNavigationHomeToNavigationEditTrainingProgram(
                        trainingProgram
                    )
                view?.findNavController()?.navigate(action)

            },
            onBtnGoToProgramClick = { trainingProgram ->
                val action =
                    HomeFragmentDirections.actionNavigationHomeToNavigationRoutine(trainingProgram)
                view?.findNavController()?.navigate(action)
            }
        )

        binding.programRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = trainingProgramAdapter
        }

        getCurrentUser { userId ->
            if (userId != 0) {
                trainingProgramViewModel.getTrainingProgramsByUserId(userId)
                    .observe(viewLifecycleOwner) { programs ->
                        val recentProgram =
                            programs.firstOrNull { it.recent }?.let { listOf(it) } ?: emptyList()
                        trainingProgramAdapter.differ.submitList(recentProgram)
                        updateUi(recentProgram.isNotEmpty())
                    }
            } else {
                trainingProgramAdapter.differ.submitList(emptyList())
                updateUi(false)
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

    private fun setupGoalRecyclerView() {
        goalAdapter = GoalAdapter(
            onBtnEditClick = { goal ->
                val action = HomeFragmentDirections.actionNavigationHomeToEditGoalFragment(goal)
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
                        val highPriorityGoals = goals.filter { it.priority == 3 }
                        val displayGoals = when {
                            highPriorityGoals.size >= 3 -> highPriorityGoals.take(3)
                            else -> {
                                val mediumPriorityGoals = goals.filter { it.priority == 2 }
                                val lowPriorityGoals = goals.filter { it.priority == 1 }
                                (highPriorityGoals + mediumPriorityGoals + lowPriorityGoals).take(3)
                            }
                        }
                        goalAdapter.differ.submitList(displayGoals)
                        binding.goalRecyclerView.visibility = View.VISIBLE
                        binding.tvNoGoals.visibility = View.GONE
                        hasGoal = true
                    } else {
                        binding.goalRecyclerView.visibility = View.GONE
                        binding.tvNoGoals.visibility = View.VISIBLE
                        hasGoal = false
                    }
                }
            } else {
                binding.goalRecyclerView.visibility = View.GONE
                binding.tvNoGoals.visibility = View.VISIBLE
                binding.pageImage.visibility = View.VISIBLE
            }
            updatePageImageVisibility()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}