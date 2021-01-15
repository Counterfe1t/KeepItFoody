package com.example.kuba.keepitfoody;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Volley request queue singleton objects handling all incoming and outgoing requests.
 */
public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private Context context;

    private VolleySingleton(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(
            context.getApplicationContext(), new HurlStack(null, getSocketFactory()));
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Attach self-signed certificate from resources to SSLSocketFactory
     * object.
     * @return An object of type SSLSocketFactory
     */
    public SSLSocketFactory getSocketFactory() {
        CertificateFactory cf;

        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources().openRawResource(R.raw.ca);
            Certificate ca;

            try {
                ca = cf.generateCertificate(caInput);
                Log.e("CERT", "CA=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            SSLContext context;
            context = SSLContext.getInstance("TLS");

            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            return context.getSocketFactory();

        } catch (CertificateException |
                NoSuchAlgorithmException |
                KeyStoreException |
                IOException |
                KeyManagementException e) {
            Log.e("VOLLEY_SINGLETON", "getSocketFactory: ", e);
        }

        return  null;
    }
}
