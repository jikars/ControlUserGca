package gcatech.net.documentcapturepicture.scanners.ocr

import android.graphics.Bitmap

interface IOcrScanner {
    fun  scan(bitmap: Bitmap, resultOcr:IResultOcr)
}