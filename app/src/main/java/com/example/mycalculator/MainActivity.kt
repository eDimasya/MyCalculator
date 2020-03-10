package com.example.mycalculator

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toastMe (view: View) {
        val myToast = Toast.makeText(this, "Hello, Toast!", Toast.LENGTH_SHORT)

        myToast.show()
    }

    fun countMe (view: View){
        val countString = textView_Value.text.toString()

        var count: Int = Integer.parseInt(countString)
        count++
        textView_Value.text = count.toString()
    }

    /*fun randomMe (view: View){
        val randomIntent = Intent(this, secondActivity::class.java)
        val countString = textView_Value.text.toString()
        val count = Integer.parseInt(countString)
        randomIntent.putExtra(secondActivity.TOTAL_COUNT, count)
        startActivity(randomIntent)

    }*/
}
