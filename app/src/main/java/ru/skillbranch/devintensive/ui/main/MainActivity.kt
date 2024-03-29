package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.setTextColor
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel
//import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите название чата"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })

/*
        menuInflater.inflate(R.menu.menu_mode, menu)
        val modeItem = menu?.findItem(R.id.action_mode)
        modeItem.setOnMenuItemClickListener {
            ProfileViewModel().switchTheme()
            Toast.makeText(this, "Clicked menu, current theme: ${ProfileViewModel().getTheme().value}", Toast.LENGTH_SHORT).show()
            true
        }
*/

        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {
            if (it.chatType == ChatType.ARCHIVE) {
                Intent(this, ArchiveActivity::class.java).also { intent -> startActivity(intent) }
            } else {
                Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
            }
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(resources.getDrawable(R.drawable.ic_archive_black_24dp, theme), chatAdapter) { chatItem ->
            viewModel.addToArchive(chatItem.id)
            Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${chatItem.title} в архив?", Snackbar.LENGTH_LONG).apply {
                view.setBackgroundColor(resources.getColor(R.color.color_primary, theme))
            }
                .setTextColor(resources.getColor(R.color.color_snack_text, theme))
                .setAction(R.string.undo) { viewModel.restoreFromArchive(chatItem.id) }
                .show()
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with (rv_chat_list) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            Intent(this, GroupActivity::class.java).also { intent -> startActivity(intent) }
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer {chatAdapter.updateData(it)})
    }
}
