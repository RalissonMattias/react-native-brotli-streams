package com.brotlistreams

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import org.brotli.dec.BrotliInputStream
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray

class BrotliStreamsModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun decompressBrotli(brotli: ReadableArray, promise: Promise) {
    val compressedData = ByteArray(brotli.size())
    for(i in 0 until compressedData.size) {
      compressedData[i] = brotli.getInt(i).toByte()
    }
    val byteArray = Arguments.createArray()
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

    for(b in outputStream.toByteArray()) {
      byteArray.pushInt(b.toInt())
    }
    map.putArray("file", byteArray)
    promise.resolve(map)
  }

  companion object {
    const val NAME = "BrotliStreams"
  }
}
