package pl.hackerspace.nfz;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SpaReferralReader implements ISpaReferralReader {

    public static final String NFZ = "https://skierowania.nfz.gov.pl/ap-przegl-skier/servlet/skierowania/show_app";
    public static final String CACERTS_FILE_LOCATION = "cacerts.jks";
    public static final String CACERTS_FILE_PASSWORD = "changeit";

	public int getNumberInQueue(String referralId) throws Exception {
        System.setProperty("javax.net.debug", "ssl");

        HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(CACERTS_FILE_LOCATION, CACERTS_FILE_PASSWORD));
        HttpsURLConnection.setDefaultHostnameVerifier(getAllTrustingHostnameVerifier());

        // 1. Read jsessionid,
        String s = readWebpage(NFZ);
        //s.indexOf("jsessionid");
        System.out.println(s);
        String jsessionid = "";

        // 2. Read number in the queue for specified referralId
        //System.out.println(readWebpage(NFZ + ";jesssionid=" +jsessionid + "?...="+referralId));


		// TODO Auto-generated method stub
		return 0;
	}

    private String readWebpage(String webpageUrl) throws IOException {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        ((HttpsURLConnection) new URL(webpageUrl).openConnection()).getInputStream()
                )
        )){

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();

            return inputLine;
        }
    }

    private SSLSocketFactory getSocketFactory(String certsFileLocation, String certsFilePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(loadKeyManagers(certsFileLocation, certsFilePassword), createTrustAllCertsManager(), new SecureRandom());
        return sc.getSocketFactory();
    }

    private KeyManager[] loadKeyManagers(String certsFileLocation, String certsFilePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        try (InputStream ksIs = SpaReferralReader.class.getResourceAsStream(certsFileLocation)) {
            ks.load(ksIs, certsFilePassword.toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, certsFilePassword.toCharArray());
        return kmf.getKeyManagers();
    }

    /**
     *
     * @return a trust manager that does not validate certificate chains
     */
    private TrustManager[] createTrustAllCertsManager() {
        return new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
    }

    /**
     * @return all-trusting host name verifier
     */
    private HostnameVerifier getAllTrustingHostnameVerifier() {
        return new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

}
