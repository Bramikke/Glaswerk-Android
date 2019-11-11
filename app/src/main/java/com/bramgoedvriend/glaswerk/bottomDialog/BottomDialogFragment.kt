package com.bramgoedvriend.glaswerk.bottomDialog

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.databinding.PopupBottomBinding
import com.bramgoedvriend.glaswerk.domain.ApiStatus
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal

@Suppress("UNCHECKED_CAST")
class BottomDialogFragment <T> (val type: Class<T>) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val binding = DataBindingUtil.inflate<PopupBottomBinding>(
            inflater, R.layout.popup_bottom, container, false
        )


        val application = requireNotNull(this.activity).application
        val viewModelFactory = DialogViewModelFactory(application, type)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DialogViewModel::class.java)

        val adapter: Any
        if(type == Lokaal::class.java){
            adapter = RoomAdapter(ItemListener { item ->
                viewModel.onItemClicked(item)
                dialog!!.dismiss()
            })
            binding.list.adapter = adapter
        } else {
            adapter = ClassAdapter(ItemListener { item ->
                viewModel.onItemClicked(item)
                dialog!!.dismiss()
            })
            binding.list.adapter = adapter
        }

        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == ApiStatus.ERROR) {
                    Toast.makeText(context, "Error, probeer het later opnieuw", Toast.LENGTH_SHORT).show()
                    dialog!!.dismiss()
                }
            }
        })

        viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(type == Lokaal::class.java){
                    (adapter as RoomAdapter).submitList(it as List<Lokaal>)
                } else {
                    (adapter as ClassAdapter).submitList(it as List<Klas>)
                }
            }
        })

        binding.cardCancel.setOnClickListener {
            dialog!!.dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog!!
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<LinearLayout>(R.id.popup_bottom_overlay).setOnClickListener{
            dialog.dismiss()
        }
    }
}