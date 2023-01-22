package net.minecraft.src;

import forge.ITextureProvider;

import java.util.Random;

public class R2NetherStorageBlock extends BlockContainer implements ITextureProvider {
    private Random random = new Random();
    protected R2NetherStorageBlock(int i1, Material material2) {
        super(i1, material2);
        this.blockIndexInTexture = 26;
    }

    @Override
    public int getBlockTexture(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
        int dir = iBlockAccess1.getBlockMetadata(i2, i3, i4);
        if(dir == i5) {
            return 2;
        }
        else if(i5 != 0 && i5 != 1){
            return 1;
        }
        if(i5 == 0){
            return 0;
        }
        if(i5 == 1){
            return 3;
        }
        return 0;
    }



    @Override
    public void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
        super.onBlockPlaced(world1, i2, i3, i4, i5);
    }

    @Override
    public void onBlockAdded(World world1, int i2, int i3, int i4) {
        super.onBlockAdded(world1, i2, i3, i4);
    }

    @Override
    public void onBlockPlacedBy(World world1, int i2, int i3, int i4, EntityLiving entityLiving5) {
        int l = MathHelper.floor_double((double)(entityLiving5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if(l == 0) {
            world1.setBlockMetadataWithNotify(i2, i3, i4, 2);
        }

        if(l == 1) {
            world1.setBlockMetadataWithNotify(i2, i3, i4, 5);
        }

        if(l == 2) {
            world1.setBlockMetadataWithNotify(i2, i3, i4, 3);
        }

        if(l == 3) {
            world1.setBlockMetadataWithNotify(i2, i3, i4, 4);
        }
    }

    @Override
    public void onBlockRemoval(World world1, int i2, int i3, int i4) {
        R2FilterInventoryImpl filterInventory = ((R2TileEntityNetherStorage)world1.getBlockTileEntity(i2, i3, i4)).getFilterInvImpl();

        for(int l = 0; l < filterInventory.getSizeInventory(); ++l) {
            ItemStack itemstack = filterInventory.getStackInSlot(l);
            if(itemstack != null) {
                float f = this.random.nextFloat() * 0.8F + 0.1F;
                float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                while(itemstack.stackSize > 0) {
                    int i1 = this.random.nextInt(21) + 10;
                    if(i1 > itemstack.stackSize) {
                        i1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(world1, (double)((float)i2 + f), (double)((float)i3 + f1), (double)((float)i4 + f2), new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);
                    world1.entityJoinedWorld(entityitem);
                }
            }
        }

        super.onBlockRemoval(world1, i2, i3, i4);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new R2TileEntityNetherStorage();
    }

    @Override
    public void updateTick(World world1, int i2, int i3, int i4, Random random5) {

    }

    public int getBlockTextureFromSide(int i1) {
        switch(i1){
            case 0:
                return 0;
            case 1:
                return 3;
            case 3:
                return 2;
            default:
                return 1;
        }
        //return i1 == 1 ? this.blockIndexInTexture - 1 : (i1 == 0 ? this.blockIndexInTexture - 1 : (i1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
    }

    public R2TileEntityNetherStorage accessTileEntity(World world, int x, int y, int z){
        return (R2TileEntityNetherStorage)world.getBlockTileEntity(x, y, z);
    }
    public boolean isIndirectlyPoweringTo(World world1, int i2, int i3, int i4, int i5) {
        return isPoweringTo(world1, i2, i3, i4, i5);
    }

    @Override
    public int tickRate() {
        return 1;
    }

    @Override
    public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
        R2TileEntityNetherStorage tileEntity = (R2TileEntityNetherStorage) world1.getBlockTileEntity(i2, i3, i4);
        if(entityPlayer5.getCurrentEquippedItem() != null && entityPlayer5.getCurrentEquippedItem().getItem().shiftedIndex == mod_NetherStorage.r2NetherStorageItem.shiftedIndex)
        {
            switch (entityPlayer5.getCurrentEquippedItem().getItemDamage())
            {
                case 0:
                    ModLoader.OpenGUI(entityPlayer5, new R2GuiNetherLabel(tileEntity.getKey()));
                    break;
                case 1:
                    ModLoader.OpenGUI(entityPlayer5, new R2GuiFilter(entityPlayer5.inventory, accessTileEntity(world1, i2, i3, i4)));
                    break;
                default:
                    entityPlayer5.displayGUIChest(tileEntity);
                    break;
            }
        }
        else {
            if(entityPlayer5.getCurrentEquippedItem() != null){
                if(entityPlayer5.isSneaking()) {
                    if(mod_NetherStorage.ignoreWhenSneaking.contains(entityPlayer5.getCurrentEquippedItem().itemID)){
                        return false;
                    }
                }
                else{
                    if(mod_NetherStorage.ignoreWhenStanding.contains(entityPlayer5.getCurrentEquippedItem().itemID)){
                        return false;
                    }
                }
            }
            entityPlayer5.displayGUIChest(tileEntity);
        }
        return true;
    }

    @Override
    public String getTextureFile() {
        return "/netherstorage/terrain.png";
    }
}
