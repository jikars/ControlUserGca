package gcatech.net.documentcapturepicture.interpreters

import gcatech.net.documentcapturepicture.documents.CitizenshipCard

class CitizenshipCardInterpreter : IInterpreter<CitizenshipCard> {
    override fun builder(value: String): CitizenshipCard {
        return CitizenshipCard()
    }
}