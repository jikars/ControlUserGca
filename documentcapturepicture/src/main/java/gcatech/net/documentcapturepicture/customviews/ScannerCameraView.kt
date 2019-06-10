package gcatech.net.documentcapturepicture.customviews

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.camera_view.view.*
import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.utils.UiTools
import java.util.logging.Logger


class ScannerCameraView @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
: RelativeLayout(context,attrs,defStyleAttr) {

    private lateinit var  camera:Camera
    private  var  imageSurfaceView : ImageSurfaceView? = null
    private  var  size : Camera.Size? = null
    private var hatInit : Boolean = false
    private var hasPreview : Boolean = false
    private var capturing : Boolean = false
    var hasFlash : Boolean = false

    companion object{
        const val COD_PERMISSIONS = 10
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.camera_view, this, true)
        frameCameraPreview.setOnClickListener{
            if(hasPreview){
                try {
                    camera.autoFocus { _, _ ->  }
                }catch (e:Exception){
                  Logger.getLogger(ScannerCameraView::class.java.name).severe(e.message)
                }
            }
        }
    }


    fun start() {
        if(!hatInit){
            if(hasPreview){
                stop()
            }

            initCamera()
        }
    }

    fun onFlash() {
        if (context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && hasPreview) {
            val parameters = camera.parameters
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera.parameters = parameters
            hasFlash = true
        }
    }

    fun offFlash() {
        if (context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && hasPreview) {
            val parameters = camera.parameters
            parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
            camera.parameters = parameters
            hasFlash = false
        }
    }

    fun takePicture(handle :(Bitmap) -> Unit){
        if(hasPreview && !capturing){
            capturing = true
            camera.takePicture(null,null ){ bytes: ByteArray, _: Camera ->
                val bm = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                handle(bm)
                capturing = false
            }
        }

    }



    private fun initCamera(){

        if(!hasPreview){

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(UiTools.getActivity(context)!!, arrayOf(Manifest.permission.CAMERA), COD_PERMISSIONS)
            } else {
                startCamera()
            }

        }
    }

    private fun startCamera(){
        if(frameCameraPreview.childCount>0){
            frameCameraPreview.removeAllViews()

        }
        camera = Camera.open()

        camera.setDisplayOrientation(0)

        val parameter = camera.parameters
        size =  parameter.previewSize

        parameter.setPictureSize(size?.width!!,size?.height!!)
        parameter.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        camera.parameters = parameter

        imageSurfaceView = ImageSurfaceView(context, camera)
        frameCameraPreview.addView(imageSurfaceView)
        hasPreview = true
        hatInit = true
    }

    fun restart(){
        stop()
        initCamera()
    }


    fun onResume(){
        if(!hasPreview){
            initCamera()
            if(hasFlash){
                onFlash()
            }
        }
    }

    fun onPause(){
        if(hasPreview){
            stop()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray){
        if(requestCode == COD_PERMISSIONS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startCamera()
        }
    }


    private fun stop(){
        if(hasPreview){
            hatInit = false
            hasPreview = false
            camera.stopPreview()
            camera.release()
        }
    }
}
