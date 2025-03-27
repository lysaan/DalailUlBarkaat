package alkaafinternational92.daroodpakcollection.dalailulbarkaat.others

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_LIGHT
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_SYSTEM
import java.io.Serializable

class MyData : Serializable {


  @SerializedName("name")
  @Expose
  var name: String? = null

  // system = 0, dark = 1, light = 2
  @SerializedName("theme")
  @Expose
  var theme: Int = THEME_LIGHT

  @SerializedName("font_size")
  @Expose
  var font_size: Int = 36


  @SerializedName("ln")
  @Expose
  var ln: String = "en"
  
  @SerializedName("translation")
  @Expose
  var show_translation: Boolean = true

  var last_seen_TYPE_DAROOD_PAK_COLLECTION : String = "1"
  var last_seen_TYPE_WARID_UL_GHAIB : String = "1"
  var last_seen_TYPE_munajat_bisalat_ibrahimia : String = "1"
  var last_seen_TYPE_duain : String = "1"

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

