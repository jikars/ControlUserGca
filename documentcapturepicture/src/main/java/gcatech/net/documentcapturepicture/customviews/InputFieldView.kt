package gcatech.net.documentcapturepicture.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.enums.ScannerMode
import kotlinx.android.synthetic.main.input_field_view.view.*

class InputFieldView  @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr) {

    private var valueSelect  : String? = null
    private   var inputTypeSelect : ScannerMode? = null
    private var inputModes : MutableMap<ScannerMode, InputModeView>
    private lateinit var typeScanner :  MutableMap<ScannerMode,String?>
    private lateinit var documentEditionView: DocumentEditionView
    lateinit var propName: String
    private lateinit var labelValue: String


    init{
        LayoutInflater.from(context).inflate(R.layout.input_field_view, this, true)
        inputModes = mutableMapOf()
    }

    fun start(documentEditionView: DocumentEditionView, typeScanner : MutableMap<ScannerMode,String?>, propName : String, labelValue : String?) {
        this.typeScanner = typeScanner
        this.documentEditionView = documentEditionView
        this.propName = propName
        this.labelValue = labelValue !!
        inputProp.setText(labelValue, TextView.BufferType.EDITABLE)
        this.typeScanner.forEach{
            val inputTypeFiled = InputModeView(context,null,0)
            if(this.typeScanner.contains((it.key))){
                inputTypeFiled.start(it.key,this,it.value)
                inputModes[it.key] = inputTypeFiled
                inputModeContainer.addView(inputTypeFiled)
            }
        }
    }

    fun changeSelect(type : ScannerMode, value: String?){
        inputTypeSelect = type
        valueSelect = value
        changeEdit(valueSelect)
    }

    private  fun changeEdit(changeText:String?){
        if(changeText != null){
            inputProp.setText(changeText, TextView.BufferType.EDITABLE)
        }
    }

    fun getCurrentValue():String?{
        return inputProp.text?.toString()
    }
}