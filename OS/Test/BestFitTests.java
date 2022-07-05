import org.junit.Test;

import java.util.ArrayList;

public class BestFitTests {

    @Test
    public void twoProcessesTest(){
        int[] blocks = {5, 10};
        ArrayList<Integer> addresses = new ArrayList<>();
        ArrayList<MemorySlot> yikes = new ArrayList<>();
        BestFit bestFit = new BestFit(blocks);

        addresses.add(bestFit.fitProcess(new Process(0,5,7), yikes));
        System.out.println(addresses.get(0));
        System.out.println(addresses.get(0) + 7);
    }
}
