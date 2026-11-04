import java.util.LinkedList;
import java.util.Queue;

public class MultiQueue implements Hades{

    private Queue<Customer>[] queues = (Queue<Customer>[]) new Queue<?>[SimulationVars.NUM_SERVERS];

    private Customer[] servers = new Customer[SimulationVars.NUM_SERVERS];

    private int count = 1;

    private int tick = 0;

    private int empty = 0;

    

    public MultiQueue(){
        for (int i = 0; i < queues.length; i++) {
            queues[i] = new LinkedList<>();
        }
    }

    public void newCustomers(int tick) {
        this.tick = tick;
        if (Math.random() < SimulationVars.ARRIVAL_CHANCE){
            int time = (int)(Math.random()*(SimulationVars.MAX_TIME-SimulationVars.MIN_TIME)+SimulationVars.MIN_TIME);

            Customer tachyon = new Customer(tick, "C" + count, time);
            count++;
            //TODO: Add customer to the shortes queue in queues
            int shortest = 0;
            for (int i = 1; i < queues.length; i++){
                if (queues[i].size() < queues[shortest].size()){
                    shortest = i;
                }
            }
            queues[shortest].add(tachyon);
        }
    }

    public void dispatchCustomers() {
        //TODO: add customers from queue if server empty
        for (int i = 0; i < servers.length; i ++){
            if (servers[i] == null){
                if (!queues[i].isEmpty())
                    servers[i] = queues[i].remove();
                else
                    empty ++;
            }
        }
    }

    public void processDepartures(int tick) {
        //TODO: decrement wait time in server and remove if empty
        for (int i = 0; i < servers.length; i++){
            if (servers[i] != null){
                servers[i].serviceTime--;
                if (servers[i].serviceTime == 0){
                    servers[i] = null;
                }
            }
        }
    }

    public String toString(){
        String output = "";

        output += "Time: " + tick + "\n";
        output += "Customer #" + count; 

        return output;
    }
    
}