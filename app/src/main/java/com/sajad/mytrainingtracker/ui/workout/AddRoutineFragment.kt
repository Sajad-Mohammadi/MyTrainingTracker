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
import com.sajad.mytrainingtracker.databinding.FragmentAddRoutineBinding
import com.sajad.mytrainingtracker.data.entities.Routine
import com.sajad.mytrainingtracker.viewModel.RoutineViewModel

class AddRoutineFragment : Fragment() {

    private var _binding: FragmentAddRoutineBinding? = null
    private val binding get() = _binding!!

    private lateinit var routineViewModel: RoutineViewModel
    private var trainingProgramId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trainingProgramId = it.getInt("trainingProgramId", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routineViewModel = ViewModelProvider(requireActivity())[RoutineViewModel::class.java]

        attachUiListeners()
    }

    private fun attachUiListeners() {
        binding.btnSave.setOnClickListener {
            val routineName = binding.etRoutineName.text.toString().trim()
            val routineDescription = binding.etNote.text.toString().trim()

            if (validateInput(routineName, routineDescription)) {
                val newRoutine = Routine(
                    0,
                    name = routineName,
                    note = routineDescription,
                    trainingProgramId = trainingProgramId
                )

                routineViewModel.insertRoutine(newRoutine)

                Toast.makeText(requireContext(), "Routine added successfully", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack()
            }
        }
    }

    private fun validateInput(name: String, description: String): Boolean {
        return when {
            name.isEmpty() -> {
                binding.etRoutineName.error = "Please enter a name"
                binding.etRoutineName.requestFocus()
                false
            }
            description.isEmpty() -> {
                binding.etNote.error = "Please enter a description"
                binding.etNote.requestFocus()
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}