package findmyphone.findmyphonebyclap.phonefindergo.others


import android.content.Context
import android.net.Uri

interface Market {
  fun getMarketURI(context: Context): Uri
}