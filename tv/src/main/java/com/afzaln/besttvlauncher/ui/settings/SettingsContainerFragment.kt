package com.afzaln.besttvlauncher.ui.settings

import android.os.Bundle
import android.view.View
import androidx.leanback.preference.LeanbackPreferenceFragmentCompat
import androidx.leanback.preference.LeanbackSettingsFragmentCompat
import androidx.preference.*
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.data.UserPreferences.Companion.KEY_CHANNELS
import com.afzaln.besttvlauncher.data.UserPreferences.Companion.USER_PREF_FILE_NAME
import com.afzaln.besttvlauncher.data.models.Channel
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.utils.locatorViewModel

class SettingsContainerFragment : LeanbackSettingsFragmentCompat() {
    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val args = pref.extras
        val fragment = childFragmentManager.fragmentFactory.instantiate(
            requireActivity().classLoader, requireNotNull(pref.fragment)
        )
        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        if (fragment is PreferenceFragmentCompat || fragment is PreferenceDialogFragmentCompat) {
            startPreferenceFragment(fragment)
        } else {
            startImmersiveFragment(fragment)
        }
        return true
    }

    override fun onPreferenceStartScreen(
        caller: PreferenceFragmentCompat,
        pref: PreferenceScreen
    ): Boolean {
        val fragment = SettingsFragment()
        val args = Bundle(1)
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref!!.key)
        fragment.arguments = args
        startPreferenceFragment(fragment)
        return true
    }

    override fun onPreferenceStartInitialScreen() {
        startPreferenceFragment(SettingsFragment())
    }
}

/**
 * The fragment that is embedded in SettingsFragment
 */
class SettingsFragment : LeanbackPreferenceFragmentCompat() {
    private val homeViewModel: HomeViewModel by locatorViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferencesName = USER_PREF_FILE_NAME
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.wrappedChannelList.observe(viewLifecycleOwner) { channelList ->
            findPreference<MultiSelectListPreference>(KEY_CHANNELS)?.let {
                setupChannels(it, channelList)
            }
        }

//         setupItems()
    }

    private fun setupItems() {
        preferenceScreen.iterator().forEach { preference ->
            when (preference.key) {
//                KEY_CHANNELS -> setupChannels(preference)
            }
        }
    }

    private fun setupChannels(
        preference: MultiSelectListPreference,
        channelList: List<Channel>,
    ) {
        val entry = channelList.associate { channel ->
            val displayName = channel.displayName
            val entry = if (displayName != channel.appName) {
                "${channel.appName} - $displayName"
            } else {
                displayName
            }

            channel.id.toString() to entry
        }

        val entries = entry.values.toTypedArray()
        val entryValues = entry.keys.toTypedArray()
        val selected = homeViewModel.selectedChannels

        preference.entries = entries
        preference.entryValues = entryValues
        preference.values = selected
    }
}
