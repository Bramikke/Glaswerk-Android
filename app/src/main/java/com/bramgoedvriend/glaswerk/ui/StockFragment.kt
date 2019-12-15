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
import com.bramgoedvriend.glaswerk.adapters.ItemListener
import com.bramgoedvriend.glaswerk.adapters.StockAdapter
import com.bramgoedvriend.glaswerk.databinding.FragmentStockBinding
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.network.ItemNavigate
import com.bramgoedvriend.glaswerk.viewmodels.stock.StockViewModel
import com.bramgoedvriend.glaswerk.viewmodels.stock.StockViewModelFactory

class StockFragment : Fragment() {
    private lateinit var binding: FragmentStockBinding
    private lateinit var adapter: StockAdapter
    private lateinit var stockViewModel: StockViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> sharedPrefChange() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stock, container, false
        )
        binding.loadingOverlay.visibility = View.VISIBLE

        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            StockViewModelFactory(application)
        stockViewModel =
            ViewModelProvider(this, viewModelFactory).get(StockViewModel::class.java)
        adapter = StockAdapter(ItemListener { item ->
            stockViewModel.onItemClicked(
                item
            )
        })
        binding.itemList.adapter = adapter

        stockViewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                findNavController().navigate(
                    StockFragmentDirections.actionStockFragmentToStockDetailFragment(
                        ItemNavigate(
                            id = it.id,
                            roomId = it.lokaalId,
                            name = it.naam,
                            amount = it.aantal,
                            minAmount = it.minAantal,
                            maxAmount = it.maxAantal,
                            orderAmount = it.bestelHoeveelheid
                        )
                    )
                )
                stockViewModel.onDetailNavigated()
            }
        })

        stockViewModel.status.observe(viewLifecycleOwner, Observer {
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

        itemRoomObserver()

        binding.room.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack("LokaalDialog")
            val dialogFragment =
                BottomDialogFragment(Lokaal::class.java, true)
            dialogFragment.show(fragmentTransaction, "dialog")
        }

        binding.room.setOnLongClickListener {
            Toast.makeText(context!!, "Long clicked - remove room", Toast.LENGTH_SHORT).show()
            true
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                StockFragmentDirections.actionStockFragmentToStockDetailFragment(
                    null
                )
            )
        }

        return binding.root
    }

    private fun itemRoomObserver() {
        if (stockViewModel.lokaal.hasObservers()) {
            stockViewModel.lokaal.removeObservers(viewLifecycleOwner)
        }
        if (stockViewModel.items.hasObservers()) {
            stockViewModel.items.removeObservers(viewLifecycleOwner)
        }
        stockViewModel.lokaal.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.roomName.text = it.lokaalNaam
            }
        })
        stockViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                binding.loadingOverlay.visibility = View.GONE
            }
        })
    }

    private fun sharedPrefChange() {
        stockViewModel.updateRoom()
        itemRoomObserver()
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
