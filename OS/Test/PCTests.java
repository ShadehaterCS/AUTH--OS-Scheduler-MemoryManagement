import org.junit.Assert;
import org.junit.Test;

public class PCTests {
    Process[] processes;
    @Test
    public void real_World_Test1(){
        processes = new Process[]{
                new Process(0, 5, 10),
                new Process(2, 2, 40),
                new Process(3, 1, 25),
                new Process(4, 3, 30),
                new Process(4, 3, 5),
                new Process(4, 3, 12)
        };
        CPU.clock = 0;
        final int[] availableBlockSizes = {15, 20, 10, 20}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new FCFS();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();
    }

    @Test
    public void real_World_Test2(){

    }

    @Test
    public void real_World_Lots_Of_Processes_Test(){
        //If it finishes, it's considered successful
        CPU.clock = 0;
        processes = new Process[]{
                new Process(0, 5, 10),
                new Process(2, 2, 40),
                new Process(3, 1, 25),
                new Process(4, 3, 30),
                new Process(4, 3, 5),
                new Process(4, 3, 12),
                new Process(5, 5, 10),
                new Process(6, 2, 40),
                new Process(7, 1, 25),
                new Process(8, 3, 30),
                new Process(8, 3, 5),
                new Process(8, 3, 12),
                new Process(9, 5, 10),
                new Process(9, 2, 40),
                new Process(10, 1, 25),
                new Process(10, 3, 30),
                new Process(10, 3, 5),
                new Process(11, 3, 12),
                new Process(12, 5, 10),
                new Process(13, 2, 40),
                new Process(14, 1, 25),
                new Process(14, 3, 30),
                new Process(14, 3, 5),
                new Process(15, 3, 12)
        };
        final int[] availableBlockSizes = {120,40,20,5,150,200,300}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new SJF();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();
    }

    @Test
    public void real_World_No_Process_Can_Fit(){
        CPU.clock = 0;
        processes = new Process[]{
                new Process(0, 5, 100),
                new Process(2, 2, 40),
                new Process(3, 1, 25),
                new Process(4, 3, 30) };
        final int[] availableBlockSizes = {5,5}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new FCFS();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();

        Assert.assertEquals(cpu.getAvailableProcesses().size(), 0,0);
        Assert.assertEquals(5, CPU.clock, 0);
    }
}
