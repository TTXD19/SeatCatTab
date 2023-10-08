package com.github.ttxd19.seatcattab.sort

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project

class SortByAlphabet : AnAction() {

    override fun update(e: AnActionEvent) {
        val currentProject = e.project
        e.presentation.isEnabledAndVisible = currentProject != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
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
}