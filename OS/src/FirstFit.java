import java.util.ArrayList;

public class FirstFit extends MemoryAllocationAlgorithm {

    public FirstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    /***
     * @param p is the process that we check if it can fit in any RAM's block.
     * @param currentlyUsedMemorySlots is the arraylist of the Memoryslots(processes) that are saved in the RAM.
     * The function starts by creating the blockAddresses which contains they starting address of every block.
     * Then it calculates the used space in every block of RAM and save it in blockUsed arraylist.
     * At last it searches the blockUsed arraylist and it finds the first block that has enough space to save the Process.
     * @return the address that the Process is saved inside the first suitable block.
     */
    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        //Assisting Arraylists.
        ArrayList<Integer> blockAddresses = new ArrayList<>();
        ArrayList<Integer> blockUsed = new ArrayList<>();


        //We find every block's address.
        int blockAddress = 0;
        for (int i = 0; i < availableBlockSizes.length; i++) {
            blockUsed.add(0);
            blockAddresses.add(blockAddress);
            blockAddress += availableBlockSizes[i];
        }

        //Calculate the amount of used memory in every block.
        for (MemorySlot m : currentlyUsedMemorySlots) {
            int blockPos = blockAddresses.indexOf(m.getBlockStart());
            blockUsed.set(blockPos, blockUsed.get(blockPos) + m.getUsedSize());

        }
        /*If a block contains enough space for a Process then it can save it for sure.
        This happens because RAM uses defragmentation every time a Process is removed from a specific block
        and all the available space is continuous.
        We check if our Process fits in any block right now.*/
        for (int i = 0; i < blockUsed.size() && !fit; i++) {
            //It fits in the box.
            if (p.getMemoryRequirements() <= (availableBlockSizes[i] - blockUsed.get(i))) {

                address = blockAddresses.get(i);

                for (MemorySlot m : currentlyUsedMemorySlots)
                {
                    if (m.getBlockStart() == blockAddresses.get(i))
                        address = m.getEnd() + 1;
                }

                fit = true;
            }
        }
        return address;


    }
}

