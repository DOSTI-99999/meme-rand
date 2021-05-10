package com.example.memeshare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class InfoActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
    }

    fun openGithub(view: View) {
        val newIntent:Intent = Intent(Intent.ACTION_VIEW)
        newIntent.setData(Uri.parse("https://github.com/silverstone-git"))
        startActivity(newIntent)
    }
    fun joinDiscord(view: View) {
        val newIntent:Intent = Intent(Intent.ACTION_VIEW)
        newIntent.setData(Uri.parse("https://discord.gg/wCuVpsjQDX"))
        startActivity(newIntent)
    }
    fun openWebsite(view: View) {
        val newIntent:Intent = Intent(Intent.ACTION_VIEW)
        newIntent.setData(Uri.parse("https://silverstone-git.github.io"))
        startActivity(newIntent)
    }
}