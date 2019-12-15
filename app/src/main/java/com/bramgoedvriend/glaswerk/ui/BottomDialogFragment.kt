package com.bramgoedvriend.glaswerk.ui

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.adapters.ClassAdapter
import com.bramgoedvriend.glaswerk.adapters.ItemListenerGeneric
import com.bramgoedvriend.glaswerk.adapters.RoomAdapter
import com.bramgoedvriend.glaswerk.databinding.PopupBottomBinding
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.network.ApiStatus
import com.bramgoedvriend.glaswerk.viewmodels.bottomDialog.DialogViewModel
import com.bramgoedvriend.glaswerk.viewmodels.bottomDialog.DialogViewModelFactory

@Suppress("UNCHECKED_CAST")
class BottomDialogFragment <T> (val type: Class<T>, val add: Boolean) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<PopupBottomBinding>(
            inflater, R.layout.popup_bottom, container, false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            DialogViewModelFactory(
                application,
                type
            )
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DialogViewModel::class.java)

        if (add) {
            binding.cardAdd.visibility = View.VISIBLE
            binding.spacer.visibility = View.GONE
        }

        val adapter: Any
        if (type == Lokaal::class.java) {
            adapter =
                RoomAdapter(ItemListenerGeneric { item ->
                    viewModel.onItemClicked(item)
                    dialog!!.dismiss()
                })
            binding.list.adapter = adapter
        } else {
            adapter =
                ClassAdapter(ItemListenerGeneric { item ->
                    viewModel.onItemClicked(item)
                    dialog!!.dismiss()
                })
            binding.list.adapter = adapter
        }

        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == ApiStatus.OFFLINE) {
                    Toast.makeText(context, "Error, probeer het later opnieuw", Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()
                }
            }
        })

        viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (type == Lokaal::class.java) {
                    (adapter as RoomAdapter).submitList(it as List<Lokaal>)
                } else {
                    (adapter as ClassAdapter).submitList(it as List<Klas>)
                }
            }
        })

        binding.cardCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.cardAdd.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle(R.string.Ad)
            val input = EditText(context!!)
            input.hint = "Naam"
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
                input.post {
                    val inputMethodManager = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
                }
            }
            input.requestFocus()
            builder.setView(input)
            builder.setPositiveButton("Voeg toe") { _, _ ->
                viewModel.add(input.text.toString())
                dialog!!.dismiss()
            }
            builder.setNegativeButton("Annuleer") { dialog, _ -> dialog.cancel() }
            builder.show()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog!!
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<LinearLayout>(R.id.popup_bottom_overlay).setOnClickListener {
            dialog.dismiss()
        }
    }
}
