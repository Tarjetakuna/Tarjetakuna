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
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentProfileBinding
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys

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
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPref = requireActivity().getSharedPreferences(
            "com.github.sdp.tarjetakuna",
            Context.MODE_PRIVATE
        )

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

        sharedPrefEntryInitiate(
            binding.nameEntry,
            SharedPreferencesKeys.user_name,
            getString(R.string.name_entry_hint)
        )
        sharedPrefEntryInitiate(
            binding.descriptionEntry,
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
