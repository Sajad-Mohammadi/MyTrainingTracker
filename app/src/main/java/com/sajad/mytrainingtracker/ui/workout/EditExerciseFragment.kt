package com.sajad.mytrainingtracker.ui.workout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.data.entities.Exercise
import com.sajad.mytrainingtracker.databinding.FragmentEditExerciseBinding
import com.sajad.mytrainingtracker.viewModel.ExerciseViewModel

class EditExerciseFragment : Fragment() {

    private var _binding: FragmentEditExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var editView: View
    private lateinit var currentExercise: Exercise

    private val args: EditExerciseFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseViewModel = ViewModelProvider(requireActivity())[ExerciseViewModel::class.java]

        currentExercise = args.exercise
        editView = view

        attacheUiListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun attacheUiListeners() {
        binding.etExerciseName.setText(currentExercise.name)
        binding.etSet.setText(currentExercise.sets.toString())
        binding.etKg.setText(currentExercise.weight.toString())
        binding.etReps.setText(currentExercise.reps.toString())

        binding.btnSave.setOnClickListener {
            updateExercise()
        }
    }

    private fun updateExercise() {
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
            val sets : Int
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
        }else set = "0"

        if (kg.isNotEmpty()) {
            val kgs : Double
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
        } else kg = "0.0"

        if (rep.isNotEmpty()) {
            val reps : Int
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
        } else rep = "0"

        exerciseViewModel.updateExercise(
            Exercise(
                id = currentExercise.id,
                name = exerciseName,
                sets = set.toInt(),
                weight = kg.toDouble(),
                reps = rep.toInt(),
                position = currentExercise.position,
                done = false,
                routineId = currentExercise.routineId
            )
        )
        Toast.makeText(requireContext(),
            getString(R.string.exercise_updated_successfully), Toast.LENGTH_SHORT).show()
        editView.findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}