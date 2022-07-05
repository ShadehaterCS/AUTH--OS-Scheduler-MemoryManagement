import java.util.ArrayList;

public abstract class Scheduler {

    protected ArrayList<Process> processes; // the list of processes to be executed
    protected int executableProcesses;

    public Scheduler() {
        this.processes = new ArrayList<Process>();
    }

    /* the addProcess() method should add a new process to the list of
     * processes that are candidates for execution. This will probably
     * differ for different schedulers */
    public abstract void addProcess(Process p);


    /* the removeProcess() method should remove a process from the list
     * of processes that are candidates for execution. Common for all
     * schedulers. */
    public void removeProcess(Process p) {
        /* TODO: you need to add some code here */
        processes.remove(p);
        executableProcesses--;
    }

    /* the getNextProcess() method should return the process that should
     * be executed next by the CPU */

    public abstract Process getNextProcess();
    // inside getNextProcess, burstTime of the selected process has to be reduced 1 time unit
    // and if burstTime=0 set its state to terminated

    protected void setExecutableProcesses(int executableProcesses){
        this.executableProcesses = executableProcesses;
    }

    protected int getExecutableProcesses(){
        return executableProcesses;
    }

    protected void zeroTimes(){}           // only used in Robin Round
}





