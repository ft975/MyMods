package mods.ft975.util
package extensibles

import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.Entity
import net.minecraftforge.common.ForgeDirection
import net.minecraft.util.{AxisAlignedBB, Icon}
import net.minecraft.block.Block

abstract class TileExt extends TileEntity {
	//<editor-fold desc="Block">

	def x = xCoord

	def x_=(v: Int) {
		xCoord = v
	}

	def y = yCoord

	def y_=(v: Int) {
		yCoord = v
	}

	def z = zCoord

	def z_=(v: Int) {
		zCoord = v
	}

	def metadata = worldObj.getBlockMetadata(x, y, z)

	def metadata_=(v: Byte) {
		worldObj.setBlockMetadataWithNotify(x, y, z, v, 2)
	}

	//</editor-fold>
	//<editor-fold desc="Light">

	def getLightValue: Int = 0

	def getLightOpacity: Int = 0

	//</editor-fold>
	//<editor-fold desc="Hardness">

	/*
	 Dirt = 0.5, Stone = 1.5, Obsidian = 50
	 */
	def getHardness: Float = 3

	/*
	 Stone = 10, Water = 500, Obsidian = 2000
	 */
	def getExplosionResistance(entity: Entity, d: Double, d1: Double, d2: Double): Float = getHardness * 5

	//</editor-fold>
	//<editor-fold desc="Redstone">

	def canConnectRedstone(side: ForgeDirection) = false

	def onNearbyBlockChange(blockID: Int) {}

	def onRedstoneUpdate(on: Boolean) {}

	//</editor-fold>

	def isBlockSolidOnSide(side: ForgeDirection): Boolean = true

	//<editor-fold desc="Texture">

	def getTexture(side: ForgeDirection): Icon = Block.blockGold.getIcon(side.ordinal(), metadata)

	//</editor-fold>
	//<editor-fold desc="AABBs">

	def getAllAABBs: List[AxisAlignedBB] = List(getOneAABB)

	def getOneAABB: AxisAlignedBB = AABBUtil.NullAABB

	//</editor-fold>
}
