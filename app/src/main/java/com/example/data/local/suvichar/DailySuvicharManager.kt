package com.example.data.local.suvichar

import com.example.data.model.DailySuvichar
import java.util.Calendar

object DailySuvicharManager {

    val allSuvichars: List<DailySuvichar> by lazy {
        YearlySuvicharJanToMar.items +
                YearlySuvicharAprToJun.items +
                YearlySuvicharJulToSep.items +
                YearlySuvicharOctToDec.items
    }

    /**
     * Returns today's suvichar based on the device's current date automatically.
     */
    fun getTodaySuvichar(): DailySuvichar {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1 // 1..12
        val day = calendar.get(Calendar.DAY_OF_MONTH) // 1..31

        return getSuvicharForDate(month, day)
    }

    /**
     * Look up suvichar for specific month and day.
     */
    fun getSuvicharForDate(month: Int, day: Int): DailySuvichar {
        val match = allSuvichars.firstOrNull { it.month == month && it.day == day }
        if (match != null) return match

        // Leap year fallback if Feb 29 doesn't match or edge case
        if (month == 2 && day == 29) {
            return allSuvichars.firstOrNull { it.month == 2 && it.day == 28 } ?: allSuvichars.first()
        }

        // Fallback to first item if not found
        return allSuvichars.first()
    }

    /**
     * Converts English numbers to Marathi numerals (e.g. 2026 -> २०२६).
     */
    fun toMarathiDigits(number: Int): String {
        return toMarathiDigits(number.toString())
    }

    fun toMarathiDigits(text: String): String {
        val marathiDigits = charArrayOf('०', '१', '२', '३', '४', '५', '६', '७', '८', '९')
        val sb = StringBuilder()
        for (ch in text) {
            if (ch in '0'..'9') {
                sb.append(marathiDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    /**
     * Returns Marathi Month Name (१ = जानेवारी, २ = फेब्रुवारी, etc.)
     */
    fun getMarathiMonthName(month: Int): String {
        return when (month) {
            1 -> "जानेवारी"
            2 -> "फेब्रुवारी"
            3 -> "मार्च"
            4 -> "एप्रिल"
            5 -> "मे"
            6 -> "जून"
            7 -> "जुलै"
            8 -> "ऑगस्ट"
            9 -> "सप्टेंबर"
            10 -> "ऑक्टोबर"
            11 -> "नोव्हेंबर"
            12 -> "डिसेंबर"
            else -> ""
        }
    }

    /**
     * Returns Marathi Day Name (वार)
     */
    fun getMarathiDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "रविवार"
            Calendar.MONDAY -> "सोमवार"
            Calendar.TUESDAY -> "मंगळवार"
            Calendar.WEDNESDAY -> "बुधवार"
            Calendar.THURSDAY -> "गुरुवार"
            Calendar.FRIDAY -> "शुक्रवार"
            Calendar.SATURDAY -> "शनिवार"
            else -> ""
        }
    }

    /**
     * Formats current date in pure Marathi (उदा. "२२ ऑगस्ट २०२६" or "शनिवार, २२ ऑगस्ट २०२६")
     */
    fun getFormattedMarathiDate(calendar: Calendar = Calendar.getInstance(), includeDayName: Boolean = false): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val dayStr = toMarathiDigits(day)
        val monthStr = getMarathiMonthName(month)
        val yearStr = toMarathiDigits(year)

        return if (includeDayName) {
            val dayName = getMarathiDayOfWeek(dayOfWeek)
            "$dayName, $dayStr $monthStr $yearStr"
        } else {
            "$dayStr $monthStr $yearStr"
        }
    }
}
