import com.jcraft.jsch.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;

public class Uptime {

    /**
     * Make Httping to the server to take the latency
     * Httping reference site
     * https://www.cyberciti.biz/faq/linux-unix-measure-lateceny-throughput-of-webserver/
     * @param server that I want to test
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public static String callUptime (String host,Integer port,String server) throws JSchException, IOException {
        String user = "user";
        String password = "6973533175";
//        String host = "83.212.102.71";
//        int port = 2206;


        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        System.out.println("Establishing Connection...");
        session.connect();
        System.out.println("Connection established.");




        String finalstr = "";
        Channel channel=session.openChannel("exec");
        System.out.println("Execute command 'uptime  "+server);
        ((ChannelExec)channel).setCommand("uptime");

        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] tmp=new byte[1024];
        while(true){
            while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                String tempString =  new String(tmp, 0, i);
                System.out.println(tempString);
                finalstr+=tempString;
            }
            if(channel.isClosed()){
                System.out.println("exit-status: "+channel.getExitStatus());
                break;
            }
        }
        in.close();
        channel.disconnect();




        //Take the information that I want from the whole string
        String t[] = finalstr.split(" ");
        //Take the k+2 value that have the times and then split to take the avg
        int k = ArrayUtils.indexOf( t, "average:" );
        System.out.println("-----------------------------------------------");
        System.out.println("Server "+ server+ " average load in last 5 minute:"+ t[k+2].substring(0, t[k+2].length() - 1));
        System.out.println("-----------------------------------------------");


        session.disconnect();
        System.out.println("Command Executed");
        System.out.println("DONE");

        return t[12];











    }



}