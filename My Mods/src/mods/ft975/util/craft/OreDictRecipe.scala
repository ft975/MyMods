package mods.ft975.util.craft

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

trait OreDictRecipe extends AbstractRecipe {
	override def doItemsMatch(_1: ItemStack, _2: ItemStack): Boolean = {
		super.doItemsMatch(_1, _2) && OreDictionary.itemMatches(_2, _1, true) && _1.getTagCompound == _2.getTagCompound
	}
}
