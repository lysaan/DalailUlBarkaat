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