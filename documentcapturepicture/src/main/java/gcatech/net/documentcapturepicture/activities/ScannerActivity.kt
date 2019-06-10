package gcatech.net.documentcapturepicture.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import gcatech.net.documentcapturepicture.R
import android.content.Intent
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
        scannerDocument.config(configDocument.type,configDocument.typeInterpreter,configDocument.webServiceType,configDocument.gothsFrontRes,configDocument.gothsBack){
            this.finish()
            handle.invoke(it)
        }
    }

    companion object{
        private  lateinit var configDocument : ConfigDocument
        private  lateinit var handle: (DocumentScannerResult?)-> Unit

        fun <TC : ConfigDocument>startActivity(context : Context, config:TC, handle: (DocumentScannerResult?)-> Unit){
            configDocument = config
            this.handle = handle
            val intent = Intent()
            intent.setClass(context, ScannerActivity::class.java)
            context.startActivity(intent)
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
}