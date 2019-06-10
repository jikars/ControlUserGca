package gcatech.net.documentcapturepicture.documents

import android.graphics.Bitmap

abstract class Document {
     lateinit var frontBitmap: Bitmap
     lateinit var backBitmap: Bitmap
}