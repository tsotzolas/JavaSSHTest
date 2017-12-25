import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;

public class ApdexMeasure {

    public static double loadTime() throws InterruptedException {
        WebDriver driver = null;
        try {

            File file = new File("/home/user/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
            System.setProperty("phantomjs.binary.path", file.getAbsolutePath());

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setJavascriptEnabled(true);
            caps.setCapability("cssSelectorsEnabled", false);
            caps.setCapability("applicationCacheEnabled", true);
            caps.setCapability("acceptSslCerts", true);

            driver = new PhantomJSDriver();
            driver = new PhantomJSDriver(caps);
            driver.get("http://10.0.0.7:8080/lab/login.jsf");
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            // time of the process of navigation and page load
            double loadTime = (Double) js.executeScript(
                    "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart) / 1000");
            System.out.print(loadTime + " seconds \n");

            return loadTime;
        }catch (Exception ex){
            System.out.println("****************"+ ex);
        }finally {
            driver.quit();
        }
        return 0;
    }


    public static double apdexScore(double t) throws InterruptedException {
        double satisfied = 0;
        double tolerating = 0;
        double finalScore = 0;

        for(int i=0;i<10 ;i++) {
            double responceTime = ApdexMeasure.loadTime();
            if (responceTime < t) {
                satisfied += 1;
            }

            if (responceTime < 4 * t && responceTime > t) {
                tolerating += 1;
            }
        }
        finalScore = (satisfied + (tolerating/2))/10;

        return finalScore;
    }


    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}
