package com.ortisan.songmachinery.audio

import com.ortisan.songmachinery.dsp.FFT
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

class Frequency {
    companion object {
         fun detectPitch(audioData: ShortArray, sampleRate: Int): Double {
            val fftSize = 1024
            val real = DoubleArray(fftSize)
            val imaginary = DoubleArray(fftSize)

            for (i in 0 until fftSize) {
                if (i < audioData.size) {
                    real[i] = audioData[i].toDouble()
                } else {
                    real[i] = 0.0
                }
                imaginary[i] = 0.0
            }

            FFT.compute(real, imaginary)

            var maxIndex = 0
            var maxMagnitude = 0.0
            for (i in 1 until fftSize / 2) {
                val magnitude = real[i].pow(2) + imaginary[i].pow(2)
                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude
                    maxIndex = i
                }
            }

            return (maxIndex * sampleRate) / fftSize.toDouble()
        }

        fun frequencyToPitch(frequency: Double): String {
            if (frequency < 0) {
                throw IllegalArgumentException("Invalid frequency: $frequency")
            }
            val semitonesFromA4 = (12 * ln(frequency / A4) / ln(2.0)).roundToInt()

            val noteIndex = (semitonesFromA4 + 9) % 12 // Adding 9 because A4 is the 9th note in the list
            val octave = 4 + (semitonesFromA4 + 9) / 12 // Adjust for octaves

            val adjustedOctave = if (noteIndex < 0) octave - 1 else octave
            val adjustedNoteIndex = (noteIndex + 12) % 12

            return "${notes[adjustedNoteIndex]}$adjustedOctave"
        }
    }
}