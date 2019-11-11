package com.bramgoedvriend.glaswerk.orders


import android.app.AlertDialog
import android.app.Application
import android.content.Context
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
import com.bramgoedvriend.glaswerk.domain.Item
import android.text.InputType
import android.widget.EditText
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


class OrdersFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentOrdersBinding>(
            inflater, R.layout.fragment_orders, container, false
        )

        application = requireNotNull(this.activity).application
        val viewModelFactory = OrderViewModelFactory(application)
        orderViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(OrderViewModel::class.java)

        val adapter = OrderAdapter(OrderListener { item -> showDialog(item) })
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

    private fun showDialog(item: Item) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.order_amount)
        val input = EditText(context!!)
        input.setText((((item.maxAmount - item.amount) / item.orderAmount) * item.orderAmount).toString())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.onFocusChangeListener = OnFocusChangeListener { _,_ ->
            input.post {
                val inputMethodManager = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        input.requestFocus()
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            orderViewModel.order(item, input.text.toString())
            Toast.makeText(context!!, input.text.toString() + " " + item.name + " zijn besteld.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Annuleer") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}
