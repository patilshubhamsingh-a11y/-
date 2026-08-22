package com.example.player

import com.example.data.model.BhajanItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BhajanPlayerManager private constructor() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null

    private val _currentBhajan = MutableStateFlow<BhajanItem?>(null)
    val currentBhajan: StateFlow<BhajanItem?> = _currentBhajan.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionSec = MutableStateFlow(0)
    val currentPositionSec: StateFlow<Int> = _currentPositionSec.asStateFlow()

    private val _durationSec = MutableStateFlow(240) // default 4 min
    val durationSec: StateFlow<Int> = _durationSec.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private var playlist: List<BhajanItem> = emptyList()
    private var currentIndex: Int = -1

    fun setPlaylist(list: List<BhajanItem>) {
        this.playlist = list
    }

    fun playBhajan(bhajan: BhajanItem, fullList: List<BhajanItem> = emptyList()) {
        if (fullList.isNotEmpty()) {
            this.playlist = fullList
        }
        this.currentIndex = playlist.indexOfFirst { it.id == bhajan.id }.takeIf { it >= 0 } ?: 0

        _currentBhajan.value = bhajan
        _isPlaying.value = true
        _currentPositionSec.value = 0
        _durationSec.value = parseDurationToSeconds(bhajan.duration)

        startProgressTicker()
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            resume()
        }
    }

    fun resume() {
        if (_currentBhajan.value != null) {
            _isPlaying.value = true
            startProgressTicker()
        }
    }

    fun pause() {
        _isPlaying.value = false
        progressJob?.cancel()
    }

    fun stop() {
        _isPlaying.value = false
        _currentPositionSec.value = 0
        progressJob?.cancel()
    }

    fun seekTo(seconds: Int) {
        _currentPositionSec.value = seconds.coerceIn(0, _durationSec.value)
    }

    fun toggleLoop() {
        _isLooping.value = !_isLooping.value
    }

    fun playNext() {
        if (playlist.isEmpty()) return
        val nextIndex = (currentIndex + 1) % playlist.size
        playBhajan(playlist[nextIndex])
    }

    fun playPrevious() {
        if (playlist.isEmpty()) return
        val prevIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        playBhajan(playlist[prevIndex])
    }

    private fun startProgressTicker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (_isPlaying.value) {
                delay(1000)
                if (_currentPositionSec.value < _durationSec.value) {
                    _currentPositionSec.value += 1
                } else {
                    if (_isLooping.value) {
                        _currentPositionSec.value = 0
                    } else {
                        playNext()
                    }
                }
            }
        }
    }

    private fun parseDurationToSeconds(durStr: String): Int {
        return try {
            // Marathi digits or English digits parsing
            val converted = durStr
                .replace('०', '0').replace('१', '1').replace('२', '2')
                .replace('३', '3').replace('४', '4').replace('५', '5')
                .replace('६', '6').replace('७', '7').replace('८', '8')
                .replace('९', '9')
            val parts = converted.split(":")
            if (parts.size == 2) {
                val min = parts[0].trim().toIntOrNull() ?: 3
                val sec = parts[1].trim().toIntOrNull() ?: 30
                min * 60 + sec
            } else 240
        } catch (e: Exception) {
            240
        }
    }

    companion object {
        @Volatile
        private var instance: BhajanPlayerManager? = null

        fun getInstance(): BhajanPlayerManager {
            return instance ?: synchronized(this) {
                instance ?: BhajanPlayerManager().also { instance = it }
            }
        }
    }
}
