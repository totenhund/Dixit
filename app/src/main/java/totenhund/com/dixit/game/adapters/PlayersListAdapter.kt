package totenhund.com.dixit.game.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.players_list.view.*
import totenhund.com.dixit.R

class PlayersListAdapter : RecyclerView.Adapter<PagerVH>() {

    private val players = intArrayOf(
        1, 2, 3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH = PagerVH(LayoutInflater.from(parent.context).inflate(
        R.layout.players_list, parent, false))

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        playerUsername.text = "Player $position"
        container.background = ContextCompat.getDrawable(context, R.drawable.host_card)
    }

}

open class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)