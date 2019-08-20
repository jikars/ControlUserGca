package gcatech.net.documentcapturepicture.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.activities.ScannerActivity
import gcatech.net.documentcapturepicture.config.ConfigDocument
import gcatech.net.documentcapturepicture.documents.Document
import kotlinx.android.synthetic.main.document_scanner_fragment.*
import kotlin.reflect.KClass

class DocumentScannerFragment   : Fragment() {

    companion object {
        private var config : ConfigDocument? = null
        private var fragment : DocumentScannerFragment? = null
        private lateinit var type : KClass<*>
        private lateinit var handleConfirm: (Document)-> Unit
        private lateinit var handleCancel: (Fragment)-> Unit
        fun newInstance(config: ConfigDocument, handle: (Document)-> Unit,handleCancel: (Fragment)-> Unit): DocumentScannerFragment? {
            if(fragment == null && this.config == null){
                this.handleConfirm = handle
                this.handleCancel = handleCancel
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
                documentEditView.visibility = View.VISIBLE
                btnCancel.visibility = View.VISIBLE
                btnConfirm.visibility = View.VISIBLE
                documentEditView?.start(type, it?.scannerResults, it?.bitmapFront, it?.bitmapBack)
            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnConfirm.setOnClickListener{
            handleConfirm.invoke(documentEditView.buildDocument())
        }
        btnCancel.setOnClickListener{
            handleCancel.invoke(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragment = null
        config = null
    }
}