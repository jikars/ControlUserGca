package gcatech.net.documentcapturepicture.documents

import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.LabelTitle
import gcatech.net.documentcapturepicture.annotations.MapValue

class CitizenshipCard : Document() {

      @Key
      @MapValue("documentNumber")
      @LabelTitle("Numero de documento")
      var documentNumber: String? = null

      @MapValue("firtsName")
      @LabelTitle("Nombre")
      var firtsName: String? = null

      @MapValue("lastName")
      @LabelTitle("Apellido")
      var lastName: String? = null

      @MapValue("birthPlace")
      @LabelTitle("Lugar de nacimiento")
      var birthPlace: String? = null

      @MapValue("birthDate")
      @LabelTitle("Fecha de nacimiento")
      var birthDate: String? = null

      @MapValue("stature")
      @LabelTitle("Statura")
      var stature: String? = null

      @MapValue("rh")
      @LabelTitle("RH")
      var rh: String? = null

      @MapValue("gender")
      @LabelTitle("Genero")
      var gender: String? = null

      @MapValue("expeditionPlaceAndDate")
      @LabelTitle("Fecha y lugar de expedicon")
      var expeditionPlaceAndDate: String? = null

}