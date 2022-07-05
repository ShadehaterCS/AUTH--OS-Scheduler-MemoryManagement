import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/*
Tests process execution times  with SJF scheduling algorithm and WORSTFIT memory allocation algorithm
 */
public class CPUTestSJF {

    final Process[] SJFTestProcesses = {
            new Process(2, 3, 1),
            new Process(0, 4, 1),
            new Process(4, 2, 2),
            new Process(5, 4, 2)};
    final int[] availableBlockSizes = {100,30}; // sizes in kB

    @Test
    public void JFSProcessExecutionOrder() {
        CPU.clock = 0;
        MemoryAllocationAlgorithm algorithm = new WorstFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new SJF();
        CPU cpu = new CPU(scheduler, mmu, SJFTestProcesses);
        cpu.run();

        ArrayList waitingTimes = new ArrayList<Integer>();
        ArrayList turnAroundTimes = new ArrayList<Integer>();
        ArrayList responseTimes = new ArrayList<Integer>();
        for (Process p : SJFTestProcesses) {
            waitingTimes.add(p.getWaitingTime());
            turnAroundTimes.add(p.getTurnAroundTime());
            responseTimes.add(p.getResponseTime());
        }

        //calculates all the real times
        ArrayList realWaitingTimes = new ArrayList<Integer>();
        realWaitingTimes.add(4.0); // waiting time of process 0
        realWaitingTimes.add(0.0); // waiting time of process 1
        realWaitingTimes.add(0.0); // waiting time of process 2
        realWaitingTimes.add(4.0); // waiting time of process 2
        ArrayList realTurnAroundTimes = new ArrayList<Integer>();
        realTurnAroundTimes.add(7.0); //turn around time of process 0
        realTurnAroundTimes.add(4.0); //turn around time of process 1
        realTurnAroundTimes.add(2.0); //turn around time of process 2
        realTurnAroundTimes.add(8.0); //turn around time of process 2

        Assert.assertArrayEquals(realWaitingTimes.toArray(), waitingTimes.toArray());
        Assert.assertArrayEquals(realTurnAroundTimes.toArray(), turnAroundTimes.toArray());
    }
}