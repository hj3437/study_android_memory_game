package com.hurdle.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var moveTextView: TextView
    private lateinit var pairTextView: TextView
    private lateinit var boardRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveTextView = findViewById(R.id.main_move_text_view)
        pairTextView = findViewById(R.id.main_pair_text_view)
        boardRecyclerView = findViewById(R.id.main_board_recycler_view)


        val boardLayoutManager = GridLayoutManager(this, 2)
        boardRecyclerView.apply {
            adapter = BoardAdapter(context, 8)
            setHasFixedSize(true)
            layoutManager = boardLayoutManager
        }
    }
}