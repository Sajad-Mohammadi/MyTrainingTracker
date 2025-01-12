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
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.databinding.FragmentAddTrainingProgramBinding
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel

class AddTrainingProgramFragment : Fragment() {
    private var _binding: FragmentAddTrainingProgramBinding? = null
    private val binding get() = _binding!!

    private lateinit var trainingProgramViewModel: TrainingProgramViewModel
    private lateinit var addTrainingProgramView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTrainingProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trainingProgramViewModel =
            ViewModelProvider(requireActivity())[TrainingProgramViewModel::class.java]
        addTrainingProgramView = view

        val userId = arguments?.getInt("userId") ?: 0
        attachUiListeners(userId)
    }

    private fun attachUiListeners(userId: Int) {
        binding.btnSave.setOnClickListener {
            saveTrainingProgram(userId)
        }
    }

    private fun saveTrainingProgram(userId: Int) {
        val programName = binding.etTrainingProgramName.text.toString().trim()
        val programDescription = binding.etDescription.text.toString().trim()
        val programDuration = binding.etDuration.text.toString().trim()

        if (programName.isEmpty()) {
            binding.etTrainingProgramName.error = getString(R.string.training_program_name_is_required)
            binding.etTrainingProgramName.requestFocus()
            return
        }

        if (programDescription.isEmpty()) {
            binding.etDescription.error = getString(R.string.description_is_required)
            binding.etDescription.requestFocus()
            return
        }

        if (programDuration.isEmpty()) {
            binding.etDuration.error = getString(R.string.duration_is_required)
            binding.etDuration.requestFocus()
            return
        }

        val duration: Int
        try {
            duration = programDuration.toInt()
        } catch (e: NumberFormatException) {
            binding.etDuration.error = getString(R.string.please_enter_a_valid_duration)
            binding.etDuration.requestFocus()
            return
        }

        if (duration < 1 || duration > 7) {
            binding.etDuration.error = getString(R.string.duration_must_be_between_1_and_7_days)
            binding.etDuration.requestFocus()
            return
        }

        trainingProgramViewModel.updateRecent(0, userId)
        trainingProgramViewModel.insertTrainingProgram(
            TrainingProgram(
                0,
                programName,
                programDescription,
                duration,
                recent = true,
                userId
            )
        )
        Toast.makeText(addTrainingProgramView.context,
            getString(R.string.training_program_saved_successfully), Toast.LENGTH_SHORT).show()
        addTrainingProgramView.findNavController().popBackStack()
        addTrainingProgramView.findNavController().navigate(R.id.navigation_workout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}