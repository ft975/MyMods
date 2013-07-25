package mods.ft975.light.render

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mods.ft975.light.ModInfo
import mods.ft975.light.util.LampColor
import net.minecraft.client.model.{ModelRenderer, ModelBase}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import net.minecraftforge.common.ForgeDirection

@SideOnly(Side.CLIENT)
abstract class ModelLamp extends ModelBase {
	protected val Base : ModelRenderer
	protected val Bulb : ModelRenderer
	protected val Cover: ModelRenderer
	protected val Rays : ModelRenderer
	protected val texW : Int
	protected val texH : Int
	val texture: String
	lazy val textureLoc = new ResourceLocation(ModInfo.modID, ModInfo.resourceBlockTextures + texture)
	lazy val nocolLoc   = new ResourceLocation(ModInfo.modID, ModInfo.resourceBlockTextures + "nocol_" + texture)

	def getRenderValues(typ: ItemRenderType): (Float, Float, Float, Float)

	def render(col: LampColor, isOn: Boolean, isItem: Boolean) {
		FMLClientHandler.instance.getClient.renderEngine.func_110577_a(nocolLoc)
		if (Base != null) Base.render(0.0625F)
		if (Cover != null) RenderUtil.renderInOut(Cover, 0.0625F)
		RenderUtil.renderBulbRays(Bulb, Rays, 0.0625F, isOn, col, isItem, textureLoc)
	}

	def render(col: LampColor, isOn: Boolean, side: ForgeDirection) {
		RenderUtil.rotateRender(this, 0.0625F, col, isOn, side)
	}

	protected def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
}
