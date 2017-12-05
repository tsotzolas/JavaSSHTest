import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.ArrayUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResponceTimeCurl {


    public static String responceTime(Session session, String server) throws JSchException, IOException {
        String finalstr = "";
        Channel channel = session.openChannel("exec");
        System.out.println("Execute command 'curl -o /dev/null -s -w %{time_total}  http://" + server + ":8080/lab/login.jsf'.");
        ((ChannelExec) channel).setCommand("curl -o /dev/null -s -w %{time_total}\\\\n  http://" + server + ":8080/lab/login.jsf\n");

        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                String tempString = new String(tmp, 0, i);
//                System.out.println(tempString);
                finalstr += tempString;
            }
            if (channel.isClosed()) {
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
        }
        in.close();
        channel.disconnect();


        return finalstr;
    }
}









