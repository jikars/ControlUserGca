package gcatech.net.documentcapturepicture.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import gcatech.net.documentcapturepicture.R
import kotlinx.android.synthetic.main.scanner_document_view.view.*
import android.support.annotation.LayoutRes
import android.view.ViewTreeObserver
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.camerakit.CameraKitView
import gcatech.net.documentcapturepicture.documents.ModelDocument
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.MapValue
import gcatech.net.documentcapturepicture.enums.ScannerMode
import gcatech.net.documentcapturepicture.interpreters.IInterpreter
import gcatech.net.documentcapturepicture.webServices.IWebService
import android.graphics.Matrix
import com.camerakit.CameraKit
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

class CaptureDocumentView @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr), INotifyCompleteScanner {

    private  lateinit var  pictureFrontBitMap : Bitmap
    private  var  elementDocumentFront : ArrayList<MappingRectangleCustomView>
    private  var  elementDocumentBack : ArrayList<MappingRectangleCustomView>
    private  var  assignableBitmap : MutableMap<Boolean,(Bitmap) -> Unit>
    private  lateinit var  gothsFront : ViewGroup
    private  lateinit var  gothsBack : ViewGroup
    private  lateinit var  pictureBackBitMap : Bitmap
    var  documentsScanner : MutableMap<ScannerMode,ModelDocument>
    private  lateinit var  type : KClass<*>
    private  var  hasFront: Boolean = true
    private  lateinit var  interpreterInstance : IInterpreter<*>
    private  lateinit var webServiceInstance : IWebService<*>

    init{
        LayoutInflater.from(context).inflate(R.layout.scanner_document_view, this, true)
        elementDocumentFront = arrayListOf()
        elementDocumentBack = arrayListOf()
        assignableBitmap =  mutableMapOf()
        documentsScanner = mutableMapOf()
        if(assignableBitmap.isEmpty()){
            assignableBitmap[true] = { bitmap :Bitmap->  assignableFront(bitmap) }
            assignableBitmap[false] = { bitmap :Bitmap->  assignableBack(bitmap) }
        }

        btnFlash.setOnClickListener{
            if(camera.flash == CameraKit.FLASH_OFF){
                camera.flash = CameraKit.FLASH_ON
            }
            if(camera.flash == CameraKit.FLASH_ON){
                camera.flash = CameraKit.FLASH_OFF
            }
        }
    }

    fun <T :ModelDocument,TInterpreter : IInterpreter<T>, TWebServer :IWebService<T>>config(type : KClass<T>, typeInterpreter : Class<TInterpreter>,webServiceType: Class<TWebServer>,
                                                                @LayoutRes gothsFrontRes : Int, @LayoutRes gothsBackRes : Int ){
        interpreterInstance = typeInterpreter.newInstance()
        webServiceInstance = webServiceType.newInstance()
        this.type = type

        documentsScanner[ScannerMode.Ocr] = type.createInstance()
        documentsScanner[ScannerMode.CodeBar] = type.createInstance()
        documentsScanner[ScannerMode.WebService] = type.createInstance()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if(inflater.inflate(gothsFrontRes, null) is ViewGroup){
            this.gothsFront  = inflater.inflate(gothsFrontRes, null) as ViewGroup
        }
        if(inflater.inflate(gothsBackRes, null) is ViewGroup){
            this.gothsBack  = inflater.inflate(gothsBackRes, null) as ViewGroup
        }

        btnCapture.setOnClickListener {
            camera.captureImage { _: CameraKitView, bytes: ByteArray ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val width = bitmap.width
                val height = bitmap.height
                val scaleX = scannerContainer.width.toFloat() / bitmap.width.toFloat()
                val scaleY = scannerContainer.height.toFloat() / bitmap.height.toFloat()
                val matrix = Matrix()
                matrix.postScale(scaleX, scaleY)
                val resizedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, width, height, matrix, false
                )
                bitmap.recycle()
                val x1 = gosh.x
                val y1 = gosh.y
                val x2 = gosh.width
                val y2 = gosh.height
                val bmp = Bitmap.createBitmap(resizedBitmap, Math.round(x1), Math.round(y1), x2, y2)
                resizedBitmap.recycle()
                assignableBitmap.getValue(hasFront).invoke(bmp)
                bmp.recycle()
                hasFront = !hasFront
            }
        }

        scannerContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                for (i in 0 until gothsFront.childCount){
                    val documentMapper = gothsFront.getChildAt(i)
                    if(documentMapper is MappingRectangleCustomView){
                        elementDocumentFront.add(documentMapper)
                    }
                }

                for (i in 0 until gothsBack.childCount){
                    val documentMapper = gothsBack.getChildAt(i)
                    if(documentMapper is MappingRectangleCustomView){
                        elementDocumentBack.add(documentMapper)
                    }
                }

                scannerContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val height = scannerContainer.height *0.8f
                val wight = height*(1.54f/1f)
                val layoutParams = gosh.layoutParams
                layoutParams.height = height.toInt()
                layoutParams.width = wight.toInt()
                gosh.layoutParams = layoutParams
                gothsFront.layoutParams = LayoutParams(MATCH_PARENT,MATCH_PARENT)
                gothsBack.layoutParams = LayoutParams(MATCH_PARENT,MATCH_PARENT)
                previewImage.layoutParams.height = height.toInt()
                previewImage.layoutParams.width = wight.toInt()
                gosh.addView(gothsFront)
                gosh.addView(gothsBack)
                gothsFront.visibility = View.VISIBLE
                gothsBack.visibility = View.INVISIBLE
                camera.aspectRatio = scannerContainer.height.toFloat()/scannerContainer.width.toFloat()
                previewImage.visibility =  View.INVISIBLE
            }
        })
    }

    private fun assignableFront (bitmap:Bitmap)
    {
        pictureFrontBitMap = bitmap
        elementDocumentFront.forEach{
            it.crop(pictureFrontBitMap,this,true)

        }
        gothsBack.visibility = View.VISIBLE
        gothsFront.visibility = View.INVISIBLE
    }

    private fun assignableBack (bitmap:Bitmap)
    {
        pictureBackBitMap = bitmap
        elementDocumentBack.forEach{
            it.crop(pictureBackBitMap,this,false)
        }
    }

    override fun scanOcrResult(isFront :Boolean,ocr:String?, propName : String) {
        if(!ocr.isNullOrEmpty()){
            val field = type.declaredMemberProperties .first { it.name == propName || it.findAnnotation<MapValue>()?.valueMapping == propName }
            field.isAccessible = true
            if (field is KMutableProperty<*>) {
                field.setter.call(documentsScanner[ScannerMode.Ocr], ocr)
                if( field.findAnnotation<Key>() != null)
                {
                    documentsScanner[ScannerMode.WebService] = webServiceInstance.getDocument(ocr)
                }
            }
        }
    }

    override fun scanCodeBarResult(codeBar: String?) {
        if(!codeBar.isNullOrEmpty()){
            val obj = interpreterInstance.builder(codeBar)
            documentsScanner[ScannerMode.CodeBar] = obj
        }
    }

    fun onStart(){
        camera.onStart()
    }

    fun onResume(){
        camera.onResume()
    }

    fun onPause(){
        camera.onPause()
    }

    fun onStop(){
        camera.onStop()
    }

    fun onRequestPermissionResult(requestCode :Int, permissions : Array<out String>, grantResults: IntArray){
        camera.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }
}
