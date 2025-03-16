package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import android.os.Bundle
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments.BooksFragment
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments.DaroodFragment
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments.SettingsFragment
import android.view.Menu
import androidx.fragment.app.Fragment

import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity  : BaseActivity() {

  private lateinit var menu: Menu


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_home, contentFrameLayout)

    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

    menu = bottomNavigationView.menu
    loadFragment(DaroodFragment())

    bottomNavigationView.setOnItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.navb_darood -> loadFragment(DaroodFragment())
        R.id.navb_books -> loadFragment(BooksFragment())
        R.id.navb_settings -> loadFragment(SettingsFragment())
        else -> false
      }
      true
    }
  }


  override fun onResume() {
    super.onResume()
    base_nav_view.setCheckedItem(base_nav_view.menu.getItem(0))
  }


  private fun loadFragment(fragment: Fragment): Boolean {
    myHelper.log("loadFragment")
    val existingFragment = supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName)
    if (existingFragment == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, fragment, fragment::class.java.simpleName)
        .commit()
    }
    return true
  }

}