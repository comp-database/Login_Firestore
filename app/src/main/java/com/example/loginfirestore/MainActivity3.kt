package com.example.loginfirestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginfirestore.Donate.NewDonate
import com.example.loginfirestore.Donate.OldDonate
import com.example.loginfirestore.databinding.ActivityMain3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    private lateinit var firebaseAuth: FirebaseAuth
    var CurrentStatus : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main3)

        val db  = Firebase.firestore

        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.emailText.text = firebaseAuth.currentUser?.email.toString()

        binding.Logout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val referenceTodbUser = db.collection("User Information").document(firebaseAuth.currentUser?.email.toString())
        referenceTodbUser.get().addOnSuccessListener {
            if(it != null){
                binding.nameText.text = it.data?.get("Name").toString()
            }
        }.addOnFailureListener {
                Toast.makeText(this, "Failed to get data", Toast.LENGTH_SHORT).show()
            }


        val referenceTodbDonor = db.collection("Donor Information").document(firebaseAuth.currentUser?.email.toString())
        referenceTodbDonor.get().addOnSuccessListener {
            if(it != null){
                CurrentStatus = it.data?.get("CurrentStatus").toString().toInt()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get data", Toast.LENGTH_SHORT).show()
        }

        binding.donateBlood.setOnClickListener {
            if(CurrentStatus == 1){
                val intent = Intent(this, NewDonate::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, OldDonate::class.java)
                startActivity(intent)
            }
        }

    }

}

/**
val UserInfo = hashMapOf(
"Name" to name,
"Email" to email
)
db.collection("User Information").document(firebaseAuth.currentUser?.email.toString()).set(UserInfo).addOnSuccessListener {
Toast.makeText(this, "Your response Stored Successfully", Toast.LENGTH_SHORT).show()
}
 */