package totenhund.com.dixit.game.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_in_game.view.*
import timber.log.Timber
import totenhund.com.dixit.R

class CardsInGameListAdapter(private val itemClickListener: (Int) -> (Unit)) : RecyclerView.Adapter<PagerVH>() {

    var cardsInGame = intArrayOf(1, 2, 3, 4, 5)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_in_game, parent, false))

    override fun getItemCount(): Int = cardsInGame.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        card.background = ContextCompat.getDrawable(context, R.drawable.host_card)
    }

    private inner class ItemViewHolder(itemView: View) : PagerVH(itemView) {

        init {
            itemView.setOnClickListener {
                Timber.i("Working")
                itemClickListener(cardsInGame[adapterPosition])
            }
        }
    }

}
