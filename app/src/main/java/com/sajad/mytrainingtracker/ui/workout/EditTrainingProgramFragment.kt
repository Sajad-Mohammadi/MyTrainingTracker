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
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.databinding.FragmentEditTrainingProgramBinding
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel

class EditTrainingProgramFragment : Fragment() {
    private var _binding: FragmentEditTrainingProgramBinding? = null
    private val binding get() = _binding!!

    private lateinit var trainingProgramViewModel: TrainingProgramViewModel
    private lateinit var editView: View
    private lateinit var currentTrainingProgram: TrainingProgram

    private val args: EditTrainingProgramFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTrainingProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trainingProgramViewModel = ViewModelProvider(requireActivity())[TrainingProgramViewModel::class.java]

        currentTrainingProgram = args.trainingProgram
        editView = view

        attacheUiListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun attacheUiListeners() {
        binding.etTrainingProgramName.setText(currentTrainingProgram.name)
        binding.etDescription.setText(currentTrainingProgram.description)
        binding.etDuration.setText(currentTrainingProgram.duration.toString())

        binding.btnSave.setOnClickListener {
            updateTrainingProgram()
        }

        binding.btnDelete.setOnClickListener {
            trainingProgramViewModel.deleteTrainingProgramById(currentTrainingProgram.id)
            Toast.makeText(requireContext(),
                getString(R.string.training_program_deleted_successfully), Toast.LENGTH_SHORT).show()
            editView.findNavController().navigateUp()
        }
    }

    private fun updateTrainingProgram() {
        val trainingProgramId = currentTrainingProgram.id
        val programName = binding.etTrainingProgramName.text.toString()
        val programDescription = binding.etDescription.text.toString()
        val programDuration = binding.etDuration.text.toString().trim()
        val userId = currentTrainingProgram.userId

        if (programName.isEmpty()) {
            binding.etTrainingProgramName.error =
                getString(R.string.training_program_name_is_required)
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

        trainingProgramViewModel.updateRecent(currentTrainingProgram.id, userId)

        trainingProgramViewModel.updateTrainingProgram(
            TrainingProgram(
                trainingProgramId,
                programName,
                programDescription,
                duration,
                recent = true,
                userId
            )
        )

        Toast.makeText(requireContext(),
            getString(R.string.training_program_updated), Toast.LENGTH_SHORT).show()
        editView.findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}