import java.util.LinkedList;
import java.util.Queue;

public class MultiQueue implements Hades{

    private Queue<Customer>[] queues = (Queue<Customer>[]) new Queue<?>[SimulationVars.NUM_SERVERS];

    private Customer[] servers = new Customer[SimulationVars.NUM_SERVERS];

    private int count = 1;

    private int tick = 0;

    private int empty = 0;

    private LinkedList<String> events = new LinkedList<String>();    

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
	    events.add("Customer #"+ (count - 1)+" joined the queue #"+shortest);
        }
    }

    public void dispatchCustomers() {
        //TODO: add customers from queue if server empty
        for (int i = 0; i < servers.length; i ++){
            if (servers[i] == null){
                if (!queues[i].isEmpty()){
                    servers[i] = queues[i].remove();
		    events.add("Customer " + servers[i].ID+" has joined server #"+i);
		}
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
		    events.add("Customer "+servers[i].ID+" left server #"+i);
                    servers[i] = null;
                }
            }
        }
    }

    public String toString(){
        StringBuilder output = new StringBuilder();

        output.append("Time: ")
	output.append(tick);
	output.append("\n");

	for (String s : events){
	   output.append(s);  
	}
	events.clear();
	 

        return output;
    }
    
}
