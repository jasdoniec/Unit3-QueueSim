import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SingleQueue implements Hades {
    private final Queue<Customer> queue = new LinkedList<>();

    private final Customer[] servers = new Customer[SimulationVars.NUM_SERVERS];

    private int tick = 0;

    private int empty = 0;

    private int cusCount = 0;

    private int longestQueue = 0;

    private final ArrayList<Integer> waitTimes = new ArrayList<>();

    private final ArrayList<String> events = new ArrayList<>();

    @Override
    public void newCustomers(Customer customer) {
        queue.add(customer);
        if (queue.size() > longestQueue) {
            longestQueue = queue.size();
        }
        events.add("Customer " + customer.ID + " joined the queue");
    }

    @Override
    public void dispatchCustomers() {
        for (int i = 0; i < servers.length; i++) {
            if (servers[i] == null) {
                if (!queue.isEmpty()) {
                    servers[i] = queue.remove();
                    events.add("Customer " + servers[i].ID + " has joined server #" + i);
                } else {
                    empty++;
                }
            }
        }
    }

    @Override
    public void processDepartures(int tick) {
        this.tick = tick;

        for (int i = 0; i < servers.length; i++) {
            if (servers[i] != null) {
                servers[i].serviceTime--;
                if (servers[i].serviceTime == 0) {
                    cusCount++;
                    events.add("Customer " + servers[i].ID + " left server #" + i);
                    waitTimes.add(tick - servers[i].arrivalTime);
                    servers[i] = null;
                }
            }
        }
    }

    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append("\n--- SINGLE-QUEUE STATUS ---\n");

        output.append("Time: ");
        output.append(tick);
        output.append("\n");

        for (String s : events) {
            output.append(s);
            output.append("\n");
        }
        events.clear();
        output.append("\n");

        output.append("Waiting queue: ");
        for (Customer c : queue) {
            output.append(c.ID);
            output.append(" ");
        }
        output.append("\n");

        for (int i = 0; i < servers.length; i++) {
            output.append("Teller ");
            output.append(i);
            if (servers[i] == null) {
                output.append("(empty)\n");
                continue;
            }
            output.append(" ");
            output.append(servers[i].ID);
            output.append("(");
            output.append(servers[i].serviceTime);
            output.append(" min left)\n");
        }

        return output.toString();
    }

    @Override
    public String finalSumm() {
        float[] times = getAvgAndMaxTime();
        float avgTime = times[0];
        float maxTime = times[1];

        StringBuilder output = new StringBuilder();
        output.append("\n--- SINGLE-QUEUE RESULTS ---");
        output.append("\nTotal Customers Served: ");
        output.append(cusCount);
        output.append("\nAverage Wait Time: ");
        output.append(avgTime);
        output.append("\nMax Wait Time: ");
        output.append(maxTime);
        output.append("\nMax Queue Length: ");
        output.append(longestQueue);
        output.append("\nServer Utilization: ");
        output.append((480*servers.length-empty)/(servers.length*480.0)*100);
        output.append("%\nCustomers Remaining: ");
        output.append(queue.size());

        return output.toString();
    }

    private float[] getAvgAndMaxTime() {
        if (waitTimes.isEmpty()) {
            return new float[] {0f, 0f};
        }

        int max = 0;
        int total = 0;

        for (Integer waitTime : waitTimes) {
            total += waitTime;
            if (waitTime > max) {
                max = waitTime;
            }
        }

        float[] output = new float[2];
        output[0] = total / (float) waitTimes.size();
        output[1] = max;
        return output;
    }
}

