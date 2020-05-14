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

                  if(comm.getMyOwnerInt() != comm.getMyId())
                  {
                    System.out.println(" Saving to server ...");
                    comm.writeRegistar(val);
                    Util.mySleep(1000);
                    // lock.sendMsg(comm.getMyOwnerInt(), "downgrade", comm.getMyId());
                    comm.setMyOwnerInt(comm.getMyId());
                    comm.setMyCurrent(comm.getMyId());
                    lock.broadcastMsg("invalidate", comm.getMyId());
                  }

                  System.out.println(" Write success. ");

                  lock.releaseCS();
                }

                else if(naredba.equals("read"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");


                  if(comm.getMyId() != comm.getMyOwnerInt())
                  {

                    System.out.println(" Loading registar from server... ");

                    lock.sendMsg(comm.getMyOwnerInt(), "downgrade", comm.getMyId());
                    lock.broadcastMsg("invalidate", comm.getMyId());
                    Util.mySleep(1000);
                    comm.setMyOwnerInt(comm.getMyId());

                    int novi = comm.readRegistar();
                    comm.writeMyRegistar(novi);
                    comm.setMyCurrent(comm.getMyId());

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
