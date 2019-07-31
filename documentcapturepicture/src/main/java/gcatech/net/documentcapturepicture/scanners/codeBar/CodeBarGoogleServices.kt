package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.microblink.MicroblinkSDK.getApplicationContext
import com.google.android.gms.vision.barcode.BarcodeDetector

class CodeBarGoogleServices  : ICodeBarScanner {


    var detector : BarcodeDetector = BarcodeDetector.Builder(getApplicationContext())
        .setBarcodeFormats(Barcode.ALL_FORMATS)
        .build()

    override fun scan(bitmap: Bitmap, resultCodeBar: IResultCodeBar) {
        val frame = Frame.Builder().setBitmap(bitmap).build()
        val barcodes = detector.detect(frame)
        val thisCode = barcodes.valueAt(0)
        resultCodeBar.resultCodeBar(thisCode.rawValue)
    }
}