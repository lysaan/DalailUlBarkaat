package alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities.MainActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_DAROOD_PAK_COLLECTION
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_WARID_UL_GHAIB
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import android.content.Context
import android.content.Intent
import android.widget.LinearLayout

class DaroodFragment : Fragment() {

  private lateinit var myHelper: MyHelper
  private lateinit var context: Context
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_darood, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    myHelper = MyHelper("BatteryFragment", requireContext())
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

  }

}