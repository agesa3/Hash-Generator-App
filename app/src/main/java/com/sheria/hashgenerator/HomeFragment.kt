package com.sheria.hashgenerator

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sheria.hashgenerator.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel:HomeViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        val hashAlgos = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, hashAlgos)
        binding.autocompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)


        binding.generateBtn.setOnClickListener {
                onGenerateClicked()
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.clear_menu){
            binding.plainText.text.clear()
            showSnackBar("Clear")
            return true
        }
        return true
    }

    public fun onGenerateClicked() {
        lifecycleScope.launchWhenCreated {
            if (binding.plainText.text.isEmpty()) {
                showSnackBar("Field Empty")
            } else {
                applyAnimations()
                getHashData()
                navigateToSuccess()
            }

        }
    }

    private fun  getHashData():String{
        val algorithm=binding.autocompleteTextView.text.toString()
        val plainText=binding.plainText.text.toString()
        return homeViewModel.getHash(plainText,algorithm)
    }
    private suspend fun applyAnimations() {
        binding.generateBtn.isClickable = false
        binding.titleTextView.animate().alpha(0f).duration = 400L
        binding.generateBtn.animate().alpha(0f).duration = 400L
        binding.textInputLayout.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L
        binding.plainText.animate().alpha(0f)
            .translationXBy(-1200f)
            .duration = 400L

        delay(300)

        binding.successBackground.animate().alpha(1f).duration = 600L
        binding.successBackground.animate().rotationBy(720f).duration = 600L
        binding.successBackground.animate().scaleXBy(900f).duration = 800L
        binding.successBackground.animate().scaleYBy(900f).duration = 800L

        delay(500)

        binding.successImageView.animate().alpha(1f).duration = 1000L
        delay(1500L)

    }

    private fun navigateToSuccess() {
        findNavController().navigate(R.id.action_homeFragment_to_successFragment)

    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.rootLayout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Okay") {}
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
