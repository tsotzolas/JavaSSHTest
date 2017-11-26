import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

public class ReloadHaproxy {



    public static void Reaload (Session session) throws JSchException, IOException {
        //Command Execution
        Channel channel=session.openChannel("exec");
        System.out.println("Execute command 'sudo service haproxy reload'.");
        ((ChannelExec)channel).setCommand("echo 6973533175 | sudo -S service haproxy reload -sf");

        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();
        byte[] tmp=new byte[1024];
        while(true){
            while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                System.out.print(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
                System.out.println("exit-status: "+channel.getExitStatus());
                break;
            }
            try{Thread.sleep(1000);}catch(Exception ee){}
        }

        in.close();
        channel.disconnect();
    }


}
