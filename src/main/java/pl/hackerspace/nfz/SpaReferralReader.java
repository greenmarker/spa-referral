package pl.hackerspace.nfz;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class SpaReferralReader implements ISpaReferralReader {

    public static final String NFZ = "https://skierowania.nfz.gov.pl/";
    public static final String CACERTS_FILE_LOCATION = "cacerts.jks";
    public static final String CACERTS_FILE_PASSWORD = "changeit";

	public int getNumberInQueue(String referralId) throws Exception {
        System.setProperty("javax.net.debug", "ssl");
        //spareferralreader

        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream ksIs = SpaReferralReader.class.getResourceAsStream(CACERTS_FILE_LOCATION);
        try {
            ks.load(ksIs, CACERTS_FILE_PASSWORD.toCharArray());
        } finally {
            if (ksIs != null) {
                ksIs.close();
            }
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, CACERTS_FILE_PASSWORD.toCharArray());

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        URL url = new URL(NFZ);
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        ((HttpsURLConnection) url.openConnection()).getInputStream()
                )
            )){

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();

            System.out.println(inputLine);
        }




		// TODO Auto-generated method stub
		return 0;
	}

}
