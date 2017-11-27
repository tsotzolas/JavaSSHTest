import com.jcraft.jsch.*;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;


public class Main {

    private static final String SERVER1 = "10.0.0.6";
    private static final String SERVER2 = "10.0.0.7";
    private static final String HOST = "83.212.102.71";
    private static final String USER = "user";
    private static final String PASSWORD = "6973533175";
    private static final Integer BALANCER_PORT = 2204;
    private static final Integer SERVER1_PORT = 2206;
    private static final Integer SERVER2_PORT = 2207;

    public static void main(String[] args) {
        String finalString = "";
        String remoteFile = "/etc/haproxy/scripts/ip.map";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, BALANCER_PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");


            System.out.println("****************Httping Server1**********");
            System.out.println("**                                     **");
            try {
                Httping.callHttping(session, SERVER1);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }

            System.out.println("**                                     **");
            System.out.println("****************Httping Server1**********");


            System.out.println("****************Httping Server2**********");
            System.out.println("**                                     **");
            try {
                Httping.callHttping(session, SERVER2);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }

            System.out.println("**                                     **");
            System.out.println("****************Httping Server2**********");


            System.out.println("****************Uptime Server1**********");
            System.out.println("**                                    **");
            try {
                Uptime.callUptime(HOST, SERVER1_PORT, SERVER2);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }
            System.out.println("**                                    **");
            System.out.println("****************Uptime Server1**********");


            System.out.println("****************Uptime Server2**********");
            System.out.println("**                                    **");
            try {
                Uptime.callUptime(HOST, SERVER2_PORT, SERVER2);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }
            System.out.println("**                                    **");
            System.out.println("****************Uptime Server2**********");


            System.out.println("****************Manage File**********");
            System.out.println("**                                 **");
            try {
                ManageFile.callManageFile(session, remoteFile);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }
            System.out.println("**                                 **");
            System.out.println("****************Manage File**********");


            System.out.println("****************Reload Haproxy**********");
            System.out.println("**                                   **");
            try {
                ReloadHaproxy.Reaload(session);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }
            System.out.println("**                                   **");
            System.out.println("****************Reload Haproxy**********");






          /*  //Command Execution
            Channel channel1=session.openChannel("exec");
            System.out.println("Execute command 'iperf3 -c 10.0.0.6 -J'.");
            ((ChannelExec)channel1).setCommand("iperf3 -c 10.0.0.7 -f k  -J");

            channel1.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream in1 = channel1.getInputStream();
            channel1.connect();

            byte[] tmp1=new byte[1024];
            while(true){
                while(in1.available()>0){
                    int i=in1.read(tmp1, 0, 1024);
                    if(i<0)break;
                    jsonString +=  new String(tmp1, 0, i);



//                    System.out.println(new String(tmp1, 0, i));
//                    JSONObject json = new JSONObject(jsonString);
//                    System.out.println(json);
                }
                if(channel1.isClosed()){
                    System.out.println("exit-status: "+channel1.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
                //                    JSONParser parser = new JSONParser();


            }

            in1.close();
//            br.close();
            channel1.disconnect();*/


            session.disconnect();
            System.out.println("Command Executed");
            System.out.println("DONE");
            System.out.println(finalString);

//            String t[] = finalString.split(" ");
//            int k = ArrayUtils.indexOf( t, "min/avg/max" );
//            System.out.println(ArrayUtils.indexOf( t, "min/avg/max" ));
//            String j[] = t[k+2].split("/");
//            System.out.println(j[1]);


//            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
//            sftpChannel.connect();
//            System.out.println("SFTP Channel created.");
//
//
//            InputStream out = null;
//            out = sftpChannel.get(remoteFile);
//            BufferedReader br = new BufferedReader(new InputStreamReader(out));
//            String line;
//            while ((line = br.readLine()) != null)
//                System.out.println(line);
//            br.close();


//            String command = "ls";
//            Process cmdProc = Runtime.getRuntime().exec(command);
//
//
//            BufferedReader stdoutReader = new BufferedReader(
//                    new InputStreamReader(cmdProc.getInputStream()));
//            String line1;
//            while ((line1 = stdoutReader.readLine()) != null) {
//                System.out.println(line1);
//            }
//
//            BufferedReader stderrReader = new BufferedReader(
//                    new InputStreamReader(cmdProc.getErrorStream()));
//            while ((line1 = stderrReader.readLine()) != null) {
//                System.out.println(line1);
//            }
//
//            int retValue = cmdProc.exitValue();
//            System.out.println(retValue);


        } catch (Exception e) {
            System.out.println("############### Exception ###############");
            System.out.println(e);
            System.out.println("############### Exception ###############");
        }


//        JSONObject json = new JSONObject(jsonString);

//        GsonBuilder builder = new GsonBuilder();
//        Object o = builder.create().fromJson(jsonString, Object.class);
//        Object o = new Gson().fromJson(jsonString, Object.class);
//        Double bytes = o.get("end").get("sum_sent").get("bytes");
        System.out.println("*************");
//        System.out.println(o);
        System.out.println("*************");


    }
}


