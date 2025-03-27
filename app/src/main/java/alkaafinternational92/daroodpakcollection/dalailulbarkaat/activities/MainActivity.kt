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
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : BaseActivity(), View.OnClickListener {

  private lateinit var recyclerView: RecyclerView
  private lateinit var title: TextView
  private lateinit var last_seen: TextView
  private lateinit var speedTextView: TextView
  private lateinit var play: ImageView
  private lateinit var plus: ImageView
  private lateinit var minus: ImageView
  private var isScrooling = false

  private lateinit var daroodAdapter: DaroodAdapter
  private val daroodList = ArrayList<Darood>()
  private val waridUlGhaibList = ArrayList<Darood>()
  private val munajat_bisalat_ibrahimiaList = ArrayList<Darood>()
  private val duainList = ArrayList<Darood>()
  private val db = FirebaseFirestore.getInstance()
  private var type = TYPE_DAROOD_PAK_COLLECTION

  private var handler: Handler? = null
  private var scrollRunnable: Runnable? = null
  private var scrollSpeed = 5 // Default speed (change for faster/slower scrolling)
  private var scrollDelay = 50L // Default delay in milliseconds


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_main, contentFrameLayout)

    recyclerView = findViewById(R.id.rv)
    title = findViewById(R.id.title)
    last_seen = findViewById(R.id.last_seen)
    play = findViewById(R.id.play)
    plus = findViewById(R.id.plus)
    minus = findViewById(R.id.minus)
    speedTextView = findViewById(R.id.speedTextView)

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
    play.setOnClickListener(this)
    plus.setOnClickListener(this)
    minus.setOnClickListener(this)

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
//        autoScrool()
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

      R.id.play -> {
        if(isScrooling){
          stopAutoScroll()
          Glide.with(this@MainActivity).load(R.drawable.play).into(play)
          isScrooling = false
        }else{
          startAutoScroll()
          Glide.with(this@MainActivity).load(R.drawable.pause).into(play)
          isScrooling = true
        }
      }

      R.id.plus -> { increaseSpeed()}
      R.id.minus -> {decreaseSpeed()}
    }
  }
  private fun startAutoScroll() {
    stopAutoScroll() // Prevent duplicate handlers

    handler = Handler(Looper.getMainLooper())
    scrollRunnable = object : Runnable {
      override fun run() {
        recyclerView.smoothScrollBy(0, scrollSpeed) // Adjust speed dynamically
        handler?.postDelayed(this, scrollDelay) // Delay adjusts based on speed
      }
    }
    handler?.postDelayed(scrollRunnable!!, scrollDelay)
  }

  private fun stopAutoScroll() {
    handler?.removeCallbacks(scrollRunnable!!)
  }

  // Speed levels: 1 to 10 (can go beyond for high speed)
  private val speedLevels = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  private var currentSpeedIndex = 4 // Default: 5

  private fun updateSpeedDisplay() {
    speedTextView.text = "$scrollSpeed X"
  }


  private fun increaseSpeed() {
    if (currentSpeedIndex < speedLevels.size - 1) { // Ensure it does NOT exceed 10
      currentSpeedIndex++
      scrollSpeed = speedLevels[currentSpeedIndex]
      scrollDelay = maxOf(5L, 100L - (scrollSpeed * 5)) // Adjust delay
      updateSpeedDisplay()
      restartAutoScroll()
    }
  }

  private fun decreaseSpeed() {
    if (currentSpeedIndex > 0) { // Ensure it does NOT go below 1
      currentSpeedIndex--
      scrollSpeed = speedLevels[currentSpeedIndex]
      scrollDelay = minOf(100L, scrollDelay + 10) // Adjust delay
      updateSpeedDisplay()
      restartAutoScroll()
    }
  }

  private fun restartAutoScroll() {
    stopAutoScroll()
    startAutoScroll()
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