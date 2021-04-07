package space.cosmicgoat.cosmicguns

import kotlinx.serialization.Serializable

@Serializable
class GunData(val name: String, val damage: Float) {

    val id: String
        get() = name.replace(Regex("\\s"), "-").toLowerCase()

    init {
        require(name.isNotEmpty()) { "Needs a name!" }
    }
}