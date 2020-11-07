package com.example.forkify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.forkify.R
import com.example.forkify.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var viewModel: RecipeViewModel
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        
        findNavController(R.id.navHostFragment).addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.recipeFragment -> hideBottomNavigation()
                R.id.searchFragment -> showBottomNavigation()
                R.id.favoriteFragment -> showBottomNavigation()
            }
        }

        viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
    }

    private fun hideBottomNavigation(){
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation(){
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}