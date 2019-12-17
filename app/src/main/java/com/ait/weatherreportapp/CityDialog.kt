package com.ait.weatherreportapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ait.weatherreportapp.Data.City
import kotlinx.android.synthetic.main.new_city_dialog.view.*

class CityDialog : DialogFragment(){

    interface CityHandler {
        fun cityCreated(item: City)
        fun cityUpdated(item: City)
    }

    private lateinit var cityHandler: CityHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CityHandler) {
            cityHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the CityHandlerInterface"
            )
        }
    }

    private lateinit var etName: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New item")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_city_dialog, null
        )

        etName = rootView.etName
        builder.setView(rootView)

        builder.setPositiveButton("OK") { dialog, witch ->
            // empty
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                handleCityCreate()
                dialog.dismiss()
            } else {
                etName.error = "This field can not be empty"
            }
        }
    }

    private fun handleCityCreate() {
        cityHandler.cityCreated(
            City(
                null,
                etName.text.toString(),
                false
            )
        )
    }

}