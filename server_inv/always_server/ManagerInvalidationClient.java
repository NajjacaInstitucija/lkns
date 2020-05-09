import java.lang.*; import java.util.*;
import java.net.*; import java.io.*;
public class ManagerInvalidationClient {

    BufferedReader din;
    PrintStream pout;
    int registar;
    boolean isCurrent;


    public ManagerInvalidationClient()
    {
       registar = 0;
       isCurrent = false;
    }

    public void getSocket() throws IOException {
        Socket server = new Socket(Symbols.nameServer,
                                                Symbols.ServerPort);
        din = new BufferedReader(
                    new InputStreamReader(server.getInputStream()));
        pout = new PrintStream(server.getOutputStream());
    }


    public void write(int val)
            throws IOException {
        getSocket();
        pout.println("write " + val);
        pout.flush();
    }


    public int read()
      throws IOException {
        getSocket();
        pout.println("read");
        pout.flush();
        return Integer.parseInt(din.readLine());
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

      public int getRegistar() { return registar; }
      public void setRegistar(int val) { registar = val; }

      public boolean getIsCurrent() { return isCurrent; }
      public void setIsCurrent(boolean val) { isCurrent = val; }

}
