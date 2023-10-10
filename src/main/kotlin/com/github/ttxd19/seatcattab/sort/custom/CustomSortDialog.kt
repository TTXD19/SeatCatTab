package com.github.ttxd19.seatcattab.sort.custom

import com.github.ttxd19.seatcattab.guiform.TestSort
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class CustomSortDialog(project: Project): DialogWrapper(true) {

    private val customSort = TestSort()

    init {
        init()
        title = "My Custom Form"
    }

    override fun createCenterPanel(): JComponent? {
        return customSort.getPanel()
    }
}