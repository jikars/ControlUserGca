package gcatech.net.documentcapturepicture.interpreters

import gcatech.net.documentcapturepicture.documents.ModelDocument

interface IInterpreter<T: ModelDocument> {
    fun builder(value : String) : T
}