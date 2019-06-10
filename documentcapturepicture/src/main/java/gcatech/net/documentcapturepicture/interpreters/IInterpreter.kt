package gcatech.net.documentcapturepicture.interpreters

import gcatech.net.documentcapturepicture.documents.Document

interface IInterpreter<T: Document> {
    fun builder(value : String) : T
}