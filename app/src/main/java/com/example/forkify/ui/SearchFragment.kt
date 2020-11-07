package com.example.forkify.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forkify.R
import com.example.forkify.adapters.RecipeAdapter
import com.example.forkify.adapters.RecipeLoadStateAdapter
import com.example.forkify.data.local.RecipeEntity
import com.example.forkify.data.remote.Recipe
import com.example.forkify.databinding.FragmentSearchBinding
import com.example.forkify.other.Operations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(), RecipeAdapter.Interaction {
    
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: RecipeAdapter
    private var searchJob: Job? = null
    private lateinit var activityMain: MainActivity
    private var searchQuery: String = ""
    private val viewModel by viewModels<RecipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        activityMain = activity as MainActivity

        if (viewModel.currentQuery.value == null) {
            activityMain.supportActionBar?.title = "Forkify"
        } else {
            activityMain.supportActionBar?.title = MainActivity.viewModel.currentQuery.value
        }
        initAdapter()

        viewModel.result.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                recipeAdapter.submitData(it)
            }
        })

        refreshSwipe()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.appbar_menu, menu)

        val searchItem = menu.findItem(R.id.searchIcon)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query!!
                search(query)
                searchView.setQuery("", false)
                searchView.clearFocus()
                searchItem.collapseActionView()
                activityMain.supportActionBar?.title = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    private fun initAdapter() {
        recipeAdapter = RecipeAdapter(this)
        binding.recipeRecyclerView.apply {
            adapter = recipeAdapter.withLoadStateHeaderAndFooter(
                header = RecipeLoadStateAdapter {
                    recipeAdapter.retry()
                },
                footer = RecipeLoadStateAdapter {
                    recipeAdapter.retry()
                }
            )
            layoutManager = LinearLayoutManager(requireView().context)
            setHasFixedSize(true)
        }
    }


    override fun onItemSelected(item: Recipe) {

        val action = SearchFragmentDirections.actionSearchFragmentToRecipeFragment(
            item.imageUrl,
            item.recipeId!!
        )
        findNavController().navigate(action)
    }

    override fun onAddItem(item: Recipe) {
        val recipeEntity =
            RecipeEntity(item.recipeId!!, item.title!!, item.publisher!!, item.imageUrl!!)
        viewModel.saveRecipe(recipeEntity)
        Toast.makeText(requireContext(), "Recipe has been added Successfully", Toast.LENGTH_SHORT)
            .show()
    }


    private fun search(query: String) {

        val connected = Operations.checkForInternetConnection(requireContext())
        if (!connected) {
            Toast.makeText(requireContext(), "There is no network", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.searchRecipe(query)

        // Make sure we cancel the previous job before creating a new one
//        searchJob?.cancel()
//        searchJob = lifecycleScope.launch {
//                recipeAdapter.submitData(viewModel.result.value!!)
//        }

        recipeAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {
                binding.recipeProgressBar.visibility = View.VISIBLE
            } else {
                binding.recipeProgressBar.visibility = View.GONE
            }
        }
    }

    private fun refreshSwipe() {
        val connected = Operations.checkForInternetConnection(requireContext())

        binding.refreshLayout.setOnRefreshListener {
            if (connected && searchQuery != "") {
                search(searchQuery)
                binding.refreshLayout.isRefreshing = false
            } else {
                binding.refreshLayout.isRefreshing = false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}