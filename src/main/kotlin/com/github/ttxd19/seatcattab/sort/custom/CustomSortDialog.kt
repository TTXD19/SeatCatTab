package com.github.ttxd19.seatcattab.sort.custom

import com.github.ttxd19.seatcattab.common.SortType
import com.github.ttxd19.seatcattab.guiform.TestSort
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.JComponent

class CustomSortDialog(private val project: Project) : DialogWrapper(true) {

    private val customSort = TestSort()

    init {
        init()
        title = "Custom Sort"
    }

    override fun createCenterPanel(): JComponent? {
        return customSort.getPanel()
    }

    override fun doOKAction() {
        super.doOKAction()
        val sortRules = customSort.getSortRules()
        val fileEditorManager = FileEditorManager.getInstance(project)
        val openFiles = fileEditorManager.openFiles.toList()
        val sortedFiles = sortItems(sortRules, openFiles)
        closeFiles(fileEditorManager, openFiles)
        openFilesInOrder(fileEditorManager, sortedFiles)
        close(OK_EXIT_CODE);
    }

    private fun sortItems(rules: List<SortType>, files: List<VirtualFile>): List<VirtualFile> {
        // If SORT_BY_FILE_DIRECTORY is a rule, handle it separately
        if (rules.contains(SortType.SORT_BY_FILE_DIRECTORY)) {
            // Remove SORT_BY_FILE_DIRECTORY from rules to avoid repetition
            val remainingRules = rules.filter { it != SortType.SORT_BY_FILE_DIRECTORY }

            // Group files by directory and sort within each group based on remaining rules
            return files.groupBy { it.parent.path }
                .flatMap { (directory, filesInDirectory) ->
                    sortItems(remainingRules, filesInDirectory)
                }
        }

        // Create a combined comparator for the rules
        val combinedComparator = rules.fold(null as Comparator<VirtualFile>?) { acc, rule ->
            acc?.thenComparing(getComparatorForRule(rule)) ?: getComparatorForRule(rule)
        } ?: return files  // If no rules or comparator is null, return the original list

        return files.sortedWith(combinedComparator)
    }

    private fun getComparatorForRule(rule: SortType): Comparator<VirtualFile> {
        return when (rule) {
            SortType.SORT_BY_ALPHABET -> Comparator { a, b -> a.name.compareTo(b.name) }
            SortType.SORT_BY_ALPHABET_REVERSED -> Comparator { a, b -> b.name.compareTo(a.name) }
            SortType.SORT_BY_LAST_MODIFIED -> Comparator { a, b -> a.timeStamp.compareTo(b.timeStamp) }
            SortType.SORT_BY_FILE_TYPE -> Comparator { a, b -> (a.extension ?: "").compareTo(b.extension ?: "") }
            SortType.SORT_BY_RECENT_OPENED -> {
                val historyFiles = EditorHistoryManager.getInstance(project).files
                val indexMap = historyFiles.withIndex().associate { it.value to it.index }
                Comparator<VirtualFile> { a, b -> (indexMap[a] ?: Int.MAX_VALUE).compareTo(indexMap[b] ?: Int.MAX_VALUE) }.reversed()
            }
            else -> Comparator { _, _ -> 0 }  // Default no-op comparator
        }
    }

    private fun closeFiles(fileEditorManager: FileEditorManager, files: List<VirtualFile>) {
        files.forEach {
            fileEditorManager.closeFile(it)
        }
    }

    private fun openFilesInOrder(fileEditorManager: FileEditorManager, files: List<VirtualFile>) {
        for (file in files) {
            fileEditorManager.openFile(file, true)
        }
    }

    override fun doCancelAction() {
        super.doCancelAction()
        close(OK_EXIT_CODE);
    }
}