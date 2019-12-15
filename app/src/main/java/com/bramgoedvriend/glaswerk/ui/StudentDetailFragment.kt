package com.bramgoedvriend.glaswerk.ui

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentStudentsDetailBinding
import com.bramgoedvriend.glaswerk.viewmodels.student.StudentDetailViewModel
import com.bramgoedvriend.glaswerk.viewmodels.student.StudentDetailViewModelFactory

class StudentDetailFragment : Fragment() {
    private lateinit var binding: FragmentStudentsDetailBinding
    private lateinit var studentDetailViewModel: StudentDetailViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_students_detail, container, false
        )

        val student = StudentDetailFragmentArgs.fromBundle(
            arguments!!
        ).student
        if (student != null) {
            binding.title.text = resources.getString(R.string.EditStudent)
            binding.removeButton.visibility = View.VISIBLE
            binding.firstnameText.setText(student.firstName)
            binding.lastnameText.setText(student.lastName)
        } else {
            binding.title.text = resources.getString(R.string.AddStudent)
        }
        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            StudentDetailViewModelFactory(
                application
            )
        studentDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(StudentDetailViewModel::class.java)

        sharedPreferences = application.getSharedPreferences(resources.getString(R.string.sharedPreferences), AppCompatActivity.MODE_PRIVATE)

        if(studentDetailViewModel.offline){
            binding.saveButton.visibility = View.GONE
            binding.removeButton.visibility = View.GONE
            binding.firstnameText.isEnabled = false
            binding.lastnameText.isEnabled = false
        }

        binding.saveButton.setOnClickListener {
            studentDetailViewModel.addStudent(
                student,
                binding.firstnameText.text.toString(),
                binding.lastnameText.text.toString()
            )
            findNavController().navigate(StudentDetailFragmentDirections.actionStudentDetailFragmentToStudentsFragment())
        }

        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    studentDetailViewModel.remove(student)
                    dialog.dismiss()
                    findNavController().navigate(StudentDetailFragmentDirections.actionStudentDetailFragmentToStudentsFragment())
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
            }
        }

        binding.removeButton.setOnClickListener {
            if (student != null) {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("Ben je zeker dat je " + student.firstName + " " + student.lastName + " wilt verwijderen?")
                    .setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nee", dialogClickListener)
                    .show()
            }
        }
        return binding.root
    }
}
