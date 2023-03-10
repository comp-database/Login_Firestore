package com.example.loginfirestore.Donate

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.loginfirestore.Helpers.DataHelper
import com.example.loginfirestore.R
import com.example.loginfirestore.databinding.ActivityOldDonateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class OldDonate : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: ActivityOldDonateBinding
    lateinit var dataHelper: DataHelper
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOldDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()

        dataHelper = DataHelper(applicationContext)

        binding.startButton.setOnClickListener { startStopAction() }
        binding.resetButton.setOnClickListener{ resetAction() }

        if (dataHelper.timerCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }
        timer.scheduleAtFixedRate(TimeTask(), 0, 500)


        binding.imgchg.setOnClickListener {
            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.green))
        }

//        val timelist = binding.timeTV.text.toList()
//        val one = timelist[6].digitToInt()
//        val two = timelist[7].digitToInt()
//        if (one < 1 &&  two >= 0){
//            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.red))
//        }else if (one == 1 &&  two == 0){
//            startStopAction()
//            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.green))
//            alert()
//        }else{
////                        startStopAction()
//            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.yellow))
//        }
        val referenceTodbDonor =
            db.collection("Donor Information").document(firebaseAuth.currentUser?.email.toString())
        referenceTodbDonor.get().addOnSuccessListener {
            if (it != null) {
                binding.DBloodGrp.text = it.data?.get("BloodGroup").toString()
                binding.DName.text = it.data?.get("Name").toString()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get data", Toast.LENGTH_SHORT).show()
        }
    }
    private inner class TimeTask: TimerTask()
    {
        override fun run()
        {
            if(dataHelper.timerCounting() )
            {
                val time = Date().time - dataHelper.startTime()!!.time
                this@OldDonate.runOnUiThread(java.lang.Runnable {
                    binding.timeTV.text = timeStringFromLong(time)

                    //code for flag change after 10 sec
                    val timelist = binding.timeTV.text.toList()
                    val one = timelist[6].digitToInt()
                    val two = timelist[7].digitToInt()
                    if (one < 1 &&  two >= 0){
                        binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.red))
                    }else if (one == 1 &&  two == 0){
                        startStopAction()
                        binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.yellow))
                        alert()
                    }else{
//                        startStopAction()
                        binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.green))
                    }
                })
                //

            }
        }
    }
    private fun alert(){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Do you Want to donate blood")
        //set message for alert dialog
        builder.setMessage("You're complete with 3 months duration")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.green))
//            currStatus = 1
        }
        //performing cancel action
        builder.setNeutralButton("Not currently"){dialogInterface , which ->
            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.yellow))
//            currStatus = -1
            }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            resetAction()
            binding.flagImg.setImageDrawable(ContextCompat.getDrawable(this@OldDonate, R.drawable.red))
//            currStatus = 0
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    private fun resetAction()
    {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }
    private fun stopTimer()
    {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)
    }
    private fun startTimer()
    {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.stop)
    }
    private fun startStopAction()
    {
        if(dataHelper.timerCounting())
        {
            dataHelper.setStopTime(Date())
            stopTimer()
        }
        else
        {
            if(dataHelper.stopTime() != null)
            {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            }
            else
            {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }
    private fun calcRestartTime(): Date
    {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }
    private fun timeStringFromLong(ms: Long): String
    {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }
    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String
    {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}