package com.app.simplenotesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {
    private lateinit var adView: AdView
    private var interstitialAd: InterstitialAd? = null
    private lateinit var editTextTask: EditText
    private lateinit var buttonAdd: Button
    private lateinit var listViewTasks: ListView
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) { }

        // Find views
        adView = findViewById(R.id.adView)
        editTextTask = findViewById(R.id.editTextTask)
        buttonAdd = findViewById(R.id.buttonAdd)
        listViewTasks = findViewById(R.id.listViewTasks)

        // Load a banner ad
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        // Load an interstitial ad
        loadInterstitialAd()

        // Set up task adapter
        taskAdapter = TaskAdapter(this, taskList) { position ->
            taskList.removeAt(position)
            taskAdapter.notifyDataSetChanged()
        }
        listViewTasks.adapter = taskAdapter

        // Set button click listener to add task
        buttonAdd.setOnClickListener {
            val task = editTextTask.text.toString()
            if (task.isNotEmpty()) {
                taskList.add(task)
                taskAdapter.notifyDataSetChanged()
                editTextTask.text.clear()
                showInterstitialAd()
            } else {
                showEmptyTaskDialog()
            }
        }
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
            }
        })
    }

    private fun showInterstitialAd() {
        interstitialAd?.let { ad ->
            ad.show(this)
        }
    }
    private fun showEmptyTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_empty_task, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val buttonOk: Button = dialogView.findViewById(R.id.buttonOk)
        buttonOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}