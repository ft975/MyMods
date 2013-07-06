package mods.ft975.util
package extensibles

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.{World, IBlockAccess}
import net.minecraftforge.common.ForgeDirection
import net.minecraft.entity.Entity
import net.minecraft.util.{AxisAlignedBB, MovingObjectPosition, Icon}
import net.minecraft.client.particle.EffectRenderer
import mods.ft975.util.particle.EntityDiggingParticle
import java.util

class BlockExt(id: Int, mat: Material) extends Block(id, mat) {
	//<editor-fold desc="Light">

	override def getLightValue(iba: IBlockAccess, x: Int, y: Int, z: Int): Int =
		asTileExt[Int](
			iba.getBlockTileEntity(x, y, z),
			(te) => {
				te.getLightValue
			},
			super.getLightValue(iba, x, y, z))

	override def getLightOpacity(wrd: World, x: Int, y: Int, z: Int): Int =
		asTileExt[Int](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				te.getLightOpacity
			},
			super.getLightOpacity(wrd, x, y, z))

	//</editor-fold>
	//<editor-fold desc="Hardness">

	override def getBlockHardness(wrd: World, x: Int, y: Int, z: Int): Float =
		asTileExt[Float](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				te.getHardness
			},
			super.getBlockHardness(wrd, x, y, z))

	override def getExplosionResistance(ent: Entity, wrd: World, x: Int, y: Int, z: Int, exX: Double, exY: Double, exZ: Double): Float =
		asTileExt[Float](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				te.getExplosionResistance(ent, exX, exY, exZ)
			},
			super.getExplosionResistance(ent, wrd, x, y, z, exX, exY, exZ))

	//</editor-fold>
	//<editor-fold desc="Block Updates">

	override def canConnectRedstone(iba: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean =
		asTileExt[Boolean](
			iba.getBlockTileEntity(x, y, z),
			(te) => {
				te.canConnectRedstone(ForgeDirection.getOrientation(side))
			},
			super.canConnectRedstone(iba, x, y, z, side))

	override def onNeighborBlockChange(wrd: World, x: Int, y: Int, z: Int, neighborBID: Int) {
		asTileExt[Unit](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				te.onNearbyBlockChange(neighborBID)
				te.onRedstoneUpdate(wrd.isBlockIndirectlyGettingPowered(x, y, z))
			},
			super.canConnectRedstone(wrd, x, y, z, neighborBID))
	}

	//</editor-fold>
	//<editor-fold desc="Block Solidity">

	override def isBlockSolidOnSide(wrd: World, x: Int, y: Int, z: Int, side: ForgeDirection): Boolean =
		asTileExt[Boolean](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				te.isBlockSolidOnSide(wrd, x, y, z, side)
			},
			super.isBlockSolidOnSide(wrd, x, y, z, side))

	override def isBlockSolid(iba: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean =
		asTileExt[Boolean](
			iba.getBlockTileEntity(x, y, z),
			(te) => {
				te.isBlockSolidOnSide(iba, x, y, z, ForgeDirection.getOrientation(side))
			},
			super.isBlockSolid(iba, x, y, z, side))

	//</editor-fold>
	//<editor-fold desc="Textures">

	override def getIcon(side: Int, meta: Int): Icon = blockIcon

	override def getBlockTexture(iba: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Icon =
	/*
		If the IBA is not null, try getting the TE and getting the
		texture from that. Otherwise, get the texture from super.getIcon
	*/
		asTileExt[Icon](
			iba.getBlockTileEntity(x, y, z),
			(t) => {
				t.getTexture(ForgeDirection.getOrientation(side))
			},
			super.getIcon(side, iba.getBlockMetadata(x, y, z))
		)


	//</editor-fold>
	//<editor-fold desc="Particles">

	final override def addBlockHitEffects(wrd: World, tgt: MovingObjectPosition, effRendr: EffectRenderer): Boolean = {
		val x: Int = tgt.blockX
		val y: Int = tgt.blockY
		val z: Int = tgt.blockZ
		val side: Int = tgt.sideHit

		if (wrd.getBlockId(x, y, z) == blockID) {
			val f: Float = 0.1F
			var d0: Double = x + rand.nextDouble * (getBlockBoundsMaxX - getBlockBoundsMinX - f * 2.0F) + f + getBlockBoundsMinX
			var d1: Double = y + rand.nextDouble * (getBlockBoundsMaxY - getBlockBoundsMinY - f * 2.0F) + f + getBlockBoundsMinY
			var d2: Double = z + rand.nextDouble * (getBlockBoundsMaxZ - getBlockBoundsMinZ - f * 2.0F) + f + getBlockBoundsMinZ

			side match {
				case 0 =>
					d1 = y + getBlockBoundsMinY - f
				case 1 =>
					d1 = y + getBlockBoundsMaxY + f
				case 2 =>
					d2 = z + getBlockBoundsMinZ - f
				case 3 =>
					d2 = z + getBlockBoundsMaxZ + f
				case 4 =>
					d0 = x + getBlockBoundsMinX - f
				case 5 =>
					d0 = x + getBlockBoundsMaxX + f
			}

			effRendr.addEffect(new EntityDiggingParticle(wrd, d0, d1, d2, 0.0D, 0.0D, 0.0D, this, side, x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F))
			return true
		}
		false
	}

	final override def addBlockDestroyEffects(wrd: World, x: Int, y: Int, z: Int, meta: Int, effRendr: EffectRenderer): Boolean = {
		if (wrd.getBlockId(x, y, z) == blockID) {
			for (j1: Double <- 0.0 to 4.0 by 1.0;
			     k1: Double <- 0.0 to 4.0 by 1.0;
			     l1: Double <- 0.0 to 4.0 by 1.0) {

				val d0: Double = x + (j1 + 0.5D) / 4D
				val d1: Double = y + (k1 + 0.5D) / 4D
				val d2: Double = z + (l1 + 0.5D) / 4D
				val side: Int = rand.nextInt(6)

				effRendr.addEffect(new EntityDiggingParticle(wrd, d0, d1, d2, d0 - x - 0.5D, d1 - y - 0.5D, d2 - z - 0.5D, this, side, x, y, z))
			}
			return true
		}
		false
	}

	//</editor-fold>
	//<editor-fold desc="AABBs">

	override def getCollisionBoundingBoxFromPool(wrd: World, x: Int, y: Int, z: Int): AxisAlignedBB =
		asTileExt[AxisAlignedBB](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				AABBUtil.translatePosition(te.getOneAABB, x, y, z)
			},
			super.getSelectedBoundingBoxFromPool(wrd, x, y, z)
		)

	override def getSelectedBoundingBoxFromPool(wrd: World, x: Int, y: Int, z: Int): AxisAlignedBB =
		asTileExt[AxisAlignedBB](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				AABBUtil.translatePosition(te.getOneAABB, x, y, z)
			},
			super.getSelectedBoundingBoxFromPool(wrd, x, y, z)
		)

	override def addCollisionBoxesToList(wrd: World, x: Int, y: Int, z: Int, aabb: AxisAlignedBB, list: util.List[_], ent: Entity) {
		asTileExt[Unit](
			wrd.getBlockTileEntity(x, y, z),
			(te) => {
				for (ab <- te.getAllAABBs) {
					if (aabb.intersectsWith(ab)) {
						list.asInstanceOf[util.List[AxisAlignedBB]].add(ab)
					}
				}
			},
			super.addCollisionBoxesToList(wrd, x, y, z, aabb, list, ent)
		)
	}


	override def setBlockBoundsBasedOnState(iba: IBlockAccess, x: Int, y: Int, z: Int) {
		asTileExt[Unit](
			iba.getBlockTileEntity(x, y, z),
			(te) => {
				val aabb = te.getOneAABB
				minX = aabb.minX
				minY = aabb.minY
				minZ = aabb.minZ
				maxX = aabb.maxX
				maxY = aabb.maxY
				maxZ = aabb.maxZ
			},
			super.setBlockBoundsBasedOnState(iba, x, y, z)
		)
	}

	//</editor-fold>
}