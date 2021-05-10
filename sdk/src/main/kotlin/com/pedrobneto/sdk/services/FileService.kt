package com.pedrobneto.sdk.services

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import org.koin.core.KoinComponent

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
