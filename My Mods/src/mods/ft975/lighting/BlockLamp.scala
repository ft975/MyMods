package mods.ft975.lighting

import net.minecraft.block.{ITileEntityProvider, Block}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.item.ItemStack
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.{AxisAlignedBB, MovingObjectPosition, Icon}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.util.logging.Level
import java.util
import mods.ft975.util.block.BlockTESR
import mods.ft975.util.extensibles.BlockExt

class BlockLamp(id: Int) extends BlockExt(id, Material.redstoneLight) with BlockTESR with ITileEntityProvider {
	override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
		val tempTe = world.getBlockTileEntity(x, y, z)
		val te = tempTe match {
			case tempTe: TileLamp => tempTe
			case _ => throw new ClassCastException
		}
		DebugOnly {log.log(Level.INFO, ItemLamp.getData(ItemLamp.buildStack(1, te.color, te.shape, te.isOn)).toString())}
		ItemLamp.buildStack(1, te.color, te.shape, te.isOn)
	}

	override def getLightValue(iba: IBlockAccess, x: Int, y: Int, z: Int): Int = {
		val te = iba.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		if (te.isOn) 15 else 0
	}

	override def getBlockDropped(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): util.ArrayList[ItemStack] = {
		val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		val al = new java.util.ArrayList[ItemStack](1)
		if (te != null) al.add(ItemLamp.buildStack(te))
		al
	}

	override def onNeighborBlockChange(wrd: World, x: Int, y: Int, z: Int, blockID: Int) {
		if (!wrd.isRemote) {
			val powered = wrd.isBlockIndirectlyGettingPowered(x, y, z)
			val te = wrd.getBlockTileEntity(x, y, z)
			if (te != null) {
				te.asInstanceOf[TileLamp].setRedstoneState(powered)
			}
		}
	}

	override def isBlockNormalCube(world: World, x: Int, y: Int, z: Int): Boolean = {
		val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		if (te != null) te.shape == Shapes.Block
		else true
	}

	override def onBlockAdded(wrd: World, x: Int, y: Int, z: Int) {
		DebugOnly {logInfo("Block added")}
		onNeighborBlockChange(wrd, x, y, z, 1)
	}

	override def getSelectedBoundingBoxFromPool(wrd: World, x: Int, y: Int, z: Int): AxisAlignedBB = getCollisionBoundingBoxFromPool(wrd, x, y, z)

	def createNewTileEntity(world: World): TileEntity = new TileLamp()

	@SideOnly(Side.CLIENT)
	override def getRenderBlockPass: Int = 1

	@SideOnly(Side.CLIENT)
	override def registerIcons(iR: IconRegister) {}

	@SideOnly(Side.CLIENT)
	override def canRenderInPass(pass: Int): Boolean = pass == 0 || pass == 1

	@SideOnly(Side.CLIENT)
	override def getIcon(id: Int, meta: Int): Icon = Block.stone.getIcon(Block.stone.blockID, 0)
}