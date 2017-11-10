import com.jcraft.jsch.*;

import java.io.*;


public class Main {

    public static void main(String[] args) {
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


            //Command Execution
            Channel channel=session.openChannel("exec");
            System.out.println("Execute command 'sudo service haproxy restart'.");
            ((ChannelExec)channel).setCommand("echo 6973533175 | sudo -S service haproxy reload");

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
            br.close();
            channel.disconnect();
            session.disconnect();
            System.out.println("Command Executed");
            System.out.println("DONE");






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
            System.err.print(e);
        }
    }
}


