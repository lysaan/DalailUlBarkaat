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
import android.widget.SeekBar
import android.widget.TextView

class SettingsActivity : BaseActivity(), View.OnClickListener {
  lateinit var theme_group: RadioGroup
  lateinit var system: RadioButton
  lateinit var dark: RadioButton
  lateinit var light: RadioButton

  lateinit var tv_font: TextView
  lateinit var font_text: TextView
  lateinit var seekbar_font: SeekBar

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

    tv_font = findViewById(R.id.tv_font)
    font_text = findViewById(R.id.font_text)
    seekbar_font = findViewById(R.id.seekbar_font)

    home = findViewById(R.id.home)

    home.setOnClickListener(this)

    seekbar_font.progress = myHelper.getAppSettings().font_size
    font_text.textSize = myHelper.getAppSettings().font_size.toFloat()
    tv_font.text = getString(R.string.font1, myHelper.getAppSettings().font_size)

    languageSpinner = findViewById(R.id.language_spinner)
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    languageSpinner.adapter = adapter

    val selectedLanguage = LanguageUtils.languageMap[myHelper.getAppSettings().ln] ?: "English"
    val position = languages.indexOf(selectedLanguage)
    languageSpinner.setSelection(position)

    seekbar_font.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tv_font.text = getString(R.string.font1, progress)
        font_text.textSize = progress.toFloat()
        val appSettings = myHelper.getAppSettings()
        appSettings.font_size = progress
        myHelper.setAppSettings(appSettings)
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {}

      override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })

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