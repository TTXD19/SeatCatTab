package com.github.ttxd19.seatcattab.common.timestamp

import com.intellij.openapi.vfs.VirtualFile

interface FileTimestampProvider {
    fun getTimestamp(file: VirtualFile): Long
}