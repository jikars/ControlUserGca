package gcatech.net.documentcapturepicture.documents

import gcatech.net.documentcapturepicture.annotations.Key
import gcatech.net.documentcapturepicture.annotations.LabelTitle
import gcatech.net.documentcapturepicture.annotations.MapValue

class CitizenshipCard : ModelDocument(){

      @MapValue("documentNumber")
      @LabelTitle("Numero de documento")
      @Key
      lateinit var  documentNumer  : String
}