package mods.ft975.lighting

import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.ForgeDirection
import java.util.logging.Level
import mods.ft975.util.BitShift

class ItemLamp(itemID: Int) extends ItemBlock(itemID) {

	setHasSubtypes(true)

	override def shouldPassSneakingClickToBlock(par2World: World, par4: Int, par5: Int, par6: Int): Boolean = super.shouldPassSneakingClickToBlock(par2World, par4, par5, par6)

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
			val on = iS.getTagCompound.getBoolean("O")
			"item." + true + col + shape + "lamp"
		}
	}

	override def getSubItems(n1: Int, n2: CreativeTabs, iList: java.util.List[_]) {
		iList.asInstanceOf[java.util.List[ItemStack]].addAll(subtypes)
	}

	lazy val subtypes = {
		val list = new java.util.ArrayList[ItemStack]
		for (sha: Shapes <- Shapes.vals;
		     col: Colors <- Colors.vals;
		     on: Boolean <- List(true, false)) {
			list.add(ItemLamp.buildStack(1, col, sha, on))
			DebugOnly {log.log(Level.INFO, "Sub Item Added: " + col + sha)}
		}
		list
	}

	override def canPlaceItemBlockOnSide(wrd: World, x: Int, y: Int, z: Int, side: Int, player: EntityPlayer, iStack: ItemStack): Boolean = {
		if (ItemLamp.getData(iStack)._2 != Shapes.Block)
			wrd.isBlockSolidOnSide(x, y, z, ForgeDirection.getOrientation(side))
		else
			true
	}
}

/*
	Item Damage Format:
	Nibble 0: Color
	Nibble 1: Shape
	Nibble 2: 0 is off, 1 is on
 */
object ItemLamp {
	def buildStack(cnt: Int, col: Colors, sap: Shapes, isOn: Boolean): ItemStack = {
		val iS = new ItemStack(LightingMod.blockLamp, cnt)
		iS.setItemDamage(BitShift.getShortFromNibbles(Array[Byte](col.meta, sap.meta, if (isOn) 1 else 0, 0)))
		iS
	}

	def buildStack(cnt: Int, dam: Short): Option[ItemStack] = {
		if (isValidDamage(dam)) {
			val data = getData(dam)
			buildStack(cnt, data._1, data._2, data._3)
		}
		None
	}

	def buildStack(te: TileLamp): ItemStack = {
		DebugOnly {logInfo(te)}
		buildStack(1, te.color, te.shape, te.isOn)
	}

	private def isValidDamage(damage: Short): Boolean = {
		val info = BitShift.splitShortToNibbles(damage)
		info(0) <= 15 && info(0) >= 0 && info(1) <= Shapes.vals.size && info(1) >= 0 && (info(3) == 1 || info(3) == 0)
	}

	def getData(input: Short): (Colors, Shapes, Boolean) = {
		val info = BitShift.splitShortToNibbles(input)
		(Colors.fromID(info(0)),
			Shapes.fromID(info(1)),
			info(2) == 1)
	}

	def getData(iStack: ItemStack): (Colors, Shapes, Boolean) = {
		getData(iStack.getItemDamage.toShort)
	}
}



