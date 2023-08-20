package com.integraboost

import org.junit.jupiter.api.Test
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.result
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions

class ArrowTest {

    private val validAccountState = AccountType.AccountTypeOne
    private val invalidAccountState = AccountType.AccountTypeTwo

    private val unsupportedFeatureOccupation = Occupation.NonDeveloper
    private val supportedFeatureOccupation = Occupation.Developer

    private val accountTypeOneOnlyValidation = AccountTypeOneOnlyValidation()
    private val developerOnlyValidation = DeveloperOnlyValidation()
    private val aggregator = ValidationResultAggregator(accountTypeOneOnlyValidation, developerOnlyValidation)

    private val nonDeveloperMessage =  "You are not a developer"

    @Test
    fun `should have only account state validation failure`() = runBlocking {
        val accountStateValidationRequest = RequestContext("AccountStateOnlyValidation", 1, RequestContextAccount(invalidAccountState, supportedFeatureOccupation))
        val result = aggregator.aggregate(accountStateValidationRequest)

        val expectedValidationFailures = 1
        val expectedValidationFailure = ValidationResult.Failure.AccountState.AccountTypeTwoInvalid

        Assertions.assertEquals(expectedValidationFailures, result.size)
        Assertions.assertEquals(expectedValidationFailure, result.first())
    }

    @Test
    fun `should have only feature unsupported validation failure`() = runBlocking {
        val featureUnsupportedValidationRequest = RequestContext("FeatureUnsupportedOnlyValidation", 2, RequestContextAccount(validAccountState, unsupportedFeatureOccupation))
        val result = aggregator.aggregate(featureUnsupportedValidationRequest)

        val expectedValidationFailures = 1
        val expectedValidationFailure = ValidationResult.Failure.FeatureUnsupported.NonDeveloper(nonDeveloperMessage)

        Assertions.assertEquals(expectedValidationFailures, result.size)
        Assertions.assertEquals(expectedValidationFailure, result.first())
    }

    @Test
    fun `should have both feature unsupported AND account state validation failures`() = runBlocking {
        val request = RequestContext("IncludesBothValidationFailureTypes", 1, RequestContextAccount(invalidAccountState, unsupportedFeatureOccupation))
        val result = aggregator.aggregate(request)

        val expectedValidationFailures = 2
        val expectedAccountStateValidationFailure = ValidationResult.Failure.AccountState.AccountTypeTwoInvalid
        val expectedFeatureUnsupportedValidationFailure = ValidationResult.Failure.FeatureUnsupported.NonDeveloper(nonDeveloperMessage)

        Assertions.assertEquals(expectedValidationFailures, result.size)
        Assertions.assertEquals(expectedAccountStateValidationFailure, result.first())
        Assertions.assertEquals(expectedFeatureUnsupportedValidationFailure, result.last())
    }


    @Test
    fun `should not have any validation failures`() = runBlocking {
        val successValidationRequest = RequestContext("HasNoFailedValidations", 3, RequestContextAccount(validAccountState, supportedFeatureOccupation))
        val result = aggregator.aggregate(successValidationRequest)

        val expectedValidationFailures = 0
        Assertions.assertEquals(expectedValidationFailures, result.size)
    }

    @Test
    fun `should be able to filter by failures`() {
        val validationResults = listOf<ValidationResult>(
            ValidationResult.Failure.AccountState.AccountTypeTwoInvalid,
            ValidationResult.Failure.AccountState.AnotherAccountStateFailure,

            ValidationResult.Failure.FeatureUnsupported.NonDeveloper(nonDeveloperMessage),
            ValidationResult.Failure.FeatureUnsupported.AnotherUnsupportedFailure,
            ValidationResult.Failure.FeatureUnsupported.AnotherUnsupportedTwoFailure,
        )

        val accountStateValidationResults = validationResults.filterIsInstance<ValidationResult.Failure.AccountState>()
        val featureUnsupportedValidationResults = validationResults.filterIsInstance<ValidationResult.Failure.FeatureUnsupported>()

        val expectedAccountStateValidationResults = 2
        val expectedFeatureUnsupportedValidationResults = 3

        Assertions.assertEquals(expectedAccountStateValidationResults, accountStateValidationResults.size)
        Assertions.assertEquals(expectedFeatureUnsupportedValidationResults, featureUnsupportedValidationResults.size)
    }

    // in this specific case we do not need to handle deserialization (not sure if that influences anything)
    @Test
    fun `should be able to serialize with expected info on each failure`() {

        val mapper = ObjectMapper()

        val validationResults = listOf<ValidationResult>(
            ValidationResult.Failure.AccountState.AccountTypeTwoInvalid,
            ValidationResult.Failure.AccountState.AnotherAccountStateFailure,

            ValidationResult.Failure.FeatureUnsupported.NonDeveloper(nonDeveloperMessage),
            ValidationResult.Failure.FeatureUnsupported.AnotherUnsupportedFailure,
            ValidationResult.Failure.FeatureUnsupported.AnotherUnsupportedTwoFailure,
        )

        val serialized = mapper.writeValueAsString(validationResults)
        val expected = "[" +
                "{\"type\":\"AccountState\",\"subType\":\"AccountTypeTwoInvalid\"}," +
                "{\"type\":\"AccountState\",\"subType\":\"AnotherAccountStateFailure\"}," +
                "{\"type\":\"FeatureUnsupported\",\"subType\":\"NonDeveloper\",\"details\":\"You are not a developer\"}," +
                "{\"type\":\"FeatureUnsupported\",\"subType\":\"AnotherUnsupportedFailure\"}," +
                "{\"type\":\"FeatureUnsupported\",\"subType\":\"AnotherUnsupportedTwoFailure\"}" +
                "]"
        Assertions.assertEquals(expected, serialized)
    }
}


sealed class ValidationResult {
    sealed class Failure(val type: ValidationFailureType): ValidationResult() {
        sealed class AccountState(val subType: AccountStateFailureType): Failure(ValidationFailureType.AccountState) {
            object AccountTypeTwoInvalid: AccountState(AccountStateFailureType.AccountTypeTwoInvalid)
            object AnotherAccountStateFailure: AccountState(AccountStateFailureType.AnotherAccountStateFailure)
        }

        sealed class FeatureUnsupported(val subType: FeatureUnsupportedFailureType): Failure(ValidationFailureType.FeatureUnsupported) {
            // each of these may have their own properties (which could be basically be anything). below is a simple example of 'details' property
            data class NonDeveloper<T>(val details: T): FeatureUnsupported(FeatureUnsupportedFailureType.NonDeveloper)
            object AnotherUnsupportedFailure: FeatureUnsupported(FeatureUnsupportedFailureType.AnotherUnsupportedFailure)
            object AnotherUnsupportedTwoFailure: FeatureUnsupported(FeatureUnsupportedFailureType.AnotherUnsupportedTwoFailure)
        }
    }

    object Success: ValidationResult()
}

enum class ValidationFailureType {
    AccountState,
    FeatureUnsupported,

    // in future might add things like below. this will stop rest of validations being run etc.
    // PreliminaryCheck,
}

enum class AccountStateFailureType {
    AccountTypeTwoInvalid,
    AnotherAccountStateFailure
}

enum class FeatureUnsupportedFailureType {
    NonDeveloper,
    AnotherUnsupportedFailure,
    AnotherUnsupportedTwoFailure
}

enum class AccountType {
    AccountTypeOne,
    AccountTypeTwo
}

enum class Occupation {
    Developer,
    NonDeveloper
}

data class RequestContextAccount(
    val type: AccountType,
    val occupation: Occupation
)

data class RequestContext(
    val name: String,
    val id: Int,
    val account: RequestContextAccount
)

interface ValidationStrategy {
    suspend fun validate(requestContext: RequestContext): ValidationResult

    suspend fun validateCondition(
        condition: Boolean,
        failureResult: ValidationResult.Failure
    ): ValidationResult {
        
        val validation = either {
            ensure (condition) { failureResult }
            ValidationResult.Success
        }

        return when(validation) {
            is Either.Left -> {
                failureResult
            }
            is Either.Right -> {
                ValidationResult.Success
            }
        }
    }
}

class ValidationResultAggregator(private vararg val strategies: ValidationStrategy) {
    suspend fun aggregate(requestContext: RequestContext): List<ValidationResult> {

        val results = mutableListOf<ValidationResult>()

        for (strategy in strategies) {
            val validationResult = strategy.validate(requestContext)

            if (validationResult is ValidationResult.Failure) {
                results.add(validationResult)
            }
        }

        return results
    }
}

class AccountTypeOneOnlyValidation: ValidationStrategy {
    override suspend fun validate(
        requestContext: RequestContext,
    ): ValidationResult {
        val isAccountTypeSupported = requestContext.account.type == AccountType.AccountTypeOne
        val failureResult = ValidationResult.Failure.AccountState.AccountTypeTwoInvalid

        return validateCondition(isAccountTypeSupported, failureResult)
    }
}

class DeveloperOnlyValidation: ValidationStrategy {
    override suspend fun validate(
        requestContext: RequestContext,
    ): ValidationResult {
        val isDeveloper = requestContext.account.occupation == Occupation.Developer
        val failureMessage = "You are not a developer"
        val failureResult = ValidationResult.Failure.FeatureUnsupported.NonDeveloper(failureMessage)

        return validateCondition(isDeveloper, failureResult)
    }
}


