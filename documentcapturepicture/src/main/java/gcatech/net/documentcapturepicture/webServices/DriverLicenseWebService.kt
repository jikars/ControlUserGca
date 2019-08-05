package gcatech.net.documentcapturepicture.webServices

import gcatech.net.documentcapturepicture.documents.DriverLicense

class DriverLicenseWebService : IWebService<DriverLicense> {
    override fun getDocument(value: Any?): DriverLicense {
         return DriverLicense()
    }
}