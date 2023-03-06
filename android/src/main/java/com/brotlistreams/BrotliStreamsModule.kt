package com.brotlistreams

import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.*
import org.brotli.dec.BrotliInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

class BrotliStreamsModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun decompressBrotli(brotli: String, promise: Promise) {
    val compressedData = Base64.getDecoder().decode(brotli)
    val map = Arguments.createMap()
    val outputStream = ByteArrayOutputStream()
    val inputStream = BrotliInputStream(ByteArrayInputStream(compressedData))

    val buffer = ByteArray(1024)
    var len = inputStream.read(buffer)

    while (len > 0) {
      outputStream.write(buffer, 0, len)
      len = inputStream.read(buffer)
    }

    inputStream.close()
    outputStream.close()

    map.putString("content", String(outputStream.toByteArray()))
    promise.resolve(map)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @ReactMethod
  fun base64ToBrotli(base64String: String, promise: Promise) {
    val bytes = Base64.getDecoder().decode(base64String)
    val inputStream = BrotliInputStream(ByteArrayInputStream(bytes))
    val outputStream = ByteArrayOutputStream()

    val byteArray = Arguments.createArray()
    val map = Arguments.createMap()

    inputStream.copyTo(outputStream)

    outputStream.close()
    inputStream.close()

    for (b in outputStream.toByteArray()) {
      byteArray.pushInt(b.toInt())
    }

    map.putArray("file", byteArray)
    promise.resolve(map)
  }

  companion object {
    const val NAME = "BrotliStreams"
  }
}
