package com.bramgoedvriend.glaswerk.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.adapters.StudentListener
import com.bramgoedvriend.glaswerk.databinding.FragmentStudentsBinding
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.network.StudentNavigate
import com.bramgoedvriend.glaswerk.orders.StudentAdapter
import com.bramgoedvriend.glaswerk.viewmodels.student.StudentViewModel
import com.bramgoedvriend.glaswerk.viewmodels.student.StudentViewModelFactory

class StudentsFragment : Fragment() {

    private lateinit var binding: FragmentStudentsBinding
    private lateinit var adapter: StudentAdapter
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> sharedPrefChange() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentStudentsBinding>(
            inflater, R.layout.fragment_students, container, false
        )
        binding.loadingOverlay.visibility = View.VISIBLE

        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            StudentViewModelFactory(application)
        studentViewModel = ViewModelProvider(this, viewModelFactory).get(StudentViewModel::class.java)

        adapter = StudentAdapter(StudentListener { student ->
            studentViewModel.onStudentClicked(
                student.student
            )
        })
        binding.studentList.adapter = adapter

        studentViewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { student ->
            student?.let {
                findNavController().navigate(
                    StudentsFragmentDirections.actionStudentsFragmentToStudentDetailFragment(
                        StudentNavigate(it.leerlingId, it.klasId, it.voornaam, it.achternaam)
                    )
                )
                studentViewModel.onDetailNavigated()
            }
        })

        studentViewModel.status.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it!!) {
                    ApiStatus.OFFLINE -> {
                        binding.offlineOverlay.visibility = View.VISIBLE
                        binding.fab.visibility = View.GONE
                    }
                    ApiStatus.ERROR ->
                        binding.errorOverlay.visibility = View.VISIBLE
                    else -> {}
                }
            }
        })

        sharedPreferences = application.getSharedPreferences(resources.getString(R.string.sharedPreferences), AppCompatActivity.MODE_PRIVATE)

        studentClassObserver()

        binding.klas.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("KlassDialog")
            val dialogFragment =
                BottomDialogFragment(Klas::class.java, true)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        binding.klas.setOnLongClickListener {
            Toast.makeText(context!!, "Long clicked - remove class", Toast.LENGTH_SHORT).show()
            true
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                StudentsFragmentDirections.actionStudentsFragmentToStudentDetailFragment(
                    null
                )
            )
        }

        return binding.root
    }

    private fun studentClassObserver() {
        if (studentViewModel.klas.hasObservers()) {
            studentViewModel.klas.removeObservers(viewLifecycleOwner)
        }
        if (studentViewModel.students.hasObservers()) {
            studentViewModel.students.removeObservers(viewLifecycleOwner)
        }
        studentViewModel.klas.observe(viewLifecycleOwner, Observer {
            binding.className.text = it.naam
        })
        studentViewModel.students.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                binding.loadingOverlay.visibility = View.GONE
            }
        })
    }

    private fun sharedPrefChange() {
        studentViewModel.updateClass()
        studentClassObserver()
    }

    override fun onResume() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
        super.onResume()
    }

    override fun onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)
        super.onPause()
    }
}
