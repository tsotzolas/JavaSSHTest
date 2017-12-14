import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ApdexMeasure {





    public static double responceTime()  {
        System.setProperty("webdriver.chrome.driver", "/home/tsotzo/chromedriver");

        WebDriver driver = new ChromeDriver();
        driver.get("https://app.zapto.org/lab/login.jsf");
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        // time of the process of navigation and page load
        double loadTime = (Double) js.executeScript(
                "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart) / 1000");
        System.out.print(loadTime + " seconds"); // 5.15 seconds

        driver.close();
        return loadTime;
    }


    public static double apdexScore(double t){
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
