package com.hurdle.memorygame

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var moveTextView: TextView
    private lateinit var pairTextView: TextView
    private lateinit var boardRecyclerView: RecyclerView

    private lateinit var boardOption: BoardOption
    private lateinit var memoryGame: MemoryGame
    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveTextView = findViewById(R.id.main_move_text_view)
        pairTextView = findViewById(R.id.main_pair_text_view)
        boardRecyclerView = findViewById(R.id.main_board_recycler_view)

        pairTextView.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))

        boardOption = BoardOption.BOARD_MIN

        memoryGame = MemoryGame(boardOption)

        boardAdapter = BoardAdapter(
            applicationContext,
            boardOption,
            memoryGame.cards,
            object : BoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    // Log.d("TAG", "onCardClicked: $position")
                    updateCardFlip(position)
                }
            })

        val boardLayoutManager = GridLayoutManager(this, boardOption.getWidth())

        boardRecyclerView.apply {
            adapter = boardAdapter
            setHasFixedSize(true)
            layoutManager = boardLayoutManager
        }
    }

    private fun updateCardFlip(position: Int) {
        if(memoryGame.haveWonGame()){
            Toast.makeText(this, "YOU WON", Toast.LENGTH_SHORT).show()
            return
        }

        if(memoryGame.isCardFaceUp(position)){
            Toast.makeText(this, "Invaild Move", Toast.LENGTH_SHORT).show()
            return
        }

        // Add data
        if(memoryGame.flipCard(position)){
            Log.d("TAG", "updateCardFlip: ${memoryGame.pairFound}")

            val color = ArgbEvaluator().evaluate(
                memoryGame.pairFound.toFloat() / boardOption.getNumberPair(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int

            pairTextView.setTextColor(color)
            pairTextView.text = "pairs : ${memoryGame.pairFound} / ${boardOption.getNumberPair()}"

            if(memoryGame.haveWonGame()){
                Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show()
            }
        }
        moveTextView.text= "Count ${memoryGame.getNumMoves()}"
        boardAdapter.notifyDataSetChanged()
    }
}