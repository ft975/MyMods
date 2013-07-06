package mods.ft975.util

import net.minecraftforge.common.ForgeDirection
import net.minecraft.util.AxisAlignedBB

object AABBUtil {
	final val NullAABB = AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1)

	def getSidedAABB(x0: Float, y0: Float, z0: Float, x1: Float, y1: Float, z1: Float, side: ForgeDirection): AxisAlignedBB = {
		val pool = AxisAlignedBB.getAABBPool
		side match {
			case ForgeDirection.DOWN => pool.getAABB(x0, y0, z0, x1, y1, z1)
			case ForgeDirection.UP => pool.getAABB(x0, 1 - y1, z0, x1, 1 - y0, z1)
			case ForgeDirection.NORTH => pool.getAABB(x0, z0, y0, x1, z1, y1)
			case ForgeDirection.SOUTH => pool.getAABB(x0, 1 - z1, 1 - y1, x1, 1 - z0, 1 - y0)
			case ForgeDirection.WEST => pool.getAABB(y0, x0, z0, y1, x1, z1)
			case ForgeDirection.EAST => pool.getAABB(1 - y1, 1 - x1, z0, 1 - y0, 1 - x0, z1)
			case _ =>
				DebugOnly {new Exception("Invalid direction " + side).printStackTrace()}
				NullAABB
		}
	}

	def translatePosition(aabb: AxisAlignedBB, x: Int, y: Int, z: Int): AxisAlignedBB = {
		aabb.minX += x
		aabb.minY += y
		aabb.minZ += z
		aabb.maxX += x
		aabb.maxY += y
		aabb.maxZ += z
		aabb
	}
}
