package alkaafinternational92.daroodpakcollection.dalailulbarkaat

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities.SettingsActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.MyBatteryInfo
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.AppRater
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.DeviceDetails
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyData
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_DARK
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_LIGHT
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_SYSTEM
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.Utils
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities.MainActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities.WaridUlGhaibActivity


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  
  private val tag1 = "BaseActivity"
  protected lateinit var myHelper: MyHelper
  protected lateinit var myData: MyData
  lateinit var drawer_layout: DrawerLayout
  lateinit var toolbar_title: TextView
  lateinit var base_content_frame: FrameLayout
  lateinit var base_nav_view: NavigationView
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    Utils.onActivityCreateSetTheme(this)
    
    setContentView(R.layout.activity_base)
    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)
    
    drawer_layout = findViewById(R.id.drawer_layout)
    toolbar_title = findViewById(R.id.toolbar_title)
    base_content_frame = findViewById(R.id.base_content_frame)
    base_nav_view = findViewById(R.id.base_nav_view)
    
    myHelper = MyHelper(tag1, this)

//    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    val navView: NavigationView = findViewById(R.id.base_nav_view)
    val toggle = ActionBarDrawerToggle(
      this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
    )
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()
    navView.setNavigationItemSelectedListener(this)
    
    myHelper.hideKeyboardOnClick(base_content_frame)
    
    val headerView: View = navView.getHeaderView(0)
    val navTitle: TextView = headerView.findViewById(R.id.nav_header_title)
    val navSubTitle: TextView = headerView.findViewById(R.id.nav_header_sub_title)
    
    
    val appAPI = myHelper.getLatestAppVersion()
    val deviceDetails = DeviceDetails()
    var versionTitle = ""
    if (packageName.equals(MyEnum.ANDROID_BATTERY)) {
      toolbar_title.text = resources.getString(R.string.override_name)
      versionTitle = resources.getString(R.string.override_name)
      navTitle.text = HtmlCompat.fromHtml(versionTitle, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    navSubTitle.text = getString(R.string.version_string, deviceDetails.VERSION_NAME)
    
    myHelper.changeLanguage(myHelper.getAppSettings().ln)

    ActivityCompat.requestPermissions(
      this,
      arrayOf(Manifest.permission.RECORD_AUDIO),
      1
    )
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    
    when (item.itemId) {
      R.id.nav_home -> {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
      }
      R.id.nav_waridulghaib -> {
        val intent = Intent(this, WaridUlGhaibActivity::class.java)
        startActivity(intent)
      }
      
      R.id.nav_night_mode -> {
        when (myHelper.getAppSettings().theme) {
          THEME_DARK -> {
            Utils.changeToTheme(this@BaseActivity, 0)
            val appSettings = myHelper.getAppSettings()
            appSettings.theme = THEME_LIGHT
            myHelper.setAppSettings(appSettings)
          }
          
          THEME_LIGHT -> {
            Utils.changeToTheme(this@BaseActivity, 1)
            val appSettings = myHelper.getAppSettings()
            appSettings.theme = THEME_DARK
            myHelper.setAppSettings(appSettings)
          }
          
          THEME_SYSTEM -> {
            val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            myHelper.log("changeToTheme:$isDarkTheme")
            if (isDarkTheme) {
              Utils.changeToTheme(this@BaseActivity, 0)
              val appSettings = myHelper.getAppSettings()
              appSettings.theme = THEME_LIGHT
              myHelper.setAppSettings(appSettings)
            } else {
              Utils.changeToTheme(this@BaseActivity, 1)
              val appSettings = myHelper.getAppSettings()
              appSettings.theme = THEME_DARK
              myHelper.setAppSettings(appSettings)
            }
          }
        }
      }
      
      R.id.nav_settings -> {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
      }
      
      R.id.nav_email -> {
        doEmail()
      }
      
      R.id.nav_privacy -> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_url)))
        startActivity(intent)
      }
      
      R.id.nav_rate -> {
        val appRater = AppRater()
        appRater.rateNow(this@BaseActivity)
      }
      
      R.id.nav_share -> {
        val appPackageName = applicationContext.packageName
        val playStoreUrl = "https://play.google.com/store/apps/details?id=$appPackageName"
        val appName = getString(R.string.override_name)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_SUBJECT, appName)
          putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message, appName, playStoreUrl))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
      }
    }
    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    drawerLayout.closeDrawer(GravityCompat.START)
    return true
  }
  
  private fun doEmail() {
    val email = "alkaafinternational92@gmail.com"
    val subject = getString(R.string.email_subject)
    val body = getString(R.string.email_body)
    
    val intent = Intent(Intent.ACTION_SENDTO).apply {
      data = Uri.parse("mailto:") // Ensures only email apps handle this
      putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
      putExtra(Intent.EXTRA_SUBJECT, subject)
      putExtra(Intent.EXTRA_TEXT, body)
    }
    
    try {
      startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)))
      
    }
    catch (e: ActivityNotFoundException) {
      myHelper.log("No email client available: ${e.message}")
      myHelper.showErrorDialog(
        getString(R.string.error_no_email_client),
        getString(R.string.error_email_instructions, email)
      )
      
    }
  }
  
}

