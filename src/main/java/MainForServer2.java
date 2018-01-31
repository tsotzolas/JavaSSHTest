import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;


public class MainForServer2 {

    private static final String SERVER1 = "10.0.0.6";
    private static final String SERVER2 = "10.0.0.7";
    private static final String HOST = "83.212.102.71";
    private static final String USER = "user";
    private static final String PASSWORD = "6973533175";
    private static final Integer BALANCER_PORT = 2204;
    private static final Integer SERVER1_PORT = 2206;
    private static final Integer SERVER2_PORT = 2207;

    private static final String FILENAME = "/home/user/filename.txt";
//    private static final String FILENAME = "/home/tsotzo/Desktop/filename.txt";

    private static final double T = 1;

    public static void main(String[] args) throws IOException {
        String finalString = "";

        try {
//            JSch jsch = new JSch();
//            Session session = jsch.getSession(USER, HOST, BALANCER_PORT);
//            session.setPassword(PASSWORD);
//            session.setConfig("StrictHostKeyChecking", "no");
//            System.out.println("Establishing Connection...");
//            session.connect();
//            System.out.println("Connection established.");
//            System.out.println("Crating SFTP Channel.");


            double satisfied = 0;
            double tolerating = 0;
            double finalScore = 0;

            double averageLoad = 0;
            double l1 = 0;
            double l2 = 0;

            double responceTimeCurl = 0;
            double finalResponceTime = 0;

            FileWriter fw = new FileWriter(FILENAME, true); //the true will append the new data
            try {

//                fw.write("Time to load  page   |  Load       | \n");//appends the string to the file

                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 10; i++) {
                    System.out.println(i);

                    double rt = 0;
                    rt = ApdexMeasure.responceTime();
                    System.out.println(rt);
                    Thread.sleep(500);
                    double ut = 0;
                    String uptime = "";
                    uptime = Uptime.callUptime(SERVER2, 22, SERVER2);
//                        uptime = Uptime.callUptime(HOST, SERVER2_PORT, SERVER2);

                    if (uptime.contains(",")) {
                        uptime = uptime.replace(",", ".");
                    }
                    if (uptime.equals("0")) {
                        ut = averageLoad / (i + 1);

                        System.out.println("****************Error Uptime***************");
                    } else {
                        ut = Double.valueOf(uptime);
                    }

                    if (i == 0) {
                        l1 = ut;
                    }
                    if (i == 9) {
                        l2 = ut;
                    }




//                        fw.write(Httping.callHttping(session, SERVER2)+"      |    "+ Uptime.callUptime(HOST, SERVER2_PORT, SERVER2)+ "   |   "+ ResponceTime.callResponceTime()+ "     |        "+ResponceTimeCurl.responceTime(session,SERVER2)+"\n");//appends the string to the file
//                        fw.write(Httping.callHttping(session, SERVER2)+"      |    "+ Uptime.callUptime(HOST, SERVER2_PORT, SERVER2)+ "   |   "+"---"+ "     |        "+ResponceTimeCurl.responceTime(session,SERVER2)+"\n");//appends the string to the file
//                        fw.write("--------"+"      |    "+ Uptime.callUptime(HOST, SERVER2_PORT, SERVER2)+ "   |   "+ResponceTime.callResponceTime()+ "     |        "+ResponceTimeCurl.responceTime(session,SERVER2)+"");//appends the string to the file
//                        fw.write(rt+ "                |        "+ut+"                  | "+"\n" );//appends the string to the file

//                        System.out.println(ResponceTimeCurl.responceTime(session,SERVER2));

//                        System.out.println(ResponceTime.callResponceTime());
//                        Thread.sleep(500);

                    if (rt <= T) {
                        satisfied += 1;
                    }

                    if (rt < 4 * T && rt > T) {
                        tolerating += 1;
                    }



                    averageLoad += ut;

                    responceTimeCurl += Double.valueOf(ResponceTimeCurlLocal());
                    System.out.println("----------------Responce Time----------------");
                    System.out.println(Double.valueOf(ResponceTimeCurlLocal()));
                    System.out.println(responceTimeCurl);
                    System.out.println("---------------------------------------------");

                }





                long endTime = System.currentTimeMillis();

                finalResponceTime = responceTimeCurl/10;

                finalScore = (satisfied + (tolerating / 2)) / 10;
                System.out.println("Final Score : " + finalScore);
                fw.write("---------------------------------------------" + "\n");//appends the string to the file
//                fw.write("Final Score : "+finalScore+"\n" );//appends the string to the file
//                fw.write("Average Load : "+averageLoad/10+"\n" );//appends the string to the file
//                fw.write("---------------------------------------------"+"\n" );//appends the string to the file


                //
                long totalTime = (endTime - startTime);

                double test = l2 - l1;
//                if (test<0){
//                    test = test*(-1);
//                }
                double dl = test / (Double.valueOf(totalTime) / 1000);


//                fw.write("Apdex Score   |   DLoad       | Average Load  | Average Responce Time \n");//appends the string to the file
//                fw.write(finalScore + "           |   " + dl + "    |    " + averageLoad / 10 + "          |              "+ finalResponceTime +" \n");//appends the string to the file

                System.out.println("Final Score : " + finalScore);
                System.out.println("dLoad : " + dl);


            } catch (Exception ex) {
                System.out.println("######1######### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            } finally {
                fw.close();
            }

//            try {
//                Uptime.callUptime(HOST, SERVER2_PORT, SERVER2);
//            } catch (Exception ex) {
//                System.out.println("############### Exception ###############");
//                System.out.println(ex);
//                System.out.println("############### Exception ###############");
//            }


//            session.disconnect();
            System.out.println("Command Executed");
            System.out.println("DONE");
            System.out.println(finalString);


        } catch (Exception e) {
            System.out.println("############### Exception ###############");
            System.out.println(e);
            System.out.println("############### Exception ###############");
        }

    }


    public static String ResponceTimeCurlLocal() {
        // read the output from the command
        String s="";

        try {
            Process p = Runtime.getRuntime().exec("curl -o /dev/null -s -w %{time_total}\\\\\\\\n  http://10.0.0.7:8080/lab/login.jsf\\n\"");
            // you can pass the system command or a script to exec command. here i used uname -a system command
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));


            while ((s = stdInput.readLine()) != null) {
                System.out.println("Std OUT: "+s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println("Std ERROR : "+s);
            }

            return s;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return s;
    }

}


