package com.jssg.servicemanagersystem.ui.workorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderBinding

class WorkOrderFragment : Fragment() {

    private lateinit var adapter: WorkOrderAdapter
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]

        binding = FragmentWorkOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkOrderAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            WorkOrderActivity.goActivity(requireActivity())
        }

        binding.fbtnAddNew.setOnClickListener {
            WorkOrderAddNewActivity.goActivity(requireActivity())
        }

        workOrderViewModel.text.observe(viewLifecycleOwner) {
            adapter.setNewInstance(mutableListOf("1", "1","1","1","1","1","1","1","1","1","1"))
        }
    }

    companion object {
        fun newInstance(): WorkOrderFragment {
            val args = Bundle()

            val fragment = WorkOrderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}