import java.util.ArrayList;

public class ProcessControlBlock {
    
    private final int pid;//ID tou process.
    private ProcessState state;
    // the following two ArrayLists should record when the process starts/stops
    // for statistical purposesProject Structure
    private ArrayList<Integer> startTimes; // when the process starts running
    private ArrayList<Integer> stopTimes;  // when the process stops running
    
    public static int pidTotal= 0;
    
    public ProcessControlBlock() {
        this.state = ProcessState.NEW;
        this.startTimes = new ArrayList<Integer>();
        this.stopTimes = new ArrayList<Integer>();
        /* TODO: you need to add some code here
         * Hint: every process should get a unique PID */
        this.pid = pidTotal; // change this line
        pidTotal++;
    }

    public ProcessState getState() {
        return this.state;
    }
    
    public void setState(ProcessState state, int currentClockTime)
    {
        /* TODO: you need to add some code here
         * Hint: update this.state, but also include currentClockTime
         * in startTimes/stopTimes */
        if ((this.state == ProcessState.READY || this.state == ProcessState.NEW) && state == ProcessState.RUNNING){ // if process state changes from ready to running we add current clock time to startTimes
            startTimes.add(CPU.clock);
        }
        if (this.state == ProcessState.RUNNING && state == ProcessState.READY){ // if process state changes from running to ready we add current clock time to stopTimes
            stopTimes.add(CPU.clock);
        }
        this.state = state;
        if (state == ProcessState.TERMINATED) //if process state changes from running to terminated we add current clock time to stopTimes
            stopTimes.add(CPU.clock);
    }
    
    public int getPid() { 
        return this.pid;
    }
    
    public ArrayList<Integer> getStartTimes() {
        return startTimes;
    }
    
    public ArrayList<Integer> getStopTimes() {
        return stopTimes;
    }
}
