package com.aaonri.app.utils

import android.app.Activity
import android.app.Dialog
import com.aaonri.app.utils.CustomDialog
import com.aaonri.app.R
import android.content.DialogInterface
import android.view.KeyEvent

object CustomDialog {
    var loadDialog: Dialog? = null
    fun showLoader(context: Activity?) {
        if (loadDialog != null) {
            if (loadDialog!!.isShowing) loadDialog!!.dismiss()
        }
        loadDialog = Dialog(context!!, R.style.TransparentDialogTheme)
        loadDialog!!.setContentView(R.layout.loader)
        loadDialog!!.setCanceledOnTouchOutside(false)
        loadDialog!!.setOnKeyListener { dialog, keyCode, event -> if (keyCode == KeyEvent.KEYCODE_BACK) true else false }
        if (!loadDialog!!.isShowing) {
            loadDialog!!.show()
        }
    }

    fun hideLoader() {
        if (loadDialog != null && loadDialog!!.isShowing) loadDialog!!.dismiss()
    }
}