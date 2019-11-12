package com.bramgoedvriend.glaswerk.damage.damage_student


import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var binding: FragmentDamageStudentsBinding
    private lateinit var damageStudentViewModel: DamageStudentViewModel
    private lateinit var adapter: DamageStudentAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private val changeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> sharedPrefChange() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_damage_students, container, false
        )

        val application = requireNotNull(this.activity).application
        val args = DamageStudentFragmentArgs.fromBundle(arguments!!)
        binding.itemName.text = args.item.itemName

        val viewModelFactory = DamageStudentViewModelFactory(application, args.item)
        damageStudentViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DamageStudentViewModel::class.java)
        adapter = DamageStudentAdapter(StudentListener { student -> showDialog(student) })
        sharedPreferences = application.getSharedPreferences(
            resources.getString(R.string.sharedPreferences),
            AppCompatActivity.MODE_PRIVATE
        )

        binding.studentList.adapter = adapter

        damageStudentViewModel.status.observe(viewLifecycleOwner, Observer {
            if (it == ApiStatus.OFFLINE) {
                binding.offlineOverlay.visibility = View.VISIBLE
            }
        })

        studentClassObserver()

        binding.classLayout.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.addToBackStack("KlassDialog")
            val dialogFragment = BottomDialogFragment(Klas::class.java, false)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        return binding.root
    }

    private fun studentClassObserver() {
        if (damageStudentViewModel.klas.hasObservers()) {
            damageStudentViewModel.klas.removeObservers(viewLifecycleOwner)
        }
        if (damageStudentViewModel.students.hasObservers()) {
            damageStudentViewModel.students.removeObservers(viewLifecycleOwner)
        }

        damageStudentViewModel.klas.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.className.text = it.name
            }
        })

        damageStudentViewModel.students.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun showDialog(student: Student) {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.popup_on_purpose)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<LinearLayout>(R.id.on_purpose_overlay).setOnClickListener {
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

    private fun sharedPrefChange() {
        damageStudentViewModel.updateClass()
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