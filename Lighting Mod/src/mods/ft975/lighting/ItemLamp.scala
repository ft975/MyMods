package mods.ft975.lighting

import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.ForgeDirection

class ItemLamp(itemID: Int) extends ItemBlock(itemID) {
  setHasSubtypes(true)

  override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, metadata: Int): Boolean = {
    if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
      val te = world.getBlockTileEntity(x, y, z)
      val tile = te match {
        case te: TileLamp => te
        case _ => throw new ClassCastException
      }
      tile.color = Colors.fromID(stack.getTagCompound.getByte("C"))
      tile.shape = Shapes.fromID(stack.getTagCompound.getByte("S"))
      true
    } else {
      false
    }
  }

  override def getUnlocalizedName(iS: ItemStack): String = {
    if (iS.getTagCompound == null) "Broken, throw in lava"
    else {
      val shape = Shapes.fromID(iS.getTagCompound.getByte("S")).unLocName
      val col = Colors.fromID(iS.getTagCompound.getByte("C")).name

      "item." + col + shape + "lamp"
    }
  }

  override def getSubItems(n1: Int, n2: CreativeTabs, iList: java.util.List[_]) {
    for (sha: Shapes <- Shapes.vals;
         col: Colors <- Colors.vals) {
      iList.asInstanceOf[java.util.List[ItemStack]].add(ItemLamp.buildStack(1, col, sha))
      // Log.logDebug("Sub Item Added: " + col + sha)
    }
  }

  override def canPlaceItemBlockOnSide(wrd: World, x: Int, y: Int, z: Int, side: Int, player: EntityPlayer, iStack: ItemStack): Boolean = {
    if (ItemLamp.getData(iStack).shape != Shapes.Block)
      wrd.isBlockSolidOnSide(x, y, z, ForgeDirection.getOrientation(side))
    else
      true
  }
}

object ItemLamp {
  def buildStack(cnt: Int, col: Colors, sap: Shapes): ItemStack = {
    val iS = new ItemStack(LightingMod.blockLamp, cnt)
    iS.setTagCompound(new NBTTagCompound())
    val tc = iS.getTagCompound
    tc.setByte("C", col.meta)
    tc.setByte("S", sap.meta)
    iS
  }

  def getData(iStack: ItemStack): LampData = {
    val tc = iStack.getTagCompound
    new LampData(Colors.fromID(tc.getByte("C")), Shapes.fromID(tc.getByte("S")))
  }

  class LampData(col: Colors, sha: Shapes) {
    val color: Colors = col
    val shape: Shapes = sha
  }

}



