package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage


class CodeBarFireBase : ICodeBarScanner {

    private var detectorCodeBar  : FirebaseVisionBarcodeDetector

    init{
        val options = FirebaseVisionBarcodeDetectorOptions.Builder().setBarcodeFormats(
            FirebaseVisionBarcode.FORMAT_PDF417).build()
        detectorCodeBar = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

    }
    override fun scan(bitmap: Bitmap): String? {
        var resultMap  : String?= null
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val result = detectorCodeBar.detectInImage(image)!!
        result.addOnSuccessListener { barcode  ->
            if(barcode?.size!! > 0){
                resultMap = barcode[0].rawValue!!
            }
        }.addOnFailureListener {
            resultMap =null
        }.result
        return  resultMap
    }
}