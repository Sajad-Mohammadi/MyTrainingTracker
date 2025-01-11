package com.sajad.mytrainingtracker.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.adapter.TrainingProgramAdapter
import com.sajad.mytrainingtracker.databinding.FragmentWorkoutBinding
import com.sajad.mytrainingtracker.ui.home.HomeFragmentDirections
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModel


class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var trainingProgramViewModel: TrainingProgramViewModel
    private lateinit var trainingProgramAdapter: TrainingProgramAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trainingProgramViewModel =
            ViewModelProvider(requireActivity())[TrainingProgramViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        attacheUiListeners()
        setupRecyclerView()

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
    }

    private fun updateUi(hasProgram: Boolean) {
        if (hasProgram) {
            binding.pageImage.visibility = View.GONE
            binding.programRecyclerView.visibility = View.VISIBLE
        } else {
            binding.pageImage.visibility = View.VISIBLE
            binding.programRecyclerView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        trainingProgramAdapter = TrainingProgramAdapter(
            onBtnEditClick = { trainingProgram ->
                val action =
                    WorkoutFragmentDirections.actionNavigationWorkoutToNavigationEditTrainingProgram(
                        trainingProgram
                    )
                view?.findNavController()?.navigate(action)
            },
            onBtnGoToProgramClick = { trainingProgram ->
                val action = WorkoutFragmentDirections.actionNavigationWorkoutToRoutineFragment(trainingProgram.id)
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
                        if (programs.isNotEmpty()) {
                            trainingProgramAdapter.differ.submitList(programs)
                            updateUi(true)
                        } else {
                            trainingProgramAdapter.differ.submitList(emptyList())
                            updateUi(false)
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}