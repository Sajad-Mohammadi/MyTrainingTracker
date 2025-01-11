package com.sajad.mytrainingtracker.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.adapter.RoutineAdapter
import com.sajad.mytrainingtracker.data.entities.TrainingProgram
import com.sajad.mytrainingtracker.databinding.FragmentRoutineBinding
import com.sajad.mytrainingtracker.viewModel.RoutineViewModel

class RoutineFragment : Fragment() {

    private var _binding: FragmentRoutineBinding? = null
    private val binding get() = _binding!!

    private lateinit var routineViewModel: RoutineViewModel
    private lateinit var routineAdapter: RoutineAdapter
    private lateinit var currentTrainingProgram: TrainingProgram

    private val args: RoutineFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routineViewModel = ViewModelProvider(requireActivity())[RoutineViewModel::class.java]
        currentTrainingProgram = args.trainingProgram

        attachUiListeners()
        setupRecyclerView()
    }

    private fun attachUiListeners() {
        binding.btnStartFresh.setOnClickListener {
            val action =
                RoutineFragmentDirections.actionNavigationRoutineToNavigationAddRoutine(currentTrainingProgram.id)
            view?.findNavController()?.navigate(action)
        }
    }

    private fun updateUi(hasRoutines: Boolean) {
        if (hasRoutines) {
            binding.pageImage.visibility = View.GONE
            binding.routineRecyclerView.visibility = View.VISIBLE

            binding.tvRoutines.text = currentTrainingProgram.name
        } else {
            binding.pageImage.visibility = View.VISIBLE
            binding.routineRecyclerView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        routineAdapter = RoutineAdapter(
            onBtnEditClick = { routine ->
                val action =
                    RoutineFragmentDirections.actionNavigationRoutineToNavigationEditRoutine(routine)
                view?.findNavController()?.navigate(action)
            },
            onBtnViewRoutineClick = { routine ->
                val action =
                    RoutineFragmentDirections.actionNavigationRoutineToNavigationExercise(routine)
                view?.findNavController()?.navigate(action)
            }
        )

        binding.routineRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = routineAdapter
        }

        routineViewModel.getRoutinesByTrainingProgramId(currentTrainingProgram.id)
            .observe(viewLifecycleOwner) { routines ->
                if (routines.isNotEmpty()) {
                    routineAdapter.differ.submitList(routines)
                    updateUi(true)
                } else {
                    routineAdapter.differ.submitList(emptyList())
                    updateUi(false)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}