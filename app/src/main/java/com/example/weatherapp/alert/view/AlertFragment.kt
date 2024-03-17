package com.example.weatherapp.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.DataBase.LocalDataSourceImpl
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.alert.viewmodel.AlertViewModel
import com.example.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.repo.Repository
import com.example.weatherapp.model.repo.RepositoryImpl
import com.example.weatherapp.network.RemoteDataSource
import com.example.weatherapp.network.RemoteDataSourceImpl
import com.example.weatherapp.utils.convertTimeToLong
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale


class AlertFragment : Fragment() {
    lateinit var alertViewModelFactory :AlertViewModelFactory
    lateinit var alertViewModel: AlertViewModel
    lateinit var binding: FragmentAlertBinding
    private var dateFrom: Long = 0
    private var dateTo: Long = 0
    private var time: Long = 0

    companion object {
        @JvmStatic
        lateinit var weather : WeatherResponse
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkOverlayPermission()
        alertViewModelFactory = AlertViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl(),
                LocalDataSourceImpl(requireContext())
            )
        )
        alertViewModel = ViewModelProvider(
            this, alertViewModelFactory
        )[AlertViewModel::class.java]

        binding.addAlertFloating.setOnClickListener{
            val alertDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.set_alarm_dialog , null)
            val alertBuilder = AlertDialog.Builder(requireContext()).setView(alertDialogView)
                .setTitle(getString(R.string.setup_alert)).setIcon(R.drawable.baseline_add_alarm_24)
            val alertDialog = alertBuilder.show()
            val fromDate: TextView = alertDialogView.findViewById(R.id.from_date)
            val toDate: TextView = alertDialogView.findViewById(R.id.to_date)
            val txtTime: TextView = alertDialogView.findViewById(R.id.time)
            alertDialogView.findViewById<MaterialButton>(R.id.buttonCancel).setOnClickListener {
                alertDialog.dismiss()
            }
            fromDate.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    requireContext(), { view, year, monthOfYear, dayOfMonth ->
                        var dateString =
                            ("$dayOfMonth ${DateFormatSymbols(Locale.ENGLISH).months[monthOfYear]}, $year")
                        fromDate.text = dateString
                        val format = SimpleDateFormat("dd MMM, yyyy")
                        dateFrom = format.parse(dateString).time
                    }, year, month, day
                )
                dpd.datePicker.minDate = c.timeInMillis
                dpd.show()
            }
            toDate.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(
                    requireContext(),{ view, year, monthOfYear, dayOfMonth ->
                        var dateString =
                            ("$dayOfMonth ${DateFormatSymbols(Locale.ENGLISH).months[monthOfYear]}, $year")
                        toDate.text = dateString
                        val format = SimpleDateFormat("dd MMM, yyyy")
                        dateTo = format.parse(dateString).time
                    },year, month, day
                )

                dpd.show()
            }
            txtTime.setOnClickListener {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)

                    txtTime.text = SimpleDateFormat("hh:mm a").format(cal.time)
                    time = convertTimeToLong(SimpleDateFormat("hh:mm a").format(cal.time))
                }
                val tpd = TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                )
                tpd.show()
            }
        }
    }
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.weather_alarm))
                .setMessage(getString(R.string.features))
                .setPositiveButton(getString(R.string.ok)) { dialog: DialogInterface, i: Int ->
                    var myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(myIntent)
                    dialog.dismiss()
                }.setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog: DialogInterface, i: Int ->
                    dialog.dismiss()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }.show()
        }
    }

}