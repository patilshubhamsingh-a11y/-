package com.example.data.model

data class DailyDarshan(
    val id: String = "today",
    val dateStr: String = "",
    val marathiDate: String = "",
    val photoUrl: String = "",
    val localDrawableName: String = "daily_darshan_today",
    val blessingMessage: String = "",
    val published: Boolean = true,
    val specialOccasion: String = ""
)

data class LiveDarshanConfig(
    val isLive: Boolean = false,
    val streamUrl: String = "",
    val title: String = "श्री संत गजानन महाराज मंदिर थेट दर्शन",
    val description: String = "घिर्णी, ता. मलकापूर, जि. बुलढाणा",
    val scheduleTimes: String = "सकाळची आरती: ०६:०० AM | दुपारची आरती: १२:०० PM | संध्या आरती: ०७:०० PM | शेजारती: ०९:०० PM",
    val offlineMessage: String = "सध्या थेट दर्शन उपलब्ध नाही. दैनंदिन आरती व विशेष उत्सवाच्या वेळी थेट प्रक्षेपण सुरू केले जाते.",
    val cameraLocation: String = "मुख्य गाभारा (श्री गजानन महाराज समाधी/मूर्ती)",
    val streamType: String = "HLS_NVR", // HLS_NVR, YOUTUBE, RTMP, SECURE_STREAM
    val lastUpdated: Long = System.currentTimeMillis()
)

data class BhajanItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String, // आरती, भजन, स्तोत्र, जप
    val duration: String,
    val audioUrl: String = "",
    val lyrics: String = "",
    val isFavorite: Boolean = false
)

data class GalleryItem(
    val id: String,
    val title: String,
    val category: String, // श्री गजानन महाराज, घिर्णी मंदिर, मंदिर परिसर, उत्सव, गुरुवार दर्शन, महाप्रसाद, विशेष कार्यक्रम
    val imageUrl: String = "",
    val localDrawableName: String = "",
    val description: String = "",
    val date: String = ""
)

data class ChapterItem(
    val chapterNumber: Int,
    val title: String,
    val summary: String,
    val fullText: String,
    val versesCount: Int = 108
)

data class TempleHistorySection(
    val id: String,
    val title: String,
    val subtitle: String,
    val content: String,
    val highlight: String = "",
    val iconName: String = "temple"
)

data class BhaktiMessage(
    val id: String,
    val quote: String,
    val author: String = "॥ गण गण गणात बोते ॥",
    val date: String = "",
    val dayName: String = ""
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: String = "GENERAL", // LIVE, DARSHAN, UTSAV, GENERAL
    val isRead: Boolean = false
)
