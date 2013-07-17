package mods.ft975.util

import net.minecraft.item.ItemStack
import cpw.mods.fml.common.registry.GameRegistry
import mods.ft975.util.$.Scala_JavaHack._

object RecipeUtil {
	type OreDict = String
	type Item = ItemStack

	def addRecipe(res: ItemStack, rec: List[String], keyValue: List[(Char, Either[OreDict, Item])]) {
		val v: Array[AnyRef] = (rec.toArray ++
			keyValue.map(
				(v) => {
					Array[Any](v._1,
						v._2 match {
							case x: Right[OreDict, Item] => x.right.get
							case x: Left[OreDict, Item] => x.left.get
						})
				}).flatten).asInstanceOf[Array[AnyRef]]
		GameRegistry.addRecipe(getShapedRecipe(res, v))
	}
}
