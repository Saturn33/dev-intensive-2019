package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        val archivedChats = chats.filter { it.isArchived }
        if (archivedChats.isEmpty())
        {
            return@map chats
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
        }
        else
        {
            val listWArchive = mutableListOf<ChatItem>(makeArchiveItem(archivedChats))
            listWArchive.addAll(chats.filter { !it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
            )
            return@map listWArchive
        }

    }


    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()
        val filterF = {
            val queryStr = query.value!!
            val chatValues = chats.value!!

            result.value = if (queryStr.isEmpty()) chatValues
            else chatValues.filter {it.title.contains(queryStr, true)}
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) {filterF.invoke()}
        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

    fun makeArchiveItem(archived: List<Chat>) :ChatItem {
        val count = archived.fold(0) { acc, chat -> acc + chat.unreadableMessageCount() }

        val lastChat: Chat = if (archived.none { it.unreadableMessageCount() != 0 }) archived.last() else
            archived.filter { it.unreadableMessageCount() != 0 }.maxBy { it.lastMessageDate()!! }!!

        return ChatItem(
            "-1",
            null,
            "",
            "Архив чатов",
            lastChat.lastMessageShort().first,
            count,
            lastChat.lastMessageDate()?.shortFormat(),
            false,
            ChatType.ARCHIVE,
            lastChat.lastMessageShort().second
        )
    }
}