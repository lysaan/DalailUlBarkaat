package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.github.barteksc.pdfviewer.PDFView
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R

class MainActivity : BaseActivity(), View.OnClickListener {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_main, contentFrameLayout)

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