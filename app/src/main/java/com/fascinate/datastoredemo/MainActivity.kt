package com.fascinate.datastoredemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.fascinate.datastoredemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                saveValue(binding.prefKey.text.toString(), binding.prefValue.text.toString())
                binding.prefKey.text = null
                binding.prefValue.text = null
                Toast.makeText(applicationContext, "Saved...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonGet.setOnClickListener {
            lifecycleScope.launch {
                val value = getValue()
                binding.prefGetText.text = value ?: "No Value found..."
            }
        }

    }

    private suspend fun saveValue(key: String, value: String) {
        val storeKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[storeKey] = value
        }

    }


    private suspend fun getValue(): String? {
        binding.prefGetKey.let { key ->
            val storeKey = stringPreferencesKey(key.text.toString())
            val preferences = dataStore.data.first()
            return preferences[storeKey]
        }
    }


}