package space.cosmicgoat.cosmicguns

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

private fun parse(file: File): List<GunData> {
    return try {
        Json.decodeFromString(file.readText())
    } catch (e: SerializationException) {
        LOGGER.error("Unable to parse JSON file!: $file")
        LOGGER.error(e.message)
//        LOGGER.error(e.stackTraceToString())
        emptyList()
    }
}

private fun parseFiles(files: Sequence<File>) = runBlocking {
    val deferredList = ArrayList<Deferred<List<GunData>>>()
    files.asFlow().buffer().collect() {
        val d = async() { parse(it) }
        deferredList.add(d)
    }

    return@runBlocking deferredList.awaitAll()
}

fun gunResourceLoader(dataDir: File): List<GunData> {

    if (!dataDir.exists()) {
        LOGGER.warn("Data directory $dataDir doesn't exist!")
        return emptyList() // TODO: figure out what to do when there is no config dir
    }

    val files = dataDir.walk()
        .onFail { file, ioException ->
            LOGGER.error("Failed to load data directory!: $file")
            LOGGER.error(ioException.message)
//            LOGGER.error(ioException.stackTraceToString())
//            throw ioException
        }
        .filter { it.extension == "json" }

    return parseFiles(files).flatten()
}