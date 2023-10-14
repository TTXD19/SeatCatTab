package com.github.ttxd19.seatcattab.common.timestamp

import com.intellij.openapi.vfs.VirtualFile

class RealFileTimestampProvider : FileTimestampProvider {
    override fun getTimestamp(file: VirtualFile): Long {
        return file.timeStamp
    }
}