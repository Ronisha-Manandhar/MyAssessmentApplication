package com.example.myassssmentapplication

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myassssmentapplication.adapter.EntityAdapter
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EntityAdapterTest {

    @Test
    fun `onBindViewHolderdisplayscorrectsummary`() {
        val data = listOf(mapOf("name" to "Ronisha", "id" to "007"))
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // Create adapter with test data
        val adapter = EntityAdapter(data) { /* no-op click */ }

        // Inflate the view manually
        val parent = FrameLayout(context)
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.item_entity, parent, false)

        // Create ViewHolder and bind data
        val viewHolder = adapter.onCreateViewHolder(parent, 0)
        adapter.onBindViewHolder(viewHolder, 0)

        val expected = "Ronisha - 007"
        val actual = viewHolder.summaryTextView.text.toString()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getItemCountreturnscorrectcount`() {
        val testData = listOf(
            mapOf("name" to "Item 1"),
            mapOf("name" to "Item 2"),
            mapOf("name" to "Item 3")
        )

        val adapter = EntityAdapter(testData) { /* no-op click */ }
        Assert.assertEquals(3, adapter.itemCount)
    }
}