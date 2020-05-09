import java.util.Scanner;
import java.util.StringTokenizer;

public class ManagerLockTester {

    public static void main(String[] args) throws Exception {
        ManagerLinker comm = null;
        try {
            String baseName = args[0];
            int myId = Integer.parseInt(args[1]);
            int numProc = Integer.parseInt(args[2]);
            comm = new ManagerLinker(baseName, myId, numProc);

            ManagerLamportMutex lock = new ManagerLamportMutex(comm);

            for (int i = 0; i < numProc; i++)
               if (i != myId)
                  (new ListenerThread(i, (MsgHandler)lock)).start();
            while (true) {

                System.out.println(" enter (read / write + (int)): ");
                Scanner keyboard = new Scanner(System.in);
                String input = keyboard.nextLine();

                StringTokenizer st = new StringTokenizer(input);
                String naredba = st.nextToken();

                if(naredba.equals("write"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");

                  int val = Integer.parseInt(st.nextToken());

                  comm.writeMyRegistar(val);
                  comm.writeRegistar(val);
                  comm.setMyCurrent(true);

                  System.out.println(" Write success. ");
                  lock.broadcastMsg("invalidate", 0);

                  lock.releaseCS();
                }

                else if(naredba.equals("read"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");

                  if(!comm.getMyCurrent())
                  {
                    System.out.println(" Loading registar from server... ");
                    int novi = comm.readRegistar();
                    comm.writeMyRegistar(novi);
                    comm.setMyCurrent(true);
                  }
                  System.out.println(" STANJE REGISTRA ---> " + comm.readMyRegistar());
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
