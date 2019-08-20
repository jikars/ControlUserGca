package gcatech.net.documentcapturepicture.config

import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

class ConfigDocument(var type : KClass<*>, var typeInterpreter : Class<*>, var webServiceType: Class<*>,
                     @LayoutRes var gothsFrontRes : Int, @LayoutRes var gothsBack : Int)