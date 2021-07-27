package com.test.pixday

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.pixday.databinding.ActivityMainBinding
import com.test.pixday.ui.PhotoLoadStateAdapter
import com.test.pixday.ui.PhotosAdapter
import com.test.pixday.ui.SearchRepositoriesViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


/**
 * The [MainActivity] class is responsible
 * for accepting inputs from users and relating with [SearchRepositoriesViewModel],
 * then organizing and displaying ShutterStock response to is suitable UI elements.
 * Also, its responsible for managing the state of the application
 * with the use of ViewModels elements and savedInstanceState.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchRepositoriesViewModel
    private val adapter = PhotosAdapter()

    private var searchJob: Job? = null

    private fun search(query: String) {
        //Cancel the previous job before creating a new job
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPhoto(query).collectLatest {
                //Submit PagingData to PhotoAdapters
                adapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Create an instance of SearchRepositoriesViewModel by Injection.
        //Different Injection class is created for each build variant to enhance AndroidTest.
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(SearchRepositoriesViewModel::class.java)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchPhoto.text.trim().toString())
    }

    private fun initAdapter() {

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PhotoLoadStateAdapter { adapter.retry() },
            footer = PhotoLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            //Displays each UI element based of the loadState.
            binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Woops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initSearch(query: String) {
        binding.searchPhoto.setText(query)

        binding.searchPhoto.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatePhotoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchPhoto.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatePhotoListFromInput()
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    //Triggers new photoSearch
    private fun updatePhotoListFromInput() {
        binding.searchPhoto.text.trim().let {
            if (it.isNotEmpty()) {
                search(it.toString())
            }
        }
    }

    //Toggles the visibility
    // for emptyList and list UI element.
    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Spain"
    }
}