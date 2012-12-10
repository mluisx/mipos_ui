package com.mipos.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context 
     * @return */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toastText) {
        Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
    }
}
