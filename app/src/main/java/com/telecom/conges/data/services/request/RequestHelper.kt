package com.telecom.conges.data.services.request

import android.util.Log
import com.telecom.conges.data.Result
import com.telecom.conges.data.models.Request
import com.telecom.conges.data.models.RequestRequest
import com.telecom.conges.data.models.RequestResponse
import com.telecom.conges.data.models.SoldeRequest.SoldeRequest
import com.telecom.conges.extensions.safeApiCall
import java.io.IOException

public class RequestHelper(
    private val service: RequestService
) {
    suspend fun createRequest(request: RequestRequest) = safeApiCall(
        call = { launchCreateRequest(request) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchCreateRequest(request: RequestRequest): Result<Request> {

        val response = service.createRequest(request).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        Log.v("MESsAge", response.errorBody()?.toString())
        Log.v("MESsAge", response.body().toString())
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

    suspend fun getAllRequest() = safeApiCall(
        call = { launchGetAllRequest() },
        errorMessage = "Error logging in"
    )

    private suspend fun launchGetAllRequest(): Result<List<Request>> {
        val response = service.getAllRequest().await()

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

    suspend fun modifySolde(id: String, soldeRequest: SoldeRequest) = safeApiCall(
        call = { launchModifySolde(id, soldeRequest) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchModifySolde(id: String, soldeRequest: SoldeRequest): Result<String> {
        val response = service.modifySolde(id, soldeRequest).await()

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

    suspend fun acceptRequest(id: String) = safeApiCall(
        call = { launchAcceptRequest(id) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchAcceptRequest(id: String): Result<String> {

        val response = service.acceptRequest(id).await()

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


    suspend fun refuseRequest(id: String) = safeApiCall(
        call = { launchRefuseRequest(id) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchRefuseRequest(id: String): Result<String> {
        val response = service.refuseRequest(id).await()

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