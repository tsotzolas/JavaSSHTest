import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;

public class Httping  {


    /**
     * Make Httping to the server to take the latency
     * Httping reference site
     * https://www.cyberciti.biz/faq/linux-unix-measure-lateceny-throughput-of-webserver/
     * @param session
     * @param server that I want to test
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public static String callHttping (Session session,String server) throws JSchException, IOException {
        String finalstr = "";
        Channel channel=session.openChannel("exec");
        System.out.println("Execute command 'httping -fg "+server+":8080/lab '.");
        ((ChannelExec)channel).setCommand("httping  -G -g "+server+":8080/lab/login.jsf -c 4");

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
//                System.out.println(tempString);
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
        //Found in the string the "min/avg/max""
        int k = ArrayUtils.indexOf( t, "min/avg/max" );
        //Take the k+2 value that have the times and then split to take the avg
        String j[] = t[k+2].split("/");
        System.out.println("-----------------------------------------------");
        System.out.println("Server "+ server+ " average latency is :"+ j[1]);
        System.out.println("-----------------------------------------------");
        return j[1];
    }
}
