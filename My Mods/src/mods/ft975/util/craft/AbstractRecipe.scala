package mods.ft975.util
package craft

import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.ItemStack
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.world.World
import java.util

class AbstractRecipe(xSize: Int, ySize: Int, rec: Array[Array[ItemStack]], res: (IndexedSeq[ItemStack]) => ItemStack) extends IRecipe {
	def this(xSize: Int, ySize: Int, rec: Array[Array[ItemStack]], res: ItemStack) = this(xSize, ySize, rec, _ => res.copy())

	val recipe: Array[ItemStack] = {
		val temp = condenseGrid(rec.flatten)
		if (xSize != temp._2 || ySize != temp._3) throw new IllegalArgumentException(s"Failed to initialize recipe: ${util.Arrays.deepToString(temp._1.asInstanceOf[Array[AnyRef]])}, ${temp._2}, ${temp._3}, size should be $xSize * $ySize")
		temp._1
	}

	def matches(ic: InventoryCrafting, wrd: World): Boolean = {
		val res = condenseGrid((for {i <- 0 until 9} yield ic.getStackInSlot(i)).toArray)

		val itemArray = res._1
		val width = res._2
		val height = res._3

		if (width != xSize || height != ySize) return false

		for (v <- 0 until itemArray.length) {
			if (itemArray(v) != null) {
				if (!itemArray(v).isItemEqual(recipe(v))) {return false}
			} else if (itemArray(v) == null && recipe(v) != null) {
				return false
			}
		}
		true
	}

	protected def doItemsMatch(_1: ItemStack, _2: ItemStack): Boolean = { true }

	def getCraftingResult(ic: InventoryCrafting): ItemStack = res(for {v <- 0 until ic.getSizeInventory} yield ic.getStackInSlot(v))

	val getRecipeSize: Int = xSize * ySize

	def getRecipeOutput: ItemStack = null

	protected def doesIntercept(x: Int, y: Int, top: Boolean, bot: Boolean, medH: Boolean, left: Boolean, right: Boolean, medV: Boolean): Boolean = {
		(x == 0 && left) ||
			(x == 1 && medV) ||
			(x == 2 && right) ||
			(y == 0 && top) ||
			(y == 1 && medH) ||
			(y == 2 && bot)
	}

	protected def condenseGrid(items: Array[ItemStack]): (Array[ItemStack], Int, Int) = {
		def gXY(x: Int, y: Int): Int = (y * 3) + x

		var height = 3
		var width = 3

		// Height information
		val topEmpty = if (items(gXY(0, 0)) == null && items(gXY(1, 0)) == null && items(gXY(2, 0)) == null) {height -= 1; true} else {false}
		val botEmpty = if (items(gXY(0, 2)) == null && items(gXY(1, 2)) == null && items(gXY(2, 2)) == null) {height -= 1; true} else {false}
		// Only if the top or bottom is empty, and this is empty
		val mHEmpty = if (items(gXY(0, 1)) == null && items(gXY(1, 1)) == null && items(gXY(2, 1)) == null
			&& (topEmpty || botEmpty)) {height -= 1; true} else {false}

		// Width information
		val leftEmpty = if (items(gXY(0, 0)) == null && items(gXY(0, 1)) == null && items(gXY(0, 2)) == null) {width -= 1; true} else {false}
		val rightEmpty = if (items(gXY(2, 0)) == null && items(gXY(2, 1)) == null && items(gXY(2, 2)) == null) {width -= 1; true} else {false}
		// Only if either the left or right is empty will this be empty
		val mVEmpty = if (items(gXY(1, 0)) == null && items(gXY(1, 1)) == null && items(gXY(1, 2)) == null
			&& (leftEmpty || rightEmpty)) {width -= 1; true} else {false}

		val itemArray = new Array[ItemStack](width * height)

		if (itemArray.length == 9) items
		else {
			val coordArray = (for {x <- 0 until 3
														 y <- 0 until 3
														 if !doesIntercept(x, y, topEmpty, botEmpty, mHEmpty, leftEmpty, rightEmpty, mVEmpty)} yield new XYCoord(x, y)).toArray
			for (v <- 0 until coordArray.length) {
				val coord = coordArray(v)
				itemArray(v) = items(gXY(coord.x, coord.y))
			}
		}
		(itemArray, width, height)
	}
}



