package totenhund.com.dixit.lobby.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentCreationLobbyBinding


class CreationLobbyFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentCreationLobbyBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_creation_lobby, container, false)


        return binding.root
    }

}