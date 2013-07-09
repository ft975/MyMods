package mods.ft975.lighting

import cpw.mods.fml.relauncher.{Side, SideOnly}

object ModInfo {
  final val prefixName = "ft975."
  final val postfixName = "lighting"
  final val modID = prefixName + postfixName
  final val modName = "Lighting Mod"

  final val versionBuild = "1"
  final val versionMinor = "0"
  final val versionMajor = "1"
  final val version = versionMajor + "." + versionMinor + "_" + versionBuild

  final val versionMinecraft = "1.5.0"
  @SideOnly(Side.CLIENT)
  final val resourceFolder = "/mods/ft975/lighting/textures/blocks/"
}