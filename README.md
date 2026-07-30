# retrieve-citizen-income-stub

## Description
This implements stubs for API#1394. Details of the API can be found on Integration Hub.

## Stub details

The stub will validate the data passed (see conf/schemas/des-request-schema-v1.json) and if valid the NINO will be used to produce a response.

## Supported NINOs

The following NINOs will return the following data (see conf/resources for the json). Anything else returns NOT_FOUND.

    "AA111111A" -> Ok(successMatchOneElement),
    "AA222222A" -> Ok(successMatchTwoElements),
    "AA333333A" -> Ok(successMatchTwoTaxYears),
    "AA444444A" -> Ok(successNoMatch),
    "AA555555A" -> NotFound(errorNotFound),
    "AA777777A" -> InternalServerError(serverError),
    "AA888888A" -> InternalServerError(serviceUnavailable),
    "AA777771A" -> Ok(singleEmpSingleTaxYear),
    "AA777772A" -> Ok(multipleEmpSingleTaxYear),
    "AA777773A" -> Ok(multipleEmpMultipleTaxYears),
    "AA777773 " -> Ok(multipleEmpMultipleTaxYears), // Temp fix for space in Nino Scenarios
    "AA777774A" -> Ok(multipleEmpMultipleTaxYearsOp),
    "AA777775A" -> Ok(multipleEmpMultipleTaxYearsYdr),
    "AA777776A" -> Ok(validNinoWithNoData),
    "AA888881A" -> Ok(successLimitedFields),
    "AB123456C" -> Ok(twoCharNiWithData)

## Licence

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
