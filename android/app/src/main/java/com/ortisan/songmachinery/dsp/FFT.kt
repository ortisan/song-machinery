package com.ortisan.songmachinery.dsp

import kotlin.math.ln

class FFT {
    companion object {
         fun compute(real: DoubleArray, imaginary: DoubleArray) {
            val n = real.size
            val logN = ln(n.toDouble()) / ln(2.0)

            if (logN.toInt().toDouble() != logN) {
                throw IllegalArgumentException("FFT size must be a power of 2")
            }

            val indices = IntArray(n)
            var bits = 0
            while ((1 shl bits) < n) bits++
            for (i in 0 until n) {
                var reversed = 0
                var temp = i
                for (j in 0 until bits) {
                    reversed = (reversed shl 1) or (temp and 1)
                    temp = temp shr 1
                }
                indices[i] = reversed
            }
            val realCopy = real.clone()
            val imaginaryCopy = imaginary.clone()
            for (i in 0 until n) {
                real[i] = realCopy[indices[i]]
                imaginary[i] = imaginaryCopy[indices[i]]
            }

            var step = 1
            while (step < n) {
                val jump = step shl 1
                val deltaAngle = -2.0 * Math.PI / jump
                val wTempReal = Math.cos(deltaAngle)
                val wTempImag = Math.sin(deltaAngle)
                for (i in 0 until step) {
                    var j = i
                    while (j < n) {
                        val k = j + step
                        val realPart = real[k] * wTempReal - imaginary[k] * wTempImag
                        val imagPart = real[k] * wTempImag + imaginary[k] * wTempReal
                        real[k] = real[j] - realPart
                        imaginary[k] = imaginary[j] - imagPart
                        real[j] += realPart
                        imaginary[j] += imagPart
                        j += jump
                    }
                }
                step = jump
            }
        }
    }

}