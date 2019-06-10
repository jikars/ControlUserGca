package gcatech.net.documentcapturepicture.webServices

import gcatech.net.documentcapturepicture.documents.Document

interface IWebService<T : Document> {
    fun getDocument(value : Any?) : T
}