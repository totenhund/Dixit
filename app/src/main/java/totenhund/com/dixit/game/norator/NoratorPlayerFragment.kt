package totenhund.com.dixit.game.norator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import timber.log.Timber
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentNoratorPlayerBinding
import totenhund.com.dixit.game.adapters.CardsInGameListAdapter
import totenhund.com.dixit.lobby.choice.ChoiceLobbyFragmentDirections


class NoratorPlayerFragment : Fragment() {

    private lateinit var binding: FragmentNoratorPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_norator_player, container, false
        )

        val cardsInGameManager = GridLayoutManager(requireContext(), 3)
        binding.noratorCards.layoutManager = cardsInGameManager
        binding.noratorCards.adapter = CardsInGameListAdapter{
            if(binding.noratorDescriptionEditView.text.isNotEmpty()){
                val action = NoratorPlayerFragmentDirections.actionNoratorPlayerFragmentToGameFragment()
                findNavController(this).navigate(action)
            }else{
                Toast.makeText(requireContext(), "You should write description", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
    }

}