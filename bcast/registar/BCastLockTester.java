import java.util.Scanner;
import java.util.StringTokenizer;

public class BCastLockTester {
    public static void main(String[] args) throws Exception {
        BCastLinker comm = null;
        try {
            String baseName = args[0];
            int myId = Integer.parseInt(args[1]);
            int numProc = Integer.parseInt(args[2]);
            comm = new Linker(baseName, myId, numProc);

            LamportMutex lock = new LamportMutex(comm);

            for (int i = 0; i < numProc; i++)
               if (i != myId)
                  (new ListenerThread(i, (MsgHandler)lock)).start();
            while (true) {
                // System.out.println(myId + " is not in CS");
                // Util.mySleep(2000);
                System.out.println(" enter (read / write + (int)): ");
                Scanner keyboard = new Scanner(System.in);
                String input = keyboard.nextLine();

                StringTokenizer st = new StringTokenizer(input);
                String naredba = st.nextToken();

                if(naredba.equals("write"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");
                  if(st.hasMoreTokens())
                  {
                    int num = Integer.parseInt(st.nextToken());
                    comm.setRegistar(num);
                    lock.broadcastMsg("change", comm.getRegistar());
                  }
                  lock.releaseCS();
                }

                else if(naredba.equals("read"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");
                  System.out.println(" STANJE REGISTRA ---> " + comm.getRegistar());
                  lock.releaseCS();

                }

            }
        }
        catch (InterruptedException e) {
            if (comm != null) comm.close();
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
