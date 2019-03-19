package main.util

import java.nio.file.Files
import java.nio.file.Paths

object Json {
    fun <T> readFile(path: String, klass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return gson.fromJson(String(Files.readAllBytes(Paths.get(path))), klass) as T
    }
}
