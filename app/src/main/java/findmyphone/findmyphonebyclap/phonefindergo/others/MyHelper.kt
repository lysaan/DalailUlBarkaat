package findmyphone.findmyphonebyclap.phonefindergo.others

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import findmyphone.findmyphonebyclap.phonefindergo.R
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
class MyHelper(var TAG: String, val context: Context) {
  @Suppress("DEPRECATION")
  private var dialog: ProgressDialog? = null
  private var progressBar: ProgressBar? = null
  private var sessionManager: SessionManager = SessionManager(context)
  
  
  fun isWifiConnection(): Boolean = !isDataConnection()
  fun isDataConnection(): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    // For API level 23 and above, use NetworkCapabilities
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
      if (networkCapabilities != null) {
        return when {
          networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> false
          networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
          else -> false
        }
      }
    }
    
    // For API level below 23, use NetworkInfo
    val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetworkInfo?.let {
      when (it.type) {
        ConnectivityManager.TYPE_WIFI -> false
        ConnectivityManager.TYPE_MOBILE -> true
        else -> false
      }
    } ?: false
  }
  
  fun getOrientation() {
    if (context.packageName.equals("MVP")) {
      return (context as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }
  }
  
  fun formatSizeUnits(bytes: BigInteger): String {
    var formattedSize = ""
    
    when {
      bytes >= BigInteger("1099511627776") -> formattedSize = "${roundToN(bytes.toDouble() / BigInteger("1099511627776").toDouble(), 2)} TB"
      bytes >= BigInteger("1073741824") -> formattedSize = "${roundToN(bytes.toDouble() / BigInteger("1073741824").toDouble(), 2)} GB"
      bytes >= BigInteger("1048576") -> formattedSize = "${roundToN(bytes.toDouble() / BigInteger("1048576").toDouble(), 2)} MB"
      bytes >= BigInteger("1024") -> formattedSize = "${roundToN(bytes.toDouble() / BigInteger("1024").toDouble(), 2)} KB"
      bytes >= BigInteger.ONE -> formattedSize = "${roundToN(bytes.toDouble() / BigInteger.ONE.toDouble(), 2)} bytes"
      bytes == BigInteger.ONE -> formattedSize = "${roundToN(bytes.toDouble() / BigInteger.ONE.toDouble(), 2)} byte"
      else -> formattedSize = "0 bytes"
    }
    return formattedSize
  }
  
  fun getAppSettings() = sessionManager.getAppSettings()
  fun setAppSettings(myData: MyData) {
    sessionManager.setAppSettings(myData)
  }
  
  
  fun isDecimal(toCheck: String): Boolean {
    return toCheck.toDoubleOrNull() != null
  }
  
  /**
   * This method will return hex random color e.g. #085a54
   */
  fun getRandomColorHex(): String {

//    val random = Random()
//    val color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
//    minus.setColorFilter(color)
    
    val obj = Random()
    val rand_num = obj.nextInt(0xffffff + 1)
    return String.format("#%06x", rand_num)
  }
  
  fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
  }
  
  fun getCurrentDay(): Int {
    val calendar = Calendar.getInstance()
    return calendar[Calendar.DAY_OF_MONTH]
  }
  
  fun getCurrentMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar[Calendar.MONTH] + 1 // due to 0 indexing we need to add 1 to get correct month number
  }
  
  fun getCurrentYear(): Int {
    val calendar = Calendar.getInstance()
    return calendar[Calendar.YEAR]
  }
  
  
  fun isNavEnabled() = sessionManager.getNav()
  fun setIsNavEnabled(status: Boolean) = sessionManager.setNav(status)
  
  fun getLatestAppVersion(): AppAPI = sessionManager.getLatestAppVersion()
  
  fun setLatestAppVersion(appAPI: AppAPI) {
    sessionManager.setLatestAppVersion(appAPI)
  }
  
  
  fun getFormattedTime(millis: Long): String {
    
    return String.format(
      "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
    )
  }
  
  internal fun getMinutesFromMillisec(totalTime: Long): Long {
    return (totalTime / 1000 / 60)
  }
  
  /**
   * This method will get formatted date and it will return unix timestamp.
   * e.g date: 2020-07-24 22:45:42 and it will return timestamp: 1595612742000
   */
  fun getTimestampFromDate(date: String): Long {
    var timestamp: Long = 0L
    try {
      val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val parsedDate = dateFormat.parse(date)
      timestamp = parsedDate.time
    }
    catch (e: Exception) {
      log("getTimestampFromData:${e.localizedMessage}")
    }
    return timestamp
  }
  
  fun getDateTimeWithSeconds(s: Long, type: Int = 1): String {
    return try {
      var sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
      when (type) {
        1 -> sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
        2 -> sdf = SimpleDateFormat("yyyy_MM_dd__HH_mm_ss")
      }
      val netDate = Date(s)
      sdf.format(netDate)
    }
    catch (e: Exception) {
      log("getDatetime:${e}")
      s.toString()
    }
  }
  
  fun getCurrentDateTimeWithSeconds(type: Int = 1): String {
    val s = System.currentTimeMillis()
    return try {
      var sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
      when (type) {
        1 -> sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
        2 -> sdf = SimpleDateFormat("yyyy_MM_dd__HH_mm_ss")
      }
      val netDate = Date(s)
      sdf.format(netDate)
    }
    catch (e: Exception) {
      log("getDatetime:${e}")
      s.toString()
    }
  }
  
  fun getCurrentDateTime(type: Int = 1): String {
    val s = System.currentTimeMillis()
    return try {
      var sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
      when (type) {
        1 -> sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
        2 -> sdf = SimpleDateFormat("yyyy_MM_dd__HH_mm")
      }
      val netDate = Date(s)
      sdf.format(netDate)
    }
    catch (e: Exception) {
      log("getDatetime:${e}")
      s.toString()
    }
  }
  
  fun getDateTime(s: Long, type: Int = 1): String {
    return try {
      var sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
      when (type) {
        1 -> sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
        2 -> sdf = SimpleDateFormat("yyyy_MM_dd__HH_mm")
        3 -> sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      }
      val netDate = Date(s)
      sdf.format(netDate)
    }
    catch (e: Exception) {
      log("getDatetime:${e}")
      s.toString()
    }
  }
  
  fun getTime(s: Long): String {
    
    return if (s > 0) {
      try {
        //            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
        val sdf = SimpleDateFormat("HH:mm")
        val netDate = Date(s)
        sdf.format(netDate)
      }
      catch (e: Exception) {
        log("getDatetime:${e}")
        s.toString()
      }
    } else {
      ""
    }
    
  }
  
  fun getDate(s: String): String {
    return try {
      //            val sdf = SimpleDateFormat("MM/dd/yy")
      val sdf = SimpleDateFormat("dd MMM yyyy")
      val netDate = Date(s.toLong())
      sdf.format(netDate)
    }
    catch (e: Exception) {
      log("getDateString:${e}")
      s
    }
  }
  
  fun getDate(s: Long): String {
    return try {
      //            val sdf = SimpleDateFormat("MM/dd/yy")
      val sdf = SimpleDateFormat("dd MMM yyyy")
      val netDate = Date(s)
      sdf.format(netDate)
    }
    catch (e: Exception) {
      log("getDate:${e}")
      s.toString()
    }
  }
  
  fun isOnline(): Boolean {
    return this.isNetworkAvailable()!!
  }
  
  @Suppress("DEPRECATION")
  private fun isNetworkAvailable(): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
  }
  
  fun hideKeyboardOnClick(view: View) {
    view.setOnTouchListener { v, _ ->
      v.performClick()
      hideKeyboard(view)
      false
    }
  }
  
  fun hideKeyboard(view: View) {
    view.requestFocus()
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(
      view.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY
    )
  }
  
  fun showKeyboard(view: View) {
    view.requestFocus()
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
  }
  
  fun showProgressBar() {
    if (progressBar!!.visibility == View.GONE && progressBar != null) progressBar!!.visibility = View.VISIBLE
  }
  
  @Suppress("SENSELESS_COMPARISON")
  fun hideProgressBar() {
    try {
      if (progressBar!!.visibility == View.VISIBLE) progressBar!!.visibility = View.GONE
    }
    catch (e: java.lang.Exception) {
      log("hideProgressBarExp:${e.message}")
    }
    
    
  }
  
  fun toast(message: String) {
    try {
      val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
//            val v = toast.view.findViewById(android.R.id.message) as TextView
//            v.gravity = Gravity.CENTER
      toast.show()
    }
    catch (e: Exception) {
      log("toastException:${e.message}")
    }
    
  }

//  fun toastOnUi(message: String) {
//    try {
//      runOnUiThread(Runnable {
//        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
//        toast.show()
//      })
//    }
//    catch (e: Exception) {
//      log("toastException:${e.message}")
//    }
//
//  }
  
  fun log(message: String) {
//        if(message == "onFinish" || message == "cancelTimer Done" || message == "cancelTimer" || message == "onFinish--isTimeOver:false" || message == "onFinish--isTimeOver:true")

//    if (getAppSettings().logs_enabled)
//      logEventToFirebase(message)
    Log.d(TAG, message)
//    if (getAppSettings().logs_enabled)
//      writeLogToFile(message)
  }
  
  fun isValidEmail(target: String): Boolean {
    return if (TextUtils.isEmpty(target)) {
      false
    } else {
      android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
  }
  
  fun hideDialog() {
    try {
      if (dialog!!.isShowing) dialog!!.dismiss()
    }
    catch (e: java.lang.Exception) {
      log("hideDialogException:${e.localizedMessage}")
    }
  }
  
  @Suppress("DEPRECATION")
  fun showDialog(message: String = context.getString(R.string.loading_data_message), cancelable: Boolean = false) {
    try {
      dialog = ProgressDialog.show(
        context, context.getString(R.string.override_name), message, true, cancelable
      )
    }
    catch (exception: Exception) {
      log("showDialogException:$exception")
    }
  }
  
  fun showErrorDialog(title: String, explanation: String = "") {
    
    val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_error, null)
    val error_title = mDialogView.findViewById<TextView>(R.id.error_title)
    val error_explanation = mDialogView.findViewById<TextView>(R.id.error_explanation)
    val error_ok = mDialogView.findViewById<TextView>(R.id.error_ok)
    
    error_title.text = title
    if (explanation.isNotBlank()) {
      error_explanation.text = explanation
      error_explanation.visibility = View.VISIBLE
    }
    
    
    val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
    val mAlertDialog = mBuilder.show()
    mAlertDialog.setCancelable(true)
    
    val window = mAlertDialog.window
    val wlp = window!!.attributes
    
    wlp.gravity = Gravity.CENTER
    window.attributes = wlp
    
    error_ok.setOnClickListener {
      mAlertDialog.dismiss()
    }
  }

//  fun showErrorDialogOnUi(title: String, explanation: String = "") {
//
//    runOnUiThread(Runnable {
//      val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_error, null)
//      val error_title = mDialogView.findViewById<TextView>(R.id.error_title)
//      val error_explanation = mDialogView.findViewById<TextView>(R.id.error_explanation)
//      val error_ok = mDialogView.findViewById<TextView>(R.id.error_ok)
//
//      error_title.text = title
//      if (explanation.isNotBlank()) {
//        error_explanation.text = explanation
//        error_explanation.visibility = View.VISIBLE
//      }
//      val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
//      val mAlertDialog = mBuilder.show()
//      mAlertDialog.setCancelable(true)
//      val window = mAlertDialog.window
//      val wlp = window!!.attributes
//      wlp.gravity = Gravity.CENTER
//      window.attributes = wlp
//      error_ok.setOnClickListener {
//        mAlertDialog.dismiss()
//      }
//    })
//
//  }
  
  /**
   * Show this dialog whenever user disable location from Settings even Location Permission is enabled.
   */
  fun showGPSDisabledAlertToUser() {
    try {
      val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_permissions, null)
      val permissions_title = mDialogView.findViewById<TextView>(R.id.permissions_title)
      val permissions_sub_title = mDialogView.findViewById<TextView>(R.id.permissions_sub_title)
      val permissions_yes = mDialogView.findViewById<TextView>(R.id.permissions_yes)
      val permissions_no = mDialogView.findViewById<TextView>(R.id.permissions_no)
      val cftd_save_bottom = mDialogView.findViewById<View>(R.id.cftd_save_bottom)
      
      permissions_title.text = context.resources.getString(R.string.gps_permission_title)
      permissions_sub_title.text = context.resources.getString(R.string.gps_permission_explanation)
      permissions_yes.text = context.resources.getString(R.string.gps_location_settings)
      permissions_no.text = context.resources.getString(R.string.cancel)
      
      cftd_save_bottom.visibility = View.VISIBLE
      permissions_no.visibility = View.VISIBLE
      
      
      val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
      val mAlertDialog = mBuilder.show()
      mAlertDialog.setCancelable(true)
      
      val window = mAlertDialog.window
      val wlp = window!!.attributes
      
      wlp.gravity = Gravity.CENTER
      window.attributes = wlp
      
      permissions_yes.setOnClickListener {
        mAlertDialog.dismiss()
        val callGPSSettingIntent = Intent(
          Settings.ACTION_LOCATION_SOURCE_SETTINGS
        )
        context.startActivity(callGPSSettingIntent)
      }
      permissions_no.setOnClickListener {
        mAlertDialog.dismiss()
      }
      
    }
    catch (e: Exception) {
      log("${e.localizedMessage}")
    }
  }
  
  /**
   * If User deny Any Permission show this dialog. This dialog can not be cancelled
   * and user has to allow certain permission to use the app.
   */
  fun showPermissionDisabledAlertToUser(title: String, sub_title: String) {
    
    val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_permissions, null)
    val permissions_title = mDialogView.findViewById<TextView>(R.id.permissions_title)
    val permissions_sub_title = mDialogView.findViewById<TextView>(R.id.permissions_sub_title)
    val permissions_yes = mDialogView.findViewById<TextView>(R.id.permissions_yes)
    
    permissions_title.text = title
    permissions_sub_title.text = sub_title
    
    val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
    val mAlertDialog = mBuilder.show()
    mAlertDialog.setCancelable(false)
    
    val window = mAlertDialog.window
    val wlp = window!!.attributes
    
    wlp.gravity = Gravity.CENTER
    window.attributes = wlp
    
    permissions_yes.setOnClickListener {
      mAlertDialog.dismiss()
      val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null)
      )
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(intent)
    }
  }
  
  fun roundToN(number: Double, scale: Int): Double {
    var rounded = 0.0
    rounded = number.toBigDecimal().setScale(scale, RoundingMode.HALF_UP).toDouble()
    return rounded
  }
  
  
  fun getRoundedText(text: String, length: Int = 40): String {
    return if (text.length > length) {
      "${text.substring(0, length)}..."
    } else {
      "$text"
    }
  }
  
  fun freeMemory() {
    System.runFinalization()
    Runtime.getRuntime().gc()
    System.gc()
  }
  
  fun roundToN(number: Float, scale: Int): Float {
    val bigDecimalValue = BigDecimal(number.toDouble()) // Convert Float to BigDecimal for precision
    return bigDecimalValue.setScale(scale, RoundingMode.HALF_UP).toFloat() // Round to n decimals
    
  }
  
  fun changeLanguage(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
  }
}




