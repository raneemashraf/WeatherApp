package com.example.weatherapp.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.PreferenceManager


class SettingFragment : Fragment() {
    lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arabicRadioButton.setOnClickListener{
            PreferenceManager.saveSelectedLanguage(requireContext(),Constants.LANGUAGE_AR)
        }
        binding.englishRadioButton.setOnClickListener{
            PreferenceManager.saveSelectedLanguage(requireContext(),Constants.LANGUAGE_EN)
        }
        binding.celsiusRadioButton.setOnClickListener{
            PreferenceManager.saveSelectedTemperatureUnit(requireContext(),Constants.UNITS_CELSIUS)
        }
        binding.fahrenheitRadioButton.setOnClickListener{
            PreferenceManager.saveSelectedTemperatureUnit(requireContext(),Constants.UNITS_Fahrenheit)
        }
        binding.kelvinRadioButton.setOnClickListener{
            PreferenceManager.saveSelectedTemperatureUnit(requireContext(),Constants.UNITS_kelvin)

        }
    }

}