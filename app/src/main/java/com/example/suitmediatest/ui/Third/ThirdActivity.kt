package com.example.suitmediatest.ui.Third

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitmediatest.ui.Second.SecondActivity
import com.example.suitmediatest.databinding.ActivityThirdBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdActivity : AppCompatActivity() {

    private var currentPage = 1
    private lateinit var btnBack: ImageButton
    private lateinit var userList: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        setupComponents()
        setupListener()
    }

    private fun initComponents() {
        btnBack = binding.btnBack
        userList = binding.userList
        swipeRefresh = binding.refreshLayout
    }

    private fun setupComponents() {
        userAdapter = UserAdapter { selectedUser ->
            val username = intent.getStringExtra("username")
            val selectedUsername = "${selectedUser.firstName} ${selectedUser.lastName}"

            val intent = Intent(this@ThirdActivity, SecondActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("selected_username", selectedUsername)
            startActivity(intent)

        }

        userList.apply {
            layoutManager = LinearLayoutManager(this@ThirdActivity)
            adapter = userAdapter
        }
    }

    private fun setupListener() {
        val username = intent.getStringExtra("username")
        val selectedUsername = intent.getStringExtra("selected_username")

        btnBack.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("selected_username", selectedUsername)
            startActivity(intent)
        }

        swipeRefresh.setOnRefreshListener {
            currentPage = 1
            fetchUserData()
        }

        userList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    currentPage++
                    fetchUserData()
                }
            }
        })

        fetchUserData()
    }

    private fun fetchUserData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getUsers(page = 1, perPage = 10)
                if (response.isSuccessful) {
                    val users = response.body()?.data ?: emptyList()
                    withContext(Dispatchers.Main) {
                        if (currentPage == 1) {
                            userAdapter.setData(users)
                        } else {
                            userAdapter.loadMore(users)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                swipeRefresh.isRefreshing = false
            }
        }
    }
}