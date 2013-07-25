package mods.ft975.energy.api


trait PowerExporter extends PowerMember {
	def getPriority: Short

	def getMaxOutput: Int

	def getPower(amount: Int): Boolean

	def testPower(amount: Int): Boolean
}
