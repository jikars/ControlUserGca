package gcatech.net.documentcapture

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import gcatech.net.documentcapturepicture.config.CitezenshipCardConfig
import gcatech.net.documentcapturepicture.fragments.DocumentScannerFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val fragment = DocumentScannerFragment.newInstance(CitezenshipCardConfig())
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containFragment, fragment)
            transaction.commit()
        }

    }
}
