package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.adapters.DaroodAdapter
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.Darood
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_DARK
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_LIGHT
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.THEME_SYSTEM
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_DAROOD_PAK_COLLECTION
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_WARID_UL_GHAIB
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
  private lateinit var daroodAdapter: DaroodAdapter
  private val daroodList = ArrayList<Darood>()
  private val waridUlGhaibList = ArrayList<Darood>()
  private val munajat_bisalat_ibrahimiaList = ArrayList<Darood>()
  private val db = FirebaseFirestore.getInstance()
  private var type = TYPE_DAROOD_PAK_COLLECTION

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_main, contentFrameLayout)

    recyclerView = findViewById(R.id.rv)
    title = findViewById(R.id.title)

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
        fetchDaroodData(waridUlGhaibList, "warid_ul_ghaib")
      }
      TYPE_munajat_bisalat_ibrahimia -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, munajat_bisalat_ibrahimiaList, textColor)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.munajat_rabb_al_bariyyah_bil_salat_al_ibrahimiya)
        fetchDaroodData(munajat_bisalat_ibrahimiaList, "munajat_bisalat_ibrahimia")
      }
    }


  }

  override fun onResume() {
    super.onResume()
    base_nav_view.setCheckedItem(base_nav_view.menu.getItem(0))
  }

  override fun onClick(v: View?) {
    when (v!!.id) {
//      R.id.start -> {
//        val intent = Intent(this, PhoneFindGoMainActivity::class.java)
//        startActivity(intent)
//      }
    }
  }

  private fun fetchDaroodData(daroodList: ArrayList<Darood>, collection: String) {
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
          tempList.sortByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE }
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