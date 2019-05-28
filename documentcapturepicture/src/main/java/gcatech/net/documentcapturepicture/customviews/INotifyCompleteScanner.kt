package gcatech.net.documentcapturepicture.customviews

interface INotifyCompleteScanner {
    fun scanOcrResult(isFront :Boolean,ocr:String?)
    fun scanCodeBarResult(codeBar:String?)
}