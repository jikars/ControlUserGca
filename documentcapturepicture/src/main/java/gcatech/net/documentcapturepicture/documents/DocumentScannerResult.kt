package gcatech.net.documentcapturepicture.documents

import android.graphics.Bitmap
import gcatech.net.documentcapturepicture.enums.ScannerMode

class DocumentScannerResult(val scannerResults : MutableMap<ScannerMode, Document?>?,val  bitmapFront: Bitmap?, val bitmapBack:Bitmap?)
