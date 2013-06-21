package ft975.lighting.render

import net.minecraft.client.model.{ModelRenderer, ModelBase}
import net.minecraftforge.common.ForgeDirection
import cpw.mods.fml.client.FMLClientHandler
import ft975.lighting.ModInfo
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import ft975.lighting.render.RenderUtil.Color

trait ModelLamp extends ModelBase {
	protected val Base: ModelRenderer
	protected val Bulb: ModelRenderer
	protected val Cover: ModelRenderer
	protected val Rays: ModelRenderer
	protected val texW: Int
	protected val texH: Int
	val texture: String

	protected val DefaultColor = new Color(1F, 0F, 1F)

	def getRenderValues(typ: ItemRenderType): (Float, Float, Float, Float)

	def render(col: Color, isOn: Boolean) {
		FMLClientHandler.instance.getClient.renderEngine.bindTexture(ModInfo.modelFolders + texture)
		if (Base != null) Base.render(0.0625F)
		if (Cover != null) RenderUtil.renderInOut(Cover, 0.0625F)
		RenderUtil.renderBulbRays(Bulb, Rays, 0.0625F, isOn, col)
	}

	def render(col: Color, isOn: Boolean, side: ForgeDirection) {
		RenderUtil.rotateRender(this, 0.0625F, col, isOn, side)
	}

	protected def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
}
