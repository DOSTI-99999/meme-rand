package com.example.memeshare
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var urltobepassed: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme(View(this))
    }

    fun loadMeme(view: View) {


        myProgress.visibility = View.VISIBLE

        //this code will execute when next button is clicked
        // Instantiate the RequestQueue

        // --> use this when not using singleton pattern
        //val queue = Volley.newRequestQueue(this)

        val url = "https://meme-api.herokuapp.com/gimme"


        // Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(
            //the third argument is the thing we are going to give to the backend
            Request.Method.GET, url, null,

            { response ->
                urltobepassed = response.getString("url")

                Glide.with(this).load(urltobepassed).into(myMeme)
                // Display the first 500 characters of the response string.
                //textView.text = "Response is: ${response.substring(0, 500)}"
            },
            {
                val snackbar = Snackbar.make(
                    this,
                    view,
                    "An error occurred, are you connected to Internet rn?",
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            })

        // Add the request to the RequestQueue.

        //queue.add(JsonObjectRequest)

        MySingleton.getInstance(this).addToRequestQueue(JsonObjectRequest)

        myProgress.visibility = View.GONE
    }

    fun shareMeme(view: View) {
        // this code will execute when share button is clicked

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Ye main kar leta hu, tu reddit ki meme dekhle: $urltobepassed"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share this meme using: ")
        //try {
        startActivity(shareIntent)
        //} catch (e: Exc){
        //    sendIntent.setPackage("com.whatsapp")
        //    startActivity(sendIntent)
        //    }
        //}
        // Commented the try catch because it started working in the end somehow
        }

        fun nextMeme(view: View) {
            loadMeme(view)
        }

}
