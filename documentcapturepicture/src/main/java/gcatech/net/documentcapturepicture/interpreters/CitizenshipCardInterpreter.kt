package gcatech.net.documentcapturepicture.interpreters

import gcatech.net.documentcapturepicture.documents.CitizenshipCard
import java.text.SimpleDateFormat
import java.util.*

class CitizenshipCardInterpreter : IInterpreter<CitizenshipCard> {

    private var charsSpace  = arrayListOf("(\u0000){1,}")
    private var secuenceSubstring  = arrayListOf("O+","O-","B+","B-","A+","A-")
    private  var hasValue : Boolean = false


    override fun builder(value: String): CitizenshipCard {

        var list : MutableList<String>? = null

        secuenceSubstring.forEach {
            if (value.contains(it)) {
                if(!hasValue){
                    val mach = value.substring(0,value.indexOf(it )+ it.length)
                    charsSpace.forEach {itS->
                        if(!hasValue){
                            list =  mach.split(itS.toRegex()).toMutableList()
                            hasValue = list?.size!! >= 5
                        }
                    }
                }
            }
        }

        list?.removeAt(0)
        list?.removeAt(0)
        val cc =  list?.get(0)?.replace("[A-Za-z]".toRegex(),"")?.takeLast(10)?.replaceFirst("^0+(?!$)".toRegex(), "")

        val lastMap = list?.last()

        val lastName =  list?.get(0)?.split(cc!!)?.last() + " " +  list?.get(1)!!;

        val firtName : String = list?.get(2)!! + " " +  if(list?.count()!! >= 4)  list?.get(3)!! else ""

        val gender = lastMap!![1]

        val date = lastMap.substring(2,10)

        val rh = lastMap.substring(lastMap.length-2, lastMap.length)

        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        val outputFormat = SimpleDateFormat("dd/MMM/yyyy",Locale.US)

        val dateOut = inputFormat.parse(date)
        val birthDate = outputFormat.format(dateOut)

        val citizenshipCardCol= CitizenshipCard()

        citizenshipCardCol.documentNumber = cc
        citizenshipCardCol.firtsName = firtName
        citizenshipCardCol.lastName = lastName
        citizenshipCardCol.gender = gender.toString()
        citizenshipCardCol.rh = rh
        citizenshipCardCol.birthDate = birthDate

        return citizenshipCardCol
    }
}