import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.xpath.SourceTree;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;

public class ApdexMeasure {





    public static double responceTime() throws InterruptedException {
//        final DesiredCapabilities caps = DesiredCapabilities.chrome();
//        final ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setBinary("/usr/bin/chromium-browser");
//        chromeOptions.addArguments("headless");
//        chromeOptions.addArguments("window-size=1200x600");
//        //chromeOptions.addArguments("--headless", "--window-size=1060x780", "--disable-gpu");
//        caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
//
//        System.setProperty("webdriver.chrome.driver", "../chromedriver");
//        System.setProperty("webdriver.gecko.driver", "../geckodriver");
//        WebDriver driver = new ChromeDriver();
        WebDriver driver = null;
        try {

            File file = new File("/home/user/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");

            System.setProperty("phantomjs.binary.path", file.getAbsolutePath());

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setJavascriptEnabled(true);
            caps.setCapability("cssSelectorsEnabled", false);
            caps.setCapability("applicationCacheEnabled", true);
            caps.setCapability("acceptSslCerts", true);
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,phantomJsPath);


            driver = new PhantomJSDriver();
            driver = new PhantomJSDriver(caps);
            driver.get("http://10.0.0.7:8080/lab/login.jsf");
//        WebDriver driver = new FirefoxDriver();
//        driver.get("https://app.zapto.org/lab/login.jsf");
//        Thread.sleep(1000);
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            // time of the process of navigation and page load
            double loadTime = (Double) js.executeScript(
                    "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart) / 1000");
            System.out.print(loadTime + " seconds \n"); // 5.15 seconds

            driver.close();
            return loadTime;
        }catch (Exception ex){
            System.out.println("****************"+ ex);
        }finally {
            if (!driver.toString().contains("(null)"))
            driver.close();
        }
        return 0;
    }


    public static double apdexScore(double t) throws InterruptedException {
        double satisfied = 0;
        double tolerating = 0;
        double finalScore = 0;

        for(int i=0;i<10 ;i++) {
            double responceTime = ApdexMeasure.responceTime();
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
