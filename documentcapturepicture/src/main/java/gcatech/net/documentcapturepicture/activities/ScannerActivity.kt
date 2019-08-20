package gcatech.net.documentcapturepicture.activities

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import gcatech.net.documentcapturepicture.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import gcatech.net.documentcapturepicture.config.ConfigDocument
import gcatech.net.documentcapturepicture.documents.DocumentScannerResult
import kotlinx.android.synthetic.main.scanner_activity.*


class ScannerActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.scanner_activity)
        supportActionBar?.hide()
        if(configDocument != null){
            scannerDocument.config(configDocument?.type!!,configDocument?.typeInterpreter!!,configDocument?.webServiceType!!,configDocument?.gothsFrontRes!!,configDocument?.gothsBack!!){
                this.finish()
                handle.invoke(it)
            }
        }

    }

    companion object{
        private  var configDocument : ConfigDocument? = null
        private  lateinit var handle: (DocumentScannerResult?)-> Unit
        private   var hasInit = false

        fun startActivity(context : Context, config: ConfigDocument, handle: (DocumentScannerResult?)-> Unit){
            if(!hasInit){
                configDocument = config
                this.handle = handle
                val intent = Intent()
                intent.setClass(context, ScannerActivity::class.java)
                context.startActivity(intent)
                hasInit = true
            }

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        scannerDocument.onRequestPermissionResult(requestCode,grantResults)
    }

    override fun onPause() {
        super.onPause()
        scannerDocument.onPause()
    }

    override fun onResume() {
        super.onResume()
        scannerDocument.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        hasInit = false
        configDocument = null
    }
}