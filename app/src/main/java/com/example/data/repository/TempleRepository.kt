package com.example.data.repository

import com.example.data.local.DevotionalDataStore
import com.example.data.local.suvichar.DailySuvicharManager
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TempleRepository private constructor() {

    private val _dailyDarshan = MutableStateFlow(DevotionalDataStore.initialDailyDarshan)
    val dailyDarshan: StateFlow<DailyDarshan> = _dailyDarshan.asStateFlow()

    private val _liveDarshanConfig = MutableStateFlow(DevotionalDataStore.initialLiveConfig)
    val liveDarshanConfig: StateFlow<LiveDarshanConfig> = _liveDarshanConfig.asStateFlow()

    private val _bhajans = MutableStateFlow(DevotionalDataStore.aartisAndBhajans)
    val bhajans: StateFlow<List<BhajanItem>> = _bhajans.asStateFlow()

    private val _gallery = MutableStateFlow(DevotionalDataStore.galleryItems)
    val gallery: StateFlow<List<GalleryItem>> = _gallery.asStateFlow()

    private val _parayanChapters = MutableStateFlow(DevotionalDataStore.parayanChapters)
    val parayanChapters: StateFlow<List<ChapterItem>> = _parayanChapters.asStateFlow()

    private val _historySections = MutableStateFlow(DevotionalDataStore.templeHistorySections)
    val historySections: StateFlow<List<TempleHistorySection>> = _historySections.asStateFlow()

    private val _bhaktiMessages = MutableStateFlow(DevotionalDataStore.dailyBhaktiMessages)
    val bhaktiMessages: StateFlow<List<BhaktiMessage>> = _bhaktiMessages.asStateFlow()

    private val _notifications = MutableStateFlow(DevotionalDataStore.initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Devotee Name (from Login / User preference or default "प्रिय भक्त")
    private val _devoteeName = MutableStateFlow("प्रिय भक्त")
    val devoteeName: StateFlow<String> = _devoteeName.asStateFlow()

    // Optional Admin / Firebase override for Today's Suvichar & Photo
    private val _customDailySuvichar = MutableStateFlow<String?>(null)
    val customDailySuvichar: StateFlow<String?> = _customDailySuvichar.asStateFlow()

    fun updateDevoteeName(name: String) {
        _devoteeName.value = if (name.isBlank()) "प्रिय भक्त" else name.trim()
    }

    fun getEffectiveDailySuvicharText(): String {
        return _customDailySuvichar.value ?: DailySuvicharManager.getTodaySuvichar().suvichar
    }

    fun setCustomDailySuvichar(suvichar: String?) {
        _customDailySuvichar.value = suvichar?.ifBlank { null }
    }

    // Jap Mantra Counter
    private val _japCounter = MutableStateFlow(0)
    val japCounter: StateFlow<Int> = _japCounter.asStateFlow()

    fun incrementJapCounter() {
        _japCounter.update { it + 1 }
    }

    fun resetJapCounter() {
        _japCounter.value = 0
    }

    // Admin updates for Daily Darshan
    fun updateDailyDarshan(
        photoUrl: String,
        message: String,
        marathiDate: String,
        published: Boolean
    ) {
        _dailyDarshan.update {
            it.copy(
                photoUrl = photoUrl,
                blessingMessage = message,
                marathiDate = marathiDate.ifBlank { it.marathiDate },
                published = published
            )
        }
    }

    // Admin updates for Live Darshan
    fun updateLiveDarshanConfig(
        isLive: Boolean,
        streamUrl: String,
        title: String = "श्री संत गजानन महाराज थेट दर्शन",
        streamType: String = "HLS_NVR"
    ) {
        _liveDarshanConfig.update {
            it.copy(
                isLive = isLive,
                streamUrl = streamUrl,
                title = title,
                streamType = streamType,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }

    // Admin Gallery Management
    fun addGalleryItem(title: String, category: String, imageUrl: String, description: String) {
        val newItem = GalleryItem(
            id = "gal_${System.currentTimeMillis()}",
            title = title,
            category = category,
            imageUrl = imageUrl,
            description = description,
            date = "नवीन फोटो"
        )
        _gallery.update { listOf(newItem) + it }
    }

    fun deleteGalleryItem(id: String) {
        _gallery.update { list -> list.filterNot { it.id == id } }
    }

    // Admin Notifications
    fun sendNotification(title: String, message: String, type: String = "GENERAL") {
        val newNotif = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = title,
            message = message,
            timestamp = "आत्ताच पाठवले",
            type = type
        )
        _notifications.update { listOf(newNotif) + it }
    }

    // Bhajan favorite toggle
    fun toggleBhajanFavorite(id: String) {
        _bhajans.update { list ->
            list.map {
                if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
            }
        }
    }

    companion object {
        @Volatile
        private var instance: TempleRepository? = null

        fun getInstance(): TempleRepository {
            return instance ?: synchronized(this) {
                instance ?: TempleRepository().also { instance = it }
            }
        }
    }
}
