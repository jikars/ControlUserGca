package gcatech.net.documentcapturepicture.interpreters

import gcatech.net.documentcapturepicture.documents.DriverLicense

class DirverLicenseInterpreter  : IInterpreter<DriverLicense> {
    override fun builder(value: String): DriverLicense {
        return DriverLicense()
    }
}