package gcatech.net.documentcapturepicture.enums

import java.io.Serializable

enum class ScannerMode(val labelText: String) : Serializable {
    CodeBar("Codebar"), Ocr("Ocr"), WebService("Webservice")
}