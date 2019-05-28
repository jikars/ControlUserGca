package gcatech.net.documentcapturepicture.scanners.ocr

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer

class OcrScannerFireBase : IOcrScanner {

    private  var detectorOcr  : FirebaseVisionTextRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

    override fun scan(bitmap: Bitmap, resultOcr : IResultOcr) {

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        detectorOcr.processImage(image)
            .addOnSuccessListener { textResult ->
                if(textResult != null && !textResult.text.isNullOrEmpty()){
                    resultOcr.resulOcr(textResult.text)
                }
            }
            .addOnFailureListener {
                resultOcr.resulOcr(null)
            }
    }
}