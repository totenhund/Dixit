package totenhund.com.dixit.game

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import totenhund.com.dixit.R
import totenhund.com.dixit.databinding.FragmentGameBinding
import totenhund.com.dixit.game.adapters.CardsInGameListAdapter
import totenhund.com.dixit.game.adapters.HorizontalMarginItemDecoration
import totenhund.com.dixit.game.adapters.PlayersListAdapter


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game, container, false
        )


        viewModelFactory = GameViewModelFactory(activity!!.application)
        gameViewModel = ViewModelProvider(this, viewModelFactory)
            .get(GameViewModel::class.java)

//        lifecycleScope.launch{
//            val card = Card(0, getBitmap())
//            gameViewModel.insertCard(card)
//        }

        // player list recycler view
        val playersListManager = LinearLayoutManager(requireContext())
        playersListManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.playersList.layoutManager = playersListManager
        binding.playersList.adapter = PlayersListAdapter()
        binding.playersList.addItemDecoration(
            HorizontalMarginItemDecoration(
                requireContext(),
                R.dimen.viewpager_current_item_horizontal_margin
            )
        )

        val cardsInGameManager = GridLayoutManager(requireContext(), 3)
        binding.cardsInGameList.layoutManager = cardsInGameManager
        binding.cardsInGameList.adapter = CardsInGameListAdapter{
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    private suspend fun getBitmap(): Bitmap {
        val loading = ImageLoader(context!!.applicationContext)
        val request = ImageRequest.Builder(context!!.applicationContext)
            .data("https://avatars3.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

}