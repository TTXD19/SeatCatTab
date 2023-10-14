package com.github.ttxd19.seatcattab.common

import com.github.ttxd19.seatcattab.common.timestamp.FileTimestampProvider
import com.github.ttxd19.seatcattab.common.timestamp.RealFileTimestampProvider
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object SortLogic {

    fun sortByAlphabet(project: Project) {
        val fileEditorManager = FileEditorManager.getInstance(project)

        val openFiles = fileEditorManager.openFiles
        val sortedFiles = openFiles.sortedBy { it.name }

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    fun sortByAlphabetReversed(project: Project) {
        val fileEditorManager = FileEditorManager.getInstance(project)

        val openFiles = fileEditorManager.openFiles
        val sortedFiles = openFiles.sortedBy { it.name }.reversed()

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    fun sortByFileDirectory(project: Project) {
        val fileEditorManager = FileEditorManager.getInstance(project)

        val openFiles = fileEditorManager.openFiles
        val sortedFiles = openFiles.sortedBy { it.parent.path }

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    var timestampProvider: FileTimestampProvider = RealFileTimestampProvider()

    fun sortByLastModified(project: Project) {
        val fileEditorManager = FileEditorManager.getInstance(project)

        val openFiles = fileEditorManager.openFiles
        val sortedFiles = openFiles.sortedBy { timestampProvider.getTimestamp(it) }

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    fun sortByFileType(project: Project) {
        val fileEditorManager = FileEditorManager.getInstance(project)

        val openFiles = fileEditorManager.openFiles
        val sortedFiles = openFiles.sortedBy { it.extension }

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    private var previousSortedFiles = ArrayList<VirtualFile>()

    fun sortByRecentOpened(project: Project) {
        val fileEditorManager = FileEditorManager.getInstance(project)

        // Obtain the files from the EditorHistoryManager
        val historyFiles = EditorHistoryManager.getInstance(project).files
        val openFiles = fileEditorManager.openFiles

        // Check if the sorted order hasn't changed
        val isMatchPrevious = previousSortedFiles.map { it.name } == openFiles.map { it.name }
        if (isMatchPrevious) {
            // It means the user clicked the same sorting type without opening or closing a file
            return
        }

        // Create a sorted list based on the order in historyFiles
        val indexMap = historyFiles.withIndex().associate { it.value to it.index }
        val sortedFiles = openFiles.sortedBy { indexMap[it] ?: Int.MAX_VALUE }.reversed()

        // Update the previous sorted order
        previousSortedFiles.clear()
        previousSortedFiles.addAll(sortedFiles)

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    fun sortByRecentOpened(project: Project, fileList: List<VirtualFile>): List<VirtualFile> {
        // Obtain the files from the EditorHistoryManager
        val historyFiles = EditorHistoryManager.getInstance(project).files

        // Create a sorted list based on the order in historyFiles
        val indexMap = historyFiles.withIndex().associate { it.value to it.index }

        return fileList.sortedBy { indexMap[it] ?: Int.MAX_VALUE }.reversed()
    }


    enum class SortType(val sortType: String) {
        EMPTY("EMPTY"),
        SORT_BY_ALPHABET("Alphabetically (A-Z)"),
        SORT_BY_ALPHABET_REVERSED("Alphabetically (Z-A)"),
        SORT_BY_FILE_DIRECTORY("By File Directory"),
        SORT_BY_LAST_MODIFIED("By Last Modified Date"),
        SORT_BY_FILE_TYPE("By File Type"),
        SORT_BY_RECENT_OPENED("Recently Opened")
    }
}