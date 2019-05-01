package com.telecom.conges.data.services.daysoff

import com.telecom.conges.data.Result
import com.telecom.conges.data.models.DaysOff
import com.telecom.conges.extensions.safeApiCall
import java.io.IOException

public class DaysOffHelper(
    private val service: DaysOffService
) {
    suspend fun createDaysOff(daysOff: DaysOff) = safeApiCall(
        call = { launchCreateDaysOff(daysOff) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchCreateDaysOff(daysOff: DaysOff): Result<DaysOff> {

        val response = service.createDaysOff(daysOff).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(
            IOException("Error creating daysOff ${response.code()}")
        )
    }


    suspend fun getAllDaysOff() = safeApiCall(
        call = { launchGetAllDaysOff() },
        errorMessage = "Error logging in"
    )

    private suspend fun launchGetAllDaysOff(): Result<List<DaysOff>> {

        val response = service.getAllDaysOff().await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(
            IOException("Error retrieving days Off ${response.code()}")
        )
    }

    suspend fun deleteDaysOff(id: String) = safeApiCall(
        call = { launchDeleteDaysOff(id) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchDeleteDaysOff(id: String): Result<String> {

        val response = service.deleteDaysOff(id).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(
            IOException("Error retrieving days Off ${response.code()}")
        )
    }

    suspend fun editDaysOff(id: String) = safeApiCall(
        call = { launchEditDaysOff(id) },
        errorMessage = "Error logging in"
    )

    private suspend fun launchEditDaysOff(id: String): Result<String> {

        val response = service.editDaysOff(id).await()

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(
            IOException("Error retrieving days Off ${response.code()}")
        )
    }

}