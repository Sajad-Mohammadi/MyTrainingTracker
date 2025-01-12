package com.sajad.mytrainingtracker.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.Exercise
import com.sajad.mytrainingtracker.databinding.FragmentAddExerciseBinding
import com.sajad.mytrainingtracker.viewModel.ExerciseViewModel

class AddExerciseFragment : Fragment() {

    private var _binding: FragmentAddExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseViewModel: ExerciseViewModel
    private var routineId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routineId = it.getInt("routineId", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseViewModel = ViewModelProvider(requireActivity())[ExerciseViewModel::class.java]

        attachUiListeners()
    }

    private fun attachUiListeners() {
        binding.btnSave.setOnClickListener {
            saveExercise()
        }
    }

    private fun saveExercise() {
        val exerciseName = binding.etExerciseName.text.toString().trim()
        var set = binding.etSet.text.toString().trim()
        var kg = binding.etKg.text.toString().trim()
        var rep = binding.etReps.text.toString().trim()

        if (exerciseName.isEmpty()) {
            binding.etExerciseName.error = getString(R.string.exercise_name_is_required)
            binding.etExerciseName.requestFocus()
            return
        }

        if (set.isNotEmpty()) {
            val sets: Int
            try {
                sets = set.toInt()
            } catch (e: NumberFormatException) {
                binding.etSet.error = getString(R.string.invalid_number)
                binding.etSet.requestFocus()
                return
            }

            if (sets < 0 || sets > 20) {
                binding.etSet.error = getString(R.string.sets_must_be_between_0_and_20)
                binding.etSet.requestFocus()
                return
            }
            set = sets.toString()
        }

        if (kg.isNotEmpty()) {
            val kgs: Double
            try {
                kgs = kg.toDouble()
            } catch (e: NumberFormatException) {
                binding.etKg.error = getString(R.string.invalid_number)
                binding.etKg.requestFocus()
                return
            }

            if (kgs < 0 || kgs > 1000) {
                binding.etKg.error = getString(R.string.kg_must_be_between_0_and_1000)
                binding.etKg.requestFocus()
                return
            }
            kg = kgs.toString()
        }

        if (rep.isNotEmpty()) {
            val reps: Int
            try {
                reps = rep.toInt()
            } catch (e: NumberFormatException) {
                binding.etReps.error = getString(R.string.invalid_number)
                binding.etReps.requestFocus()
                return
            }

            if (reps < 0 || reps > 100) {
                binding.etReps.error = getString(R.string.reps_must_be_between_0_and_100)
                binding.etReps.requestFocus()
                return
            }
            rep = reps.toString()
        }


        exerciseViewModel.getMaxPositionAsync(routineId) { lastPosition ->
            val newExercise = Exercise(
                id = 0,
                name = exerciseName,
                sets = set.toIntOrNull() ?: 0,
                reps = rep.toIntOrNull() ?: 0,
                weight = kg.toDoubleOrNull() ?: 0.0,
                position = lastPosition + 1,
                done = false,
                routineId = routineId
            )
            exerciseViewModel.insertExercise(newExercise)
            Toast.makeText(requireContext(),
                getString(R.string.exercise_added_successfully), Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
    }
}