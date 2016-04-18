package be.vergauwen.simon.customloadview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_layout.*

class Activity : AppCompatActivity(){

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_layout)

    load_view.setMax(100)

    increase_progress.setOnClickListener { load_view.incrementProgressBy(1) }

    reset.setOnClickListener { load_view.resetProgress() }

  }
}