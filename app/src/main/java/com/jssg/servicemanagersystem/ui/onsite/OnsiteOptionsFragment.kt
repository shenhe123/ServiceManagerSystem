package com.jssg.servicemanagersystem.ui.onsite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentOnsiteOptionsBinding

class OnsiteOptionsFragment : BaseFragment() {

    private var closeCaseSwitch: Boolean = false
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
        adapter = OnsiteOptionsAdapter(closeCaseSwitch)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            OrderHandleActivity.goActivity(requireContext())
        }

        binding.tvCloseCase.setOnClickListener {
            closeCaseSwitch = !closeCaseSwitch
            adapter.updateCloseSwitch(closeCaseSwitch)

            binding.tvCloseCase.text = if (closeCaseSwitch) "提交" else "批量结案"
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