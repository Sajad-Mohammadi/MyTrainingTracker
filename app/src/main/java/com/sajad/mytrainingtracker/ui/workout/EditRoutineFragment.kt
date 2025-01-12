package com.sajad.mytrainingtracker.ui.workout

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
import com.sajad.mytrainingtracker.data.entities.Routine
import com.sajad.mytrainingtracker.databinding.FragmentEditRoutineBinding
import com.sajad.mytrainingtracker.viewModel.RoutineViewModel

class EditRoutineFragment : Fragment() {
    private var _binding: FragmentEditRoutineBinding? = null
    private val binding get() = _binding!!

    private lateinit var routineViewModel: RoutineViewModel
    private lateinit var editView: View
    private lateinit var currentRoutine: Routine

    private val args: EditRoutineFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routineViewModel = ViewModelProvider(requireActivity())[RoutineViewModel::class.java]

        currentRoutine = args.routine
        editView = view

        attacheUiListeners()
    }

    private fun attacheUiListeners() {
        binding.etRoutineName.setText(currentRoutine.name)
        binding.etNote.setText(currentRoutine.note)

        binding.btnSave.setOnClickListener {
            updateRoutine()
        }

        binding.btnDelete.setOnClickListener {
            routineViewModel.deleteRoutine(currentRoutine)
            Toast.makeText(requireContext(), getString(R.string.routine_deleted), Toast.LENGTH_SHORT).show()
            editView.findNavController().navigateUp()
        }
    }

    private fun updateRoutine() {
        val name = binding.etRoutineName.text.toString()
        val note = binding.etNote.text.toString()

        if (name.isEmpty()) {
            binding.etRoutineName.error = getString(R.string.name_is_required)
            return
        }

        routineViewModel.updateRoutine(
            Routine(
                id = currentRoutine.id,
                name = name,
                note = note,
                trainingProgramId = currentRoutine.trainingProgramId
            )
        )
        Toast.makeText(requireContext(),
            getString(R.string.routine_updated_successfully), Toast.LENGTH_SHORT).show()
        editView.findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}