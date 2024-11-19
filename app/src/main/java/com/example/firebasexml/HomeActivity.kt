package com.example.firebasexml

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.example.firebasexml.databinding.ActivityAuthBinding
import com.example.firebasexml.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.remoteConfig

enum class ProviderType {
    BASIC,
    GOOGLE
}

private lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()


    private fun setup(email: String, provider: String) {
        title = "Inicio"
        binding.emailTextView.text = email
        binding.providerTextView.text = provider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setup(email ?: "", provider ?: "")

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()


        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener {

            if (it.isSuccessful) {
                binding.errorText.text = Firebase.remoteConfig.getString("errorText")
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()

            prefs.clear()
            prefs.apply()
        }

        binding.SaveButton.setOnClickListener {
            if (email != null) {
                db.collection("users").document(email).set(
                    hashMapOf("phone" to binding.phoneTV.text.toString())
                )
            }
        }
        binding.RecoverButton.setOnClickListener {
            if (email != null) {
                db.collection("users").document(email).get().addOnSuccessListener {
                   binding.phoneTV.setText(it.get("phone") as String?)
                }
            }
        }
        binding.deleteButton.setOnClickListener {
            if (email != null) {
                db.collection("users").document(email).delete()
            }
        }
    }
}