package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap

interface ICodeBarScanner {
    fun  scan(bitmap: Bitmap) : String?
}