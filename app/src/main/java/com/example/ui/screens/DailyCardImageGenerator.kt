package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object DailyCardImageGenerator {

    fun shareCardImage(
        context: Context,
        dateMarathi: String,
        suvicharText: String,
        devoteeName: String
    ) {
        try {
            val width = 1080
            val height = 1350

            val bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)

            // Background
            canvas.drawColor(
                android.graphics.Color.rgb(255, 248, 240)
            )

            // Border
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(230, 81, 0)
                style = Paint.Style.STROKE
                strokeWidth = 12f
            }

            canvas.drawRect(
                30f,
                30f,
                width - 30f,
                height - 30f,
                borderPaint
            )

            // Temple title
            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(191, 54, 12)
                textSize = 50f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }

            canvas.drawText(
                "श्री संत गजानन महाराज",
                width / 2f,
                120f,
                titlePaint
            )

            // Temple name
            val templePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(93, 64, 55)
                textSize = 32f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }

            canvas.drawText(
                "मंदिर घिर्णी",
                width / 2f,
                175f,
                templePaint
            )

            canvas.drawText(
                "ता. मलकापूर, जि. बुलढाणा",
                width / 2f,
                220f,
                templePaint
            )

            // Mantra
            val mantraPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(230, 81, 0)
                textSize = 40f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }

            canvas.drawText(
                "॥ गण गण गणात बोते ॥",
                width / 2f,
                300f,
                mantraPaint
            )

            // Date
            val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(117, 117, 117)
                textSize = 30f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }

            canvas.drawText(
                dateMarathi,
                width / 2f,
                360f,
                datePaint
            )

            // Suvichar
            val quotePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(62, 39, 35)
                textSize = 42f
                typeface = Typeface.DEFAULT
                textAlign = Paint.Align.CENTER
            }

            val lines = wrapText(
                suvicharText,
                quotePaint,
                900f
            )

            var y = 500f

            for (line in lines.take(7)) {
                canvas.drawText(
                    "“$line”",
                    width / 2f,
                    y,
                    quotePaint
                )
                y += 65f
            }

            // Devotee name
            if (devoteeName.isNotBlank()) {

                val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = android.graphics.Color.rgb(136, 14, 79)
                    textSize = 32f
                    typeface = Typeface.DEFAULT_BOLD
                    textAlign = Paint.Align.CENTER
                }

                canvas.drawText(
                    "🙏 $devoteeName",
                    width / 2f,
                    1000f,
                    namePaint
                )
            }

            // Bottom
            val bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.rgb(191, 54, 12)
                textSize = 32f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }

            canvas.drawText(
                "गजानन महाराजांचा आशीर्वाद सदैव असो 🙏",
                width / 2f,
                1160f,
                bottomPaint
            )

            canvas.drawText(
                "॥ गण गण गणात बोते ॥",
                width / 2f,
                1230f,
                bottomPaint
            )

            // Save image
            val directory = File(
                context.cacheDir,
                "shared_cards"
            )

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val imageFile = File(
                directory,
                "gajanan_suvichar_${System.currentTimeMillis()}.png"
            )

            imageFile.outputStream().use { output ->
                bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    output
                )
            }

            bitmap.recycle()

            // FileProvider URI
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )

            // Share
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"

                putExtra(
                    Intent.EXTRA_STREAM,
                    uri
                )

                putExtra(
                    Intent.EXTRA_TEXT,
                    "॥ गण गण गणात बोते ॥ 🙏\n\n" +
                            "श्री संत गजानन महाराज मंदिर घिर्णी"
                )

                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    "फोटो कार्ड शेअर करा"
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun wrapText(
        text: String,
        paint: Paint,
        maxWidth: Float
    ): List<String> {

        val words = text.trim().split(" ")

        val lines = mutableListOf<String>()
        var currentLine = ""

        for (word in words) {

            val testLine =
                if (currentLine.isEmpty()) {
                    word
                } else {
                    "$currentLine $word"
                }

            if (paint.measureText(testLine) <= maxWidth) {
                currentLine = testLine
            } else {

                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                }

                currentLine = word
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }
}
