package com.mipos.activities;

import java.math.BigDecimal;

import com.mipos.asyncs.CashOpenAsync;
import com.mipos.pojos.Cash;
import com.mipos.pojos.Sale;
import com.mipos.pojos.SerializedBitmap;
import com.mipos.utils.WebAppInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MercadoPagoActivity extends Activity {

	private WebView webView;
	private CookieManager cookieManager;
	private Button exitButton;
	private Bundle extras;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		exitButton = (Button) findViewById(R.id.mercado_pago_button);
		exitButton.setEnabled(false);
		webView = (WebView) findViewById(R.id.mercado_pago_webView);
		webView.setWebViewClient(new WebViewClient() {		
			@Override 
			//show the web page in webview but not in web browser
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("MercadoPagoActivity", "shouldOverrideUrlLoading: " + url);
				view.loadUrl (url);
				if (url.contains("https://www.mercadopago.com/mla/checkout/pay?execution=e2s5&_eventId=next")) {
					exitButton.setEnabled(true);
				}
				return true;
			}; });
		webView.setWebChromeClient(new WebChromeClient() {
			@Override  
			public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)   
			{  
				Log.i("MercadoPagoActivity", "onJsAlert: " + message);
				return true;  
			};  }); 			
		webView.getSettings().setJavaScriptEnabled(true);
		cookieManager = CookieManager.getInstance();
		cleanCookies();
		//webView.loadUrl("http://www.google.com");

		//String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
		
		// String customHtml = "<html><body><div onclick=\"alert('hello')\"> Click Me !!  </div></body></html>";
		
		String customHtml = "<html><style type=\"text/css\">#myid {z-index: 1000;position: absolute;" +
				"line-height: 40px;padding: 0px 50px;font-size: 20px;color: white;margin: 85% 20%;" +
				"background: url(http://mp-tools.mlstatic.com/buttons/assets/MP-payButton-green.png) #077574;" +
				"border: 1px solid #0B898B;font-weight: normal;text-shadow: #0B898B 1px 1px;cursor: pointer;" +
				"display: inline-block;border-top-left-radius: 7px;border-top-right-radius: 7px;" +
				"border-bottom-right-radius: 7px;border-bottom-left-radius: 7px;text-decoration: initial;" +
				"border-image: initial;}</style><body>" +
				"<iframe onreturn=\"execute_my_onreturn\" src=\"https://www.mercadopago.com/checkout" +
				"/pay?pref_id=129918644-d961c1c5-bcd5-4f41-9886-7774243abf29\" name=\"MP-Checkout\" " +
				"width=\"100%\" height=\"100%\" frameborder=\"0\"></iframe>" +
				
			//	"<a href=\"https://www.mercadopago.com/checkout/pay?pref_id=129918644-d961c1c5-bcd5-4f41-9886-7774243abf29\"" +
			//	" name=\"MP-Checkout\" class=\"green-L-Rn\" mp-mode=\"modal\" onreturn=\"execute_my_onreturn\">Pagar</a>" +
				"<a id=\"myid\" href=\"#\" onClick=\"closeMPIframe();\" >Finalizar</a>" +
				"<script type=\"text/javascript\">(function(){function $MPBR_load(){window.$MPBR_loaded !== " +
				"true && (function(){var s = document.createElement(\"script\");s.type = \"text/javascript\";s.async =" +
				" true;s.src = (\"https:\"==document.location.protocol?\"https://www.mercadopago.com/org-img/jsapi/mptools/buttons/\"" +
				":\"http://mp-tools.mlstatic.com/buttons/\")+\"render.js\";var x = " +
				"document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);window.$MPBR_loaded = " +
				"true;})();}window.$MPBR_loaded !== true ? (window.attachEvent ? window.attachEvent('onload', $MPBR_load)" +
				" : window.addEventListener('load', $MPBR_load, false)) : null;})();" +
				"function closeMPIframe(trigger,isMessage){if($MPC.Modal.__opened&&!$MPC.Modal.__isLoading&&!$MPC.Modal.__closeRequested)" +
				"{if($MPC.Modal.__dialog.__container.contentWindow.postMessage!=null){$MPC.Modal.__dialog.__container.contentWindow.postMessage(" +
				"{\"action\":\"requestClose\"},\"*\");$MPC.Modal.__closeRequested=true;$MPC.Modal.__closeTimeout=" +
				"setTimeout($MPC.__actions.close,2000)}}}</script>" + 
				"<script type=\"text/javascript\">function execute_my_onreturn (json) {if" +
				" (json.collection_status=='approved'){alert ('Pago acreditado'); Android.showToast('approved');} else " +
				"if(json.collection_status=='pending'){alert ('El usuario no completó el pago'); Android.showToast('pending');} else " +
				"if(json.collection_status=='in_process'){alert ('El pago está siendo revisado'); Android.showToast('in_process');} else" +
				" if(json.collection_status=='rejected'){alert ('El pago fué rechazado, el usuario puede" +
				" intentar nuevamente el pago'); Android.showToast('rejected');} else if(json.collection_status==null){alert" +
				" ('El usuario no completó el proceso de pago, no se ha generado ningún pago'); Android.showToast('null');} else {alert" +
				" ('json.collection_status'); Android.showToast(json.collection_status);}}" +
				"</script><script>function testEcho(message){window.Android.showToast(message);}</script>" +
				"</body></html>";
		
		webView.loadData(customHtml, "text/html", "UTF-8");

		webView.addJavascriptInterface(new WebAppInterface(this), "Android");
		
	//	webView.loadUrl("javascript:testEcho('Hello World!')");
		
		exitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finishActivity();
			}			
		});
		
		extras = getIntent().getExtras();
		
	}
	
	private void cleanCookies() {
	    cookieManager.removeAllCookie();	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
	    	Log.i("MercadoPagoActivity", "GoBack");
	    	webView.goBack();
	    	if (!webView.canGoBack()) {
	    		Log.i("MercadoPagoActivity", "CleanCookies");
	    		cleanCookies();
//	    		super.onBackPressed();
	    	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    	    // system behavior (probably exit the activity)
	    	    return super.onKeyDown(keyCode, event);
	    	}
	        return true;
	    }
	    return false;
	}
	
	public void finishActivity() {
		Intent intent = new Intent();
		Sale saleToSend = null;
		if (extras != null) {
			saleToSend = (Sale) extras.getSerializable("SaleData");
		}
		intent.putExtra("SaleData", saleToSend);
		setResult(RESULT_OK, intent);
		finish();
	}
	
}
