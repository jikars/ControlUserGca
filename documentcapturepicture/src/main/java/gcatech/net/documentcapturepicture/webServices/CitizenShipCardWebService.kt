package gcatech.net.documentcapturepicture.webServices

import gcatech.net.documentcapturepicture.documents.CitizenshipCard

class CitizenShipCardWebService : IWebService<CitizenshipCard> {
    override fun getDocument(value: Any?): CitizenshipCard {
        return  CitizenshipCard()
    }
}