package com.github.ttxd19.seatcattab.guiform

import com.github.ttxd19.seatcattab.common.SortType
import javax.swing.JComboBox
import javax.swing.JPanel


class TestSort {

    private var rootPanel: JPanel? = null
    private var comboBox1: JComboBox<String>? = null
    private var comboBox2: JComboBox<String>? = null

    init {
        initSortSelection()
    }

    private fun initSortSelection() {
        val sortList = SortType.values().toList()
        for (item in sortList) {
            comboBox1?.addItem(item.sortType)
            comboBox2?.addItem(item.sortType)
        }
    }

    fun getPanel(): JPanel? {
        return rootPanel
    }

    fun getSortRules(): List<SortType> {

        val sortRules = mutableSetOf<SortType>()

        val sortRule1 = comboBox1?.selectedItem as? String
        val sortType1 = SortType.values().find { it.sortType == sortRule1 }

        if (sortType1 != null) {
            sortRules.add(sortType1)
        }

        val sortRule2 = comboBox2?.selectedItem as? String
        val sortType2 = SortType.values().find { it.sortType == sortRule2 }

        if (sortType2 != null) {
            sortRules.add(sortType2)
        }


        return sortRules.toList()
    }

}