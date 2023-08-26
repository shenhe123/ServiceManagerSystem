package com.jssg.servicemanagersystem.ui.accountcenter

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jssg.servicemanagersystem.R

class AccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    companion object {
        fun newInstance(): AccountFragment {
            val args = Bundle()

            val fragment = AccountFragment()
            fragment.arguments = args
            return fragment
        }
    }
}