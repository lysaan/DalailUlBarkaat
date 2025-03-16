package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.adapters.DaroodAdapter
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.Darood
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_DAROOD_PAK_COLLECTION
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_WARID_UL_GHAIB
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : BaseActivity(), View.OnClickListener {

  private lateinit var recyclerView: RecyclerView
  private lateinit var title: TextView
  private lateinit var daroodAdapter: DaroodAdapter
  private val daroodList = ArrayList<Darood>()
  private val waridUlGhaibList = ArrayList<Darood>()
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

    when(type){
      TYPE_DAROOD_PAK_COLLECTION -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, daroodList,)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.darood_pak_collection)
        fetchDaroodData()
      }
      TYPE_WARID_UL_GHAIB -> {
        recyclerView.layoutManager = LinearLayoutManager(this)
        daroodAdapter = DaroodAdapter(this@MainActivity,type, waridUlGhaibList)
        recyclerView.adapter = daroodAdapter
        title.text = getString(R.string.warid_ul_ghaib_min_noor_e_muhammadi_sallallhu_alaihi_wasalam)
        fetchWaridUlGhaibData()
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

  private fun fetchDaroodData() {
    myHelper.showDialog()
    db?.collection("daroodarabic")
      ?.document("Uzcijxle9p4leTfWJBxs") // Main document
      ?.collection("ar") // Fetch all documents inside "ar"
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
  private fun fetchWaridUlGhaibData() {
    myHelper.showDialog()
    db?.collection("daroodarabic")
      ?.document("Uzcijxle9p4leTfWJBxs") // Main document
      ?.collection("warid_ul_ghaib") // Fetch all documents inside "ar"
      ?.get()
      ?.addOnSuccessListener { documents ->
        if (!documents.isEmpty) {
          waridUlGhaibList.clear() // Clear list before adding new data

          val tempList = mutableListOf<Darood>()

          for (document in documents) {
            val id = document.id
            val name = document.getString("n") ?: "No darood found"
            val youtube = document.getString("y")?.takeIf { it.isNotBlank() }
            val ur = document.getString("ur")?.takeIf { it.isNotBlank() }
            tempList.add(Darood(id, name, youtube, ur))
          }
          tempList.sortBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
          waridUlGhaibList.addAll(tempList)

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