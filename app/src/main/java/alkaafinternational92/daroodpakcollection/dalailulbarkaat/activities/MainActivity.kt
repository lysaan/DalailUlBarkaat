package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.adapters.DaroodAdapter
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.Darood
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.SORT_TYPE_ASC
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.SORT_TYPE_DESC
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_DARK
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_LIGHT
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_SYSTEM
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_DAROOD_PAK_COLLECTION
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_WARID_UL_GHAIB
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_duain
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_munajat_bisalat_ibrahimia
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : BaseActivity(), View.OnClickListener {

  private lateinit var recyclerView: RecyclerView
  private lateinit var title: TextView
  private lateinit var last_seen: TextView
  private lateinit var daroodAdapter: DaroodAdapter
  private val daroodList = ArrayList<Darood>()
  private val waridUlGhaibList = ArrayList<Darood>()
  private val munajat_bisalat_ibrahimiaList = ArrayList<Darood>()
  private val duainList = ArrayList<Darood>()
  private val db = FirebaseFirestore.getInstance()
  private var type = TYPE_DAROOD_PAK_COLLECTION

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_main, contentFrameLayout)

    recyclerView = findViewById(R.id.rv)
    title = findViewById(R.id.title)
    last_seen = findViewById(R.id.last_seen)

    val bundle: Bundle? = intent.extras
    if (bundle != null) {
      type = bundle.getInt("type")
    }

    var textColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)

    when (myHelper.getAppSettings().theme) {
      THEME_DARK -> {
        textColor = ContextCompat.getColor(this@MainActivity, R.color.white)
      }

      THEME_LIGHT -> {
        textColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
      }

      THEME_SYSTEM -> {
        val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        myHelper.log("changeToTheme:$isDarkTheme")
        if (isDarkTheme) {
          textColor = ContextCompat.getColor(this@MainActivity, R.color.white)
        } else {
          textColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
        }
      }
    }

    when(type){
      TYPE_DAROOD_PAK_COLLECTION -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, daroodList, textColor)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.darood_pak_collection)
        fetchDaroodData(daroodList, "ar")

      }
      TYPE_WARID_UL_GHAIB -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, waridUlGhaibList, textColor)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.warid_ul_ghaib_min_noor_e_muhammadi_sallallhu_alaihi_wasalam)
        fetchDaroodData(waridUlGhaibList, "warid_ul_ghaib", SORT_TYPE_ASC)
      }
      TYPE_munajat_bisalat_ibrahimia -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, munajat_bisalat_ibrahimiaList, textColor)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.munajat_rabb_al_bariyyah_bil_salat_al_ibrahimiya)
        fetchDaroodData(munajat_bisalat_ibrahimiaList, "munajat_bisalat_ibrahimia", SORT_TYPE_ASC)
      }
      TYPE_duain -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, duainList, textColor)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.ism_e_azam_and_prayers)
        fetchDaroodData(duainList, "duain")
      }
    }

    last_seen.setOnClickListener(this)

  }

  override fun onResume() {
    super.onResume()
    base_nav_view.setCheckedItem(base_nav_view.menu.getItem(0))
    val appSettings = myHelper.getAppSettings()
    when {
      type == TYPE_DAROOD_PAK_COLLECTION -> {
        last_seen.text = getString(R.string.last_seen, appSettings.last_seen_TYPE_DAROOD_PAK_COLLECTION)
      }
      type == TYPE_WARID_UL_GHAIB -> {
        last_seen.text = getString(R.string.last_seen, appSettings.last_seen_TYPE_WARID_UL_GHAIB)
      }
      type == TYPE_munajat_bisalat_ibrahimia -> {
        last_seen.text = getString(R.string.last_seen, appSettings.last_seen_TYPE_munajat_bisalat_ibrahimia)
      }
      type == TYPE_duain -> {
        last_seen.text = getString(R.string.last_seen, appSettings.last_seen_TYPE_duain)
      }
    }
  }

  override fun onClick(v: View?) {
    when (v!!.id) {
      R.id.last_seen -> {
        val appSettings = myHelper.getAppSettings()
        var id = "1"
        when {
          type == TYPE_DAROOD_PAK_COLLECTION -> {
            id = appSettings.last_seen_TYPE_DAROOD_PAK_COLLECTION
            moveToIdSmooth(daroodList, id)
          }
          type == TYPE_WARID_UL_GHAIB -> {
            id = appSettings.last_seen_TYPE_WARID_UL_GHAIB
            moveToIdSmooth(waridUlGhaibList, id)
          }
          type == TYPE_munajat_bisalat_ibrahimia -> {
            id = appSettings.last_seen_TYPE_munajat_bisalat_ibrahimia
            moveToIdSmooth(munajat_bisalat_ibrahimiaList, id)
          }
          type == TYPE_duain -> {
            id = appSettings.last_seen_TYPE_duain
            moveToIdSmooth(duainList, id)
          }
        }
      }
    }
  }

  fun saveLastSeen(tips: Darood) {

    val appSettings = myHelper.getAppSettings()
    when {
      type == TYPE_DAROOD_PAK_COLLECTION -> {
        appSettings.last_seen_TYPE_DAROOD_PAK_COLLECTION = tips.id
      }
      type == TYPE_WARID_UL_GHAIB -> {
        appSettings.last_seen_TYPE_WARID_UL_GHAIB = tips.id
      }
      type == TYPE_munajat_bisalat_ibrahimia -> {
        appSettings.last_seen_TYPE_munajat_bisalat_ibrahimia = tips.id
      }
      type == TYPE_duain -> {
        appSettings.last_seen_TYPE_duain = tips.id
      }
    }

    myHelper.setAppSettings(appSettings)
    last_seen.text = getString(R.string.last_seen, tips.id)
    myHelper.toast("Last seen updated.")
  }

  fun moveToIdSmooth(daroodList: ArrayList<Darood>, targetId: String) {
    val position = daroodList.indexOfFirst { it.id == targetId }
    if (position != -1) {
      recyclerView.smoothScrollToPosition(position) // Smooth scroll to item
    }
  }


  private fun fetchDaroodData(daroodList: ArrayList<Darood>, collection: String, sort_type: Int = SORT_TYPE_DESC) {
    myHelper.showDialog()
    db?.collection("daroodarabic")
      ?.document("Uzcijxle9p4leTfWJBxs") // Main document
      ?.collection(collection) // Fetch all documents inside "ar"
      ?.get()
      ?.addOnSuccessListener { documents ->
        if (!documents.isEmpty) {
          daroodList.clear() // Clear list before adding new data

          val tempList = mutableListOf<Darood>()

          for (document in documents) {
            val id = document.id
            val name = document.getString("n") ?: "No darood found"
            val youtube = document.getString("y")?.takeIf { it.isNotBlank() }
            tempList.add(Darood(id, name, youtube))
          }
          when(sort_type){
            SORT_TYPE_DESC -> { tempList.sortByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE } }
            SORT_TYPE_ASC -> { tempList.sortBy { it.id.toIntOrNull() ?: Int.MAX_VALUE } }
          }
          daroodList.addAll(tempList)
          daroodAdapter.notifyDataSetChanged()
          Log.d("Firestore", "Fetched ${documents.size()} documents")
          myHelper.hideDialog()
        } else {
          myHelper.hideDialog()
          Log.w("Firestore", "No documents found in 'ar' collection")
        }
      }
      ?.addOnFailureListener { e ->
        myHelper.hideDialog()
        Log.e("Firestore", "Error getting documents", e)
      }
  }

}