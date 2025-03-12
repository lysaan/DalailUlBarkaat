package findmyphone.findmyphonebyclap.phonefindergo.others

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import findmyphone.findmyphonebyclap.phonefindergo.others.MyEnum.Companion.THEME_DARK
import findmyphone.findmyphonebyclap.phonefindergo.others.MyEnum.Companion.THEME_LIGHT
import findmyphone.findmyphonebyclap.phonefindergo.others.MyEnum.Companion.THEME_SYSTEM
import findmyphone.findmyphonebyclap.phonefindergo.R


object Utils {
  private var sTheme: Int = 0
  
  const val tag = "Utils"
  
  @SuppressLint("StaticFieldLeak")
  private lateinit var myHelper: MyHelper
  
  fun changeToTheme(activity: Activity, theme: Int) {
    sTheme = theme
    
    var dataNew = MyData()
    val bundle: Bundle? = activity.intent.extras
    if (bundle != null) {
      if (bundle.getSerializable("myData") != null)
        dataNew = bundle.getSerializable("myData") as MyData
    }
    
    when (theme) {
      THEME_LIGHT -> activity.setTheme(R.style.AppTheme_NoActionBar)
      THEME_DARK -> activity.setTheme(R.style.AppTheme_NightMode)
      THEME_SYSTEM -> {
        val isDarkTheme = (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        myHelper.log("changeToTheme:$isDarkTheme")
        if (isDarkTheme)
          activity.setTheme(R.style.AppTheme_NightMode)
        else
          activity.setTheme(R.style.AppTheme_NoActionBar)
      }
    }
    
    activity.finish()
    
    val intent = Intent(activity, activity.javaClass)
    intent.putExtra("myData", dataNew)
    activity.startActivity(intent)
    activity.overridePendingTransition(
      android.R.anim.fade_in,
      android.R.anim.fade_out
    )
  }
  
  fun onActivityCreateSetTheme(activity: Activity) {
    
    myHelper = MyHelper(tag, activity)
    when (myHelper.getAppSettings().theme) {
      THEME_SYSTEM -> {
        val isDarkTheme = (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        myHelper.log("onActivityCreateSetTheme:$isDarkTheme")
        if (isDarkTheme)
          activity.setTheme(R.style.AppTheme_NightMode)
        else
          activity.setTheme(R.style.AppTheme_NoActionBar)
      }
      
      THEME_DARK -> {
        activity.setTheme(R.style.AppTheme_NightMode)
      }
      
      THEME_LIGHT -> {
        activity.setTheme(R.style.AppTheme_NoActionBar)
      }
      
      else -> activity.setTheme(R.style.AppTheme_NoActionBar)
    }
    
  }
}