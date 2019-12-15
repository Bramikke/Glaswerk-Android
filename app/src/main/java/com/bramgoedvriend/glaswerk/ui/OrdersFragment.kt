package com.bramgoedvriend.glaswerk.ui

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.adapters.OrderAdapter
import com.bramgoedvriend.glaswerk.adapters.OrderListener
import com.bramgoedvriend.glaswerk.databinding.FragmentOrdersBinding
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.viewmodels.order.OrderViewModel
import com.bramgoedvriend.glaswerk.viewmodels.order.OrderViewModelFactory

class OrdersFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentOrdersBinding>(
            inflater, R.layout.fragment_orders, container, false
        )

        application = requireNotNull(this.activity).application
        val viewModelFactory =
            OrderViewModelFactory(application)
        orderViewModel =
            ViewModelProvider(this, viewModelFactory).get(OrderViewModel::class.java)

        val adapter =
            OrderAdapter(OrderListener { item ->
                if (orderViewModel.status.value != ApiStatus.OFFLINE) {
                    showDialog(item)
                } else {
                    Toast.makeText(context!!, "Je bent momenteel offline.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        binding.itemList.adapter = adapter

        orderViewModel.status.observe(viewLifecycleOwner, Observer {
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

        orderViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun showDialog(item: Item) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.order_amount)
        val input = EditText(context!!)
        input.setText((((item.maxAantal - item.aantal) / item.bestelHoeveelheid) * item.bestelHoeveelheid).toString())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.onFocusChangeListener = OnFocusChangeListener { _, _ ->
            input.post {
                val inputMethodManager = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        input.requestFocus()
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            orderViewModel.order(item, input.text.toString())
            Toast.makeText(context!!, input.text.toString() + " " + item.naam + " zijn besteld.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Annuleer") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}
