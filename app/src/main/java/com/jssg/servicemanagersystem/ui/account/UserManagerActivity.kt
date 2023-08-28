package com.jssg.servicemanagersystem.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityUserManagerBinding
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment

class UserManagerActivity : BaseActivity() {
    private lateinit var adapter: UserManagerAdapter
    private lateinit var binding: ActivityUserManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserManagerAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setOnItemChildClickListener { _, v, position ->
            when(v.id) {
                R.id.card_layout -> UserManagerDetailActivity.goActivity(this)

                R.id.mbt_permission -> PermissionDialogFragment.newInstance().show(supportFragmentManager, "permission_dialog")

                R.id.mbt_delete -> SingleBtnDialogFragment.newInstance("删除", "确定要删除此用户吗？").show(supportFragmentManager, "delete_user_dialog")
            }


        }

        val accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.text.observe(this) {
            adapter.setNewInstance(mutableListOf("1", "1","1","1","1","1","1","1","1","1","1"))
        }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, UserManagerActivity::class.java))
        }
    }
}