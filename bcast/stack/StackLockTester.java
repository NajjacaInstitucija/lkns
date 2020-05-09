import java.util.Scanner;
import java.util.StringTokenizer;

public class StackLockTester {
    public static void main(String[] args) throws Exception {
        StackLinker comm = null;
        try {
            String baseName = args[0];
            int myId = Integer.parseInt(args[1]);
            int numProc = Integer.parseInt(args[2]);
            comm = new StackLinker(baseName, myId, numProc);


            StackCircToken lock = new StackCircToken(comm, 0);
            lock.initiate();

            for (int i = 0; i < numProc; i++)
               if (i != myId)
                  (new ListenerThread(i, (MsgHandler)lock)).start();
            while (true) {
                System.out.println(" enter (empty / peek / pop / push + (int)): ");                Scanner keyboard = new Scanner(System.in);
                String input = keyboard.nextLine();

                StringTokenizer st = new StringTokenizer(input);
                String naredba = st.nextToken();

                if(naredba.equals("push"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");
                  if(st.hasMoreTokens())
                  {
                    int num = Integer.parseInt(st.nextToken());
                    comm.stackPush(num);
                    lock.broadcastMsg("push", num);
                  }
                  lock.releaseCS();
                }

                else if(naredba.equals("pop"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");
                  comm.stackPop();
                  lock.broadcastMsg("pop", 0);

                  lock.releaseCS();

                }

                else if(naredba.equals("empty"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");
                  System.out.println("Stack is empty - " + comm.stackEmpty());
                  lock.releaseCS();
                }

                else if(naredba.equals("peek"))
                {
                  lock.requestCS();
                  System.out.println(myId + " is in CS *****");
                  System.out.println("Stack peek: " + comm.stackPeek());
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
