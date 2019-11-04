package com.bramgoedvriend.glaswerk.damage.damage_student


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentDamageBinding
import com.bramgoedvriend.glaswerk.databinding.FragmentDamageStudentsBinding

class DamageStudentFragment : Fragment() {

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

        return binding.root
    }

}