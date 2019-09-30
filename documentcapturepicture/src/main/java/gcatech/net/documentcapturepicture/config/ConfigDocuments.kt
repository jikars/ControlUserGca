package gcatech.net.documentcapturepicture.config

import gcatech.net.documentcapturepicture.R
import gcatech.net.documentcapturepicture.documents.CitizenshipCard
import gcatech.net.documentcapturepicture.documents.DriverLicense
import gcatech.net.documentcapturepicture.interpreters.CitizenshipCardInterpreter
import gcatech.net.documentcapturepicture.interpreters.DirverLicenseInterpreter
import gcatech.net.documentcapturepicture.webServices.CitizenShipCardWebService
import gcatech.net.documentcapturepicture.webServices.DriverLicenseWebService

class ConfigDocuments{

    companion object{
        val  CitizenshipCardConfig = ConfigDocument(CitizenshipCard::class ,CitizenshipCardInterpreter::class.java
            ,CitizenShipCardWebService::class.java,R.layout.gosht_citizenship_card_front, R.layout.gosht_citizenship_card_back)

        val  DriverLicenceConfig = ConfigDocument(DriverLicense::class , DirverLicenseInterpreter::class.java
            , DriverLicenseWebService::class.java,R.layout.gosht_driver_license_front, R.layout.gosht_driver_license_back)

    }

}
