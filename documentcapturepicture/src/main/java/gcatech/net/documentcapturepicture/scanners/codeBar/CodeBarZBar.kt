package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import net.sourceforge.zbar.Config
import net.sourceforge.zbar.Image
import net.sourceforge.zbar.ImageScanner
import net.sourceforge.zbar.Symbol


class CodeBarZBar  :  ICodeBarScanner{

    private var scanner : ImageScanner? = null

    init{
        scanner = ImageScanner()
        scanner?.setConfig(0, Config.ENABLE,0)
        scanner?.setConfig(Symbol.PDF417,Config.ENABLE,1)
    }

    override fun scan(bitmap: Bitmap, resultCodeBar: IResultCodeBar) {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width*height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val barcode =  Image(width, height, "RGB4")
        barcode.setData(pixels)
        val result = scanner?.scanImage(barcode.convert("Y800"))

        if(result!! >0){
            val scannerResult = scanner?.results?.first()?.data.toString()
            resultCodeBar.resultCodeBar(scannerResult)
        }
    }
}