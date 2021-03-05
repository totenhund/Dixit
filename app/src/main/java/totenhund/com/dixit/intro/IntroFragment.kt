package totenhund.com.dixit.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import timber.log.Timber
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentIntroBinding
import androidx.navigation.fragment.NavHostFragment.findNavController


class IntroFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentIntroBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_intro, container, false)

        binding.startGameButton.setOnClickListener {
            val action = IntroFragmentDirections.actionIntroFragmentToChoiceLobbyFragment()
            findNavController(this).navigate(action)
        }

        Timber.i("Intro fragment is running")

        return binding.root

    }

}