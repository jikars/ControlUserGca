package gcatech.net.documentcapturepicture.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.activities.ScannerActivity
import gcatech.net.documentcapturepicture.config.ConfigDocument
import kotlinx.android.synthetic.main.document_scanner_fragment.*
import kotlin.reflect.KClass

class DocumentScannerFragment   : Fragment() {


    companion object {
        private var config : ConfigDocument? = null
        private var fragment : DocumentScannerFragment? = null
        private lateinit var type : KClass<*>

        fun <TC :ConfigDocument>newInstance(config:TC): DocumentScannerFragment? {
            if(fragment == null && this.config == null){
                fragment = DocumentScannerFragment()
                this.config = config
                this.type = config.type
                return fragment!!
            }
            return  null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.document_scanner_fragment, container, false)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null && config != null) {
            ScannerActivity.startActivity(activity!!, config!!) {
                documentEditView?.start(type, it?.scannerResults, it?.bitmapFront, it?.bitmapBack)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragment = null
        config = null
    }
}