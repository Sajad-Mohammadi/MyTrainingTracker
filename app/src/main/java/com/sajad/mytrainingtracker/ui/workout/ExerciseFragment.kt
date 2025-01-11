package com.sajad.mytrainingtracker.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.adapter.ExerciseAdapter
import com.sajad.mytrainingtracker.data.entities.Routine
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.databinding.FragmentExerciseBinding
import com.sajad.mytrainingtracker.viewModel.ExerciseViewModel

class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var exerciseAdapter: ExerciseAdapter

    private lateinit var currentRoutine: Routine

    private val args: ExerciseFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseViewModel = ViewModelProvider(requireActivity())[ExerciseViewModel::class.java]
        currentRoutine = args.routine

//        attachUiListeners()
        setupRecyclerView()
    }

    private fun attachUiListeners() {
        TODO("Not yet implemented")
    }

    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(
            onBtnEditClick = { exerciseId ->
                TODO("Not yet implemented")
            },
        )

        binding.exercisesRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = exerciseAdapter
        }

        exerciseViewModel.getExercisesByRoutineId(currentRoutine.id)
            .observe(viewLifecycleOwner) { exercises ->
                if (exercises.isNotEmpty()) {
                    exerciseAdapter.differ.submitList(exercises)
                    updateUi(true)
                } else {
                    updateUi(false)
                }
            }
    }

    private fun updateUi(ex: Boolean) {
        if (ex) {
            binding.pageImage.visibility = View.GONE
            binding.exercisesRecyclerView.visibility = View.VISIBLE
        } else {
            binding.pageImage.visibility = View.VISIBLE
            binding.exercisesRecyclerView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}