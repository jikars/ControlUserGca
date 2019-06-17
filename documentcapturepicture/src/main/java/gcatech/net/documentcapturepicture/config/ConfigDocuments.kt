package gcatech.net.documentcapturepicture.config

import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.documents.CitizenshipCard
import gcatech.net.documentcapturepicture.interpreters.CitizenshipCardInterpreter
import gcatech.net.documentcapturepicture.webServices.CitizenShipCardWebService

class ConfigDocuments{

    companion object{
        val  CitizenshipCardConfig = ConfigDocument(CitizenshipCard::class ,CitizenshipCardInterpreter::class.java
            ,CitizenShipCardWebService::class.java,R.layout.gosht_citizenship_card_front, R.layout.gosht_citizenship_card_back)

    }

}
