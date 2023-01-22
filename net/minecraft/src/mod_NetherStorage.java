package net.minecraft.src;

import forge.MinecraftForgeClient;
import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class mod_NetherStorage extends BaseMod{
    public static List<Integer> ignoreWhenStanding = new ArrayList<>();
    public static List<Integer> ignoreWhenSneaking = new ArrayList<>();
    public static Map<String, Object> netherInventories = new HashMap<>();
    public static Map<String, String> netherLabels = new HashMap<>();
    private World prevWorld = null;
    public static long prevSaveTime = 0;
    @MLProp
    public static int r2NetherStorageBlockID = 240;
    public static Block r2NetherStorageBlock;
    public static int r2NetherStorageItemID = 6850;
    public static Item r2NetherStorageItem;
    int r2ItemNetherBagId = 8000;
    Item r2ItemNetherBag;
    public static R2MapInvImpl mapInvImpl = new R2MapInvImpl();

    @Override
    public String Version() {
        return "1.0.1";
    }
    public boolean loadedStorage = false;

    public mod_NetherStorage() {
        r2NetherStorageBlock = new R2NetherStorageBlock(r2NetherStorageBlockID, Material.rock).setHardness(0.4F).setStepSound(Block.soundStoneFootstep).setBlockName("r2NetherStorageBlock");
        r2NetherStorageItem = new R2NetherStorageItem(r2NetherStorageItemID).setItemName("r2NetherStorageItem");
        ModLoader.AddName(r2NetherStorageBlock, "Nether Chest");
        ModLoader.RegisterBlock(r2NetherStorageBlock);
        ModLoader.RegisterTileEntity(R2TileEntityNetherStorage.class, "NetherStorage");

        MinecraftForgeClient.preloadTexture("/netherstorage/terrain.png");
        MinecraftForgeClient.preloadTexture("/netherstorage/items.png");

        ModLoader.AddRecipe(new ItemStack(r2NetherStorageBlock, 1), new Object[]{"XNX", "#$#", "X#X", 'X', Item.ingotGold, '#', Block.netherrack, '$', Block.chest, 'N', new ItemStack(r2NetherStorageItem, 1, 2)});
        ModLoader.AddRecipe(new ItemStack(r2NetherStorageItem,1 ,0), new Object[]{"XNX", "$$$", "XXX", 'X', Block.netherrack, '$', Item.paper, 'N', Item.slimeBall});
        ModLoader.AddRecipe(new ItemStack(r2NetherStorageItem,1 ,1), new Object[]{"XNX", "X$X", "X#X", 'X', Block.netherrack, '#', Item.ingotGold, '$', Block.obsidian, 'N', Item.redstoneRepeater});
        ModLoader.AddRecipe(new ItemStack(r2NetherStorageItem,1 ,2), new Object[]{"XNX", "N$N", "XNX", 'X', Item.lightStoneDust, '$', Item.diamond, 'N', Block.obsidian});

        ModLoader.AddName(new ItemStack(r2NetherStorageItem,1 ,0), "Nether Label");
        ModLoader.AddName(new ItemStack(r2NetherStorageItem,1 ,1), "Nether Channel Editor");
        ModLoader.AddName(new ItemStack(r2NetherStorageItem,1 ,2), "Nether Core");
        ModLoader.SetInGameHook(this, true, false);
        try{
            ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(new R2TextureAnimationBlock(3, 1, ImageIO.read(Minecraft.class.getResource("/netherstorage/netherchestfx.png")), 1));
            ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(new R2TextureAnimationItem(2, 1, ImageIO.read(Minecraft.class.getResource("/netherstorage/nethercorefx.png")), 1));
        }
        catch (Exception e){

        }
    }


    public static ItemStack[] getNetherInventory(String key){
        ItemStack[] netherInventory = null;
        if(netherInventories != null){
            if(netherInventories.containsKey(key)){
                netherInventory = (ItemStack[]) netherInventories.get(key);
            }
            else{
                netherInventories.put(key, new ItemStack[32]);
                netherInventory = (ItemStack[]) netherInventories.get(key);
            }
            return netherInventory;
        }
        else {
            netherInventories = new HashMap();
            netherInventories.put(key, new ItemStack[32]);
            return (ItemStack[]) netherInventories.get(key);
        }
    }
    public static void saveStorage(World world){
        if(prevSaveTime == world.getWorldTime())
        {
            return;
        }
        if(netherInventories.keySet().toArray().length == 0)
        {
            return;
        }
        try{
            File savePath = getWorldSaveLocation(world);
            File storageFile = new File(savePath, "netherstorage.dat");

            if(!storageFile.exists()) {
                CompressedStreamTools.writeGzippedCompoundToOutputStream(new NBTTagCompound(), new FileOutputStream(storageFile));
            }

            NBTTagCompound compound = CompressedStreamTools.func_1138_a(new FileInputStream(storageFile));
            NBTTagCompound storageCompound = new NBTTagCompound();
            NBTTagCompound keyCompound = new NBTTagCompound();
            for (Object key: netherInventories.keySet().toArray()) {
                String keyString = (String)key;
                keyCompound.setString(keyString, keyString);
                NBTTagCompound entryCompound = new NBTTagCompound();
                NBTTagList keyInvCompound = new NBTTagList();
                if(netherLabels.containsKey(keyString))
                {
                    entryCompound.setString("label", netherLabels.get(keyString));
                }
                for (int index = 0; index <  ((ItemStack[])netherInventories.get(keyString)).length; index++) {
                    ItemStack[] inventory = (ItemStack[])netherInventories.get(keyString);
                    if(inventory[index] != null) {
                        NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
                        nBTTagCompound4.setByte("Slot", (byte)index);
                        inventory[index].writeToNBT(nBTTagCompound4);
                        keyInvCompound.setTag(nBTTagCompound4);
                    }
                }
                entryCompound.setTag("inventory", keyInvCompound);
                storageCompound.setCompoundTag(keyString, entryCompound);
            }
            compound.setCompoundTag("storage", storageCompound);
            compound.setCompoundTag("keys", keyCompound);
            CompressedStreamTools.writeGzippedCompoundToOutputStream(compound, new FileOutputStream(storageFile));
            prevSaveTime = world.getWorldTime();
        }
        catch (Exception e){
            System.out.println("Netherchests: Save failed");
            System.out.println(e);
        }
    }
    public static void loadStorage(World world){
        if(netherInventories.keySet().toArray().length != 0)
        {
            return;
        }
        try{
            File savePath = getWorldSaveLocation(world);
            File storageFile = new File(savePath, "netherstorage.dat");

            if(!storageFile.exists()) {
                CompressedStreamTools.writeGzippedCompoundToOutputStream(new NBTTagCompound(), new FileOutputStream(storageFile));
            }

            NBTTagCompound compound =  CompressedStreamTools.func_1138_a(new FileInputStream(storageFile));
            NBTTagCompound storageCompound = compound.getCompoundTag("storage");
            NBTTagCompound keyCompound = compound.getCompoundTag("keys");
            Iterator iterator = keyCompound.func_28110_c().iterator();
            while(iterator.hasNext()) {
                NBTBase nbtBaseKey = (NBTBase)iterator.next();
                if(nbtBaseKey instanceof NBTTagString) {
                    NBTTagString nbtTagStringKey = (NBTTagString)nbtBaseKey;
                    String keyString = nbtTagStringKey.stringValue;
                    NBTTagCompound entryCompound = storageCompound.getCompoundTag(keyString);
                    if(entryCompound.hasKey("label"))
                    {
                        netherLabels.put(keyString, entryCompound.getString("label"));
                    }
                    NBTTagList keyInvCompound = entryCompound.getTagList("inventory");
                    ItemStack[] inventory = new ItemStack[32];
                    for (int index = 0; index < keyInvCompound.tagCount(); index++) {
                        NBTTagCompound nBTTagCompound4 = (NBTTagCompound)keyInvCompound.tagAt(index);
                        int i5 = nBTTagCompound4.getByte("Slot") & 255;
                        if(i5 >= 0 && i5 < inventory.length) {
                            inventory[i5] = new ItemStack(nBTTagCompound4);
                        }
                    }
                    netherInventories.put(keyString, inventory);
                }
            }
        }
        catch (Exception e){
            System.out.println("Netherchests: Load failed");
            System.out.println(e);
        }
    }

    public static File getWorldSaveLocation(World world) {
        return world.saveHandler instanceof SaveHandler ? ((SaveHandler)world.saveHandler).getSaveDirectory() : null;
    }
    @Override
    public boolean OnTickInGame(Minecraft game) {
        if(game.theWorld != null){
            if(!loadedStorage){
                prevWorld = game.theWorld;
                System.out.println("Netherchests: Loading items");
                loadStorage(game.theWorld);
                loadedStorage = true;
            }
            else {
                if(prevWorld != game.theWorld){
                    netherInventories = new HashMap<>();
                    netherLabels = new HashMap<>();
                    System.out.println("Netherchests: Loading items");
                    loadStorage(game.theWorld);
                    prevWorld = game.theWorld;
                }
            }
        }
        return true;
    }

    public static R2MapInvImpl getMapInvImpl(String key){
        mapInvImpl.inventory = getNetherInventory(key);
        if(netherLabels.containsKey(key)) {
            mapInvImpl.invName = netherLabels.get(key);
        }
        else{
            mapInvImpl.invName = "Nether Chest";
        }
        return mapInvImpl;
    }

    @Override
    public void ModsLoaded() {
        if(ModLoader.isModLoaded("mod_ItemNBT")){
            r2ItemNetherBag = new R2ItemNetherBag(r2ItemNetherBagId).setItemName("r2ItemNetherBag");
            ignoreWhenSneaking.add(r2ItemNetherBag.shiftedIndex);

            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,0), "Black Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,1), "Red Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,2), "Green Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,3), "Brown Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,4), "Blue Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,5), "Purple Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,6), "Cyan Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,7), "Light Gray Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,8), "Gray Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,9), "Pink Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,10), "Lime Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,11), "Yellow Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,12), "Light Blue Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,13), "Magenta Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,14), "Orange Nether Bag");
            ModLoader.AddName(new ItemStack(r2ItemNetherBag,1 ,15), "White Nether Bag");

            for(int i = 0; i < 16; i++)
            {
                ModLoader.AddRecipe(new ItemStack(r2ItemNetherBag,1 ,i), new Object[]{"XNX", "N$N", "XOX", 'X', Block.obsidian, '$', mod_NetherStorage.r2NetherStorageBlock, 'N', Item.leather, 'O', new ItemStack(Item.dyePowder, 1, i)});
            }
        }
        else{
            System.out.println("NetherStorage: ItemNBT mod was not found, bags will not be available");
        }
    }
}
