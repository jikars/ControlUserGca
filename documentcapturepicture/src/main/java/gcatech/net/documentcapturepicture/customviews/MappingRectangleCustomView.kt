package gcatech.net.documentcapturepicture.customviews

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.RelativeLayout
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.enums.ScannerMode
import gcatech.net.documentcapturepicture.scanners.codeBar.CodeBarFireBase
import gcatech.net.documentcapturepicture.scanners.codeBar.ICodeBarScanner
import gcatech.net.documentcapturepicture.scanners.codeBar.IResultCodeBar
import gcatech.net.documentcapturepicture.scanners.ocr.IOcrScanner
import gcatech.net.documentcapturepicture.scanners.ocr.IResultOcr
import gcatech.net.documentcapturepicture.scanners.ocr.OcrScannerFireBase


class MappingRectangleCustomView   @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr), IResultCodeBar,IResultOcr {

    var bitmapCrop : Bitmap? = null
    var  propName : String
    private  var  value : String?  = null
    var  valueOcr : String?  = null
    var  isCodeBarScanner :  Boolean = false
    var  isFingerPrint :  Boolean = false

    private  var  mappingTypes : MutableMap<ScannerMode,(Bitmap?) -> Unit>
    private   var   ocrScanner: IOcrScanner = OcrScannerFireBase()
    private  lateinit  var   notify:INotifyCompleteScanner
    private   var   codeBarScanner: ICodeBarScanner = CodeBarFireBase()
    private   var   isFront = false

    init{
        val attributes  = context?.obtainStyledAttributes(attrs, R.styleable.MappingRectangleCustomView)
        propName = attributes?.getString(R.styleable.MappingRectangleCustomView_propName)!!
        isCodeBarScanner = attributes.getBoolean(R.styleable.MappingRectangleCustomView_codeBarMapping,false)
       isFingerPrint = attributes.getBoolean(R.styleable.MappingRectangleCustomView_fingerPrint,false)

        attributes.recycle()
        mappingTypes = mutableMapOf()
        mappingTypes[ScannerMode.CodeBar] = { bitmap :Bitmap?->   scanForCodeBar(bitmap) }
        mappingTypes[ScannerMode.Ocr] = { bitmap :Bitmap?->   scanForOcr(bitmap) }
    }

    fun crop(bitmapPattern : Bitmap, notify: INotifyCompleteScanner, isFront : Boolean ){
      try{
          this.notify = notify
          this.isFront = isFront
          bitmapCrop = Bitmap.createBitmap(
              bitmapPattern,
              Math.round(this.x),
              Math.round(this.y),
              this.width,
              this.height
          )

          if(isCodeBarScanner && !isFingerPrint){
              scanForCodeBar(bitmapCrop)
          }
          else if(!isFingerPrint){

              scanForOcr(bitmapCrop)
          }
      }
      catch (ex : Exception){
          throw  ex
      }
    }


    private fun scanForCodeBar(bitmap: Bitmap?){
        if(isCodeBarScanner && bitmap != null){
            codeBarScanner.scan(bitmap,this)
        }
    }

    private fun scanForOcr(bitmap: Bitmap?){
        if(bitmap != null){
            ocrScanner.scan(bitmap,this)
        }
    }

    override fun resultCodeBar(result: String?) {
        if(!result.isNullOrEmpty()){
            value = result
            notify.scanCodeBarResult(value)
        }
    }

    override fun resulOcr(result: String?) {
        if(!result.isNullOrEmpty()){
            valueOcr = result
            value = result
            notify.scanOcrResult(isFront,value,propName)
        }
    }



}

