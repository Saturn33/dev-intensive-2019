package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private fun unreadableMessageCount(): Int = messages.count { !it.isReaded }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private fun lastMessageDate(): Date? = if (messages.isEmpty()) null else messages.last().date

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private fun lastMessageShort(): Pair<String, String>{
        if (messages.isEmpty())
            return "Сообщений ещё нет" to ""
        else
        {
            with (messages.last()){
                return when (this) {
                    is TextMessage -> text ?: ""
                    is ImageMessage -> "${from.firstName} - отправил фото"
                    else -> ""
                } to (from.firstName ?: "")
            }

        }
    }

    private  fun isSingle() : Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            val user = members.first()
            ChatItem(
                id,
                null,
                //"",
                Utils.toInitials(user.firstName, null) ?: "??",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastMessageShort().second
            )
        }

    }

}

enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}
