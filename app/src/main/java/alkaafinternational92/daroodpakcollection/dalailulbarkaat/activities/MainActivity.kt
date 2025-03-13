package alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.BaseActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.adapters.DaroodAdapter
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.Darood
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : BaseActivity(), View.OnClickListener {

  private lateinit var recyclerView: RecyclerView
  private lateinit var daroodAdapter: DaroodAdapter
  private val daroodList = ArrayList<Darood>()
  private val db = FirebaseFirestore.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentFrameLayout = findViewById<FrameLayout>(R.id.base_content_frame)
    layoutInflater.inflate(R.layout.activity_main, contentFrameLayout)

    recyclerView = findViewById(R.id.rv)
    recyclerView.layoutManager = LinearLayoutManager(this)
    daroodAdapter = DaroodAdapter(this@MainActivity,daroodList)
    recyclerView.adapter = daroodAdapter

    fetchDaroodData()
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

          // **Sort numerically in descending order**
          tempList.sortByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE }

          // Add sorted items to the main list
          daroodList.addAll(tempList)

          daroodAdapter.notifyDataSetChanged()
          Log.d("Firestore", "Fetched ${documents.size()} documents")
        } else {
          Log.w("Firestore", "No documents found in 'ar' collection")
        }
      }
      ?.addOnFailureListener { e ->
        Log.e("Firestore", "Error getting documents", e)
      }
  }

}