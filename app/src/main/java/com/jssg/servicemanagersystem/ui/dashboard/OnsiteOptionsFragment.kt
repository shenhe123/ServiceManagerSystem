package com.jssg.servicemanagersystem.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentOnsiteOptionsBinding
import com.jssg.servicemanagersystem.ui.onsite.OnsiteOptionsAdapter
import com.jssg.servicemanagersystem.ui.onsite.OrderHandleActivity

class OnsiteOptionsFragment : BaseFragment() {

    private lateinit var binding: FragmentOnsiteOptionsBinding
    private lateinit var adapter: OnsiteOptionsAdapter
    private lateinit var onsiteOptionsViewModel: OnsiteOptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onsiteOptionsViewModel = ViewModelProvider(this)[OnsiteOptionsViewModel::class.java]

        binding = FragmentOnsiteOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = OnsiteOptionsAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            OrderHandleActivity.goActivity(requireContext())
        }

        onsiteOptionsViewModel.text.observe(viewLifecycleOwner) {
            adapter.setNewInstance(mutableListOf("1", "1","1","1","1","1","1","1","1","1","1"))
        }
    }

    companion object {
        fun newInstance(): OnsiteOptionsFragment {
            val args = Bundle()

            val fragment = OnsiteOptionsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}