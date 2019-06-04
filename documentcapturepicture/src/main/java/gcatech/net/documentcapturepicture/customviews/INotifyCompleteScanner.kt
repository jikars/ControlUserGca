package gcatech.net.documentcapturepicture.customviews

interface INotifyCompleteScanner {
    fun scanOcrResult(isFront :Boolean,ocr:String?,  propName : String)
    fun scanCodeBarResult(codeBar:String?)
}