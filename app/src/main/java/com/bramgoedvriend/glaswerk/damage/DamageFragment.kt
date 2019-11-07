package com.bramgoedvriend.glaswerk.damage


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentDamageBinding
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.bottomDialog.BottomDialogFragment
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal


class DamageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDamageBinding>(
            inflater, R.layout.fragment_damage, container, false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = DamageViewModelFactory(application)
        val damageViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DamageViewModel::class.java)

        val adapter = DamageAdapter(ItemListener { item -> damageViewModel.onItemClicked(item) })
        binding.itemList.adapter = adapter

        damageViewModel.navigateToLeerlingen.observe(this, Observer { item ->
            item?.let {
                findNavController().navigate(
                    DamageFragmentDirections
                        .actionDamageFragmentToDamageStudentFragment(it.itemid, it.naam, it.aantal)
                )
                damageViewModel.onLeerlingenNavigated()
            }
        })

        damageViewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    ApiStatus.LOADING -> {
                        binding.itemList.visibility = View.INVISIBLE
                        binding.loadingOverlay.visibility = View.VISIBLE
                    }
                    ApiStatus.ERROR -> {
                        binding.loadingOverlay.visibility = View.GONE
                        binding.errorOverlay.visibility = View.VISIBLE
                    }
                    ApiStatus.DONE -> {
                        binding.loadingOverlay.visibility = View.GONE
                        binding.itemList.visibility = View.VISIBLE
                    }
                }
            }
        })

        damageViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lokaal.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.addToBackStack("LokaalDialog")
            val dialogFragment = BottomDialogFragment(Lokaal::class.java)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        binding.klas.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.addToBackStack("KlassDialog")
            val dialogFragment = BottomDialogFragment(Klas::class.java)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        return binding.root
    }
}