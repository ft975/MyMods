package mods.ft975.lighting.render

import net.minecraft.client.model.ModelRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType


object ModelBlockLamp extends ModelLamp {
  protected val Base: ModelRenderer = null
  protected val Bulb: ModelRenderer = new ModelRenderer(this, 0, 0)
  protected val Cover: ModelRenderer = null
  protected val Rays: ModelRenderer = new ModelRenderer(this)
  protected val texW: Int = 64
  protected val texH: Int = 32
  val texture: String = "block.png"

  Bulb.addBox(0F, 0F, 0F, 16, 16, 16)
  Bulb.setRotationPoint(-8F, 8F, -8F)
  Bulb.setTextureSize(texW, texH)
  Bulb.mirror = true
  setRotation(Bulb, 0F, 0F, 0F)

  Rays.addBox(0F, 0F, 0F, 18, 18, 18)
  Rays.setRotationPoint(-9F, 7F, -9F)
  Rays.setTextureSize(texW, texH)
  Rays.mirror = true
  setRotation(Rays, 0F, 0F, 0F)

  def getRenderValues(typ: ItemRenderType): (Float, Float, Float, Float) = {
    typ match {
      case ItemRenderType.ENTITY => (0, 1.2F, 0, 1F)
      case ItemRenderType.EQUIPPED => (.5F, 1.75F, .5F, 1F)
      case ItemRenderType.INVENTORY => (0, 1F, 0, 1f)
      case ItemRenderType.EQUIPPED_FIRST_PERSON => (.5F, 2.25F, .5F, 1F)
      case _ => (0, 0, 0, 1)
    }
  }
}