package gcatech.net.documentcapturepicture.documents

import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.LabelTitle
import gcatech.net.documentcapturepicture.annotations.MapValue

class DriverLicense : Document() {

    @Key
    @MapValue("licenseNumber")
    @LabelTitle("Numero de licensia")
    var licenseNumber: String? = null

    @MapValue("name")
    @LabelTitle("Nombre")
    var name: String? = null

    @MapValue("birthDate")
    @LabelTitle("Fecha de nacimiento")
    var birthDate: String? = null

    @MapValue("rh")
    @LabelTitle("Sangre-RH")
    var rh: String? = null

    @MapValue("expeditionDate")
    @LabelTitle("Fecha de expedicion")
    var expeditionDate: String? = null

    @MapValue("restrictionsDriver")
    @LabelTitle("Restriciones del conductor")
    var restrictionsDriver: String? = null

    @MapValue("expeditionDriverOrganization")
    @LabelTitle("Organizacion de transito expedidor")
    var expeditionDriverOrganization: String? = null

    @MapValue("category1")
    @LabelTitle("Categoria")
    var category1: String? = null

    @MapValue("category2")
    @LabelTitle("Categoria")
    var category2: String? = null

    @MapValue("category3")
    @LabelTitle("Categoria")
    var category3: String? = null
}