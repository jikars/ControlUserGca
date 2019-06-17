@file:Suppress("DEPRECATION")

package gcatech.net.documentcapturepicture.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException


@SuppressLint("ViewConstructor")
class ImageSurfaceView  (context: Context, private val camera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val surfaceHolder: SurfaceHolder = holder
    init {
        this.surfaceHolder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            this.camera.setPreviewDisplay(holder)
            this.camera.startPreview()
        } catch (e: IOException) {
            Log.i(e.message,e.printStackTrace().toString())
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        try {
            this.camera.stopPreview()
            this.camera.release()
        } catch (e: IOException) {
            Log.i(e.message,e.printStackTrace().toString())
        }
    }
}