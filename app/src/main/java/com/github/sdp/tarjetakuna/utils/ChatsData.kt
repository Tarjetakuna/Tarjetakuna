package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.database.DBChat
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.DBMessage
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.Date

object ChatsData {

    // Valid data used for testing.

    val fakeCoordinates: Coordinates = Coordinates(0.0, 0.0)
    val fakeCards: ArrayList<DBMagicCard> = ArrayList()

    val user_id1 = "user_id1"
    val user_id2 = "user_id2"
    val user_id3 = "user_id3"

    val user_mail1 = "fakeUser1.email@gmail.com"
    val user_mail2 = "fakeUser2.email@gmail.com"
    val user_mail3 = "fakeUser3.email@gmail.com"

    val msg_uid1_1 = "msg_uid1_1"
    val msg_uid1_2 = "msg_uid1_2"
    val msg_uid1_3 = "msg_uid1_3"
    val msg_uid1_4 = "msg_uid1_4"
    val msg_uid1_5 = "msg_uid1_5"

    val msg_uid2_1 = "msg_uid2_1"
    val msg_uid2_2 = "msg_uid2_2"
    val msg_uid2_3 = "msg_uid2_3"

    val chat_uid1 = "chat_uid1"
    val chat_uid2 = "chat_uid2"
    val chat_uid3 = "chat_uid3"
    val chat_uid4 = "chat_uid4"

    val date_1 = 1683828633000
    val date_2 = 1683828661000
    val date_3 = 1683828691000
    val date_4 = 1683828791000
    val date_5 = 1683928691000

    val msg1_1_content = "Hello"
    val msg1_2_content = "Hello you too"
    val msg1_3_content = "What are you doing ?"
    val msg1_4_content = "Nothing, you ?"
    val msg1_5_content =
        "Chilling in bed doing sdp \n but i m so lazy \n i hate it asd adsfsdf asdasdfs adf asdssd fsd fff  fffff"

    val msg2_1_content = "Bonjour"
    val msg2_2_content = "Bonjour you too"
    val msg2_3_content = "Qu est ce que tu fais?"

    val fakeUser1: User = User(user_id1, user_mail1, fakeCards, fakeCoordinates)
    val fakeLastReadUser1 = date_3

    val fakeUser2: User = User(user_id2, user_mail2, fakeCards, fakeCoordinates)
    val fakeLastReadUser2 = date_4

    val fakeUser3: User = User(user_id3, user_mail3, fakeCards, fakeCoordinates)
    val fakeLastReadUser3 = date_5

    val fakeMessage1_1: Message = Message(msg_uid1_1, fakeUser1, fakeUser2, msg1_1_content, date_1)
    val fakeMessage1_2: Message = Message(msg_uid1_2, fakeUser2, fakeUser1, msg1_2_content, date_2)
    val fakeMessage1_3: Message = Message(msg_uid1_3, fakeUser1, fakeUser2, msg1_3_content, date_3)
    val fakeMessage1_4: Message = Message(msg_uid1_4, fakeUser2, fakeUser1, msg1_4_content, date_4)
    val fakeMessage1_5: Message = Message(msg_uid1_5, fakeUser1, fakeUser2, msg1_5_content, date_5)

    val fakeMessages1: ArrayList<Message> =
        arrayListOf(fakeMessage1_1, fakeMessage1_2, fakeMessage1_3, fakeMessage1_4, fakeMessage1_5)

    val fakeChat1: Chat =
        Chat(chat_uid1, fakeUser1, fakeUser2, fakeMessages1, fakeLastReadUser1, fakeLastReadUser2)

    val fakeMessage2_1: Message = Message(msg_uid2_1, fakeUser2, fakeUser1, msg2_1_content, date_1)
    val fakeMessage2_2: Message = Message(msg_uid2_2, fakeUser1, fakeUser2, msg2_2_content, date_2)
    val fakeMessage2_3: Message = Message(msg_uid2_3, fakeUser2, fakeUser1, msg2_3_content, date_3)

    val fakeMessages2 = arrayListOf(fakeMessage2_1, fakeMessage2_2, fakeMessage2_3)
    val fakeChat2: Chat =
        Chat(chat_uid2, fakeUser1, fakeUser2, fakeMessages2, fakeLastReadUser1, fakeLastReadUser2)

    val fakeChat1_emptyMsgs: Chat =
        Chat(chat_uid3, fakeUser1, fakeUser2, ArrayList(), fakeLastReadUser1, fakeLastReadUser2)

    val fakeChat4: Chat =
        Chat(chat_uid4, fakeUser1, fakeUser3, fakeMessages1, fakeLastReadUser1, fakeLastReadUser3)

    val fakeChats1: ArrayList<Chat> = arrayListOf(fakeChat2, fakeChat1)
    val fakeChats2: ArrayList<Chat> = arrayListOf(fakeChat4)

    val fakeChats_empty: ArrayList<Chat> = ArrayList()

    // DB data used for testing.

    val fakeDBMessage1_1: DBMessage =
        DBMessage(msg_uid1_1, user_id1, user_id2, msg1_1_content, date_1)
    val fakeDBMessage1_2: DBMessage =
        DBMessage(msg_uid1_2, user_id2, user_id1, msg1_2_content, date_2)
    val fakeDBMessage1_3: DBMessage =
        DBMessage(msg_uid1_3, user_id1, user_id2, msg1_3_content, date_3)
    val fakeDBMessage1_4: DBMessage =
        DBMessage(msg_uid1_4, user_id2, user_id1, msg1_4_content, date_4)
    val fakeDBMessage1_5: DBMessage =
        DBMessage(msg_uid1_5, user_id1, user_id2, msg1_5_content, date_5)

    val fakeDBMessage2_1: DBMessage =
        DBMessage(msg_uid2_1, user_id2, user_id1, msg2_1_content, date_1)
    val fakeDBMessage2_2: DBMessage =
        DBMessage(msg_uid2_2, user_id1, user_id2, msg2_2_content, date_2)
    val fakeDBMessage2_3: DBMessage =
        DBMessage(msg_uid2_3, user_id2, user_id1, msg2_3_content, date_3)

    val fakeDBChat1: DBChat = DBChat(
        chat_uid1, user_id1, user_id2,
        arrayListOf(msg_uid1_1, msg_uid1_2, msg_uid1_3, msg_uid1_4, msg_uid1_5),
        fakeLastReadUser1, fakeLastReadUser2
    )

    val fakeDBChat2: DBChat = DBChat(
        chat_uid2, user_id1, user_id2,
        arrayListOf(msg_uid2_1, msg_uid2_2, msg_uid2_3),
        fakeLastReadUser1, fakeLastReadUser2
    )

    val fakeDBChat3: DBChat = DBChat(
        chat_uid3, user_id1, user_id2,
        ArrayList(),
        fakeLastReadUser1, fakeLastReadUser2
    )

    val fakeDBChat4: DBChat = DBChat(
        chat_uid4, user_id1, user_id3,
        arrayListOf(msg_uid1_1, msg_uid1_2, msg_uid1_3, msg_uid1_4, msg_uid1_5),
        fakeLastReadUser1, fakeLastReadUser3
    )
}
