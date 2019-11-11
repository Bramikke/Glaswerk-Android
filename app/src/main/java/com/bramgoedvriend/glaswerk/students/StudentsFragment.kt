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
import com.bramgoedvriend.glaswerk.bottomDialog.BottomDialogFragment
import com.bramgoedvriend.glaswerk.databinding.FragmentStudentsBinding
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.orders.*

class StudentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentStudentsBinding>(
            inflater, R.layout.fragment_students, container, false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = StudentViewModelFactory(application)
        val studentViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StudentViewModel::class.java)

        val adapter = StudentAdapter()
        binding.studentList.adapter = adapter

        studentViewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ApiStatus.LOADING -> {
                        binding.studentList.visibility = View.INVISIBLE
                        binding.loadingOverlay.visibility = View.VISIBLE
                    }
                    ApiStatus.ERROR -> {
                        binding.loadingOverlay.visibility = View.GONE
                        binding.errorOverlay.visibility = View.VISIBLE
                    }
                    ApiStatus.DONE -> {
                        binding.loadingOverlay.visibility = View.GONE
                        binding.studentList.visibility = View.VISIBLE
                    }
                }
            }
        })

        studentViewModel.students.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        /*binding.klas.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.addToBackStack("KlassDialog")
            val dialogFragment = BottomDialogFragment(Klas::class.java)
            dialogFragment.show(fragmentTransaction, "dialog")
        }*/ //TODO

        return binding.root
    }


}
