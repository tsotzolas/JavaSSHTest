import com.jcraft.jsch.*;
import java.io.*;


public class Main {



    public static void main(String[] args) {
//        String jsonString = "";
        String finalString = "";
        String server1 = "";
        String server2 = "";
        String user = "user";
        String password = "6973533175";
        String host = "83.212.102.71";
        int port = 2204;

        String remoteFile = "/etc/haproxy/scripts/ip.map";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");



            System.out.println(Httping.callHttping(session,"10.0.0.6"));
            System.out.println(Httping.callHttping(session,"10.0.0.7"));





            //Read file from remote server via ssh
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out = null;
            out = sftpChannel.get(remoteFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;

            FileWriter fstreamWrite = new FileWriter("ip.map");
            BufferedWriter out1 = new BufferedWriter(fstreamWrite);

            //Parse the file and change the server's names.
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String ip = (line.split(" ")[0]);
                String server = (line.split(" ")[1]);
                //Change the server name
                //Todo Here must put the logic to change the server name
                if (line.contains("server1")){
                    line = line.replace(server, "server2");

                }else{
                    line = line.replace(server, "server1");
                }
                System.out.println(line);
                System.out.println("------------------");

                out1.write(line.toString());
                out1.write("\n");

            }


            out1.close();
            out.close();


            //Take the temp file
            File newFile = new File("ip.map");
            //Put the file in the remote server
            sftpChannel.put(newFile.getAbsolutePath(),"/etc/haproxy/scripts");
            //Delete the temp file
            newFile.delete();


            //Reload Haproxy
                ReloadHaproxy.Reaload(session);






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
            System.out.println("----------------------");
            System.out.println("----------------------");
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


