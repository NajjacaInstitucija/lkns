import java.lang.*; import java.util.*;
import java.net.*; import java.io.*;
public class Name {
    BufferedReader din;
    PrintStream pout;
    public void getSocket() throws IOException {
        Socket server = new Socket(Symbols.nameServer,
                                                Symbols.ServerPort);
        din = new BufferedReader(
                    new InputStreamReader(server.getInputStream()));
        pout = new PrintStream(server.getOutputStream());
    }
    public int insertName(String name, String hname, int portnum)
            throws IOException {
        getSocket();
        pout.println("insert " + name + " " + hname + " " + portnum);
        pout.flush();
        return Integer.parseInt(din.readLine());
    }
    public PortAddr searchName(String name) throws IOException {
        getSocket();
        pout.println("search " + name);
        pout.flush();
        String result = din.readLine();
        StringTokenizer st = new StringTokenizer(result);
        int portnum = Integer.parseInt(st.nextToken());
        String hname = st.nextToken();
        return new PortAddr(hname, portnum);
    }
    public static void main(String[] args) {
        Name myClient = new Name();
        try {
            myClient.insertName("hello1", "student.math.hr", 1000);
            myClient.insertName("hello2", "cromath.math.hr", 1010);
            myClient.insertName("hello3", "josko.pmf.hr", 1020);
            PortAddr pa = myClient.searchName("hello1");
            PortAddr pa2 = myClient.searchName("hello2");
            PortAddr pa3 = myClient.searchName("hello3");
            PortAddr pa5 = myClient.searchName("hello5");
            System.out.println(pa.getHostName() + ":" + pa.getPort());
            System.out.println(pa2.getHostName() + ":" + pa2.getPort());
            System.out.println(pa3.getHostName() + ":" + pa3.getPort());
            System.out.println(pa5.getHostName() + ":" + pa5.getPort());
        } catch (Exception e) {
            System.err.println("Server aborted:" + e);
        }
    }
}
