package gcatech.net.documentcapturepicture.customviews

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
    }


    fun start() {
        if(!hatInit){
            initCamera()
            camera = Camera.open()
            hatInit = true
        }
    }

    fun onFlash() {
        if (context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            val parameters = camera.parameters
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera.parameters = parameters
            hasFlash = true
        }
    }

    fun offFlash() {
        if (context.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
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

   private  fun getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    private fun initCamera(){

        if(!hasPreview){

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(getActivity()!!, arrayOf(Manifest.permission.CAMERA), COD_PERMISSIONS)
            } else {
                startCamera()
            }

        }
    }

    private fun startCamera(){
        frameCameraPreview.removeAllViews()
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
    }

    fun restart(){
        stop()
        initCamera()
    }


    fun onResume(){
        if(hasPreview){
            initCamera()
        }
    }

    fun onPause(){
        if(hasPreview){
            stop()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray){
        if(requestCode == COD_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startCamera()
        }
    }


    fun stop(){
        if(hasPreview){
            hatInit = false
            camera.stopPreview()
            camera.release()
        }
    }
}
