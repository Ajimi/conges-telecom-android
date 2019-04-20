package com.telecom.conges.data.services.request

import com.telecom.conges.data.Result
import com.telecom.conges.data.models.Request
import com.telecom.conges.data.models.RequestRequest
import com.telecom.conges.data.models.RequestResponse
import com.telecom.conges.extensions.safeApiCall
import java.io.IOException

public class RequestHelper(
    private val service: RequestService
) {
    suspend fun createRequest(request: RequestRequest) = safeApiCall(
        call = { launchCreateRequest(request) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchCreateRequest(request: RequestRequest): Result<String> {

        val response = service.createRequest(request).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success("Created")
            }
        }

        return Result.Error(
            IOException("Error creating request ${response.code()}")
        )
    }


    suspend fun getRequestsByType(id: String, type: String) = safeApiCall(
        call = { launchGetRequestByType(id, type) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchGetRequestByType(id: String, type: String): Result<List<RequestResponse>> {

        val response = service.getAllRequestByTypes(id, type).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(
            IOException("Error creating request ${response.code()}")
        )
    }

    suspend fun getRequest(id: String) = safeApiCall(
        call = { launchGetRequest(id) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchGetRequest(id: String): Result<Request> {
        val response = service.getRequest(id).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(
            IOException("Error creating request ${response.code()}")
        )
    }

}