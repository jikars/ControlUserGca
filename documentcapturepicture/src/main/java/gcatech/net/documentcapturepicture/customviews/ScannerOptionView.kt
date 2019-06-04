package gcatech.net.documentcapturepicture.customviews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.enums.ScannerMode
import kotlinx.android.synthetic.main.scanner_option_view.view.*

class ScannerOptionView@JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr)  {

    private lateinit var inputType : ScannerMode
    private var codeRequest : Int = 0
    private lateinit var activity : Activity

    init {
        LayoutInflater.from(context).inflate(R.layout.scanner_option_view, this, true)
    }

   /* fun start(inputType: ScannerMode, codeRequest: Int, activity: Activity){
        this.codeRequest = codeRequest
        this.activity = activity
        this.btnScannerOption.text = inputType.keyString
        this.inputType = inputType
        this.btnScannerOption.setOnClickListener{
            inputMode.resolve(activity,this.inputType,codeRequest)
        }
    }*/
}