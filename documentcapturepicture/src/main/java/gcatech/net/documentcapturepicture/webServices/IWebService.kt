package gcatech.net.documentcapturepicture.webServices

import gcatech.net.documentcapturepicture.documents.ModelDocument

interface IWebService<T : ModelDocument> {
    fun getDocument(value : Any?) : T
}