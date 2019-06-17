package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import net.sourceforge.zbar.Image
import net.sourceforge.zbar.ImageScanner
import java.nio.ByteBuffer


class CodeBarZBar  :  ICodeBarScanner{

    private var scanner : ImageScanner? = null

    init{
        scanner = ImageScanner()
    }

    override fun scan(bitmap: Bitmap, resultCodeBar: IResultCodeBar) {
        val width = bitmap.width
        val height = bitmap.height

        val size = bitmap.rowBytes * bitmap.height
        val byteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        val byteArray = byteBuffer.array()

        bitmap.recycle()
        val barcode =  Image(width, height, "NV21")
        barcode.data = byteArray
        val result = scanner?.scanImage(barcode.convert("Y800"))



        if(result!! >0){
            val scannerResult = scanner?.results?.first()?.data.toString()
            resultCodeBar.resultCodeBar(scannerResult)
        }
    }
}