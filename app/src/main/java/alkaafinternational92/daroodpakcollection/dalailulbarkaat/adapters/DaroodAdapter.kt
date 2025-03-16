package alkaafinternational92.daroodpakcollection.dalailulbarkaat.adapters

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.Darood
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_DAROOD_PAK_COLLECTION
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyEnum.Companion.TYPE_WARID_UL_GHAIB
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DaroodAdapter(
  val context: Activity,
  val type: Int,
  private val myDataList: List<Darood>
) : RecyclerView.Adapter<DaroodAdapter.ViewHolder>() {
  
  private val tag1 = this::class.java.simpleName
  lateinit var myHelper: MyHelper
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.list_row_darood, parent, false)
    myHelper = MyHelper(tag1, context)
    return ViewHolder(v)
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val v = holder.itemView
    val name = v.findViewById<TextView>(R.id.text)
    val translation = v.findViewById<TextView>(R.id.text1)
    val youtube = v.findViewById<ImageView>(R.id.youtube)
    val report = v.findViewById<ImageView>(R.id.report)
    val share = v.findViewById<ImageView>(R.id.share)
    val id = v.findViewById<TextView>(R.id.id)

    name.textSize = myHelper.getAppSettings().font_size.toFloat()
    translation.textSize = myHelper.getAppSettings().font_size.toFloat()

    val tips = myDataList[position]
    name.text = tips.name
    id.text = tips.id

    if (tips.ur.isNullOrEmpty() || !myHelper.getAppSettings().show_translation) {
      translation.visibility = View.GONE
    } else {
      translation.visibility = View.VISIBLE
      translation.text = tips.ur
    }

    if (tips.youtube.isNullOrEmpty()) {
      youtube.visibility = View.INVISIBLE
    } else {
      youtube.visibility = View.VISIBLE
      youtube.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tips.youtube))
        it.context.startActivity(intent)
      }
    }

    report.setOnClickListener {

      val email = context.getString(R.string.email_address)
      val subject = context.getString(R.string.report_subject)
      val body = "Incorrect Darood Details:\n\nType:$type \nID: ${tips.id} \nText: ${tips.name} \n\nCorrection:"

      val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Ensures only email apps handle this
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
      }

      try {
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)))
      }
      catch (e: ActivityNotFoundException) {
        myHelper.log("No email client available: ${e.message}")
        myHelper.showErrorDialog(
          context.getString(R.string.error_no_email_client),
          context.getString(R.string.error_email_instructions, email)
        )
      }

    }

    share.setOnClickListener {
      val appPackageName = context.packageName
      val playStoreUrl = "https://play.google.com/store/apps/details?id=$appPackageName"
      val appName = context.getString(R.string.override_name)

      var message = "${tips.name} \n\n Download $appName:\n$playStoreUrl"

      when {
        type === TYPE_DAROOD_PAK_COLLECTION -> {}
        type == TYPE_WARID_UL_GHAIB && !tips.ur.isNullOrEmpty()  && myHelper.getAppSettings().show_translation -> {
          message = "${tips.name} \n\n ${tips.ur} \n\n Download $appName:\n$playStoreUrl"
        }
      }

      val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, message)
        putExtra(Intent.EXTRA_TEXT, message)
      }
      context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_via)))
    }
  }
  
  
  override fun getItemCount(): Int {
    return myDataList.size
  }
  
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
  
}

