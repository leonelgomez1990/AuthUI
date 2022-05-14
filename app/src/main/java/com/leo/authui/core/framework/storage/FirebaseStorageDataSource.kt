package com.leo.authui.core.framework.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.leo.authui.core.framework.ExifUtil
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.RealPathUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class FirebaseStorageDataSource(
    private val storage: FirebaseStorage,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StorageDataSource {

    companion object {
        const val TAG = "FirebaseStorageDataSource"
    }

    override suspend fun deleteFile(path: String): MyResult<Boolean> {
        return deleteStorageReference("files/$path")
    }

    override suspend fun deleteImage(path: String): MyResult<Boolean> {
        return deleteStorageReference("images/$path")
    }

    override suspend fun uploadFile(
        localPath: String,
        remotePath: String,
        name: String?
    ): MyResult<String> {

        return try {
            val pathString = "files/$remotePath/${name ?: UUID.randomUUID().toString()}"
            val fileRef = storage.reference.child(pathString)

            withContext(ioDispatcher) {
                val url = fileRef.putFile(localPath.toUri()).await()
                    .storage.downloadUrl.await()
                    .toString()
                MyResult.Success(url)
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadFile: Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    override suspend fun uploadImage(
        localPath: String,
        remotePath: String,
        name: String?
    ): MyResult<String> {

        return try {
            val pathString = "images/$remotePath/${name ?: UUID.randomUUID().toString()}.bmp"
            val imageRef = storage.reference.child(pathString)
            val data: ByteArray = convertImageToBytes(localPath.toUri())

            withContext(ioDispatcher) {
                val url = imageRef.putBytes(data).await()
                    .storage.downloadUrl.await()
                    .toString()
                MyResult.Success(url)
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadImage: Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    private fun convertImageToBytes(uri: Uri): ByteArray {
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

        val realPath = RealPathUtil.getRealPath(context, uri)
        val bmp: Bitmap = BitmapFactory.decodeFile(realPath.toUri().path)
        val correctedBmp = ExifUtil.rotateBitmap(realPath, bmp)
        val baos = ByteArrayOutputStream()
        correctedBmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        return baos.toByteArray()
    }

    private suspend fun deleteStorageReference(pathString: String): MyResult<Boolean> {
        return try {
            val storageRef = storage.reference.child(pathString)
            storageRef.delete().await()
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, "deleteStorageReference: Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }
}