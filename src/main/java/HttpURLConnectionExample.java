import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();


        System.out.println("Testing 1 - Send Http GET request");
        int i;
        Double responceTime = new Double(0);
        for (i = 0; i < 10; i++) {
            try {

                long time = http.sendGet();
                if (i != 0) {
                    responceTime += Double.valueOf(time);
                }
            } catch (Exception e) {

            }
        }
        System.out.println("Final Responce time is : " + responceTime/9 );

    }

    /**
     * This is for testing if balancer throw packet in the reload
     * This method just send request to server
     *
     * @throws Exception
     */
    private long sendGet() throws Exception {
        long startTime = System.currentTimeMillis();
//        System.out.println("Start Time"+ startTime);
        String url = "https://app.zapto.org/lab/login.jsf";
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        con.setUseCaches(false);

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        long endTime = System.currentTimeMillis();
//        System.out.println("Finish Time"+ endTime);
        //print result
        System.out.println(response.toString());
        long totalTime = endTime - startTime;
        System.out.println(totalTime);

        return totalTime;

    }

}


