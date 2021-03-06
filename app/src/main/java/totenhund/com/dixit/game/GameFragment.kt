package totenhund.com.dixit.game

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs

import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.launch
import totenhund.com.dixit.R
import totenhund.com.dixit.database.Card
import totenhund.com.dixit.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game, container, false)


        viewModelFactory = GameViewModelFactory(activity!!.application)
        gameViewModel = ViewModelProvider(this, viewModelFactory)
                .get(GameViewModel::class.java)

        lifecycleScope.launch{
            val card = Card(0, getBitmap())
            gameViewModel.insertCard(card)
        }

        gameViewModel.readCards.observe(this) {
            binding.cardImageView.load(it[0].image)
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