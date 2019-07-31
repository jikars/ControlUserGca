package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.microblink.MicroblinkSDK.getApplicationContext
import net.sourceforge.zbar.Config
import net.sourceforge.zbar.Image
import net.sourceforge.zbar.ImageScanner
import net.sourceforge.zbar.Symbol
import java.nio.ByteBuffer
import com.google.zxing.pdf417.detector.Detector.detect
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import android.R.attr.data
import java.nio.file.Files.size
import android.R.attr.bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import net.sourceforge.zbar.SymbolSet








class CodeBarZBar  :  ICodeBarScanner{

    private var scanner : ImageScanner? = null

    init{
        scanner = ImageScanner()
        scanner?.setConfig(0, Config.X_DENSITY, 3)
        scanner?.setConfig(0, Config.Y_DENSITY, 3)
    }

    override fun scan(bitmap: Bitmap, resultCodeBar: IResultCodeBar) {


        val barcode = Image(bitmap.width, bitmap.height, "Y800")

        barcode.data = bitmap.convertToByteArray()

        val result = scanner?.scanImage(barcode)
        if (result != 0) {

            val symbolSets = scanner?.results

            symbolSets?.forEach {
                if(it.type == Symbol.PDF417){
                    var a = true
                }
            }
        }

        /*val width = bitmap.width
        val height = bitmap.height

        val size = bitmap.rowBytes * bitmap.height
        val byteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        val byteArray = byteBuffer.array()

        bitmap.recycle()
        val barcode =  Image(width, height, "RGB4")
        barcode.data = byteArray
        val result = scanner?.scanImage(barcode.convert("Y800"))



        if(result!! >0){
            val scannerResult = scanner?.results?.first()?.data.toString()
            resultCodeBar.resultCodeBar(scannerResult)
        }

          val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val barcode =  Image(width, height, "RGB4")
        barcode.setData(pixels)
        val result = scanner?.scanImage(barcode.convert("Y800"))

        if (result != 0) {
            val scanResult =  scanner?.results?.first()?.data?.trim()
            return
        }
        */
    }

    fun Bitmap.convertToByteArray(): ByteArray {
        //minimum number of bytes that can be used to store this bitmap's pixels
        val size = this.byteCount

        //allocate new instances which will hold bitmap
        val buffer = ByteBuffer.allocate(size)
        val bytes = ByteArray(size)

        //copy the bitmap's pixels into the specified buffer
        this.copyPixelsToBuffer(buffer)

        //rewinds buffer (buffer position is set to zero and the mark is discarded)
        buffer.rewind()

        //transfer bytes from buffer into the given destination array
        buffer.get(bytes)

        //return bitmap's pixels
        return bytes
    }
}