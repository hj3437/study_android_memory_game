package com.hurdle.memorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class BoardAdapter(
    private val context: Context,
    private val boardOption: BoardOption,
    private val shuffledIcons: List<Int>
) :
    RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    // inner class 사용시 BoardAdapter에서 받은 shuffledIcons을 inner class 안에서 사용할 수 있습니다.
    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cardImageView: ImageView = itemView.findViewById(R.id.card_item_image_view)

        fun bind(position: Int) {
            cardImageView.setImageResource(shuffledIcons[position])

            cardImageView.setOnClickListener {
                Log.d("TAG", "bind: IMAEGE CLICKED")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val cardWidth = parent.width / boardOption.getWidth()
        val cardHeight = parent.height / boardOption.getHeight()

        val cardSize = min(cardWidth, cardHeight)

        val inflater = LayoutInflater.from(parent.context)
        val rootView: View = inflater.inflate(R.layout.card_list_item, parent, false)

        rootView.layoutParams.width = cardSize
        rootView.layoutParams.height = cardSize

        return BoardViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = boardOption.cardNumber
}

class OnCardClickListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}

