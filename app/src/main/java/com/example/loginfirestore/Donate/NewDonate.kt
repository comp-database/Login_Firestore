package com.example.loginfirestore.Donate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginfirestore.MainActivity2
import com.example.loginfirestore.R
import com.example.loginfirestore.databinding.ActivityNewDonateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewDonate : AppCompatActivity() {
    lateinit var binding: ActivityNewDonateBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_donate)
        binding = ActivityNewDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var CurrentStatus : Int = 1

        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.BtnDonateNewUser.setOnClickListener {
            val Name = binding.Name.text.toString()
            val Age = binding.Age.text.toString().toInt()
            val BloodGrp = binding.BloodGrp.text.toString()
            val MedHistory = binding.MedHistory.text.toString()

            if (Name.isNotEmpty()  && BloodGrp.isNotEmpty()){
                val intent = Intent(this, OldDonate::class.java)
                startActivity(intent)
                CurrentStatus = 0
                val DonorInfo = hashMapOf(
                    "Name" to Name,
                    "Age" to Age,
                    "BloodGroup" to BloodGrp,
                    "MedHistory" to MedHistory,
                    "CurrentStatus" to CurrentStatus
                )
                db.collection("Donor Information").document(firebaseAuth.currentUser?.email.toString()).set(DonorInfo).addOnSuccessListener {
                    Toast.makeText(this, "Your response Stored Successfully", Toast.LENGTH_SHORT).show()
                }
            }else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
}