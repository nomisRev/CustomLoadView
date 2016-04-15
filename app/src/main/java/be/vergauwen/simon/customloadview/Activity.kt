package be.vergauwen.simon.customloadview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class Activity : AppCompatActivity(){

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_layout)

//    load_view.startAnimation()
  }
}