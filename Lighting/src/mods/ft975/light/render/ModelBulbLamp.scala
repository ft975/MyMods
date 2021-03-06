package mods.ft975.light.render

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.model.ModelRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType

@SideOnly(Side.CLIENT)
object ModelBulbLamp extends ModelLamp {
	protected val Base : ModelRenderer = new ModelRenderer(this, 0, 0)
	protected val Bulb : ModelRenderer = new ModelRenderer(this, 0, 13)
	protected val Cover: ModelRenderer = null
	protected val Rays : ModelRenderer = new ModelRenderer(this)
	protected val texW : Int           = 64
	protected val texH : Int           = 32
	val texture: String = "bulb.png"

	Base.addBox(0F, 0F, 0F, 12, 1, 12)
	Base.setRotationPoint(-6F, 23F, -6F)
	Base.setTextureSize(64, 32)
	Base.mirror = true
	setRotation(Base, 0F, 0F, 0F)

	Bulb.addBox(0F, 0F, 0F, 8, 5, 8)
	Bulb.setRotationPoint(-4F, 18F, -4F)
	Bulb.setTextureSize(64, 32)
	Bulb.mirror = true
	setRotation(Bulb, 0F, 0F, 0F)

	Rays.addBox(0F, 0F, 0F, 10, 6, 10)
	Rays.setRotationPoint(-5F, 17.5F, -5F)
	Rays.setTextureSize(64, 32)
	Rays.mirror = true
	setRotation(Rays, 0F, 0F, 0F)

	def getRenderValues(typ: ItemRenderType): (Float, Float, Float, Float) = {
		typ match {
			case ItemRenderType.ENTITY => (0, 2.5F, 0, 2F)
			case ItemRenderType.EQUIPPED => (.5F, 2.5F, .5F, 1.5F)
			case ItemRenderType.INVENTORY => (0, 1.75F, 0, 1.5f)
			case ItemRenderType.EQUIPPED_FIRST_PERSON => (.3F, 2.25F, .5F, 1.25F)
			case _ => (0, 0, 0, 0)
		}
	}
}