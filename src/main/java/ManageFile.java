import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.*;

public class ManageFile {

    public static void callManageFile (Session session,  String remoteFile) throws JSchException, SftpException, IOException {

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
            String server1 = (line.split(" ")[1]);
            //Change the server name
            //Todo Here must put the logic to change the server name
            if (line.contains("server1")){
                line = line.replace(server1, "server2");

            }else{
                line = line.replace(server1, "server1");
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

    }

}
