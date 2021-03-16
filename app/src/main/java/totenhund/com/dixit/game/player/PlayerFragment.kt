package totenhund.com.dixit.game.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentPlayerBinding
import totenhund.com.dixit.game.adapters.CardsInGameListAdapter
import totenhund.com.dixit.game.norator.NoratorPlayerFragmentDirections


class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_player, container, false
        )

        val cardsInGameManager = GridLayoutManager(requireContext(), 3)
        binding.playerCards.layoutManager = cardsInGameManager
        binding.playerCards.adapter = CardsInGameListAdapter{
            val action = NoratorPlayerFragmentDirections.actionNoratorPlayerFragmentToGameFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }

        return binding.root
    }

}