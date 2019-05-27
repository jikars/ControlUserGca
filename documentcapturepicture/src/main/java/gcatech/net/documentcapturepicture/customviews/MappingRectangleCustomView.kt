package gcatech.net.documentcapturepicture.customviews

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.RelativeLayout
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.enums.ScannerMode
import gcatech.net.documentcapturepicture.scanners.codeBar.CodeBarFireBase
import gcatech.net.documentcapturepicture.scanners.codeBar.ICodeBarScanner
import gcatech.net.documentcapturepicture.scanners.ocr.IOcrScanner
import gcatech.net.documentcapturepicture.scanners.ocr.OcrScannerFireBase


class MappingRectangleCustomView   @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {


    private var bitmapCrop : Bitmap? = null
    var  propName : String
    private  var  value : String?  = null
    var  valueOcr : String?  = null
    var  isCodeBarScanner :  Boolean = false
    private  var  mappingTypes : MutableMap<ScannerMode,(Bitmap?) -> String?>
    private  var  enabledMappingCodeBar :  Boolean = false
    private   var   ocrScanner: IOcrScanner = OcrScannerFireBase()
    private   var   codeBarScanner: ICodeBarScanner = CodeBarFireBase()

    init{
        val attributes  = context?.obtainStyledAttributes(attrs, R.styleable.MappingRectangleCustomView)
        propName = attributes?.getString(R.styleable.MappingRectangleCustomView_propName)!!
        isCodeBarScanner = attributes.getBoolean(R.styleable.MappingRectangleCustomView_codeBarMapping,false)
        attributes.recycle()
        mappingTypes = mutableMapOf()
        mappingTypes[ScannerMode.CodeBar] = { bitmap :Bitmap?->   scanForCodeBar(bitmap) }
        mappingTypes[ScannerMode.Ocr] = { bitmap :Bitmap?->   scanForOcr(bitmap) }
    }

    fun crop(bitmapPattern : Bitmap){
        bitmapCrop = Bitmap.createBitmap(
            bitmapPattern,
            this.x.toInt(),
            this.y.toInt(),
            this.width,
            this.height
        )
    }

    fun scan(scannerMode: ScannerMode) : String?{
        value =  mappingTypes[scannerMode]?.invoke(bitmapCrop)
        return  value
    }

    private fun scanForCodeBar(bitmap: Bitmap?) :String?{
        if(enabledMappingCodeBar && isCodeBarScanner && bitmap != null){
            return codeBarScanner.scan(bitmap)
        }
        return null
    }

    private fun scanForOcr(bitmap: Bitmap?) :String?{
        if(bitmap != null){
            valueOcr = ocrScanner.scan(bitmap)
            return  valueOcr
        }
        return null
    }
}

