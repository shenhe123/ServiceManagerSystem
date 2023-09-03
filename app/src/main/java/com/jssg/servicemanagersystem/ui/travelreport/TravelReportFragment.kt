package com.jssg.servicemanagersystem.ui.travelreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentTravelReportBinding

class TravelReportFragment : BaseFragment() {

    private var _binding: FragmentTravelReportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val travelReportViewModel =
            ViewModelProvider(this).get(TravelReportViewModel::class.java)

        _binding = FragmentTravelReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        travelReportViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() =
            TravelReportFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable("inputData", inputData)
                }
            }
    }
}