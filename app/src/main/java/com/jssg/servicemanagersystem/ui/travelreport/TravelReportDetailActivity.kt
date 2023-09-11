package com.jssg.servicemanagersystem.ui.travelreport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityTravelReportDetailBinding
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo

class TravelReportDetailActivity : BaseActivity() {
    private var inputData: TravelReportInfo? = null
    private lateinit var binding: ActivityTravelReportDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTravelReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addListener()

        inputData = intent?.getParcelableExtra("inputData")
        inputData?.let {
            binding.tvDept.text = it.dept
            binding.tvApplyName.text = it.applyName
            binding.tvPartner.text = it.partner
            binding.tvCustomer.text = it.customer
            binding.tvProductCode.text = it.productCode
            binding.tvProjectCode.text = it.projectCode
            binding.tvPlaceFrom.text = it.placeFrom
            binding.tvPlaceTo.text = it.placeTo
            binding.tvStartDate.text = it.startDate
            binding.tvEndDate.text = it.endDate
            binding.tvAddress.text = it.address

            binding.etPurpose.setText(it.purpose)
            binding.etMainTask.setText(it.mainTask)
            binding.etExpectedResult.setText(it.expectedResult)
            binding.etSchedule.setText(it.schedule)
        }
    }

    private fun addListener() {
        binding.toolBar.setNavigationOnClickListener { finish() }
    }

    companion object {
        fun goActivity(context: Context, travelReportInfo: TravelReportInfo) {
            context.startActivity(Intent(context, TravelReportDetailActivity::class.java).apply {
                putExtra("inputData", travelReportInfo)
            })
        }
    }


}