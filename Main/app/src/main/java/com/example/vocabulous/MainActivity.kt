package com.example.vocabulous

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabulous.databinding.ActivityMainBinding
import com.example.vocabulous.ui.theme.MeaningAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MeaningAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchButton.setOnClickListener{
            val word = binding.searchInput.text.toString()
            getMeaning(word)
        }
        adapter = MeaningAdapter(emptyList())
        binding.meaningRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.meaningRecyclerView.adapter = adapter
    }
    private fun getMeaning(word : String) {
        setInProgress(true)
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.dictionaryApi.getMeaning(word)
                if (response.body() == null){
                    runOnUiThread{
                        setInProgress(false)
                        Toast.makeText(applicationContext, "No results found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        setInProgress(false)
                        response.body()?.first()?.let {
                            setUI(it)
                        }
                    }
                }
            } catch (e : Exception){
                runOnUiThread{
                    setInProgress(false)
                    Toast.makeText(applicationContext, "Something went wrong :(", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun setUI(response : WordResult) {
        binding.wordTextview.text = response.word
        binding.phoneticTextview.text = response.phonetic
        adapter.updateNewData(response.meanings)
    }
    private fun setInProgress(inProgress : Boolean) {
        if(inProgress) {
            binding.searchButton.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.searchButton.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}