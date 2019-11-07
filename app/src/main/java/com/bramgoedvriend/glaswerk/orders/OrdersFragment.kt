package com.bramgoedvriend.glaswerk.orders


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentOrdersBinding
import com.bramgoedvriend.glaswerk.domain.ApiStatus

class OrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentOrdersBinding>(
            inflater, R.layout.fragment_orders, container, false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = OrderViewModelFactory(application)
        val orderViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(OrderViewModel::class.java)

        val adapter = OrderAdapter()
        binding.itemList.adapter = adapter

        orderViewModel.status.observe(viewLifecycleOwner, Observer {
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

        orderViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}
