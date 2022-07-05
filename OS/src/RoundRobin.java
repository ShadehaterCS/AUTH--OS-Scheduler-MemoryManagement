/* Round Robin cycles around executing processes.
 * It executes each process for quantum times (quantum = 1 by default) before moving on to the next one.
 * When it passes every process, it goes back to the first one and repeats this cycle until it executes all processes.
 */
public class RoundRobin extends Scheduler {

    private int quantum;
    private int position; // position of the next process, it iterates over processes in a loop until there are no more processes
    private int times; // the amount of times the algorithm chose the same process in a row

    public RoundRobin() {
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */
        this.position = 0;
        this.times = 0;
    }

    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        super.processes.add(p);
        super.executableProcesses++;
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        if (times == this.quantum) { //times
            times = 1;
            position++;
        }
        else {
            times++;
        }
        if (position == super.processes.size() || position < 0 ) // when the algorithm iterates through the whole arraylist it goes back its start
            position = 0;

        return super.processes.get(position);
    }

    protected void zeroTimes(){times = 0;} // If a process finishes execution, variable times has to be initialized for the next process.

}