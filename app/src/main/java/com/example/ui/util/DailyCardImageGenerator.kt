package com.example.ui.util
import com.example.gajananmandir.R
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.R
import java.io.File
import java.io.FileOutputStream

object DailyCardImageGenerator {

    /**
     * Generates a high-resolution Devotional Daily Card Bitmap and returns the File URI.
     */
    fun createCardBitmap(
        context: Context,
        templeName: String = "श्री संत गजानन महाराज मंदिर घिर्णी",
        location: String = "घिर्णी, ता. मलकापूर, जि. बुलढाणा",
        dateMarathi: String,
        suvicharText: String,
        devoteeName: String = "प्रिय भक्त",
        customBitmap: Bitmap? = null
    ): Bitmap {
        val width = 1080
        val height = 1560

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Warm Auspicious Gradient Background (Deep Saffron - Gold - White Cream)
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(
                    Color.parseColor("#FFF3E0"), // Cream light orange
                    Color.parseColor("#FFF8E1"), // Warm amber white
                    Color.parseColor("#FFE0B2"), // Soft golden saffron
                    Color.parseColor("#FFF3E0")
                ),
                floatArrayOf(0.0f, 0.35f, 0.85f, 1.0f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 2. Decorative Golden Frame & Border
        val borderPaint = Paint().apply {
            color = Color.parseColor("#E65100")
            style = Paint.Style.STROKE
            strokeWidth = 14f
            isAntiAlias = true
        }
        val innerBorderPaint = Paint().apply {
            color = Color.parseColor("#FFB300")
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }
        val cardRect = RectF(24f, 24f, width - 24f, height - 24f)
        canvas.drawRoundRect(cardRect, 32f, 32f, borderPaint)

        val innerRect = RectF(36f, 36f, width - 36f, height - 36f)
        canvas.drawRoundRect(innerRect, 24f, 24f, innerBorderPaint)

        // Corner Auspicious Circles
        val cornerPaint = Paint().apply {
            color = Color.parseColor("#880E4F")
            isAntiAlias = true
        }
        val cornerGold = Paint().apply {
            color = Color.parseColor("#FFD54F")
            isAntiAlias = true
        }
        val corners = listOf(
            Pair(44f, 44f),
            Pair(width - 44f, 44f),
            Pair(44f, height - 44f),
            Pair(width - 44f, height - 44f)
        )
        for ((cx, cy) in corners) {
            canvas.drawCircle(cx, cy, 14f, cornerPaint)
            canvas.drawCircle(cx, cy, 7f, cornerGold)
        }

        // 3. Top Header: Saffron Banner for Temple Name
        val headerBannerRect = RectF(50f, 50f, width - 50f, 200f)
        val headerBannerPaint = Paint().apply {
            shader = LinearGradient(
                50f, 50f, width - 50f, 200f,
                Color.parseColor("#BF360C"),
                Color.parseColor("#E65100"),
                Shader.TileMode.CLAMP
            )
            isAntiAlias = true
        }
        canvas.drawRoundRect(headerBannerRect, 20f, 20f, headerBannerPaint)

        // Golden trim for header banner
        val bannerTrimPaint = Paint().apply {
            color = Color.parseColor("#FFD54F")
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }
        canvas.drawRoundRect(headerBannerRect, 20f, 20f, bannerTrimPaint)

        // Temple Name (Bold & Large)
        val templeNamePaint = TextPaint().apply {
            color = Color.WHITE
            textSize = 42f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            setShadowLayer(4f, 2f, 2f, Color.parseColor("#4E342E"))
        }
        canvas.drawText(templeName, width / 2f, 115f, templeNamePaint)

        // Location Subtitle
        val locationPaint = TextPaint().apply {
            color = Color.parseColor("#FFE082")
            textSize = 28f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(location, width / 2f, 168f, locationPaint)

        // 4. Photo of Shri Gajanan Maharaj
        val photoTop = 220f
        val photoHeight = 560f
        val photoLeft = 140f
        val photoRight = width - 140f

        val photoRect = RectF(photoLeft, photoTop, photoRight, photoTop + photoHeight)

        // Photo Frame Glow
        val photoFramePaint = Paint().apply {
            color = Color.parseColor("#FFB300")
            style = Paint.Style.STROKE
            strokeWidth = 8f
            isAntiAlias = true
        }
        val photoBgPaint = Paint().apply {
            color = Color.parseColor("#FFF8E1")
            isAntiAlias = true
        }
        canvas.drawRoundRect(photoRect, 24f, 24f, photoBgPaint)

        // Load & Draw Maharaj Image
        val maharajBitmap: Bitmap = customBitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.gajanan_maharaj)
            ?: BitmapFactory.decodeResource(context.resources, R.drawable.daily_darshan_today)

        maharajBitmap?.let { srcBm ->
            val path = Path()
            path.addRoundRect(photoRect, 24f, 24f, Path.Direction.CW)
            canvas.save()
            canvas.clipPath(path)

            val srcRect = Rect(0, 0, srcBm.width, srcBm.height)
            canvas.drawBitmap(srcBm, srcRect, photoRect, Paint(Paint.FILTER_BITMAP_FLAG))
            canvas.restore()
        }
        canvas.drawRoundRect(photoRect, 24f, 24f, photoFramePaint)

        // 5. Date Badge (Pill Badge right below photo)
        val dateBadgeTop = photoTop + photoHeight + 18f
        val dateBadgeRect = RectF(width / 2f - 240f, dateBadgeTop, width / 2f + 240f, dateBadgeTop + 64f)

        val dateBadgeBg = Paint().apply {
            shader = LinearGradient(
                dateBadgeRect.left, dateBadgeRect.top, dateBadgeRect.right, dateBadgeRect.bottom,
                Color.parseColor("#880E4F"),
                Color.parseColor("#AD1457"),
                Shader.TileMode.CLAMP
            )
            isAntiAlias = true
        }
        canvas.drawRoundRect(dateBadgeRect, 32f, 32f, dateBadgeBg)

        val dateBadgeTrim = Paint().apply {
            color = Color.parseColor("#FFD54F")
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }
        canvas.drawRoundRect(dateBadgeRect, 32f, 32f, dateBadgeTrim)

        val dateTextPaint = TextPaint().apply {
            color = Color.WHITE
            textSize = 28f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("📅  $dateMarathi", width / 2f, dateBadgeTop + 43f, dateTextPaint)

        // 6. "आजचा सुविचार" Title & Box
        val suvicharBoxTop = dateBadgeTop + 84f
        val suvicharBoxBottom = suvicharBoxTop + 370f
        val suvicharRect = RectF(60f, suvicharBoxTop, width - 60f, suvicharBoxBottom)

        val suvicharBgPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
        }
        canvas.drawRoundRect(suvicharRect, 20f, 20f, suvicharBgPaint)

        val suvicharBorderPaint = Paint().apply {
            color = Color.parseColor("#FFE082")
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }
        canvas.drawRoundRect(suvicharRect, 20f, 20f, suvicharBorderPaint)

        // Header inside Suvichar Box
        val suvicharHeaderPaint = TextPaint().apply {
            color = Color.parseColor("#BF360C")
            textSize = 30f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("॥ आजचा अमृत सुविचार ॥", width / 2f, suvicharBoxTop + 48f, suvicharHeaderPaint)

        // Separator Line
        val sepPaint = Paint().apply {
            color = Color.parseColor("#FFCC80")
            strokeWidth = 2f
        }
        canvas.drawLine(width / 2f - 180f, suvicharBoxTop + 64f, width / 2f + 180f, suvicharBoxTop + 64f, sepPaint)

        // Suvichar Text (Multi-line StaticLayout)
        val suvicharTextPaint = TextPaint().apply {
            color = Color.parseColor("#212121")
            textSize = 34f
            isAntiAlias = true
            isFakeBoldText = false
        }
        val textWidth = (width - 180).toInt()
        val staticLayout = StaticLayout.Builder.obtain(
            "“ $suvicharText ”",
            0,
            ("“ $suvicharText ”").length,
            suvicharTextPaint,
            textWidth
        ).setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(14f, 1.15f)
            .setIncludePad(true)
            .build()

        canvas.save()
        val textStartY = suvicharBoxTop + 90f + ((suvicharBoxBottom - (suvicharBoxTop + 90f) - staticLayout.height) / 2f).coerceAtLeast(10f)
        canvas.translate(90f, textStartY)
        staticLayout.draw(canvas)
        canvas.restore()

        // 7. Devotee Name (भक्त : [Name])
        val devoteeBoxTop = suvicharBoxBottom + 20f
        val devoteeNameFormatted = if (devoteeName.isBlank() || devoteeName == "प्रिय भक्त") "प्रिय भक्त" else devoteeName

        val devoteePaint = TextPaint().apply {
            color = Color.parseColor("#4E342E")
            textSize = 28f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("भक्त : $devoteeNameFormatted", width / 2f, devoteeBoxTop + 36f, devoteePaint)

        // 8. Footer: "॥ गण गण गणात बोते ॥ 🙏"
        val footerBannerTop = devoteeBoxTop + 54f
        val footerBannerRect = RectF(50f, footerBannerTop, width - 50f, footerBannerTop + 90f)

        val footerBannerPaint = Paint().apply {
            shader = LinearGradient(
                50f, footerBannerTop, width - 50f, footerBannerTop + 90f,
                Color.parseColor("#E65100"),
                Color.parseColor("#BF360C"),
                Shader.TileMode.CLAMP
            )
            isAntiAlias = true
        }
        canvas.drawRoundRect(footerBannerRect, 20f, 20f, footerBannerPaint)

        val footerTrimPaint = Paint().apply {
            color = Color.parseColor("#FFD54F")
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }
        canvas.drawRoundRect(footerBannerRect, 20f, 20f, footerTrimPaint)

        val mantraPaint = TextPaint().apply {
            color = Color.WHITE
            textSize = 38f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            setShadowLayer(4f, 2f, 2f, Color.parseColor("#3E2723"))
        }
        canvas.drawText("॥ गण गण गणात बोते ॥ 🙏", width / 2f, footerBannerTop + 58f, mantraPaint)

        // 9. Sub-footer Trust Note
        val trustPaint = TextPaint().apply {
            color = Color.parseColor("#8D6E63")
            textSize = 20f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("श्री संत गजानन महाराज मंदिर ट्रस्ट, घिर्णी (ता. मलकापूर, जि. बुलढाणा)", width / 2f, height - 34f, trustPaint)

        return bitmap
    }

    /**
     * Saves card to temporary file and triggers Android Share Sheet with image/png.
     */
    fun shareCardImage(
        context: Context,
        dateMarathi: String,
        suvicharText: String,
        devoteeName: String,
        customBitmap: Bitmap? = null
    ) {
        try {
            val bitmap = createCardBitmap(
                context = context,
                dateMarathi = dateMarathi,
                suvicharText = suvicharText,
                devoteeName = devoteeName,
                customBitmap = customBitmap
            )

            val cacheFolder = File(context.cacheDir, "daily_cards")
            if (!cacheFolder.exists()) {
                cacheFolder.mkdirs()
            }

            val file = File(cacheFolder, "ghirni_daily_suvichar.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val authority = "${context.packageName}.fileprovider"
            val imageUri: Uri = FileProvider.getUriForFile(context, authority, file)

            val shareText = "॥ गण गण गणात बोते ॥ 🙏\n\n" +
                    "🛕 *श्री संत गजानन महाराज मंदिर घिर्णी*\n" +
                    "📍 घिर्णी, ता. मलकापूर, जि. बुलढाणा\n\n" +
                    "📅 *तारीख:* $dateMarathi\n" +
                    "🌸 *आजचा सुविचार:*\n\"$suvicharText\"\n\n" +
                    "🚩 *भक्त:* ${if (devoteeName.isBlank()) "प्रिय भक्त" else devoteeName}\n\n" +
                    "॥ गण गण गणात बोते ॥"

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "आजचा सुविचार - श्री संत गजानन महाराज मंदिर घिर्णी")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "दैनिक सुविचार कार्ड शेअर करा")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "फोटो कार्ड शेअर करताना त्रुटी आली: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
