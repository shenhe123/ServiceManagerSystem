package com.jssg.servicemanagersystem.ui.onsite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActOnsiteOptionsLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class OnsiteOptionsActivity: BaseActivity() {

    private lateinit var adapter: OnsiteOptionsAdapter
    private lateinit var binding: ActOnsiteOptionsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActOnsiteOptionsLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OnsiteOptionsAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->

        }

        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenResumed {
            adapter.setNewInstance(mutableListOf("1", "1","1","1","1","1","1","1","1","1","1"))
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, OnsiteOptionsActivity::class.java))
        }
    }
}