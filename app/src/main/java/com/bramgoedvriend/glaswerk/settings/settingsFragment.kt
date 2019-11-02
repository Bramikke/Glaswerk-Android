package com.bramgoedvriend.glaswerk.settings


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentSettingsBinding
import com.bramgoedvriend.glaswerk.login.LoginActivity

class settingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(
            inflater, R.layout.fragment_settings, container, false)
        (activity as MainActivity).setActionBarTitle(getString(R.string.title_settings))

        val username = binding.username
        val sp = (activity as MainActivity).getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        username.text = sp.getString("displayName","")

        binding.logout.setOnClickListener{_:View ->
            (activity as MainActivity).logout()

            val loginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(loginActivity)

            (activity as MainActivity).finish()
        }

        return binding.root
    }
}
