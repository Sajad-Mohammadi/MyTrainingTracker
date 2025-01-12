package com.sajad.mytrainingtracker.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
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

        attachUiListeners()
        setupRecyclerView()
    }

    private fun attachUiListeners() {
        binding.btnAddExercise.setOnClickListener {
            val action =
                ExerciseFragmentDirections.actionNavigationExerciseToNavigationAddExercise(currentRoutine.id)
            view?.findNavController()?.navigate(action)
        }
        binding.btnInfo.setOnClickListener {
            Toast.makeText(context, getString(R.string.reorder_exercise_info), Toast.LENGTH_SHORT).show()
        }
        binding.btnResetRoutine.setOnClickListener {
            exerciseViewModel.resetDoneStatus(currentRoutine.id)
        }
    }


    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(
            onExerciseReordered = { reorderedList ->
                exerciseViewModel.updateExercises(reorderedList)
            },
            onBtnEditClick = { exercise ->
                val action =
                    ExerciseFragmentDirections.actionNavigationExerciseToNavigationEditExercise(
                        exercise
                    )
                view?.findNavController()?.navigate(action)
            },
            onDoneChecked = { exercise, isChecked ->
                exercise.done = isChecked
                exerciseViewModel.updateExercise(exercise)
            }
        )

        binding.exercisesRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = exerciseAdapter
        }

        val itemTouchHelper = ItemTouchHelper(exerciseAdapter.itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.exercisesRecyclerView)

        exerciseViewModel.getExercisesByRoutineId(currentRoutine.id)
            .observe(viewLifecycleOwner) { exercises ->
                val sortedExercises = exercises.sortedBy { it.position }
                exerciseAdapter.differ.submitList(sortedExercises)
                updateUi(sortedExercises.isNotEmpty())
            }
    }

    private fun updateUi(hasExercises: Boolean) {
        if (hasExercises) {
            binding.pageImage.visibility = View.GONE
            binding.exercisesRecyclerView.visibility = View.VISIBLE
        } else {
            binding.pageImage.visibility = View.VISIBLE
            binding.exercisesRecyclerView.visibility = View.GONE
        }
        binding.tvExercises.text = getString(R.string.exercises_title, currentRoutine.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}