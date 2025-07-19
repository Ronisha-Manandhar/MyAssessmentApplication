package com.example.myassssmentapplication

import com.example.myassssmentapplication.data.model.DashboardResponse
import com.example.myassssmentapplication.data.network.ApiService
import io.mockk.*
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiServiceTest {

    private val mockApi = mockk<ApiService>()
    private val mockCall = mockk<Call<DashboardResponse>>(relaxed = true)

    @Test
    fun `getDashboardData returns successful response`() {
        val fakeResponse = DashboardResponse(
            entities = listOf(mapOf("name" to "Test Entity")),
            entityTotal = 1
        )

        //  Mock API to return mocked Call object
        every { mockApi.getDashboardData("testkey") } returns mockCall

        // Capture the callback and trigger onResponse with fake data
        val callbackSlot = slot<Callback<DashboardResponse>>()
        every { mockCall.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onResponse(mockCall, Response.success(fakeResponse))
        }

        // Execute API call and verify response
        mockApi.getDashboardData("testkey").enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                assertTrue(response.isSuccessful)
                assertEquals(fakeResponse, response.body())
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                fail("API call failed unexpectedly")
            }
        })

        // Verify mocked methods called
        verify { mockApi.getDashboardData("testkey") }
        verify { mockCall.enqueue(any()) }
    }
}
