package mods.ft975.lighting.render

import cpw.mods.fml.relauncher.{Side, SideOnly}

import net.minecraft.client.model.ModelRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType

@SideOnly(Side.CLIENT)
object ModelCagedLamp extends ModelLamp {
  protected override val Base: ModelRenderer = new ModelRenderer(this, 1, 1)
  protected override val Bulb: ModelRenderer = new ModelRenderer(this, 30, 15)
  protected override val Cover: ModelRenderer = new ModelRenderer(this, 1, 13)
  protected override val Rays: ModelRenderer = new ModelRenderer(this)
  override val texture = "caged.png"
  protected override val texW: Int = 64
  protected override val texH: Int = 32

  Base.addBox(0F, 0F, 0F, 10, 1, 10)
  Base.setRotationPoint(-5F, 23F, -5F)
  Base.setTextureSize(texW, texH)
  Base.mirror = true
  setRotation(Base, 0F, 0F, 0F)

  Bulb.addBox(0F, 0F, 0F, 6, 5, 6)
  Bulb.setRotationPoint(-3F, 18F, -3F)
  Bulb.setTextureSize(texW, texH)
  Bulb.mirror = true
  setRotation(Bulb, 0F, 0F, 0F)

  Cover.addBox(0F, 0F, 0F, 7, 6, 7)
  Cover.setRotationPoint(-3.533333F, 17.5F, -3.533333F)
  Cover.setTextureSize(texW, texH)
  Cover.mirror = true
  setRotation(Cover, 0F, 0F, 0F)

  Rays.addBox(0F, 0F, 0F, 8, 6, 8)
  Rays.setRotationPoint(-4F, 17.1F, -4F)
  Rays.setTextureSize(texW, texH)
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