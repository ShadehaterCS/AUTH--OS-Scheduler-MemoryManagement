//import groovy.json.JsonOutput;

import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    //If there aren't any Processes in the memory then the initial values will work the algorithm.
    private int lastAddressBlockID=0;

    /***
     * @param p is the process that we check if it can fit in any RAM's block.
     * @param currentlyUsedMemorySlots is the arraylist of the Memoryslots(processes) that are saved in the RAM.
     * The function starts by creating the blockAddresses which contains they starting address of every block.
     * Then it calculates the used space in every block of RAM and save it in blockUsed arraylist.
     * At last it searches the blockUsed arraylist,but from the block that it stopped the last time it search,
     * and it finds the first block that has enough space to save the Process.
     * @return the address that the Process is saved inside the block.
     */
    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        //Helping Arraylists.
        ArrayList<Integer> blockUsed=new ArrayList<>();
        ArrayList<Integer> blockAddresses=new ArrayList<>();

        //Create the block addresses Arraylist.
        int blockAddress=0;
        for(int i=0;i<availableBlockSizes.length;i++)
        {
            blockUsed.add(0);
            blockAddresses.add(blockAddress);
            blockAddress+=availableBlockSizes[i];
        }

        //Count the used space of every block.
        for(MemorySlot m:currentlyUsedMemorySlots)
        {
            //Find the block of this memory slot.
            int blockPos=blockAddresses.indexOf(m.getBlockStart());
            //Update the memory of this slot.
            blockUsed.set(blockPos,blockUsed.get(blockPos)+m.getUsedSize());
        }


        /*If a block contains enough space for a Process then it can save it for sure.
        This happens because RAM uses defragmentation every time a Process is removed from a specific block
        and all the available space is continuous.
        Due to defragmentation we cant save the address of the last added Process because it can change.
        So every time we insert a memory slot, we save the last used block.
        And the next Process will be added in the remaining space of the block.
        */
        for(int i=lastAddressBlockID;i<blockUsed.size()+lastAddressBlockID && !fit;i++)
        {
            //Use mod to roll around the ram blocks and not get out of the bounds.
            int realPos=i%blockUsed.size();

            //There is enough space for our Process.
            if(p.getMemoryRequirements()<=(availableBlockSizes[realPos])-blockUsed.get(realPos))
            {
                //If there not any Processes in this block then we save our Process in the start of the block.
                address=blockAddresses.get(realPos);

                //Watching for memory slots in the block to save our Process after the last one.
                for(MemorySlot m:currentlyUsedMemorySlots)
                {
                    if(m.getBlockStart()==blockAddresses.get(realPos))
                    {
                        address=m.getEnd()+1;
                    }
                }

                //We save the last block that we used to save a Process.
                lastAddressBlockID=realPos;
                fit=true;
            }
        }
        return address;


    }

}
