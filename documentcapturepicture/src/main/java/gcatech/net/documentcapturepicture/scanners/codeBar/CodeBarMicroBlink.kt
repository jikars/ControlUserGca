package gcatech.net.documentcapturepicture.scanners.codeBar

import android.graphics.Bitmap
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.blinkbarcode.pdf417.Pdf417Recognizer
import android.content.Context
import com.microblink.directApi.RecognizerRunner
import com.microblink.hardware.orientation.Orientation
import android.widget.Toast
import com.microblink.directApi.DirectApiErrorListener
import com.microblink.entities.recognizers.Recognizer


class CodeBarMicroBlink(var context: Context) : ICodeBarScanner {

    private var mRecognizer: Pdf417Recognizer? = null
    private var mRecognizerBundle: RecognizerBundle? = null
    private var mRecognizerRunner: RecognizerRunner? = null

    init {
        mRecognizer = Pdf417Recognizer()
        mRecognizerBundle = RecognizerBundle(mRecognizer)
        mRecognizerRunner = RecognizerRunner.getSingletonInstance()
        mRecognizerRunner?.initialize(context, mRecognizerBundle!!, DirectApiErrorListener { t ->
            Toast.makeText(
                context,
                "There was an error in initialization of Recognizer: " + t.message,
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun scan(bitmap: Bitmap, resultCodeBar: IResultCodeBar) {
        mRecognizerRunner?.recognizeBitmap(bitmap, Orientation.ORIENTATION_LANDSCAPE_RIGHT) {
            val result = mRecognizer?.result
            if (result?.resultState == Recognizer.Result.State.Valid) {
                resultCodeBar.resultCodeBar(result.stringData)
            }
        }

    }
}