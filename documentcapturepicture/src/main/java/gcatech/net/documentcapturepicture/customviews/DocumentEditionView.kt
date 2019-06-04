package gcatech.net.documentcapturepicture.customviews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.LabelTitle
import gcatech.net.documentcapturepicture.documents.ModelDocument
import gcatech.net.documentcapturepicture.enums.ScannerMode
import kotlinx.android.synthetic.main.capture_document_view.view.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

class DocumentEditionView @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {

    private var inputFieldDocumentViews : ArrayList<InputFieldView>
    private lateinit var  scannerResults : MutableMap<ScannerMode, ModelDocument>
    private lateinit var  type : KClass<*>

    init{
        LayoutInflater.from(context).inflate(R.layout.scanner_document_view, this, true)
        inputFieldDocumentViews = arrayListOf()
    }


    fun  <T:ModelDocument >start (type:KClass<T>, scannerResults : MutableMap<ScannerMode, ModelDocument>){
        this.type = type
        this.scannerResults = scannerResults

     /*   this.scannerResults.forEach{
            val sca = ScannerOptionView(context,null,this.codeRequest)
            sca.start(it.key,this.codeRequest,activity)
            scannerOptions.addView(sca)
        }*/

        this.type.declaredMemberProperties.forEach{
            if (it is KMutableProperty<*>) {
                val field = InputFieldView(context,null)
                val  list : MutableMap<ScannerMode,String?> = mutableMapOf()
                list[ScannerMode.CodeBar] = it.getter.call(scannerResults[ScannerMode.CodeBar])?.toString()
                list[ScannerMode.Ocr] = it.getter.call(scannerResults[ScannerMode.Ocr])?.toString()
                list[ScannerMode.WebService] = it.getter.call(scannerResults[ScannerMode.WebService])?.toString()
                field.start(this,list,it.name,it.findAnnotation<LabelTitle>()?.labelValue)
                if(!inputFieldDocumentViews.contains(field)){
                    inputFieldDocumentViews.add(field)
                }
                bottomPart.addView(field)
            }
        }
    }



}