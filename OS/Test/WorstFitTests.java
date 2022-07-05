import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class WorstFitTests {
    @Test
    public void testOutOfMemory(){
        int[] blocks = {5, 10};

        ArrayList<Boolean> result = new ArrayList<>();
        MMU mmu = new MMU(blocks, new WorstFit(blocks));

        Process p1 = new Process(0,5,5);
        Process p2 = new Process(0,5,2);
        Process p3 = new Process(0,5,2);
        Process p4 = new Process(0,5,1);
        Process p5 = new Process(0,5,6);

        result.add(mmu.loadProcessIntoRAM(p1));
        result.add(mmu.loadProcessIntoRAM(p2));
        result.add(mmu.loadProcessIntoRAM(p3));
        result.add(mmu.loadProcessIntoRAM(p4));
        result.add(mmu.loadProcessIntoRAM(p5));

        ArrayList<Boolean> expected = new ArrayList<>(Arrays.asList(true,true,true,true,false));
        Assert.assertArrayEquals(expected.toArray(),result.toArray());
    }

    @Test
    public void manyProcessesTest(){
        int[] blocks = {5,10,20,30,40};
        ArrayList result = new ArrayList<Boolean>();
        MMU mmu = new MMU(blocks, new WorstFit(blocks));

        result.add(mmu.loadProcessIntoRAM(new Process(0,5,15)));
        result.add(mmu.loadProcessIntoRAM(new Process(1,4,3)));
        result.add(mmu.loadProcessIntoRAM(new Process(3,15,20)));
        result.add(mmu.loadProcessIntoRAM(new Process(4,2,25)));
        result.add(mmu.loadProcessIntoRAM(new Process(5,2,35)));
        result.add(mmu.loadProcessIntoRAM(new Process(6,2,15)));

        ArrayList expected = new ArrayList<>(Arrays.asList(true,true,true,true,false,true));
        Assert.assertArrayEquals(expected.toArray(),result.toArray());

        result = mmu.getCurrentlyUsedMemorySlots().stream()
                .map(MemorySlot::getStart)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        expected = new ArrayList<>(Arrays.asList(15,35,38,65,80));
        Assert.assertArrayEquals(expected.toArray(),result.toArray());
    }

    @Test
    public void exactBlockSizeTest(){
        int[] blocks = {10,20,30,40};
        ArrayList result = new ArrayList<>();
        MMU mmu = new MMU(blocks, new WorstFit(blocks));

        result.add(mmu.loadProcessIntoRAM(new Process(0,5,40)));
        result.add(mmu.loadProcessIntoRAM(new Process(0,5,30)));
        result.add(mmu.loadProcessIntoRAM(new Process(0,5,20)));
        result.add(mmu.loadProcessIntoRAM(new Process(0,5,10)));
        result.add(mmu.loadProcessIntoRAM(new Process(0,5,2)));

        ArrayList expected = new ArrayList<>(Arrays.asList(true,true,true,true,false));
        Assert.assertArrayEquals(expected.toArray(), result.toArray());

        result = mmu.getCurrentlyUsedMemorySlots().stream()
                .map(MemorySlot::getStart)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        expected = new ArrayList<>(Arrays.asList(0,10,30,60)); //because the memory slots are always sorted
        Assert.assertArrayEquals(expected.toArray(),result.toArray());
    }
}
