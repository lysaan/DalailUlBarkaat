package findmyphone.findmyphonebyclap.phonefindergo.others

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import findmyphone.findmyphonebyclap.phonefindergo.others.MyEnum.Companion.TEMP_UNIT_CELSIUS
import findmyphone.findmyphonebyclap.phonefindergo.others.MyEnum.Companion.THEME_SYSTEM
import java.io.Serializable

class MyData : Serializable {


  @SerializedName("name")
  @Expose
  var name: String? = null


  // Celsius = 0, Fahrenheit = 1, Kelvin = 2
  @SerializedName("temp_unit")
  @Expose
  var temp_unit: Int = TEMP_UNIT_CELSIUS

  // system = 0, dark = 1, light = 2
  @SerializedName("theme")
  @Expose
  var theme: Int = THEME_SYSTEM

  @SerializedName("min_battery_alert")
  @Expose
  var min_battery_alert: Int = 20

  @SerializedName("max_battery_alert")
  @Expose
  var max_battery_alert: Int = 80

  @SerializedName("high_temp_c")
  @Expose
  var high_temp_c: Int = 35

  @SerializedName("high_temp_f")
  @Expose
  var high_temp_f: Int = 95

  @SerializedName("high_temp_k")
  @Expose
  var high_temp_k: Int = 308

  @SerializedName("is_service_enabled")
  @Expose
  var is_service_enabled: Boolean = false

  @SerializedName("ln")
  @Expose
  var ln: String = "en"

//  fun copy(): MyData {
//    val copy = MyData()
//    copy.name = this.name
//    copy.temp_unit = this.temp_unit
//
//    return copy
//  }

  override fun toString(): String {
    return "\nMyData(" +
        "name=$name, " +
        ")"
  }

}

