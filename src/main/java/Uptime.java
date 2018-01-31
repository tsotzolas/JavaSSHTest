import com.jcraft.jsch.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;

public class Uptime {
    private static final String USER = "user";
    private static final String PASSWORD = "6973533175";

    /**
     * Make Httping to the server to take the latency
     * Httping reference site
     * https://www.cyberciti.biz/faq/linux-unix-measure-lateceny-throughput-of-webserver/
     *
     * @param server that I want to test
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public static String callUptime(String host, Integer port, String server) throws JSchException, IOException {

        try {
            JSch jsch = new JSch();
            //Connect with ssh to the server
            Session session = jsch.getSession(USER, host, port);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");

            String finalstr = "";
            //Execute the command that we want
            Channel channel = session.openChannel("exec");
            System.out.println("Execute command 'uptime  " + server);
            ((ChannelExec) channel).setCommand("uptime | grep -ohe 'load average[s:][: ].*' | awk '{ print $3 }'");

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
                    System.out.println(tempString);
                    finalstr += tempString;
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
            }
            in.close();
            channel.disconnect();
            //Take the value that we want
            String t = finalstr.substring(0, finalstr.length() - 2);

                System.out.println("-----------------------------------------------");
                System.out.println("Server " + server + " average load in last 1 minute:" +t );
                System.out.println("-----------------------------------------------");

                session.disconnect();
                System.out.println("Command Executed");
                System.out.println("DONE");
                return t;


        } catch (Exception ex) {
            System.out.println("-----------------------------------------------");
            System.out.println(ex);
            System.out.println("-----------------------------------------------");
        }
        return "20";
    }
}
