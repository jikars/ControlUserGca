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
import gcatech.net.documentcapturepicture.documents.Document
import java.util.*
import android.graphics.Bitmap
import android.view.View
import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.MapValue
import gcatech.net.documentcapturepicture.enums.ScannerMode
import gcatech.net.documentcapturepicture.interpreters.IInterpreter
import gcatech.net.documentcapturepicture.webServices.IWebService
import android.graphics.Matrix
import gcatech.net.documentcapturepicture.documents.DocumentScannerResult
import gcatech.net.documentcapturepicture.utils.DoAsync
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
    private var  documentsScanner : MutableMap<ScannerMode,Document>
    private  lateinit var  type : KClass<*>
    private  lateinit var  typeInterpreter : Class<*>
    private  lateinit var  webServiceType : Class<*>
    private  var  hasFront: Boolean = false
    private  lateinit var  interpreterInstance : IInterpreter<*>
    private  lateinit var webServiceInstance : IWebService<*>
    private  lateinit var handleResult: (DocumentScannerResult?)-> Unit

    init{
        LayoutInflater.from(context).inflate(R.layout.scanner_document_view, this, true)
        elementDocumentFront = arrayListOf()
        elementDocumentBack = arrayListOf()
        assignableBitmap =  mutableMapOf()
        documentsScanner = mutableMapOf()
        if(assignableBitmap.isEmpty()){
            assignableBitmap[false] = { bitmap :Bitmap->  assignableFront(bitmap) }
            assignableBitmap[true] = { bitmap :Bitmap->  assignableBack(bitmap) }
        }
    }


    fun config(type : KClass<*>, typeInterpreter : Class<*>, webServiceType: Class<*>,
               @LayoutRes gothsFrontRes : Int, @LayoutRes gothsBackRes : Int,  handleResult: (DocumentScannerResult?)-> Unit ){

        this.type = type
        this.typeInterpreter = typeInterpreter
        this.webServiceType = webServiceType
        this.handleResult = handleResult

        generateInstanceTypes()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if(inflater.inflate(gothsFrontRes, null) is ViewGroup){
            this.gothsFront  = inflater.inflate(gothsFrontRes, null) as ViewGroup
        }
        if(inflater.inflate(gothsBackRes, null) is ViewGroup){
            this.gothsBack  = inflater.inflate(gothsBackRes, null) as ViewGroup
        }

        btnCapture.setOnClickListener {
            camera.takePicture {  bitmap ->
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
            }
        }


        btnFlash.setOnClickListener{
            if(!camera.hasFlash){
                camera.onFlash()
            }
            else{
                camera.offFlash()
            }
        }

        btnReady.setOnClickListener{
            handleResult.invoke(DocumentScannerResult(documentsScanner,pictureFrontBitMap,pictureBackBitMap))
        }

        btnArrowRefresh.setOnClickListener{
            hasFront = false
            initialCharge()
            generateInstanceTypes()
            btnReady.visibility = View.GONE
            btnCapture.visibility = View.VISIBLE
            camera.restart()
        }

        btnCancel.setOnClickListener{
            this.handleResult.invoke(DocumentScannerResult(documentsScanner,null,null))
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
                initialCharge()
                camera.start()
            }
        })

    }

    private fun  initialCharge(){
        val height = scannerContainer.height *0.8f
        val wight = height*(1.54f/1f)
        val layoutParams = gosh.layoutParams
        layoutParams.height = height.toInt()
        layoutParams.width = wight.toInt()
        gosh.layoutParams = layoutParams
        gothsFront.layoutParams = LayoutParams(MATCH_PARENT,MATCH_PARENT)
        gothsBack.layoutParams = LayoutParams(MATCH_PARENT,MATCH_PARENT)
        gosh.removeAllViews()
        gosh.addView(gothsFront)
        gosh.addView(gothsBack)
        gothsFront.visibility = View.VISIBLE
        gothsBack.visibility = View.INVISIBLE
    }

    private fun generateInstanceTypes(){
        DoAsync{
            interpreterInstance = typeInterpreter.newInstance() as IInterpreter<*>
            webServiceInstance = webServiceType.newInstance() as IWebService<*>
            documentsScanner[ScannerMode.Ocr] = type.createInstance() as Document
            documentsScanner[ScannerMode.CodeBar] = type.createInstance() as Document
            documentsScanner[ScannerMode.WebService] = type.createInstance() as Document
        }.execute()
    }

    private fun assignableFront (bitmap:Bitmap)
    {
        hasFront = true
        pictureFrontBitMap = bitmap
        elementDocumentFront.forEach{
            it.crop(pictureFrontBitMap,this,true)

        }
        gothsBack.visibility = View.VISIBLE
        gothsFront.visibility = View.INVISIBLE
        camera.restart()
    }

    private fun assignableBack (bitmap:Bitmap)
    {
        pictureBackBitMap = bitmap
        elementDocumentBack.forEach{
            it.crop(pictureBackBitMap,this,false)
        }
        btnReady.visibility = View.VISIBLE
        btnCapture.visibility = View.GONE
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


    fun onResume(){
        camera.onResume()
    }

    fun onPause(){
        camera.onPause()
    }

    fun onRequestPermissionResult(requestCode :Int, grantResults: IntArray){
        camera.onRequestPermissionsResult(requestCode,grantResults)
    }
}
