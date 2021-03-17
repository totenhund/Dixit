package totenhund.com.dixit.lobby.choice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentChoiceLobbyBinding
import totenhund.com.dixit.peer2peer.GameLogic.GameLogic


class ChoiceLobbyFragment : Fragment() {

    private lateinit var binding: FragmentChoiceLobbyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_choice_lobby, container, false)

        binding.createLobbyButton.setOnClickListener {
//            val action = ChoiceLobbyFragmentDirections.actionChoiceLobbyFragmentToCreationLobbyFragment()
//            findNavController(this).navigate(action)
            addLobby()
        }

        return binding.root
    }

    private fun addLobby(){
        val button = Button(context)
        button.text= "Lobby"
        button.setOnClickListener {
            var gameLogic: GameLogic = GameLogic.getInstance()
            gameLogic.playerAlias = "ahahah"
            val action = ChoiceLobbyFragmentDirections.actionChoiceLobbyFragmentToNoratorPlayerFragment(gameLogic)
            findNavController(this).navigate(action)
        }
        binding.listLobbiesLayout.addView(button)
    }

}