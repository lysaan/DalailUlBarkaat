package findmyphone.findmyphonebyclap.phonefindergo.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.NotificationManagerCompat
import com.github.barteksc.pdfviewer.PDFView
import com.lysaan.batterygo.others.MyForegroundService
import findmyphone.findmyphonebyclap.phonefindergo.BaseActivity
import findmyphone.findmyphonebyclap.phonefindergo.R

class PhoneFindGoMainActivity : BaseActivity(), View.OnClickListener {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_phone_find_go_main, contentFrameLayout)

    val pdfView = findViewById<PDFView>(R.id.pdfView)

    val themeMode = myHelper.getAppSettings().theme // 0 = System, 1 = Night, 2 = Light

    val isNightMode = when (themeMode) {
      1 -> true  // Night Mode
      2 -> false // Light Mode
      0 -> { // System Default
        val nightModeFlags = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
      }
      else -> false
    }

    pdfView.fromAsset("WaridUlGhaib.pdf")
      .enableSwipe(true) // Allows swiping pages
      .swipeHorizontal(false) // Set to true for horizontal scrolling
      .nightMode(isNightMode)
      .enableDoubletap(true) // Zoom using double-tap
      .enableAnnotationRendering(true) // Display annotations
      .enableAntialiasing(true) // Improves text quality
      .pageSnap(true) // Enables snapping
      .autoSpacing(true) // Adjust spacing automatically
      .pageFling(true) // Enables fling to change pages
      .load()

  }

  override fun onResume() {
    super.onResume()
    base_nav_view.setCheckedItem(base_nav_view.menu.getItem(0))
  }

  override fun onClick(v: View?) {
    when (v!!.id) {
//      R.id.start -> {
//        val intent = Intent(this, PhoneFindGoMainActivity::class.java)
//        startActivity(intent)
//      }
    }
  }

}