package com.mipos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MercadoPagoActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webView = (WebView) findViewById(R.id.mercado_pago_webView);
		webView.getSettings().setJavaScriptEnabled(true);
		//			webView.loadUrl("http://www.google.com");

		//String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
		
		String customHtml = "<html><head>Hola</head><body>" +
				"<a href=\"https://www.mercadopago.com/checkout/pay?pref_id=129918644-d961c1c5-bcd5-4f41-9886-7774243abf29\"" +
				" name=\"MP-Checkout\" class=\"green-L-Rn\" mp-mode=\"modal\" onreturn=\"execute_my_onreturn\">Pagar</a>" +
				"<script type=\"text/javascript\">(function(){function $MPBR_load(){window.$MPBR_loaded !== " +
				"true && (function(){var s = document.createElement(\"script\");s.type = \"text/javascript\";s.async =" +
				" true;s.src = (\"https:\"==document.location.protocol?\"https://www.mercadopago.com/org-img/jsapi/mptools/buttons/\"" +
				":\"http://mp-tools.mlstatic.com/buttons/\")+\"render.js\";var x = " +
				"document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);window.$MPBR_loaded = " +
				"true;})();}window.$MPBR_loaded !== true ? (window.attachEvent ? window.attachEvent('onload', $MPBR_load)" +
				" : window.addEventListener('load', $MPBR_load, false)) : null;})();</script>" +
				"<script type=\"text/javascript\">function execute_my_onreturn (json) {if" +
				" (json.collection_status=='approved'){alert ('Pago acreditado');} else " +
				"if(json.collection_status=='pending'){alert ('El usuario no completó el pago');} else " +
				"if(json.collection_status=='in_process'){alert ('El pago está siendo revisado');} else" +
				" if(json.collection_status=='rejected'){alert ('El pago fué rechazado, el usuario puede" +
				" intentar nuevamente el pago');} else if(json.collection_status==null){alert" +
				" ('El usuario no completó el proceso de pago, no se ha generado ningún pago');}}</script></body></html>";
		
		webView.loadData(customHtml, "text/html", "UTF-8");

	}
	
}
