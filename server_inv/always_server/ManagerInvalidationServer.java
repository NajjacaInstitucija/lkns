import java.net.*;
import java.io.*;
import java.util.*;
public class ManagerInvalidationServer {
    int registar;
    NameTable table;
    public ManagerInvalidationServer() {
        registar = 0;
        table = new NameTable();
    }

    public int getRegistar() { return registar; }
    public void setRegistar(int val) { registar = val; }

    void handleclient(Socket theClient) {
        try {
            BufferedReader din = new BufferedReader
            (new InputStreamReader(theClient.getInputStream()));
            PrintWriter pout = new PrintWriter(theClient.getOutputStream());
            String getline = din.readLine();
            StringTokenizer st = new StringTokenizer(getline);
            String tag = st.nextToken();

            if (tag.equals("read")) {
                pout.println(getRegistar());
            }

            else if(tag.equals("write"))
            {
              int val = Integer.parseInt(st.nextToken());
              setRegistar(val);
            }

            else if (tag.equals("search"))
            {
                int index = table.search(st.nextToken());
                if (index == -1) // not found
                    pout.println(-1 + " " + "nullhost");
                else
                    pout.println(table.getPort(index) + " "
                    + table.getHostName(index));
            }

            else if (tag.equals("insert"))
            {
                String name = st.nextToken();
                String hostName = st.nextToken();
                int port = Integer.parseInt(st.nextToken());
                int retValue = table.insert(name, hostName, port);
                pout.println(retValue);
            }

            pout.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public static void main(String[] args) {
        ManagerInvalidationServer is = new ManagerInvalidationServer();
        System.out.println("ManagerInvalidationServer started:");
        try {
            ServerSocket listener = new ServerSocket(Symbols.ServerPort);
            while (true) {
                Socket aClient = listener.accept();
                is.handleclient(aClient);
                aClient.close();
            }
        } catch (IOException e) {
            System.err.println("Server aborted:" + e);
        }
    }
}
