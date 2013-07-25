package mods.ft975.util.hack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Scala_JavaHack {
	public static ShapedOreRecipe getShapedRecipe(ItemStack result, Object[] vals) {
		return new ShapedOreRecipe(result, vals);
	}
}
