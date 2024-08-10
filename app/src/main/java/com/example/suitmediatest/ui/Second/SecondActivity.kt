package com.example.suitmediatest.ui.Second

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.suitmediatest.ui.First.FirstActivity
import com.example.suitmediatest.ui.Third.ThirdActivity
import com.example.suitmediatest.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    private lateinit var username: TextView
    private lateinit var selectedUser: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnChooseUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        setupComponents()
        setupListener()
    }

    private fun initComponents() {
        username = binding.name
        btnBack = binding.btnBack
        selectedUser = binding.selectedUser
        btnChooseUser = binding.btnChooseUser
    }

    private fun setupComponents() {
        val username = intent.getStringExtra("username")
        val selectedUser = intent.getStringExtra("selected_username")

        this.username.text = username ?: "Guest"
        this.selectedUser.text = selectedUser ?: "Selected User Name"
    }

    private fun setupListener() {
        val username = intent.getStringExtra("username")
        val selectedUsername = intent.getStringExtra("selected_username")

        btnBack.setOnClickListener {
            startActivity(Intent(this, FirstActivity::class.java))
        }

        btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("selected_username", selectedUsername)
            startActivity(intent)
        }
    }
}