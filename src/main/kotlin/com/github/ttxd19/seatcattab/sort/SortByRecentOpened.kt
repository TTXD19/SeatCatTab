package com.github.ttxd19.seatcattab.sort

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class SortByRecentOpened : AnAction() {

    private var previousSortedFiles = ArrayList<VirtualFile>()

    override fun update(e: AnActionEvent) {
        val currentProject = e.project
        e.presentation.isEnabledAndVisible = currentProject != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
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
}