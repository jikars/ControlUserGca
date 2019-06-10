package gcatech.net.documentcapturepicture.config

import android.support.annotation.LayoutRes
import kotlin.reflect.KClass

abstract class ConfigDocument(var type : KClass<*>, var typeInterpreter : Class<*>, var webServiceType: Class<*>,
                              @LayoutRes var gothsFrontRes : Int, @LayoutRes var gothsBack : Int)