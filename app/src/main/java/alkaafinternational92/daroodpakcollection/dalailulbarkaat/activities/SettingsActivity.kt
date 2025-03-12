package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.LanguageUtils
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.LanguageUtils.languages
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.Utils

class SettingsActivity : BaseActivity(), View.OnClickListener {
  lateinit var theme_group: RadioGroup
  lateinit var system: RadioButton
  lateinit var dark: RadioButton
  lateinit var light: RadioButton
  lateinit var temp_group: RadioGroup
  lateinit var celsius: RadioButton
  lateinit var fahrenheit: RadioButton
  lateinit var kelvin: RadioButton
  lateinit var home: Button
  private var isUserInteraction = false
  private lateinit var languageSpinner: Spinner

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_settings, contentFrameLayout)

    theme_group = findViewById(R.id.theme_group)
    system = findViewById(R.id.system)
    dark = findViewById(R.id.dark)
    light = findViewById(R.id.light)
    temp_group = findViewById(R.id.temp_group)
    celsius = findViewById(R.id.celsius)
    fahrenheit = findViewById(R.id.fahrenheit)
    kelvin = findViewById(R.id.kelvin)
    home = findViewById(R.id.home)

    theme_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
      if (isUserInteraction) {
        when (checkedId) {
          R.id.system -> {
            val appSettings = myHelper.getAppSettings()
            appSettings.theme = MyEnum.THEME_SYSTEM
            myHelper.setAppSettings(appSettings)
            myHelper.toast(getString(R.string.theme_settings_saved))
            val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            val isDarkTheme = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
            myHelper.log("isDarkTheme:$isDarkTheme")
            if (isDarkTheme)
              Utils.changeToTheme(this@SettingsActivity, 1)
            else
              Utils.changeToTheme(this@SettingsActivity, 0)
          }

          R.id.dark -> {
            val appSettings = myHelper.getAppSettings()
            appSettings.theme = MyEnum.THEME_DARK
            myHelper.setAppSettings(appSettings)
            Utils.changeToTheme(this@SettingsActivity, 1)
            myHelper.toast(getString(R.string.theme_settings_saved))
          }

          R.id.light -> {
            val appSettings = myHelper.getAppSettings()
            appSettings.theme = MyEnum.THEME_LIGHT
            myHelper.setAppSettings(appSettings)
            Utils.changeToTheme(this@SettingsActivity, 0)
            myHelper.toast(getString(R.string.theme_settings_saved))
          }
        }
      }
    })

    temp_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
      if (isUserInteraction) {
        when (checkedId) {
          R.id.celsius -> {
            val appSettings = myHelper.getAppSettings()
            appSettings.temp_unit = MyEnum.TEMP_UNIT_CELSIUS
            myHelper.setAppSettings(appSettings)
            myHelper.toast(getString(R.string.temperature_unit_set_to_celsius))

          }

          R.id.fahrenheit -> {
            val appSettings = myHelper.getAppSettings()
            appSettings.temp_unit = MyEnum.TEMP_UNIT_FAHRENHEIT
            myHelper.setAppSettings(appSettings)
            myHelper.toast(getString(R.string.temperature_unit_set_to_fahrenheit))
          }

          R.id.kelvin -> {
            val appSettings = myHelper.getAppSettings()
            appSettings.temp_unit = MyEnum.TEMP_UNIT_KELVIN
            myHelper.setAppSettings(appSettings)
            myHelper.toast(getString(R.string.temperature_unit_set_to_kelvin))
          }
        }
      }
    })
    home.setOnClickListener(this)


    languageSpinner = findViewById(R.id.language_spinner)
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    languageSpinner.adapter = adapter

    val selectedLanguage = LanguageUtils.languageMap[myHelper.getAppSettings().ln] ?: "English"
    val position = languages.indexOf(selectedLanguage)
    languageSpinner.setSelection(position)

//    languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//      override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        if (isUserInteraction) {
//          val languageCode = LanguageUtils.getLanguageCodeByPosition(position)
//          val appSettings = myHelper.getAppSettings()
//          appSettings.ln = languageCode
//          myHelper.setAppSettings(appSettings)
//          myHelper.toast(getString(R.string.language_selection_updated_successfully))
//          myHelper.changeLanguage(languageCode)  // Change the language
//
//          val intent = Intent(this@SettingsActivity, BatteryMainActivity::class.java)  // Replace MainActivity with the activity you want to start
//          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//          startActivity(intent)
//
//          finish()
//
//        }
//      }
//
//      override fun onNothingSelected(parentView: AdapterView<*>?) {}
//    }

  }

  override fun onResume() {
    super.onResume()
    base_nav_view.setCheckedItem(base_nav_view.menu.getItem(2))

    when (myHelper.getAppSettings().theme) {
      MyEnum.THEME_SYSTEM -> system.isChecked = true
      MyEnum.THEME_DARK -> dark.isChecked = true
      MyEnum.THEME_LIGHT -> light.isChecked = true
      else -> light.isChecked = true
    }

    when (myHelper.getAppSettings().temp_unit) {
      MyEnum.TEMP_UNIT_CELSIUS -> celsius.isChecked = true
      MyEnum.TEMP_UNIT_FAHRENHEIT -> fahrenheit.isChecked = true
      MyEnum.TEMP_UNIT_KELVIN -> kelvin.isChecked = true
      else -> celsius.isChecked = true
    }
  }

  override fun onUserInteraction() {
    super.onUserInteraction()
    isUserInteraction = true // Allow listener to respond after interaction
  }

  override fun onClick(v: View?) {
    when (v!!.id) {
      R.id.home -> {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
      }
    }
  }
}