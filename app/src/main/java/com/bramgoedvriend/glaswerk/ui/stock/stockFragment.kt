package com.bramgoedvriend.glaswerk.ui.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentStockBinding

class stockFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentStockBinding>(
            inflater, R.layout.fragment_stock, container, false)
        (activity as MainActivity).setActionBarTitle(getString(R.string.title_stock))

        return binding.root
    }
}
