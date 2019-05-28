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
import java.lang.reflect.Type
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.MapValue
import gcatech.net.documentcapturepicture.enums.ScannerMode
import gcatech.net.documentcapturepicture.interpreters.IInterpreter
import gcatech.net.documentcapturepicture.webServices.IWebService


class CaptureDocument @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr), INotifyCompleteScanner {



    private  lateinit var  pictureFrontBitMap : Bitmap
    private  var  elementDocumentFront : ArrayList<MappingRectangleCustomView>
    private  var  elementDocumentBack : ArrayList<MappingRectangleCustomView>
    private  var  assignableBitmap : MutableMap<Boolean,(Bitmap) -> Unit>
    private  lateinit var  gothsFront : ViewGroup
    private  lateinit var  gothsBack : ViewGroup
    private  lateinit var  pictureBackBitMap : Bitmap
    private  lateinit var  documentOcrBuilder : ModelDocument
    private  var  documentsScanner : MutableMap<ScannerMode,ModelDocument>
    private  lateinit var  type : Type
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
    }

    fun <T :ModelDocument,TInterpreter : IInterpreter<T>, TWebServer :IWebService<T>>config(type : Class<T>, typeInterpreter : Class<TInterpreter>,webServiceType: Class<TWebServer>,
                                                                @LayoutRes gothsFrontRes : Int, @LayoutRes gothsBackRes : Int ){

        documentOcrBuilder = type.newInstance()
        interpreterInstance = typeInterpreter.newInstance()
        webServiceInstance = webServiceType.newInstance()
        this.type = type
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if(inflater.inflate(gothsFrontRes, null) is ViewGroup){
            this.gothsFront  = inflater.inflate(gothsFrontRes, null) as ViewGroup
        }
        if(inflater.inflate(gothsBackRes, null) is ViewGroup){
            this.gothsBack  = inflater.inflate(gothsFrontRes, null) as ViewGroup
        }


        btnCapture.setOnClickListener {
            camera.captureImage{ _: CameraKitView, bytes: ByteArray ->

                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val scaleX = bitmap.width.toFloat() / camera.width.toFloat()
                val scaleY = bitmap.height.toFloat() / camera.height.toFloat()
                val x1 = gosh.x
                val y1 = gosh.y
                val x2 = gosh.width
                val y2 = gosh.height
                val cropStartX = Math.round(x1 * scaleX)
                val cropStartY = Math.round(y1 * scaleY)
                val cropWidthX = Math.round(x2 * scaleX)
                val cropHeightY = Math.round(y2 * scaleY)
                val bmp = Bitmap.createBitmap(bitmap,cropStartX, cropStartY, cropWidthX,cropHeightY)
                assignableBitmap.getValue(hasFront).invoke(bmp)
                hasFront = !hasFront
            }
        }

        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                for (i in 0 until gothsFront.childCount){
                    val documentMapper = gothsFront.getChildAt(i)
                    if(documentMapper is MappingRectangleCustomView){
                        elementDocumentFront.add(documentMapper)
                    }
                }
                camera.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val height = camera.height *0.8f
                val wight = height*(1.54f/1f)
                val layoutParams = gosh.layoutParams
                layoutParams.height = height.toInt()
                layoutParams.width = wight.toInt()
                gosh.layoutParams = layoutParams
                gothsFront.layoutParams = LayoutParams(MATCH_PARENT,MATCH_PARENT)
                gothsBack.layoutParams = LayoutParams(MATCH_PARENT,MATCH_PARENT)
                previewImage.layoutParams.height = height.toInt()
                previewImage.layoutParams.width = wight.toInt()
                gosh.removeAllViews()
                gosh.addView(gothsFront)
            }
        })
    }

    private fun assignableFront (bitmap:Bitmap)
    {
        pictureFrontBitMap = bitmap
        elementDocumentFront.forEach{
            it.crop(pictureFrontBitMap,this,true)
        }
        gosh.removeAllViews()
        gosh.addView(gothsBack)
        gothsBack.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                for (i in 0 until gothsBack.childCount){
                    val documentMapper = gothsBack.getChildAt(i)
                    if(documentMapper is MappingRectangleCustomView){
                        elementDocumentBack.add(documentMapper)
                    }
                }
                gothsBack.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun assignableBack (bitmap:Bitmap)
    {
        pictureBackBitMap = bitmap
        elementDocumentBack.forEach{
            it.crop(pictureBackBitMap,this,false)
        }

    }

    private fun scanCodeBar(resultCodeBar : String ) {
        val obj = interpreterInstance.builder(resultCodeBar)
        documentsScanner[ScannerMode.CodeBar] = obj

    }

    private fun scanOcr(elementsMatch : ArrayList<MappingRectangleCustomView>){
        type.javaClass.declaredFields.forEach {
            if (it.isAnnotationPresent(MapValue::class.java)) {
                val valueName = it.getAnnotation(MapValue::class.java)
                val valueScan = elementsMatch.first{ite -> ite.propName == valueName.valueMapping}
                it.set(documentOcrBuilder,valueScan.valueOcr)
            }
        }
        documentsScanner[ScannerMode.Ocr] = documentOcrBuilder

    }

    private fun scanWebService(){
        val field = type.javaClass.declaredFields.first { it.isAnnotationPresent(Key::class.java) }
        val key =  field.get(documentsScanner[ScannerMode.Ocr])
        documentsScanner[ScannerMode.WebService] = webServiceInstance.getDocument(key)
    }

    override fun scanOcrResult(isFront :Boolean,ocr:String?) {
        if(!ocr.isNullOrEmpty()){
            Toast.makeText(context,ocr,Toast.LENGTH_SHORT).show()
        }
    }

    override fun scanCodeBarResult(codeBar: String?) {
        if(!codeBar.isNullOrEmpty()){
            Toast.makeText(context,codeBar,Toast.LENGTH_SHORT).show()
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
