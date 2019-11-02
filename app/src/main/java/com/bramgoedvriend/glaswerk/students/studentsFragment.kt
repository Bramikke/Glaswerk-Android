package com.bramgoedvriend.glaswerk.ui.students


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentStudentsBinding

class studentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentStudentsBinding>(
            inflater, R.layout.fragment_students, container, false)
        (activity as MainActivity).setActionBarTitle(getString(R.string.title_students))

        return binding.root
    }


}
