package mods.ft975.util

import mods.ft975.util.data.Coord
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection

object WorldUtil {
	final val MaxRecursiveDepth = 1000

	type WorldOp = Coord => Unit
	/* (curr, prev) */
	type WorldPrevOp = (Coord, Coord) => Unit
	type WorldCond = Coord => Boolean
	/* (curr, prev) */
	type WorldPrevCond = (Coord, Coord) => Boolean
	type Coord = data.Coord[Int]
	type CoordOpSet = Set[Coord]

	//<editor-fold desc="Coordinate Operation Sets">

	val ImmediateSurroundings: CoordOpSet = Set(
		Coord(+1, 0, 0),
		Coord(-1, 0, 0),
		Coord(0, +1, 0),
		Coord(0, -1, 0),
		Coord(0, 0, +1),
		Coord(0, 0, -1))
	val CornerSurroundings   : CoordOpSet = Set(
		Coord(+1, +1, +1),
		Coord(+1, +1, -1),
		Coord(+1, -1, +1),
		Coord(+1, -1, -1),
		Coord(-1, +1, +1),
		Coord(-1, +1, -1),
		Coord(-1, -1, +1),
		Coord(-1, -1, -1))
	val EdgeSurroundings     : CoordOpSet = Set(
		Coord(+1, +1, 0),
		Coord(-1, +1, 0),
		Coord(0, +1, +1),
		Coord(0, +1, -1),
		Coord(+1, 0, +1),
		Coord(-1, 0, +1),
		Coord(+1, 0, -1),
		Coord(-1, 0, -1),
		Coord(+1, -1, 0),
		Coord(-1, -1, 0),
		Coord(0, -1, +1),
		Coord(0, -1, -1))
	val BoxSurroundings      : CoordOpSet = {
		(
			ImmediateSurroundings
				| CornerSurroundings
				| EdgeSurroundings)
	}
	//</editor-fold>

	def iterateTouching(x: Int, y: Int, z: Int, cond: WorldCond, op: WorldOp, transverseCoords: CoordOpSet) {
		iterateDef(x, y, z, 0)
		@inline def iterateDef(x: Int, y: Int, z: Int, currLevel: Int) {
			if (currLevel < MaxRecursiveDepth) {
				for (deltaCoord <- transverseCoords) {
					if (cond(Coord(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z))) {
						op(Coord(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z))
						iterateDef(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, currLevel + 1)
					}
				}
			}
		}
	}

	def iterateTouchingPast(x: Int, y: Int, z: Int, cond: WorldPrevCond, op: WorldPrevOp, transverseCoords: CoordOpSet) {
		iterateDef(x, y, z, 0)
		@inline def iterateDef(x: Int, y: Int, z: Int, currLevel: Int) {
			if (currLevel < MaxRecursiveDepth) {
				for (deltaCoord <- transverseCoords) {
					if (cond(Coord(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z), Coord(x, y, z))) {
						op(Coord(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z), Coord(x, y, z))
						iterateDef(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, currLevel + 1)
					}
				}
			}
		}
	}

	def opOnCoordSet(x: Int, y: Int, z: Int, op: WorldOp, coordSet: CoordOpSet) {
		for (deltaPos <- coordSet) {
			op(Coord(x + deltaPos.x, y + deltaPos.y, z + deltaPos.z))
		}
	}

	def dropItemStack(items: List[ItemStack], world: World, x: Int, y: Int, z: Int) {
		// Thanks to mDiyo for this method: https://github.com/mDiyo/InfiCraft/blob/master/inficraft/microblocks/core/BlockCombined.java#L142-L164
		for (stack: ItemStack <- items) {
			val xPos: Float = x + rand.nextFloat() * 0.8f + 0.1f
			val yPos: Float = y + rand.nextFloat() * 0.8f + 0.1f
			val zPos: Float = z + rand.nextFloat() * 0.8f + 0.1f

			DebugOnly {System.out.println("Dropping " + stack + "@" + xPos + ", " + yPos + ", " + zPos)}

			// chests do this (multiple drops per stack, 10-30 items at a time)
			var left: Int = stack.stackSize
			while (left > 0) {
				var removeCount: Int = math.min(rand.nextInt(21) + 10, left)
				left -= removeCount

				val ent: EntityItem = new EntityItem(world, xPos, yPos, zPos, new ItemStack(stack.itemID, removeCount, stack.getItemDamage))
				ent.delayBeforeCanPickup = 10

				ent.motionX = rand.nextGaussian() * 0.05f
				ent.motionY = rand.nextGaussian() * 0.05f + 0.2f
				ent.motionZ = rand.nextGaussian() * 0.05f

				if (stack.hasTagCompound)
					ent.getEntityItem.setTagCompound(stack.getTagCompound)

				world.spawnEntityInWorld(ent)
			}
		}
	}

	def sideFromCoord(prev: Coord, post: Coord): ForgeDirection = sideFromPos(prev.x, prev.y, prev.z, post.x, post.y, post.z)

	def sideFromPos(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): ForgeDirection = sideFromDiff(x2 - x1, y2 - y1, z2 - z1)

	def sideFromDiff(dx: Int, dy: Int, dz: Int): ForgeDirection = {
		import ForgeDirection._
		if (dx == 1) EAST
		else if (dx == -1) WEST
		else if (dy == 1) UP
		else if (dy == -1) DOWN
		else if (dz == 1) SOUTH
		else if (dz == -1) NORTH
		else UNKNOWN
	}
}
