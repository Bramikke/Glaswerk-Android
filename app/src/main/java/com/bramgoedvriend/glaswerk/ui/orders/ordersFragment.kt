package com.bramgoedvriend.glaswerk.ui.orders


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentOrdersBinding

class ordersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentOrdersBinding>(
            inflater, R.layout.fragment_orders, container, false)
        (activity as MainActivity).setActionBarTitle(getString(R.string.title_orders))

        return binding.root
    }


}
