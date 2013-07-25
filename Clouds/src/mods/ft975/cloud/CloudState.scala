package mods.ft975.cloud

import aurelienribon.tweenengine.{Tween, TweenAccessor}
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.common.ForgeDirection
import net.minecraftforge.common.ForgeDirection.{DOWN => Down, UP => Up, NORTH => North, EAST => East, WEST => West, SOUTH => South, UNKNOWN => _}
import scala.collection.parallel.mutable

object CloudState {
	private val moistureModifier    = 1F
	private val temperatureModifier = .6F
	@inline private final def cloudDeleteEquation(height: Int) = { math.min(math.pow(1.25F, height - 100) / 300F, 1.001F) }

	final val TickRate             = 20
	final val CloudUpdateFrequency = TickRate * 5

	private val cloudSet = mutable.ParSet[Cloud]()

	var time: Int = 0
	var tween     = 0F

	def removeCloud(cld: Cloud) {
		cloudSet -= cld
	}
	def addCloud(cld: Cloud) {
		cloudSet += cld
	}
	def filterOp(filter: (Cloud) => Boolean, op: (Cloud) => Unit) {
		for (v <- cloudSet) {
			if (filter(v)) op(v)
		}
	}
	def op(op: (Cloud) => Unit) {
		filterOp((c) => true, op)
	}

	class Cloud(private[CloudState] var x: Int, private[CloudState] var z: Int, var moisture: Float) {
		def getX: Float = x * pxSize
		def getZ: Float = (z * pxSize) - tween * pxSize

		def update(wrd: World, biome: BiomeGenBase, height: Int, player: EntityClientPlayerMP) {
			calcMoisture(biome)
			cloneOrDelete(height, wrd)
			checkDespawn(player)
		}

		private def calcMoisture(biome: BiomeGenBase) {
			moisture += biome.getFloatRainfall * moistureModifier
			moisture -= biome.getFloatTemperature * temperatureModifier
		}

		private def cloneOrDelete(height: Int, wrd: World) {
			val num = (rand.nextFloat() + 1) * 2

			if (isValidHeight(height)) {
				removeCloud(this)
			} else if (math.abs(moisture / 2 - num) < .1F) {
				clone()
				delete()
			} else if (moisture / 2 > num) {
				clone()
			} else if (moisture < .7 && rand.nextBoolean()) {
				delete()
			}

			def clone() {
				for (v <- 0 to 5) {
					val s = getRandSide
					if (checkValidAndPlace(x + s.offsetX, z + s.offsetZ))
						return
				}

				def checkValidAndPlace(nx: Int, nz: Int) = {
					if (isValidHeight(Util.getHeight(wrd, nx * pxSize, nz * pxSize, pxSize / 2))) {
						CloudState.addCloud(new Cloud(x, z, moisture / 2))
						moisture /= 2
						true
					}
					false
				}
			}

			def delete() {
				filterOp(
					(c) => c.x == x + 1 || c.x == x - 1 || c.z == z + 1 || c.z == z - 1,
					(c) => c.moisture += moisture / 4
				)
				removeCloud(this)
			}

			def isValidHeight(height: Int) = {
				rand.nextFloat() < cloudDeleteEquation(height)
			}

			def getRandSide: ForgeDirection = {
				val num = rand.nextInt(8)
				if (num < 3) {
					North
				} else if (num < 5) {
					South
				} else if (num < 7) {
					East
				} else {
					null
				}
			}

		}

		private def checkDespawn(player: EntityClientPlayerMP) {
			// Is the cloud further than 384 meters from the player?
			if (math.abs((player.posX - x * 12) + (player.posZ - z * 12)) > 147456 /*384^2*/ ) removeCloud(this)
		}

		override def hashCode(): Int = x + z * 31
		override def equals(obj: Any): Boolean = obj match {case obj: Cloud => obj.x == x && obj.z == z; case _ => false }
		override def toString: String = s"Cloud @ ${x * 12}, ${(z + tween) * 12}, moisture = $moisture"
	}

	object CloudTweener extends TweenAccessor[CloudState.type] {
		final val tweenType = 0

		def getValues(target: CloudState.type, tweenType: Int, returnValues: Array[Float]): Int = {
			returnValues(0) = target.tween
			1
		}

		def setValues(target: CloudState.type, tweenType: Int, newValues: Array[Float]): Unit = {
			if (newValues(0) - 1 >= -.01) {
				target.tween = newValues(0)
			} else {
				cloudSet.foreach(_.z -= 1)
				target.tween = 0
				Tween.to(target, 0, CloudUpdateFrequency).target(1).start()
			}
		}
	}

}
