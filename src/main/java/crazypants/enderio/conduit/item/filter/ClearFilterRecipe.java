package crazypants.enderio.conduit.item.filter;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import crazypants.enderio.conduit.item.FilterRegister;
import crazypants.enderio.machine.AbstractMachineBlock;
import crazypants.util.Lang;

public class ClearFilterRecipe implements IRecipe{

  private ItemStack output;
  
  @Override
  public boolean matches(InventoryCrafting inv, World world) {
    int count = 0;
    ItemStack input = null;
    
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack checkStack = inv.getStackInSlot(i);
      if (checkStack != null && checkStack.getItem() instanceof IItemFilterUpgrade) {
        count++;
      }
      input = (count == 1 && checkStack != null) ? checkStack : input;
    }
    
    if (count == 1 && FilterRegister.isFilterSet(input)) {
      ItemStack out = input.copy();
      out.stackTagCompound = null;
      this.output = out;
    } else {
      this.output = null;
    }
    
    return count == 1 && output != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return output.copy();
  }

  @Override
  public int getRecipeSize() {
    return 1;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return output;
  }
  
  @SubscribeEvent
  public void onTooltip(ItemTooltipEvent event) {
    if (this.output != null && ItemStack.areItemStacksEqual(output, event.itemStack)) {
      event.toolTip.add(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC + Lang.localize("itemConduitFilterUpgrade.clearConfigWarning", true));
    }
  }

}
