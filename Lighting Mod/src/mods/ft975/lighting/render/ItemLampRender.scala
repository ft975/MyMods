package mods.ft975.lighting.render

import net.minecraftforge.client.IItemRenderer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer.{ItemRendererHelper, ItemRenderType}
import org.lwjgl.opengl.GL11
import mods.ft975.lighting.{Colors, Shapes}
import mods.ft975.lighting.render.RenderUtil.Color

class ItemLampRender extends IItemRenderer {
  def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true

  def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true

  def renderItem(typ: ItemRenderType, item: ItemStack, data: AnyRef*) {
    val model = RenderUtil.getModel(Shapes.fromID(item.getTagCompound.getByte("S")))
    val rV = model.getRenderValues(typ)
    render(model, rV._1, rV._2, rV._3, rV._4, Colors.fromID(item.getTagCompound.getByte("C")).color)
  }

  private def render(model: ModelLamp, x: Float, y: Float, z: Float, scale: Float, color: Color) {
    GL11.glPushMatrix()
    GL11.glTranslatef(x.toFloat, y.toFloat, z.toFloat)
    GL11.glRotatef(180, 1, 0, 0.0F)
    GL11.glScalef(scale, scale, scale)
    model.render(color, false)
    GL11.glPopMatrix()
  }
}
