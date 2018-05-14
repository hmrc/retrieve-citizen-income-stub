# retrieve-citizen-income-stub

[![Build Status](https://travis-ci.org/hmrc/retrieve-citizen-income-stub.svg)](https://travis-ci.org/hmrc/retrieve-citizen-income-stub) [ ![Download](https://api.bintray.com/packages/hmrc/releases/retrieve-citizen-income-stub/images/download.svg) ](https://bintray.com/hmrc/releases/retrieve-citizen-income-stub/_latestVersion)

Retrieve Citizen Income Stub Usage
--------------

To add an envelope to the `Retrieve Citizen Income` to return a successful response use the following `curl` request:

    curl -XPOST \
      -H "Content-type: application/json" \
      -d '{"increasesTax":{"incomes":{"taxCodeIncomes":{"employments":{"taxCodeIncomes":[{"taxCode":"K804"}]}}},"benefitsFromEmployment":{"iabdSummaries":[{"iabdType":31},{"iabdType":31},{"iabdType":31}]}}}' \
      http://localhost:9359/seed/individuals/AB216913B/income

To add an envelope to the `Retrieve Citizen Income` stub to return a failure response, use a `curl` request like the following:

    curl -XPOST \
      "http://localhost:9359/seed/individuals/AB216913B/income?status=503&description=Service+unavailable"

A call to the `Retrieve Citizen Income` stub endpoint will be made by the application in the following way:

    curl -v http://localhost:9359/individuals/AB216913B/income

> Note: the `Nino` (AB216913B) in the url have no effect on the response, it will always return the active profile.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")