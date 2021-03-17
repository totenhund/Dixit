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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.launch
import timber.log.Timber
import totenhund.com.dixit.R
import totenhund.com.dixit.database.Card
import totenhund.com.dixit.databinding.FragmentGameBinding
import totenhund.com.dixit.game.adapters.CardsInGameListAdapter
import totenhund.com.dixit.game.adapters.HorizontalMarginItemDecoration
import totenhund.com.dixit.game.adapters.PlayersListAdapter


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory
//    private var links = arrayOf(
//        "https://i.pinimg.com/236x/9d/74/58/9d745854c6669773095b86d6ab28e0cd--big-bad-wolf-hoods.jpg",
//        "https://i.pinimg.com/236x/c4/3a/ed/c43aed39d3e1397486da0e0a647b16ec--martin-puryear-washington.jpg",
//        "https://i.pinimg.com/236x/65/b8/66/65b8667296b6d983a765ddffbe12f616--fish-swimming-red-fish.jpg",
//        "https://i.pinimg.com/236x/47/5f/69/475f69d5144046797a5169a4132b4e9a--magic-art-surreal-art.jpg",
//        "https://i.pinimg.com/236x/4c/cc/be/4cccbed9282b74b5de9e454bfea35a59--dancing-in-the-rain-dance-in.jpg",
//        "https://i.pinimg.com/236x/61/31/86/6131863d1cba9df0875a5cbbe310c9ed--story-starter-blabla.jpg",
//        "https://i.pinimg.com/236x/a2/80/72/a280725d8940e99d4213e9d075b51768--illustration-kids-art-illustrations.jpg",
//        "https://i.pinimg.com/236x/a9/4a/43/a94a4359cbf1808f5cd0df9353501022--game-party-xavier.jpg",
//        "https://i.pinimg.com/236x/93/be/81/93be81f24cbe3e48af321b6bf0fb2df1--artist-painting-art-illustrations.jpg",
//        "https://i.pinimg.com/236x/bd/69/f7/bd69f711e6f619c1b0243891df6e2afd--belles-images-marie.jpg",
//        "https://i.pinimg.com/236x/14/71/e7/1471e79a8f67f57335e43db6737d89b6--illustration-children-illustration-art.jpg",
//        "https://i.pinimg.com/236x/01/de/ba/01deba51ddeb414b44976e1f5624a0e3--illustration-children-blabla.jpg",
//        "https://i.pinimg.com/236x/7c/21/d3/7c21d353d77f33d0bb9dcf17b8ed004c--blabla-d.jpg",
//        "https://i.pinimg.com/236x/d6/b3/41/d6b3417cdc74d85b3e16b6eda56bf247--board-games-writing-inspiration.jpg",
//        "https://i.pinimg.com/236x/0c/e1/3b/0ce13b1fe6d44678b781c8f562f78fe7--conceptual-illustrations-illustrations-posters.jpg",
//        "https://i.pinimg.com/236x/e2/93/8f/e2938f0dc331b1dfe690cc83919354a6--blabla-a-song.jpg"
//    )

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
//            for(link in links){
//                val card = Card(0, getBitmap(link))
//                gameViewModel.insertCard(card)
//            }
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

    private suspend fun getBitmap(link: String): Bitmap {
        val loading = ImageLoader(context!!.applicationContext)
        val request = ImageRequest.Builder(context!!.applicationContext)
            .data(link)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

}