package com.bramgoedvriend.glaswerk.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.adapters.DamageAdapter
import com.bramgoedvriend.glaswerk.adapters.ItemListener
import com.bramgoedvriend.glaswerk.databinding.FragmentDamageBinding
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.viewmodels.damage.DamageViewModel
import com.bramgoedvriend.glaswerk.viewmodels.damage.DamageViewModelFactory

class DamageFragment : Fragment() {

    private lateinit var binding: FragmentDamageBinding
    private lateinit var damageViewModel: DamageViewModel
    private lateinit var adapter: DamageAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> sharedPrefChange(key) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_damage, container, false
        )
        binding.loadingOverlay.visibility = View.VISIBLE

        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            DamageViewModelFactory(application)
        damageViewModel = ViewModelProvider(this, viewModelFactory).get(DamageViewModel::class.java)

        adapter =
            DamageAdapter(ItemListener { item ->
                damageViewModel.onItemClicked(item)
            })
        binding.itemList.adapter = adapter

        damageViewModel.navigateToLeerlingen.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                findNavController().navigate(
                    DamageFragmentDirections.actionDamageFragmentToDamageStudentFragment(
                        it
                    )
                )
                damageViewModel.onLeerlingenNavigated()
            }
        })

        damageViewModel.status.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it!!) {
                    ApiStatus.OFFLINE ->
                        binding.offlineOverlay.visibility = View.VISIBLE
                    ApiStatus.ERROR ->
                        binding.errorOverlay.visibility = View.VISIBLE
                    else -> {}
                }
            }
        })

        lokaalItemObserver()
        classObserver()

        sharedPreferences = application.getSharedPreferences(resources.getString(R.string.sharedPreferences), AppCompatActivity.MODE_PRIVATE)

        binding.lokaal.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("LokaalDialog")
            val dialogFragment =
                BottomDialogFragment(Lokaal::class.java, false)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        binding.klas.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("KlassDialog")
            val dialogFragment =
                BottomDialogFragment(Klas::class.java, false)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        return binding.root
    }

    private fun lokaalItemObserver() {
        if (damageViewModel.lokaal.hasObservers()) {
            damageViewModel.lokaal.removeObservers(viewLifecycleOwner)
        }
        if (damageViewModel.items.hasObservers()) {
            damageViewModel.items.removeObservers(viewLifecycleOwner)
        }

        damageViewModel.lokaal.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.lokaalName.text = it.lokaalNaam
            }
        })

        damageViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                binding.loadingOverlay.visibility = View.GONE
            }
        })
    }

    private fun classObserver() {
        if (damageViewModel.klas.hasObservers()) {
            damageViewModel.klas.removeObservers(viewLifecycleOwner)
        }

        damageViewModel.klas.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.klasName.text = it.naam
            }
        })
    }

    private fun sharedPrefChange(key: String) {
        when (key) {
            "room" -> {
                damageViewModel.updateRoom()
                lokaalItemObserver()
            }
            "class" -> {
                damageViewModel.updateClass()
                classObserver()
            }
        }
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
