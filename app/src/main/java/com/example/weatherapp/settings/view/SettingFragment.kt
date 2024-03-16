package com.example.weatherapp.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.weatherapp.R
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

        val selectedLanguage = PreferenceManager.getSelectedLanguage(requireContext())
        if (selectedLanguage == Constants.LANGUAGE_AR) {
            binding.arabicRadioButton.isChecked = true
        } else {
            binding.englishRadioButton.isChecked = true
        }

        when (PreferenceManager.getSelectedTemperatureUnit(requireContext())) {
            Constants.UNITS_CELSIUS -> binding.celsiusRadioButton.isChecked = true
            Constants.UNITS_Fahrenheit -> binding.fahrenheitRadioButton.isChecked = true
            Constants.UNITS_kelvin -> binding.kelvinRadioButton.isChecked = true
        }

        when (PreferenceManager.getSelectedLocation(requireContext())) {
            Constants.LOCATION_GPS -> binding.gpsRadioButton.isChecked = true
            Constants.LOCATION_MAP -> binding.mapRadioButton.isChecked = true
        }

        binding.arabicRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedLanguage(requireContext(), Constants.LANGUAGE_AR)
            changeLanguageLocaleTo("ar")
        }
        binding.englishRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedLanguage(requireContext(), Constants.LANGUAGE_EN)
            changeLanguageLocaleTo("en")
        }

        binding.celsiusRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedTemperatureUnit(requireContext(), Constants.UNITS_CELSIUS)
        }
        binding.fahrenheitRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedTemperatureUnit(requireContext(), Constants.UNITS_Fahrenheit)
        }
        binding.kelvinRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedTemperatureUnit(requireContext(), Constants.UNITS_kelvin)
        }

        binding.gpsRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedLocation(requireContext(), Constants.LOCATION_GPS)
        }
        binding.mapRadioButton.setOnClickListener {
            PreferenceManager.saveSelectedLocation(requireContext(), Constants.LOCATION_MAP)

            val bundle = bundleOf("fav" to "map")
            Navigation.findNavController(view)
                .navigate(R.id.action_settingFragment_to_mapFragment,bundle)


        }
    }

    private fun changeLanguageLocaleTo(lan: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lan)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

}