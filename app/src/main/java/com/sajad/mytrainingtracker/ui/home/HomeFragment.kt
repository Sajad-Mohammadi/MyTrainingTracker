package com.sajad.mytrainingtracker.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sajad.mytrainingtracker.R
import com.sajad.mytrainingtracker.adapter.TrainingProgramAdapter
import com.sajad.mytrainingtracker.adapter.UserAdapter
import com.sajad.mytrainingtracker.databinding.FragmentHomeBinding
import com.sajad.mytrainingtracker.viewModel.TrainingProgramViewModel
import com.sajad.mytrainingtracker.viewModel.UserViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var trainingProgramViewModel: TrainingProgramViewModel
    private lateinit var trainingProgramAdapter: TrainingProgramAdapter

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        binding.btnAddProgram.setOnClickListener {
            TODO()
        }
    }

    private fun setupRecyclerView() {
        trainingProgramAdapter = TrainingProgramAdapter(
            onBtnEditClick = {
                TODO()
            },
            onBtnGoToProgramClick = {
                TODO()
            }
        )

        binding.programRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = trainingProgramAdapter
        }

        activity?.let {
            trainingProgramViewModel.getTrainingProgramsByUserId(getCurrentUser())
                .observe(viewLifecycleOwner) { programs ->
                    if (programs != null) {
                        trainingProgramAdapter.differ.submitList(programs)
                    } else
                        trainingProgramAdapter.differ.submitList(emptyList())
                }
        }
    }

    private fun getCurrentUser(): Int {
        var id: Int = 0
        activity?.let {
            userViewModel.getLoggedInUser().observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    id = user.id
                }
            }
        }
        return id
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}