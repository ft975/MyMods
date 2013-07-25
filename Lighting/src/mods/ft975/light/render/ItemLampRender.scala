package mods.ft975.light.render

import cpw.mods.fml.relauncher.{Side, SideOnly}
import mods.ft975.light.block.ItemLamp
import mods.ft975.light.util.LampColor
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.{ItemRendererHelper, ItemRenderType}
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
object ItemLampRender extends IItemRenderer {
	def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true

	def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true

	def renderItem(typ: ItemRenderType, item: ItemStack, data: AnyRef*) {
		val data = ItemLamp.getData(item)
		val model = RenderUtil.getModel(data._2)
		val rV = model.getRenderValues(typ)
		render(model, rV._1, rV._2, rV._3, rV._4, data._1, data._3)
	}

	private def render(model: ModelLamp, x: Float, y: Float, z: Float, scale: Float, color: LampColor, on: Boolean) {
		GL11.glPushMatrix()
		GL11.glTranslatef(x, y, z)
		GL11.glRotatef(180, 1, 0, 0.0F)
		GL11.glScalef(scale, scale, scale)
		model.render(color, on, isItem = true)
		GL11.glPopMatrix()
	}
}