package com.bramgoedvriend.glaswerk.stock.stockDetail

import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.FragmentStockDetailBinding
import com.bramgoedvriend.glaswerk.domain.Item

class StockDetailFragment : Fragment() {
    private lateinit var binding: FragmentStockDetailBinding
    private lateinit var stockDetailViewModel: StockDetailViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stock_detail, container, false
        )


        val item = StockDetailFragmentArgs.fromBundle(arguments!!).item
        if (item != null) {
            binding.title.text = resources.getString(R.string.EditItem)
            binding.removeButton.visibility = View.VISIBLE
            binding.nameText.setText(item.name)
            binding.amountText.setText(item.amount.toString())
            binding.minAmountText.setText(item.minAmount.toString())
            binding.maxAmountText.setText(item.maxAmount.toString())
            binding.orderAmountText.setText(item.orderAmount.toString())
        } else {
            binding.title.text = resources.getString(R.string.AddItem)
        }


        val application = requireNotNull(this.activity).application
        val viewModelFactory = StockDetailViewModelFactory(application)
        stockDetailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StockDetailViewModel::class.java)

        sharedPreferences =  application.getSharedPreferences(resources.getString(R.string.sharedPreferences), AppCompatActivity.MODE_PRIVATE)


        binding.saveButton.setOnClickListener {
            stockDetailViewModel.addItem(
                item,
                binding.nameText.text.toString(),
                binding.amountText.text.toString().toInt(),
                binding.minAmountText.text.toString().toInt(),
                binding.maxAmountText.text.toString().toInt(),
                binding.orderAmountText.text.toString().toInt()
            )
            findNavController().navigate(StockDetailFragmentDirections.actionStockDetailFragmentToStockFragment())
        }

        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when(which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    stockDetailViewModel.remove(item)
                    dialog.dismiss()
                    findNavController().navigate(StockDetailFragmentDirections.actionStockDetailFragmentToStockFragment())
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.cancel()
            }
        }

        binding.removeButton.setOnClickListener {
            if(item!=null) {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("Ben je zeker dat je " + item.name + " wilt verwijderen?")
                    .setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nee", dialogClickListener)
                    .show()
            }
        }

        return binding.root
    }
}
