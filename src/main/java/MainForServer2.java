import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainForServer2 {

    private static final String SERVER1 = "10.0.0.6";
    private static final String SERVER2 = "10.0.0.7";
    private static final String HOST = "83.212.102.71";
    private static final String USER = "user";
    private static final String PASSWORD = "6973533175";
    private static final Integer BALANCER_PORT = 2204;
    private static final Integer SERVER1_PORT = 2206;
    private static final Integer SERVER2_PORT = 2207;

    private static final String FILENAME = "/home/tsotzo/Desktop/filename.txt";

    public static void main(String[] args) throws IOException {
        String finalString = "";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, BALANCER_PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");


            FileWriter fw = new FileWriter(FILENAME,true); //the true will append the new data
            try {

                fw.write("Latency |  Load  \n");//appends the string to the file

                    for (int i=0;i<20;i++) {
                        // true = append file
//                        FileWriter fw = new FileWriter(FILENAME,true); //the true will append the new data
                        fw.write(Httping.callHttping(session, SERVER2)+"     |    "+ Uptime.callUptime(HOST, SERVER2_PORT, SERVER2)+ "\n");//appends the string to the file
//                        fw.close();
//                        bw.write(");
                        Thread.sleep(2000);
                    }

            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }finally {
                fw.close();
            }

            try {
                Uptime.callUptime(HOST, SERVER2_PORT, SERVER2);
            } catch (Exception ex) {
                System.out.println("############### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            }








            session.disconnect();
            System.out.println("Command Executed");
            System.out.println("DONE");
            System.out.println(finalString);





        } catch (Exception e) {
            System.out.println("############### Exception ###############");
            System.out.println(e);
            System.out.println("############### Exception ###############");
        }

    }
}


