package alkaafinternational92.daroodpakcollection.dalailulbarkaat.others

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.google.gson.Gson

private var PRIVATE_MODE = 0
private const val PREF_NAME = "battery_care"
private const val KEY_APP_SETTINGS = "app_settings"
private const val KEY_DISABLE_NAV = "disable_navigation"
private const val KEY_APP_API = "app_api"

class SessionManager(_context: Context) {
  
  private var pref: SharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
  private var editor: Editor
  
  init {
    editor = pref.run { edit() }
  }
  
  
  fun getAppSettings(): MyData {
    val gson = Gson()
    val json = pref.getString(KEY_APP_SETTINGS, "")
    return when (val obj = gson.fromJson(json, MyData::class.java)) {
      null -> MyData()
      else -> obj
    }
  }
  
  fun setAppSettings(myData: MyData) {
    val gson = Gson()
    val json = gson.toJson(myData)
    editor.putString(KEY_APP_SETTINGS, json)
    editor.commit()
  }
  
  fun getNav() = pref.getBoolean(KEY_DISABLE_NAV, true)
  
  /**
   * Disable OR Enable Side Menu and Bottom Navigation.
   * This is useful when we want to restrict user from entering into app by using Map Activity OR Bottom Nav OR Menu Nav.
   */
  fun setNav(status: Boolean) {
    editor.putBoolean(KEY_DISABLE_NAV, status)
    editor.commit()
  }
  
  fun getLatestAppVersion(): AppAPI {
    val gson = Gson()
    val json = pref.getString(KEY_APP_API, "")
    return when (val obj = gson.fromJson(json, AppAPI::class.java)) {
      null -> AppAPI()
      else -> obj
    }
  }
  
  fun setLatestAppVersion(appAPI: AppAPI) {
    val gson = Gson()
    val json = gson.toJson(appAPI)
    editor.putString(KEY_APP_API, json)
    editor.commit()
  }
}