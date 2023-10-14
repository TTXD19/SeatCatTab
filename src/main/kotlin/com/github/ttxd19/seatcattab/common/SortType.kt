package com.github.ttxd19.seatcattab.common

enum class SortType(val sortType: String) {
    EMPTY("EMPTY"),
    SORT_BY_ALPHABET("Alphabetically (A-Z)"),
    SORT_BY_ALPHABET_REVERSED("Alphabetically (Z-A)"),
    SORT_BY_FILE_DIRECTORY("By File Directory"),
    SORT_BY_LAST_MODIFIED("By Last Modified Date"),
    SORT_BY_FILE_TYPE("By File Type"),
    SORT_BY_RECENT_OPENED("Recently Opened")
}