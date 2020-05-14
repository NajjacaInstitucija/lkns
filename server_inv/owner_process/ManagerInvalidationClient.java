import java.lang.*; import java.util.*;
import java.net.*; import java.io.*;
public class ManagerInvalidationClient {

    BufferedReader din;
    PrintStream pout;
    int registar;
    int isCurrent;
    boolean isOwner;
    int owner;


    public ManagerInvalidationClient()
    {
       registar = 0;
       owner = 0;
       isCurrent = 0;
       isOwner = false;
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
        System.out.println(din.readLine());
    }


    public int read()
      throws IOException {
        getSocket();
        pout.println("read");
        pout.flush();
        return Integer.parseInt(din.readLine());
      }

    public void setOwn(int val)
          throws IOException {
          getSocket();
          pout.println("setowner " + val);
          pout.flush();
      }

   public void setCurr(int val)
          throws IOException {
          getSocket();
          pout.println("setcurrent " + val);
          pout.flush();
        }

    public int getOwn()
          throws IOException {
            getSocket();
            pout.println("getowner");
            pout.flush();
            return Integer.parseInt(din.readLine());
          }

    public int getCurr()
      throws IOException {
        getSocket();
        pout.println("getcurrent");
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

      public int getIsCurrent() { return isCurrent; }
      public void setIsCurrent(int val) { isCurrent = val; }
      //current bi trebala bit lista

      // public boolean getIsOwner() { return isOwner; }
      // public void setIsOwner(boolean val) { isOwner = val; }

      public int getOwner() { return owner; }
      public void setOwner(int val) { owner = val; }

}
