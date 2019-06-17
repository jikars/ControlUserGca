package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer


class CodeBarZxing : ICodeBarScanner {

    private  val scanner  = MultiFormatReader()


    override fun scan(bitmap: Bitmap, resultCodeBar: IResultCodeBar) {
        try{

            val aspectRatio = bitmap.width / bitmap.height as Float
            val width = 480
            val height = Math.round(width / aspectRatio)

            val bitmapCrop = Bitmap.createScaledBitmap(
                bitmap, width, height, false
            )
            val intArray = IntArray(bitmapCrop.width * bitmapCrop.height)
            bitmap.getPixels(intArray, 0, bitmapCrop.width, 0, 0, bitmapCrop.width, bitmapCrop.height)
            val source = RGBLuminanceSource(bitmapCrop.width, bitmapCrop.height, intArray)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            val result  = scanner.decode(binaryBitmap)
            if(result  != null){
                resultCodeBar.resultCodeBar(result.text)
            }
        }
        catch ( e : NotFoundException) { e.printStackTrace() }
        catch ( e : ChecksumException) { e.printStackTrace() }
        catch ( e : FormatException) { e.printStackTrace() }
    }

}