import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class MMUTests {

    @Test
    public void defragmentTest(){
        final int[] availableBlockSizes = {16,30};
        MMU manager = new MMU(availableBlockSizes, new BestFit(availableBlockSizes));
        Process testingDeleteProcess = new Process(5,10,5);

        manager.loadProcessIntoRAM(new Process(5,10,5));
        manager.loadProcessIntoRAM(testingDeleteProcess);
        manager.loadProcessIntoRAM(new Process(5,10,6));
        manager.clearProcessFromRAM(testingDeleteProcess);

        Integer[] expected = new Integer[]{0,4,5,10};
        ArrayList<Integer> result = new ArrayList<>();
        for (MemorySlot mem : manager.getCurrentlyUsedMemorySlots()){
            result.add(mem.getStart());
            result.add(mem.getEnd());
        }
        Assert.assertArrayEquals(expected,result.toArray());
    }

    @Test
    public void addAllRemoveAllTest(){
        final int[] availableBlockSizes = {10,20};
        MMU manager = new MMU(availableBlockSizes, new BestFit(availableBlockSizes));
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,10));

        for (Process p : processes)
            manager.loadProcessIntoRAM(p);
        Assert.assertEquals(6,manager.getCurrentlyUsedMemorySlots().size(),0);

        for (Process p : processes)
            manager.clearProcessFromRAM(p);
        Assert.assertEquals(0,manager.getCurrentlyUsedMemorySlots().size(),0);
    }
    @Test
    public void addDeleteAddTest(){
        final int[] availableBlockSizes = {10,20};
        MMU manager = new MMU(availableBlockSizes, new BestFit(availableBlockSizes));
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));
        processes.add(new Process(5,10,5));

        for (Process p : processes)
            manager.loadProcessIntoRAM(p);

        manager.clearProcessFromRAM(processes.get(0));
        manager.clearProcessFromRAM(processes.get(1));

        manager.loadProcessIntoRAM(new Process(5,10,10));
        manager.loadProcessIntoRAM(new Process(5,10,10));

        Assert.assertEquals(4, manager.getCurrentlyUsedMemorySlots().size());
    }

    @Test
    public void removeNonExistentProcessTest(){
        final int[] availableBlockSizes = {10,20};
        MMU manager = new MMU(availableBlockSizes, new BestFit(availableBlockSizes));
        int before = manager.getCurrentlyUsedMemorySlots().size();
        manager.clearProcessFromRAM(new Process(2,1,15));
        int after = manager.getCurrentlyUsedMemorySlots().size();
    }
}
