package mods.ft975.util

import net.minecraft.item.ItemStack
import net.minecraft.world.World
import scala.util.Random
import net.minecraft.entity.item.EntityItem

object BlockHelper {
	private val rand = new Random()

	// Thanks to mDiyo for this method: https://github.com/mDiyo/InfiCraft/blob/master/inficraft/microblocks/core/BlockCombined.java#L142-L164
	def dropItemStack(items: List[ItemStack], world: World, x: Int, y: Int, z: Int) {
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
