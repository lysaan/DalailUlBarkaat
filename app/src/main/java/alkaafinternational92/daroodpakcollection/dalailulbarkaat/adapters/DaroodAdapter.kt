package com.lysaan.batterygo.adapters

import alkaafinternational92.daroodpakcollection.dalailulbarkaat.R
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes.Darood
import alkaafinternational92.daroodpakcollection.dalailulbarkaat.others.MyHelper
import android.annotation.SuppressLint
import android.app.Activity
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
  private val myDataList: List<Darood>
) : RecyclerView.Adapter<DaroodAdapter.ViewHolder>() {
  
  private val tag1 = this::class.java.simpleName
  lateinit var myHelper: MyHelper
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.list_row_tips, parent, false)
    myHelper = MyHelper(tag1, context)
    return ViewHolder(v)
  }
  
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val v = holder.itemView
    val name = v.findViewById<TextView>(R.id.text)
    val youtube = v.findViewById<ImageView>(R.id.youtube)

    name.textSize = myHelper.getAppSettings().font_size.toFloat()

    val tips = myDataList[position]
    name.text = tips.name

    if (tips.youtube.isNullOrEmpty()) {
      youtube.visibility = View.GONE // Hide if null
    } else {
      youtube.visibility = View.VISIBLE // Show if valid
      youtube.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tips.youtube))
        it.context.startActivity(intent)
      }
    }
  }
  
  
  override fun getItemCount(): Int {
    return myDataList.size
  }
  
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
  
}

