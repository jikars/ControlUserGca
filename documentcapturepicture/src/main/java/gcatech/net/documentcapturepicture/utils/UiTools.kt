package gcatech.net.documentcapturepicture.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

class UiTools {

    companion object{
        fun getActivity(currentContext:Context): Activity? {
            var context = currentContext
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            return null
        }
    }
}