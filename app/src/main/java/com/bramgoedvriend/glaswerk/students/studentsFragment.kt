package com.bramgoedvriend.glaswerk.students


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentStudentsBinding
import com.bramgoedvriend.glaswerk.orders.*

class studentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentStudentsBinding>(
            inflater, R.layout.fragment_students, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = StudentViewModelFactory(application)
        val studentViewModel = ViewModelProviders.of(this, viewModelFactory).get(StudentViewModel::class.java)

        val adapter = StudentAdapter()
        binding.studentList.adapter = adapter

        studentViewModel.students.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}
