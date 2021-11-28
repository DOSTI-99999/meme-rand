package com.example.memeshare
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private var urltobepassed: String? = null
    private val urlarr: MutableList<String?> = ArrayList()
    private var prevcount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var thyview = View(this)
        loadMeme(View(this))
    }

    private fun extfind(urltbp : String) : String? {
        var ext = ""
        for (i in urltbp.length-1 downTo 0){
            if (urltbp[i] == '.') {
                ext = urltbp.slice(i..urltbp.length-1)
                break
            }
        }
        return ext
    }

    private fun loadMeme(view: View): Unit? {

        // --- use this when using Progress bar from xml
        myProgress.visibility = View.VISIBLE

        // this code will execute when next button is clicked
        // Instantiate the RequestQueue

        // use this when not using singleton pattern
        // val queue = Volley.newRequestQueue(this)

        val url = "https://meme-api.herokuapp.com/gimme"

        if (prevcount != 0) {
            prevcount = prevcount - 1
            gliderboi(prevcount)
            return null
        }

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            //the third argument is the thing we are going to give to the backend
            Request.Method.GET, url, null,

            { response ->
                urltobepassed = response.getString("url")
                if (urltobepassed !in urlarr) {
                    urlarr.add(urltobepassed)
                    gliderboi(prevcount)
                }
                // Display the first 500 characters of the response string.
                //textView.text = "Response is: ${response.substring(0, 500)}"
            },
            {
                // Snackbar seems to give View related errors in this setting, works fine otherwise
                //val snackbar = Snackbar.make(
                //    this,
                //   View(MainActivity()),
                //   "An error occurred, are you connected to Internet rn?",
                //   Snackbar.LENGTH_LONG
                //)
                Toast.makeText(this, "An error occurred, are you connected to Internet rn?",Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue.

        // queue.add(JsonObjectRequest)


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        return null
    }

    fun shareMeme(view: View) {
        // this code will execute when share button is clicked

        val urlnow = urlarr[urlarr.size - prevcount - 1]
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(

                Intent.EXTRA_TEXT,
                "Ye main kar leta hu, tu reddit ki meme dekhle: $urlnow"
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

    fun prevMeme(view: View) {
        prevcount ++
        if (urlarr.size - prevcount - 1 > -1) {
            urltobepassed = urlarr[urlarr.size - prevcount - 1]
            gliderboi(prevcount)
        } else {
            // Snackbar Displaying this is the first damn meme
            val newsnacc = Snackbar.make(
                this,
                view,
                "You are at the first Meme",
                Snackbar.LENGTH_LONG
            )
            newsnacc.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_info -> {
            // User chose the About option
            // some intent stuff
            val newIntent = Intent(this, InfoActivity::class.java)
            startActivity(newIntent)

            true
        }

        R.id.action_download -> {
            // User chose the "Favorite" action (the Download Button)
            // here


            // Checking if User has given the storage permission
            if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this,
                        "Download can't occur without Storage Permission, but you can get the link to the file by Share Button",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // can ask more permissions in this array
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
            }

            // Adjusting the File name
            val cur_url = urlarr[urlarr.size-prevcount-1]
            val time_raw = LocalDateTime.now()
            val time_formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")  // Can also use SSS, ie, milliseconds
            val time_formatted = time_raw.format(time_formatter)
            var mname = "meme_" + "$time_formatted"
            mname = mname + cur_url?.let { extfind(it) }

            //Using the DownloadManager
            val img_uri = Uri.parse(cur_url)
            val request = DownloadManager.Request(img_uri)
            val dmanager : DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("A Virtual Upvote")
                .setDescription("The Meme you requested")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mname)
                .setMimeType("*/*")
            dmanager.enqueue(request)

            Toast.makeText(this, "Download Started", Toast.LENGTH_SHORT).show()

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun gliderboi(prevcount: Int){
        // this function downloads a certain link from urlarr into myMeme ImageView
        // and uses myProgress to show the loading
        // takes in the prevcount : zero if it's the last loaded Meme in the list
        Glide.with(this).load(urlarr[urlarr.size - prevcount - 1])
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    myProgress.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    myProgress.visibility = View.GONE
                    return false
                }


            }).into(myMeme)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.myactionbutton, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
