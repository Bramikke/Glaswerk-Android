package com.bramgoedvriend.glaswerk.damage


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
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentDamageBinding

class DamageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentDamageBinding>(
            inflater, R.layout.fragment_damage, container, false
        )
        (activity as MainActivity).setActionBarTitle(getString(R.string.title_damage))

        val application = requireNotNull(this.activity).application
        val viewModelFactory = DamageViewModelFactory(application)
        val damageViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(DamageViewModel::class.java)

        val adapter = DamageAdapter(ItemListener {
            itemId -> Toast.makeText(context, "${itemId}", Toast.LENGTH_LONG).show()
        })
        binding.itemList.adapter = adapter

        damageViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lokaal.setOnClickListener {
            Log.i("loglog", "lokaalClick")
        }

        binding.klas.setOnClickListener {
            Log.i("loglog", "klasClick")
        }

        return binding.root
    }

}