package gcatech.net.documentcapturepicture.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.activities.ScannerActivity
import gcatech.net.documentcapturepicture.config.ConfigDocument
import kotlinx.android.synthetic.main.document_scanner_fragment.*

class DocumentScannerFragment   : Fragment() {


    companion object {
        private  var config : ConfigDocument? = null
        private  var fragment : DocumentScannerFragment? = null

        fun <TC :ConfigDocument>newInstance(config:TC): DocumentScannerFragment {
            if(fragment == null){
                fragment = DocumentScannerFragment()
                this.config = config
            }
            return fragment!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.document_scanner_fragment, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            ScannerActivity.startActivity(context!!,config!!){
                if(it == null){
                    activity?.finish()
                }
                documentEditView.start(config?.type!!,it?.scannerResults,it?.bitmapFront,it?.bitmapBack)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragment = null
        config = null
    }
}