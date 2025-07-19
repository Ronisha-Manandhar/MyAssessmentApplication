package com.example.myassssmentapplication

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myassssmentapplication.ViewModel.DashboardViewModel
import com.example.myassssmentapplication.data.model.DashboardResponse
import com.example.myassssmentapplication.data.network.ApiService
import io.mockk.*
import org.junit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel
    private val mockApi = mockk<ApiService>()
    private val mockCall = mockk<Call<DashboardResponse>>(relaxed = true)

    @Before
    fun setup() {
        //  Mock android.util.Log to prevent test crashes
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0

        viewModel = DashboardViewModel(mockApi)
    }

    @Test
    fun `fetchDashboardData updates entities LiveData on success`() {
        val fakeEntity = mapOf("name" to "Test")
        val fakeResponse = DashboardResponse(
            entities = listOf(fakeEntity),
            entityTotal = 1
        )

        val callbackSlot = slot<Callback<DashboardResponse>>()
        every { mockApi.getDashboardData(any()) } returns mockCall
        every { mockCall.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onResponse(mockCall, Response.success(fakeResponse))
        }

        val latch = CountDownLatch(1)
        val observer = Observer<List<Map<String, Any>>> {
            if (it == listOf(fakeEntity)) latch.countDown()
        }

        viewModel.entities.observeForever(observer)
        viewModel.fetchDashboardData("testkey")

        if (!latch.await(1, TimeUnit.SECONDS)) {
            Assert.fail("Observer did not receive expected entities")
        }

        viewModel.entities.removeObserver(observer)
    }

    @Test
    fun `fetchDashboardData updates error LiveData on failure`() {
        val callbackSlot = slot<Callback<DashboardResponse>>()
        val errorMessage = "Network error"

        every { mockApi.getDashboardData(any()) } returns mockCall
        every { mockCall.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onFailure(mockCall, Throwable(errorMessage))
        }

        val latch = CountDownLatch(1)
        val observer = Observer<String?> {
            if (it?.contains(errorMessage) == true) latch.countDown()
        }

        viewModel.error.observeForever(observer)
        viewModel.fetchDashboardData("testkey")

        if (!latch.await(1, TimeUnit.SECONDS)) {
            Assert.fail("Observer did not receive expected error message")
        }

        viewModel.error.removeObserver(observer)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }
}