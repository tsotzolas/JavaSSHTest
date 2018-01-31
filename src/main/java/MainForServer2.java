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

    //    private static final String FILENAME = "/filename.txt";
    private static final String FILENAME = "/home/user/filename.txt";

    private static final double T = 1;

    public static void main(String[] args) throws IOException {
        String finalString = "";

        try {
            double satisfied = 0;
            double tolerating = 0;
            double finalScore = 0;

            double averageLoad = 0;

            FileWriter fw = new FileWriter(FILENAME, true);
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i);
                    double rt = 0;
                    //Measure the load time for the reference page
                    rt = ApdexMeasure.loadTime();
                    System.out.println(rt);
                    Thread.sleep(500);
                    double ut = 0;
                    String uptime = "";
                    //Measure the load of the server
                    uptime = Uptime.callUptime(SERVER2, 22, SERVER2);
                    System.out.println("Uptime----->" + uptime);
                    if (uptime.contains(",")) {
                        uptime = uptime.replace(",", ".");
                    }
                    if (uptime.equals("0")) {
                        ut = 20;
                        System.out.println("****************Error Uptime***************");
                    } else {
                        ut = Double.valueOf(uptime);
                    }

                    if (rt <= T) {
                        satisfied += 1;
                    }

                    if (rt < 4 * T && rt > T) {
                        tolerating += 1;
                    }
                    averageLoad += ut;
                }
                 //Calculate the final Apdex Score
                finalScore = (satisfied + (tolerating / 2)) / 10;

                System.out.println("Final Score : " + finalScore);
                fw.write("---------------------------------------------" + "\n");
                fw.write("Apdex Score   |   Load       | \n");
                fw.write(finalScore + "           |   " + averageLoad / 10 + "   | \n");
            } catch (Exception ex) {
                System.out.println("######1######### Exception ###############");
                System.out.println(ex);
                System.out.println("############### Exception ###############");
            } finally {
                fw.close();
            }

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
