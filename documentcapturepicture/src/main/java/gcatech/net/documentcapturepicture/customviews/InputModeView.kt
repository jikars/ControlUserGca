package gcatech.net.documentcapturepicture.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.enums.ScannerMode
import kotlinx.android.synthetic.main.input_mode_view.view.*

class InputModeView  @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr){

    lateinit var  inputType : ScannerMode
    private  lateinit var  inputField : InputFieldView
    private  var  value : String? = null

    init{
        LayoutInflater.from(context).inflate(R.layout.input_mode_view, this, true)
    }

    fun  start(inputType : ScannerMode, inputField : InputFieldView, value : String?){
        this.inputType  = inputType
        this.inputField = inputField
        this.value = value
        btnMod.text = inputType.labelText
        if(this.value.isNullOrEmpty()){
            visibility = View.GONE
            return
        }
        this.value = value
        visibility = View.VISIBLE
        btnMod.setOnClickListener{
            inputField.changeSelect(inputType, this.value)
        }


    }

}