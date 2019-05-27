package gcatech.net.documentcapture

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import gcatech.net.documentcapturepicture.documents.CitizenshipCard
import gcatech.net.documentcapturepicture.interpreters.CitizenshipCardInterpreter
import gcatech.net.documentcapturepicture.webServices.CitizenShipCardWebService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        capturePicture.config(CitizenshipCard::class.java,CitizenshipCardInterpreter::class.java, CitizenShipCardWebService::class.java,
            R.layout.gosht_citizenship_card_front,R.layout.gosht_citizenship_card_front)
    }

    override fun onStart() {
        super.onStart()
        capturePicture.onStart()
    }

    override fun onResume() {
        super.onResume()
        capturePicture.onResume()
    }

    override fun onStop() {
        super.onStop()
        capturePicture.onStop()
    }

    override fun onPause() {
        super.onPause()
        capturePicture.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capturePicture.onRequestPermissionResult(requestCode,permissions,grantResults)
    }
}
