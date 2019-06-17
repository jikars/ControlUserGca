package gcatech.net.documentcapturepicture.utils

import android.os.AsyncTask

class DoAsync (val handler: () -> Unit, var postExecute:() -> Unit ) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        postExecute()
    }
}