package com.bramgoedvriend.glaswerk.damage


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import com.bramgoedvriend.glaswerk.network.DamageItemNavigate


class DamageFragment : Fragment() {

    private lateinit var binding: FragmentDamageBinding
    private lateinit var damageViewModel: DamageViewModel
    private lateinit var adapter: DamageAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener {_, key -> sharedPrefChange(key)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_damage, container, false
        )
        binding.loadingOverlay.visibility = View.VISIBLE

        val application = requireNotNull(this.activity).application
        val viewModelFactory = DamageViewModelFactory(application)
        damageViewModel = ViewModelProviders.of(this, viewModelFactory).get(DamageViewModel::class.java)

        adapter = DamageAdapter(ItemListener { item -> damageViewModel.onItemClicked(item) })
        binding.itemList.adapter = adapter

        damageViewModel.navigateToLeerlingen.observe(this, Observer { item ->
            item?.let {
                findNavController().navigate(DamageFragmentDirections.actionDamageFragmentToDamageStudentFragment(it))
                damageViewModel.onLeerlingenNavigated()
            }
        })

        lokaalItemObserver()
        classObserver()

        sharedPreferences =  application.getSharedPreferences(resources.getString(R.string.sharedPreferences), AppCompatActivity.MODE_PRIVATE)

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

    private fun lokaalItemObserver() {
        if (damageViewModel.lokaal.hasObservers()) {
            damageViewModel.lokaal.removeObservers(viewLifecycleOwner)
        }
        if(damageViewModel.items.hasObservers()) {
            damageViewModel.items.removeObservers(viewLifecycleOwner)
        }

        damageViewModel.lokaal.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.lokaalName.text = it.name
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
        if(damageViewModel.klas.hasObservers()) {
            damageViewModel.klas.removeObservers(viewLifecycleOwner)
        }

        damageViewModel.klas.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.klasName.text = it.name
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