package mods.ft975.util

import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.entity.item.EntityItem

object WorldUtil {
	private final val MaxRecursiveDepth = 950
	type WorldOp = (Int, Int, Int) => Unit
	type WorldCond = (Int, Int, Int) => Boolean
	type CoordOp = (Int, Int, Int)
	type CoordOpSet = IndexedSeq[CoordOp]

	val ImmediateSurroundings: CoordOpSet = IndexedSeq(
		(+1, 0, 0),
		(-1, 0, 0),
		(0, +1, 0),
		(0, -1, 0),
		(0, 0, +1),
		(0, 0, -1))
	val CornerSurroundings: CoordOpSet = IndexedSeq(
		(+1, +1, +1),
		(+1, +1, -1),
		(+1, -1, +1),
		(+1, -1, -1),
		(-1, +1, +1),
		(-1, +1, -1),
		(-1, -1, +1),
		(-1, -1, -1))
	val EdgeSurroundings: CoordOpSet = IndexedSeq(
		(+1, +1, 0),
		(-1, +1, 0),
		(0, +1, +1),
		(0, +1, -1),
		(+1, 0, +1),
		(-1, 0, +1),
		(+1, 0, -1),
		(-1, 0, -1),
		(+1, -1, 0),
		(-1, -1, 0),
		(0, -1, +1),
		(0, -1, -1))
	val BoxSurroundings: CoordOpSet = {
		(
			ImmediateSurroundings
				++ CornerSurroundings
				++ EdgeSurroundings)
	}

	def iterateTouching(x: Int, y: Int, z: Int, cond: WorldCond, op: WorldOp, transverseCoords: CoordOpSet) {
		iterateDef(x, y, z, 0)
		/*
		While loop used in order to allow for more recursion before StackOverflow. A for loop compiles into 3 classes, causing a StackOverflow 3x faster
		 */
		def iterateDef(x: Int, y: Int, z: Int, currLevel: Int) {
			if (currLevel < MaxRecursiveDepth) {
				var i = 0
				while (i < transverseCoords.size) {
					val deltaCoord: (Int, Int, Int) = transverseCoords(i)
					if (cond(x + deltaCoord._1, y + deltaCoord._2, z + deltaCoord._3)) {
						op(x + deltaCoord._1, y + deltaCoord._2, z + deltaCoord._3)
						iterateDef(x + deltaCoord._1, y + deltaCoord._2, z + deltaCoord._3, currLevel + 1)
					}
					i += 1
				}
			}
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
}
