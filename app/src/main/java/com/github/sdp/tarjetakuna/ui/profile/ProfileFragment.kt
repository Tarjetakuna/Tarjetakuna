package com.github.sdp.tarjetakuna.ui.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentProfileBinding
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys.shared_pref_name

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPref = requireActivity().getSharedPreferences(
            shared_pref_name,
            Context.MODE_PRIVATE
        )

        /* This function is used to initiate the sharedPrefEntryInitiate
         * It takes a textView, a key and a hint
         * Its goal is to always match the textView value to the value stored in sharedPref with
         * the given key if it exists, otherwise it sets the value to the given hint
         * It retrieves the value stored in sharedPref at startup to set the textView value
         * It sets a textWatcher to update the value in sharedPref when the textView value changes
         */
        fun sharedPrefEntryInitiate(textView: TextView, key: String, hint: String) {
            // Not using a viewmodel for this as it is stored in sharedPref
            textView.text = sharedPref.getString(key, hint)
            textView.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    with(sharedPref.edit()) {
                        putString(key, s.toString())
                        apply()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        // Link the user name and description to the sharedPref
        sharedPrefEntryInitiate(
            binding.profileNameEdittext,
            SharedPreferencesKeys.user_name,
            getString(R.string.name_entry_hint)
        )
        sharedPrefEntryInitiate(
            binding.profileDescriptionEdittext,
            SharedPreferencesKeys.user_description,
            getString(R.string.description_entry_hint)
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
