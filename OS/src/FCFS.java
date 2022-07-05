
public class FCFS extends Scheduler {


    public FCFS() {
        /* TODO: you _may_ need to add some code here */
    }

    // FCFS algorithm always returns the process with the minimum Arrival Time
    // processes are sorted by arrival time
    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        boolean added = false;

        if (executableProcesses == 0) {
            processes.add(p);
            added = true;
        }

        for(int i=0;i<super.executableProcesses;i++){
            if(p.getArrivaltime() < processes.get(i).getArrivaltime()){
                processes.add(i,p);
                added = true;
            }
        }

        if (!added)
            processes.add(p);

        super.executableProcesses++;
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        return processes.get(0);
    }


}
