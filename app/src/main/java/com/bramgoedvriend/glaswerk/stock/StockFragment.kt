package com.bramgoedvriend.glaswerk.stock

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.bottomDialog.BottomDialogFragment
import com.bramgoedvriend.glaswerk.databinding.FragmentStockBinding
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Lokaal

class StockFragment : Fragment() {
    private lateinit var binding: FragmentStockBinding
    private lateinit var adapter: StockAdapter
    private lateinit var stockViewModel: StockViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> sharedPrefChange()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentStockBinding>(
            inflater, R.layout.fragment_stock, container, false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = StockViewModelFactory(application)
        stockViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StockViewModel::class.java)
        adapter = StockAdapter()
        binding.itemList.adapter = adapter

        sharedPreferences =  application.getSharedPreferences(resources.getString(R.string.sharedPreferences), AppCompatActivity.MODE_PRIVATE)

        itemObserver()

        binding.room.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.addToBackStack("LokaalDialog")
            val dialogFragment = BottomDialogFragment(Lokaal::class.java)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        return binding.root
    }

    private fun itemObserver() {
        if(stockViewModel.items.hasObservers()){
            stockViewModel.items.removeObservers(viewLifecycleOwner)
        }
        stockViewModel.items.observe(viewLifecycleOwner, Observer {
            binding.loadingOverlay.visibility = View.VISIBLE
            it?.let {
                adapter.submitList(it)
                binding.loadingOverlay.visibility = View.GONE
            }
        })
    }

    private fun sharedPrefChange() {
        stockViewModel.updateRoom()
        itemObserver()
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
