package alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.LanguageUtils
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

  private lateinit var theme_group: RadioGroup
  private lateinit var system: RadioButton
  private lateinit var dark: RadioButton
  private lateinit var light: RadioButton
  private lateinit var tvFont: TextView
  private lateinit var fontText: TextView
  private lateinit var seekbarFont: SeekBar
  private lateinit var languageSpinner: Spinner

  private lateinit var myHelper: MyHelper
  private lateinit var context: Context
  private var isUserInteraction = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return inflater.inflate(R.layout.fragment_settings, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    context = requireContext()
    myHelper = MyHelper("SettingsFragment", requireContext())

    view.isFocusableInTouchMode = true
    view.requestFocus()

    theme_group = view.findViewById(R.id.theme_group)
    system = view.findViewById(R.id.system)
    dark = view.findViewById(R.id.dark)
    light = view.findViewById(R.id.light)

    tvFont = view.findViewById(R.id.tv_font)
    fontText = view.findViewById(R.id.font_text)
    seekbarFont = view.findViewById(R.id.seekbar_font)
    languageSpinner = view.findViewById(R.id.language_spinner)


    seekbarFont.progress = myHelper.getAppSettings().font_size
    fontText.textSize = myHelper.getAppSettings().font_size.toFloat()
    tvFont.text = getString(R.string.font1, myHelper.getAppSettings().font_size)

    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, LanguageUtils.languages)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    languageSpinner.adapter = adapter

    val selectedLanguage = LanguageUtils.languageMap[myHelper.getAppSettings().ln] ?: "English"
    val position = LanguageUtils.languages.indexOf(selectedLanguage)
    languageSpinner.setSelection(position)

    seekbarFont.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tvFont.text = getString(R.string.font1, progress)
        fontText.textSize = progress.toFloat()
        val appSettings = myHelper.getAppSettings()
        appSettings.font_size = progress
        myHelper.setAppSettings(appSettings)
      }
      override fun onStartTrackingTouch(seekBar: SeekBar?) {}
      override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })

    theme_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
//      if (isUserInteraction) {
      when (checkedId) {
        R.id.system -> {
          val appSettings = myHelper.getAppSettings()
          appSettings.theme = MyEnum.THEME_SYSTEM
          myHelper.setAppSettings(appSettings)
//          myHelper.toast(context.getString(R.string.theme_settings_saved))
//          val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//          val isDarkTheme = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
//          myHelper.log("isDarkTheme:$isDarkTheme")
//          if (isDarkTheme)
//            Utils.changeToTheme(requireActivity(), 1)
//          else
//            Utils.changeToTheme(requireActivity(), 0)
        }

        R.id.dark -> {
          val appSettings = myHelper.getAppSettings()
          appSettings.theme = MyEnum.THEME_DARK
          myHelper.setAppSettings(appSettings)
//          Utils.changeToTheme(requireActivity(), 1)
//          myHelper.toast(getString(R.string.theme_settings_saved))
        }

        R.id.light -> {
          val appSettings = myHelper.getAppSettings()
          appSettings.theme = MyEnum.THEME_LIGHT
          myHelper.setAppSettings(appSettings)
//          Utils.changeToTheme(requireActivity(), 0)
//          myHelper.toast(getString(R.string.theme_settings_saved))
        }
//        }
      }
    })
  }


  override fun onResume() {
    super.onResume()
    isUserInteraction = false
    when (myHelper.getAppSettings().theme) {
      MyEnum.THEME_SYSTEM -> system.isChecked = true
      MyEnum.THEME_DARK -> dark.isChecked = true
      MyEnum.THEME_LIGHT -> light.isChecked = true
      else -> light.isChecked = true
    }
  }

}