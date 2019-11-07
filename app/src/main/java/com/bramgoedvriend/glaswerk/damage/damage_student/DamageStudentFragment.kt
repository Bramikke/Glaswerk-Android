package com.bramgoedvriend.glaswerk.damage.damage_student


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.bottomDialog.BottomDialogFragment
import com.bramgoedvriend.glaswerk.databinding.FragmentDamageStudentsBinding
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Student


class DamageStudentFragment : Fragment() {

    private lateinit var damageStudentViewModel : DamageStudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDamageStudentsBinding>(
            inflater, R.layout.fragment_damage_students, container, false
        )

        val application = requireNotNull(this.activity).application
        val args = DamageStudentFragmentArgs.fromBundle(arguments!!)
        binding.itemName.text = args.itemName

        val viewModelFactory = DamageStudentViewModelFactory(application, args)
        damageStudentViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DamageStudentViewModel::class.java)

        val adapter = DamageStudentAdapter(StudentListener { student ->
            damageStudentViewModel.onStudentClicked(student)
        })

        binding.studentList.adapter = adapter

        damageStudentViewModel.status.observe(viewLifecycleOwner, Observer {
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

        damageStudentViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        damageStudentViewModel.selectStudent.observe(this, Observer { student ->
            student?.let {
                showDialog(it)
                damageStudentViewModel.onNavigated()
            }
        })

        binding.classLayout.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.addToBackStack("KlassDialog")
            val dialogFragment = BottomDialogFragment(Klas::class.java)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        return binding.root
    }

    private fun showDialog(student: Student) {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.popup_on_purpose)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<LinearLayout>(R.id.on_purpose_overlay).setOnClickListener{
            Toast.makeText(activity, "Geannuleerd", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.findViewById<CardView>(R.id.card_on_purpose).setOnClickListener {
            val msg = damageStudentViewModel.studentBroke(student, 1)
            dialog.dismiss()
            findNavController().navigate(
                DamageStudentFragmentDirections
                    .actionDamageStudentFragmentToDamageFragment()
            )
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }

        dialog.findViewById<CardView>(R.id.card_not_on_purpose).setOnClickListener {
            val msg = damageStudentViewModel.studentBroke(student, 0)
            dialog.dismiss()
            findNavController().navigate(
                DamageStudentFragmentDirections
                    .actionDamageStudentFragmentToDamageFragment()
            )
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }

        dialog.show()
    }
}