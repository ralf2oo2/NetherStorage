package net.minecraft.src;

public class R2FilterInventoryImpl implements IInventory{
    public R2TileEntityNetherStorage tileEntityNetherStorage;
    public R2FilterInventoryImpl(R2TileEntityNetherStorage tileEntityNetherStorage) {
        this.tileEntityNetherStorage = tileEntityNetherStorage;
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int i1) {
        return this.tileEntityNetherStorage.freqSlots[i1];
    }

    @Override
    public ItemStack decrStackSize(int i1, int i2) {
        if(this.tileEntityNetherStorage.freqSlots[i1] != null) {
            ItemStack itemStack3;
            if(this.tileEntityNetherStorage.freqSlots[i1].stackSize <= i2) {
                itemStack3 = this.tileEntityNetherStorage.freqSlots[i1];
                this.tileEntityNetherStorage.freqSlots[i1] = null;
                this.onInventoryChanged();
                return itemStack3;
            } else {
                itemStack3 = this.tileEntityNetherStorage.freqSlots[i1].splitStack(i2);
                if(this.tileEntityNetherStorage.freqSlots[i1].stackSize == 0) {
                    this.tileEntityNetherStorage.freqSlots[i1] = null;
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
        this.tileEntityNetherStorage.freqSlots[i1] = itemStack2;
        if(itemStack2 != null && itemStack2.stackSize > this.getInventoryStackLimit()) {
            itemStack2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "Filter";
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onInventoryChanged() {
        tileEntityNetherStorage.onInventoryChanged();
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer1) {
        return true;
    }
}
