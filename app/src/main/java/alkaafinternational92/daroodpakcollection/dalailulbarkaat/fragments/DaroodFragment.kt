package alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities.MainActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_DAROOD_PAK_COLLECTION
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_WARID_UL_GHAIB
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_duain
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_munajat_bisalat_ibrahimia
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DaroodFragment : Fragment() {

  private lateinit var myHelper: MyHelper
  private lateinit var context: Context
  private val db = FirebaseFirestore.getInstance()
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_darood, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    myHelper = MyHelper("DaroodFragment", requireContext())
    context = requireContext()

    view.findViewById<LinearLayout>(R.id.darood_pak_collection).setOnClickListener {
      val intent = Intent(context, MainActivity::class.java)
      intent.putExtra("type", TYPE_DAROOD_PAK_COLLECTION)
      context.startActivity(intent)
    }

    view.findViewById<LinearLayout>(R.id.warid_ul_ghaib).setOnClickListener {
      val intent = Intent(context, MainActivity::class.java)
      intent.putExtra("type", TYPE_WARID_UL_GHAIB)
      context.startActivity(intent)
    }

    view.findViewById<LinearLayout>(R.id.munajat_bisalat_ibrahimia).setOnClickListener {
      val intent = Intent(context, MainActivity::class.java)
      intent.putExtra("type", TYPE_munajat_bisalat_ibrahimia)
      context.startActivity(intent)
    }

    view.findViewById<LinearLayout>(R.id.duain).setOnClickListener {
      val intent = Intent(context, MainActivity::class.java)
      intent.putExtra("type", TYPE_duain)
      context.startActivity(intent)
    }

    lifecycleScope.launch {
      val count = countDarood("ar")
      val count_warid_ul_ghaib = countDarood("warid_ul_ghaib")
      val count_munajat_bisalat_ibrahimia = countDarood("munajat_bisalat_ibrahimia")
      val count_duain = countDarood("duain")
      myHelper.log("Total documents in 'ar': $count")
      view.findViewById<TextView>(R.id.darood_pak_count).text = getString(R.string.darood_pak1, count)
      view.findViewById<TextView>(R.id.warid_ul_ghaib_count).text = getString(R.string.warid_ul_ghaib_with_urdu1, count_warid_ul_ghaib)
      view.findViewById<TextView>(R.id.silsilat_dua_al_munfarid_biallah).text = getString(R.string.silsilat_dua_al_munfarid_biallah1, count_munajat_bisalat_ibrahimia)
      view.findViewById<TextView>(R.id.duain_text).text = getString(R.string.duain1, count_duain)
    }

  }

  suspend fun countDarood(list_name: String): Long? {
    return try {
      val aggregateSnapshot = db?.collection("daroodarabic")
        ?.document("Uzcijxle9p4leTfWJBxs")
        ?.collection(list_name)
        ?.count()
        ?.get(AggregateSource.SERVER)
        ?.await() // Use await() to wait for the result
      aggregateSnapshot?.count
    } catch (e: Exception) {
      myHelper.log( "Error counting documents:$e")
      null
    }
  }


}