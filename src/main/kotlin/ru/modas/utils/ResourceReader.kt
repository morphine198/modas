package ru.modas.utils

import java.nio.file.Files
import java.nio.file.Paths

class ResourceReader {
    @Throws(Exception::class)
    fun readTextResource(filename: String?): String {
        val uri = javaClass.getResource(String.format("/%s", filename))?.toURI()
        println(uri)
        return Files.readString(Paths.get(uri))
    }
}