package com.pedrobneto.sdk.services

import android.content.Context
import com.pedrobneto.sdk.entities.Response
import com.squareup.moshi.Moshi
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import org.koin.core.KoinComponent
import org.koin.core.get

class FileService : KoinComponent {
    private fun getGifsDirPath(context: Context): String {
        var path = context.getExternalFilesDir(null)?.absolutePath ?: ""
        path += "${File.separator}/Giphy App/favorite_gifs${File.separator}"

        return path
    }

    private fun getFilePath(context: Context, fileId: String): String {
        var path = getGifsDirPath(context)
        val dir = File(path)
        if (!dir.exists()) dir.mkdirs()

        path += "gif_$fileId"
        return path
    }

    fun getLocalGifs(context: Context): List<Response.Gif> {
        val gifsDir = File(getGifsDirPath(context))
        val gifList = mutableListOf<Response.Gif>()

        if (gifsDir.exists()) {
            gifsDir.listFiles()?.forEach { file ->
                val objectInputStream = ObjectInputStream(FileInputStream(file))
                val json = objectInputStream.readObject() as? String
                objectInputStream.close()

                json?.let {
                    get<Moshi>()
                        .adapter(Response.Gif::class.java)
                        .fromJson(it)
                        ?.run(gifList::add)
                }
            }
        }

        return gifList
    }

    fun saveFile(context: Context, fileId: String, json: String) {
        val gifFile = File(getFilePath(context, fileId))
        if (!gifFile.createNewFile()) {
            gifFile.delete()
            gifFile.createNewFile()
        }

        val objectOutputStream = ObjectOutputStream(FileOutputStream(gifFile))
        objectOutputStream.writeObject(json)
        objectOutputStream.close()
    }

    fun deleteFile(context: Context, fileId: String) {
        File(getFilePath(context, fileId)).delete()
    }
}
