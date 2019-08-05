package gcatech.net.documentcapture

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import gcatech.net.documentcapturepicture.fragments.DocumentScannerFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.GsonBuilder
import gcatech.net.documentcapturepicture.config.ConfigDocument
import gcatech.net.documentcapturepicture.config.ConfigDocuments


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        startScanningCC.setOnClickListener{
            scanner(ConfigDocuments.CitizenshipCardConfig)
        }
        startScannigLicenseDriver.setOnClickListener {
            scanner(ConfigDocuments.DriverLicenceConfig)
        }
    }

    private fun scanner(configDocument: ConfigDocument){
        val fragment = DocumentScannerFragment.newInstance(configDocument,{itDocument->
            val jsonInString = GsonBuilder().create().toJson(itDocument)
            Toast.makeText(this,jsonInString, Toast.LENGTH_LONG).show()
        },{itFragment ->
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(itFragment)
            transaction.commit()
            containFragment.visibility = View.GONE
            startScanningCC.visibility = View.VISIBLE
            startScannigLicenseDriver.visibility = View.VISIBLE
        })
        if(fragment != null){
            startScanningCC.visibility = View.GONE
            startScannigLicenseDriver.visibility = View.GONE
            containFragment.visibility = View.VISIBLE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containFragment, fragment)
            transaction.commit()
        }
    }
}
