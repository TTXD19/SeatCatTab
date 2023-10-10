package com.github.ttxd19.seatcattab.sort.custom

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class SortCustomAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val dialog = CustomSortDialog(project)
        dialog.show()
    }
}