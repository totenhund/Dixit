package totenhund.com.dixit.lobby.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentMenuLobbyBinding


class LobbyMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMenuLobbyBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_menu_lobby, container, false)


        return binding.root
    }

}