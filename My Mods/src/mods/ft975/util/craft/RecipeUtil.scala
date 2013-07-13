package mods.ft975.util.craft

import net.minecraft.item.ItemStack

object RecipeUtil {
	val alphabet = for (v <- 'a' to 'z') yield v

	def addRecipe(xSize: Int, ySize: Int, rec: Array[Array[ItemStack]], res: ItemStack) {
		//	GameRegistry.addRecipe(res, for (x <- 0 to xSize * ySize))
	}
}
