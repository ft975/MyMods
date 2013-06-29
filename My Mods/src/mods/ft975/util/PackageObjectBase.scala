package mods.ft975.util

import java.util.logging.Logger
import java.util.logging.Level._

trait PackageObjectBase {
	var log: Logger = null
	private val isDebug = false

	def DebugOnly(op: => Unit) {
		if (isDebug) op
	}

	def logInfo(a: Any) {
		log.log(INFO, tS(a))
	}

	def tS(a: Any): String = {
		if (a != null) a.toString else "() NULL"
	}
}
