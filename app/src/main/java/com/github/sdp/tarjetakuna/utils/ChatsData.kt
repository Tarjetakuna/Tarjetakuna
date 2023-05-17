package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.Date

object ChatsData {
    val fakeCoordinates: Coordinates = Coordinates(0.0, 0.0)
    val fakeCards: ArrayList<DBMagicCard> = ArrayList()

    val fakeUser1: User =
        User("user_id1", "fakeUser1.email@gmail.com", fakeCards, fakeCoordinates)
    val fakeLastReadUser1 = Date(1683828691000)

    val fakeUser2: User =
        User("user_id2", "fakeUser2.email@gmail.com", fakeCards, fakeCoordinates)
    val fakeLastReadUser2 = Date(1683828791000)

    val fakeUser3: User =
        User("user_id3", "fakeUser3.email@gmail.com", fakeCards, fakeCoordinates)
    val fakeLastReadUser3 = Date(1683928691000)

    val fakeMessage1_1: Message =
        Message(1325, fakeUser1, fakeUser2, "Hello", Date(1683828633000))
    val fakeMessage1_2: Message =
        Message(1326, fakeUser2, fakeUser1, "Hello you too", Date(1683828661000))
    val fakeMessage1_3: Message =
        Message(1327, fakeUser1, fakeUser2, "What are you doing ?", Date(1683828691000))
    val fakeMessage1_4: Message =
        Message(1328, fakeUser2, fakeUser1, "Nothing, you ?", Date(1683828791000))
    val fakeMessage1_5: Message =
        Message(
            1329,
            fakeUser1,
            fakeUser2,
            "Chilling in bed doing sdp \n but i m so lazy \n i hate it asd adsfsdf asdasdfs adf asdssd fsd fff  fffff",
            Date(1683828891000)
        )

    val fakeMessages1: ArrayList<Message> =
        arrayListOf(fakeMessage1_1, fakeMessage1_2, fakeMessage1_3, fakeMessage1_4, fakeMessage1_5)

    val fakeChat1: Chat =
        Chat(1200, fakeUser1, fakeUser2, fakeMessages1, fakeLastReadUser1, fakeLastReadUser2)

    val fakeMessage2_1: Message =
        Message(1328, fakeUser2, fakeUser1, "Bonjour", Date(1683828633000))
    val fakeMessage2_2: Message =
        Message(1329, fakeUser1, fakeUser2, "Bonjour you too", Date(1683828661000))
    val fakeMessage2_3: Message =
        Message(1330, fakeUser2, fakeUser1, "Qu est ce que tu fais?", Date(1683848691000))

    val fakeMessages2 =
        arrayListOf(fakeMessage2_1, fakeMessage2_2, fakeMessage2_3)
    val fakeChat2: Chat =
        Chat(1201, fakeUser1, fakeUser2, fakeMessages2, fakeLastReadUser1, fakeLastReadUser2)

    val fakeChat1_emptyMsgs: Chat =
        Chat(1202, fakeUser1, fakeUser2, ArrayList(), fakeLastReadUser1, fakeLastReadUser2)

    val fakeChat3: Chat =
        Chat(1203, fakeUser1, fakeUser3, fakeMessages1, fakeLastReadUser1, fakeLastReadUser3)

    val fakeChats1: ArrayList<Chat> = arrayListOf(fakeChat2, fakeChat1)
    val fakeChats2: ArrayList<Chat> = arrayListOf(fakeChat3)

    val fakeChats_empty: ArrayList<Chat> = ArrayList()
}
