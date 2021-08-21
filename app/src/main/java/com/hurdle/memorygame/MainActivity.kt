package com.hurdle.memorygame

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    private var gameName: String? = null
    private var customGameImages: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveTextView = findViewById(R.id.main_move_text_view)
        pairTextView = findViewById(R.id.main_pair_text_view)
        boardRecyclerView = findViewById(R.id.main_board_recycler_view)

        boardOption = BoardOption.BOARD_MIN

        initBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        initBoard()

                    })
                } else {
                    initBoard()

                }
            }

            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)

        when (boardOption) {
            BoardOption.BOARD_MIN -> radioGroupSize.check(R.id.rbEasy)
            BoardOption.BOARD_MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardOption.BOARD_MAX -> radioGroupSize.check(R.id.rbHard)
        }

        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardOption = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardOption.BOARD_MIN
                R.id.rbMedium -> BoardOption.BOARD_MEDIUM
                else -> BoardOption.BOARD_MAX
            }
            initBoard()
        })
    }

    private fun showAlertDialog(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun initBoard() {
        when (boardOption) {
            BoardOption.BOARD_MIN -> {
                moveTextView.text = "Lv1 4x2"
                pairTextView.text = "Pairs: 0/4"
            }

            BoardOption.BOARD_MEDIUM -> {
                moveTextView.text = "Lv2 6x3"
                pairTextView.text = "Pairs: 0/9"
            }

            BoardOption.BOARD_MAX -> {
                moveTextView.text = "Lv3 6x6"
                pairTextView.text = "Pairs: 0/12"
            }
        }
        pairTextView.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))

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
        if (memoryGame.haveWonGame()) {
            Toast.makeText(this, "YOU WON", Toast.LENGTH_SHORT).show()
            return
        }

        if (memoryGame.isCardFaceUp(position)) {
            Toast.makeText(this, "Invaild Move", Toast.LENGTH_SHORT).show()
            return
        }

        // Add data
        if (memoryGame.flipCard(position)) {
            Log.d("TAG", "updateCardFlip: ${memoryGame.pairFound}")

            val color = ArgbEvaluator().evaluate(
                memoryGame.pairFound.toFloat() / boardOption.getNumberPair(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int

            pairTextView.setTextColor(color)
            pairTextView.text = "pairs : ${memoryGame.pairFound} / ${boardOption.getNumberPair()}"

            if (memoryGame.haveWonGame()) {
                Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show()
            }
        }
        moveTextView.text = "Count ${memoryGame.getNumMoves()}"
        boardAdapter.notifyDataSetChanged()
    }
}