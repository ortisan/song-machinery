package com.ortisan.songmachinery.audio

import android.media.AudioFormat
import android.media.AudioRecord


val A4 = 440.0

val notes = listOf(
    "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
)

val sampleRate = 44100
val bufferSize = AudioRecord.getMinBufferSize(
    sampleRate,
    AudioFormat.CHANNEL_IN_MONO,
    AudioFormat.ENCODING_PCM_16BIT
)
