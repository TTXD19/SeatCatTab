package com.github.ttxd19.seatcattab.sort.custom

import com.github.ttxd19.seatcattab.common.SortLogic
import com.github.ttxd19.seatcattab.guiform.TestSort
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.JComponent

class CustomSortDialog(private val project: Project) : DialogWrapper(true) {

    private val customSort = TestSort()

    init {
        init()
        title = "My Custom Form"
    }

    override fun createCenterPanel(): JComponent? {
        return customSort.getPanel()
    }

    override fun doOKAction() {
        super.doOKAction()
        val sortRules = customSort.getSortRules()
        sortItems(sortRules)
        close(OK_EXIT_CODE);
    }

    private fun sortItems(rules: List<SortLogic.SortType>) {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val openFiles = fileEditorManager.openFiles

        var sortedFiles = openFiles.toList()

        for (rule in rules) {
            sortedFiles = getSortedFilesByRule(sortedFiles, rule)
        }

        // Close all open files
        openFiles.forEach {
            fileEditorManager.closeFile(it)
        }

        // Open files in sorted order
        for (file in sortedFiles) {
            fileEditorManager.openFile(file, true)
        }
    }

    private fun getSortedFilesByRule(files: List<VirtualFile>, rule: SortLogic.SortType): List<VirtualFile> {
        return when (rule) {
            SortLogic.SortType.SORT_BY_ALPHABET -> files.sortedBy { it.name }
            SortLogic.SortType.SORT_BY_ALPHABET_REVERSED -> files.sortedByDescending { it.name }
            SortLogic.SortType.SORT_BY_FILE_DIRECTORY -> files.sortedBy { it.parent.path }
            SortLogic.SortType.SORT_BY_LAST_MODIFIED -> files.sortedBy { it.timeStamp }
            SortLogic.SortType.SORT_BY_FILE_TYPE -> files.sortedBy { it.extension }
            SortLogic.SortType.SORT_BY_RECENT_OPENED -> SortLogic.sortByRecentOpened(project, files)
            SortLogic.SortType.EMPTY -> files
        }
    }

    override fun doCancelAction() {
        super.doCancelAction()
        close(OK_EXIT_CODE);
    }
}