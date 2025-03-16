package alkaafinternational92.daroodpakcollection.dalailulbarkaat.fragments

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.activities.BooksActivity
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.BOOK_TYPE_munajat_rabb_al_bariyyah
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.BOOK_TYPE_warid_ul_ghaib
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class BooksFragment : Fragment() {

  private lateinit var myHelper: MyHelper
  private lateinit var context: Context

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_books, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    myHelper = MyHelper("BatteryFragment", requireContext())
    context = requireContext()

    view.findViewById<LinearLayout>(R.id.warid_ul_ghaib).setOnClickListener {
      val intent = Intent(context, BooksActivity::class.java)
      intent.putExtra("type", BOOK_TYPE_warid_ul_ghaib)
      context.startActivity(intent)
    }

  }

}