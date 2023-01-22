package net.minecraft.src;

public class R2TileEntityNetherStorage extends TileEntity implements IInventory{
    public R2FilterInventoryImpl filterInventory;
    public ItemStack[] freqSlots = new ItemStack[3];
    public String getKey(){
        String key = "";
        if(freqSlots[0] != null){
            key += freqSlots[0].itemID + "." + freqSlots[0].getItemDamage() + "-";
        }
        else {
            key += "empty-";
        }
        if(freqSlots[1] != null){
            key += freqSlots[1].itemID + "." + freqSlots[1].getItemDamage() + "-";
        }
        else {
            key += "empty-";
        }
        if(freqSlots[2] != null){
            key += freqSlots[2].itemID + "." + freqSlots[2].getItemDamage();
        }
        else {
            key += "empty";
        }
        return key;
    }

    public ItemStack[] getInventory(){
        return mod_NetherStorage.getNetherInventory(getKey());
    }
    public R2TileEntityNetherStorage() {
        filterInventory = new R2FilterInventoryImpl(this);
    }

    int slotpos = 0;
    int count = -1;
    @Override
    public void updateEntity() {
        if(count != -1)
        {
            if(getInventory()[slotpos] != null)
            {
                if(getInventory()[slotpos].stackSize != count)
                {
                    mod_NetherStorage.saveStorage(worldObj);
                }
            }
        }
        count = -1;
    }

    @Override
    public int getSizeInventory() {
        if(getInventory() != null){
            return getInventory().length;
        }
        else {
            return 0;
        }
    }

    @Override
    public ItemStack getStackInSlot(int i1) {
        ItemStack stack = getInventory()[i1];
        if(stack != null)
        {
            slotpos = i1;
            count = stack.stackSize;
        }
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int i1, int i2) {
        if(getInventory()[i1] != null) {
            ItemStack itemStack3;
            if(this.getInventory()[i1].stackSize <= i2) {
                itemStack3 = this.getInventory()[i1];
                this.getInventory()[i1] = null;
                this.onInventoryChanged();
                return itemStack3;
            } else {
                itemStack3 = this.getInventory()[i1].splitStack(i2);
                if(this.getInventory()[i1].stackSize == 0) {
                    this.getInventory()[i1] = null;
                }

                this.onInventoryChanged();
                return itemStack3;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i1, ItemStack itemStack2) {
        getInventory()[i1] = itemStack2;
        onInventoryChanged();
    }

    @Override
    public String getInvName() {
        if(mod_NetherStorage.netherLabels.containsKey(getKey())){
            return mod_NetherStorage.netherLabels.get(getKey());
        }
        return "Nether Chest";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer1) {
        if(getInventory().length == 1){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        mod_NetherStorage.saveStorage(worldObj);
        NBTTagList nBTTagList2 = new NBTTagList();

        for(int i3 = 0; i3 < this.freqSlots.length; ++i3) {
            if(this.freqSlots[i3] != null) {
                NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
                nBTTagCompound4.setByte("Slot", (byte)i3);
                this.freqSlots[i3].writeToNBT(nBTTagCompound4);
                nBTTagList2.setTag(nBTTagCompound4);
            }
        }

        nBTTagCompound1.setTag("FilterItems", nBTTagList2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("FilterItems");
        this.freqSlots = new ItemStack[freqSlots.length];

        for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
            NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3);
            int i5 = nBTTagCompound4.getByte("Slot") & 255;
            if(i5 >= 0 && i5 < this.freqSlots.length) {
                this.freqSlots[i5] = new ItemStack(nBTTagCompound4);
            }
        }
    }

    public R2FilterInventoryImpl getFilterInvImpl()
    {
        return filterInventory;
    }

    @Override
    public void onInventoryChanged() {
        mod_NetherStorage.saveStorage(worldObj);
        super.onInventoryChanged();
    }
}
