package com.github.ttxd19.seatcattab

import com.github.ttxd19.seatcattab.common.SortLogic
import com.github.ttxd19.seatcattab.common.timestamp.FileTimestampProvider
import com.intellij.testFramework.FileEditorManagerTestCase
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class SortLogicTest: FileEditorManagerTestCase() {

    @Test
    fun `test sortByAlphabet`() {
        // Create or get mock files
        val fileA = createFile("path/to/apple.txt", "apple content".toByteArray())
        val fileB = createFile("path/to/banana.txt", "banana content".toByteArray())
        val fileC = createFile("path/to/cherry.txt", "cherry content".toByteArray())

        // Open the files in a random order
        manager?.openFile(fileC, true)
        manager?.openFile(fileA, true)
        manager?.openFile(fileB, true)

        // Apply sorting
        SortLogic.sortByAlphabet(project)

        // Verify order
        val expectedOrder = listOf(fileA, fileB, fileC)
        val actualOrder = manager?.openFiles?.toList() ?: emptyList()
        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `test sortByAlphabetReversed`() {
        // Create or get mock files
        val fileA = createFile("path/to/apple.txt", "apple content".toByteArray())
        val fileB = createFile("path/to/banana.txt", "banana content".toByteArray())
        val fileC = createFile("path/to/cherry.txt", "cherry content".toByteArray())

        // Open the files in a random order
        manager?.openFile(fileA, true)
        manager?.openFile(fileC, true)
        manager?.openFile(fileB, true)

        // Apply sorting
        SortLogic.sortByAlphabetReversed(project)

        // Verify order
        val expectedOrder = listOf(fileC, fileB, fileA)
        val actualOrder = manager?.openFiles?.toList() ?: emptyList()
        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `test sortByFileDirectory`() {
        // Note: Directory sorting will depend on the paths, adjust as needed.
        // For this example, 'cherry' has the 'deepest' directory.
        val fileA = createFile("path/apple.txt", "apple content".toByteArray())
        val fileB = createFile("path/to/banana.txt", "banana content".toByteArray())
        val fileC = createFile("path/to/dir/cherry.txt", "cherry content".toByteArray())

        // Open the files in a random order
        manager?.openFile(fileB, true)
        manager?.openFile(fileA, true)
        manager?.openFile(fileC, true)

        // Apply sorting
        SortLogic.sortByFileDirectory(project)

        // Verify order
        val expectedOrder = listOf(fileA, fileB, fileC)
        val actualOrder = manager?.openFiles?.toList() ?: emptyList()
        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `test sortByFileType`() {
        // Note: File type sorting will depend on extensions, adjust as needed.
        val fileA = createFile("path/apple.py", "apple content".toByteArray())  // Python
        val fileB = createFile("path/banana.txt", "banana content".toByteArray())  // Text
        val fileC = createFile("path/cherry.java", "cherry content".toByteArray())  // Java

        // Open the files in a random order
        manager?.openFile(fileB, true)
        manager?.openFile(fileC, true)
        manager?.openFile(fileA, true)

        // Apply sorting
        SortLogic.sortByFileType(project)

        // Verify order
        val expectedOrder = listOf(fileC, fileA, fileB)  // Java -> Python -> Text (example order)
        val actualOrder = manager?.openFiles?.toList() ?: emptyList()
        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `test sortByLastModified`() {
        val fileA = createFile("path/apple.txt", "apple content".toByteArray())
        val fileB = createFile("path/banana.txt", "banana content".toByteArray())
        val fileC = createFile("path/cherry.txt", "cherry content".toByteArray())

        // Open the files in a random order
        manager?.openFile(fileB, true)
        manager?.openFile(fileA, true)
        manager?.openFile(fileC, true)

        val mockedTimestampProvider = mockk<FileTimestampProvider> {
            every { getTimestamp(fileA) } returns 1
            every { getTimestamp(fileB) } returns 3
            every { getTimestamp(fileC) } returns 2
        }

        SortLogic.timestampProvider = mockedTimestampProvider

        // Apply sorting
        SortLogic.sortByLastModified(project)

        // Verify order
        val expectedOrder = listOf(fileA, fileC, fileB)
        val actualOrder = manager?.openFiles?.toList() ?: emptyList()
        assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun `test sortByRecentOpened`() {
        val fileA = createFile("path/apple.txt", "apple content".toByteArray())
        val fileB = createFile("path/banana.txt", "banana content".toByteArray())
        val fileC = createFile("path/cherry.txt", "cherry content".toByteArray())

        // Open the files in a random order
        manager?.openFile(fileC, true)
        manager?.openFile(fileA, true)
        manager?.openFile(fileB, true)

        // Apply sorting
        SortLogic.sortByRecentOpened(project)

        // Verify order
        val expectedOrder = listOf(fileB, fileA, fileC)  // Based on recent opened order
        val actualOrder = manager?.openFiles?.toList() ?: emptyList()
        assertEquals(expectedOrder, actualOrder)
    }

}