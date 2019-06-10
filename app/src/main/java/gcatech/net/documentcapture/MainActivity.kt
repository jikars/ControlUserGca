package gcatech.net.documentcapture

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import gcatech.net.documentcapturepicture.config.CitezenshipCardConfig
import gcatech.net.documentcapturepicture.fragments.DocumentScannerFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        startScanningCC.setOnClickListener{
            it.visibility = View.GONE
            val fragment = DocumentScannerFragment.newInstance(CitezenshipCardConfig()){
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                val jsonInString = gson.toJson(it)
                Toast.makeText(this,jsonInString, Toast.LENGTH_LONG).show()
            }
            if(fragment != null){
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.containFragment, fragment)
                transaction.commit()
            }
        }

    }
}
