# retrieve-citizen-income-stub

[![Build Status](https://travis-ci.org/hmrc/retrieve-citizen-income-stub.svg)](https://travis-ci.org/hmrc/retrieve-citizen-income-stub) [ ![Download](https://api.bintray.com/packages/hmrc/releases/retrieve-citizen-income-stub/images/download.svg) ](https://bintray.com/hmrc/releases/retrieve-citizen-income-stub/_latestVersion)

# retrieve-citizen-income-stub

[![Build Status](https://travis-ci.org/hmrc/retrieve-citizen-income-stub.svg)](https://travis-ci.org/hmrc/retrieve-citizen-income-stub) [ ![Download](https://api.bintray.com/packages/hmrc/releases/retrieve-citizen-income-stub/images/download.svg) ](https://bintray.com/hmrc/releases/retrieve-citizen-income-stub/_latestVersion)

Retrieve Citizen Income Stub Usage
--------------

To add an envelope to the `Retrieve Citizen Income stub` to return a successful response use the following `curl` request:

    curl -XPOST \
      -H "Content-type: application/json" \
      -d '{"matchPattern":63,"taxYears":[{"taxYear":"16-17","taxYearIndicator":"P","hmrcOfficeNumber":"099","employerPayeRef":"A1B2c3d4e5","employerName1":"Bugs Bunny","nationalInsuranceNumber":"AB123456C","surname":"Disney","gender":"M","uniqueEmploymentSequenceNumber":9999,"taxablePayInPeriod":999999.99,"taxDeductedOrRefunded":-12345.67,"grossEarningsForNICs":888888.66,"taxablePayToDate":999999.99,"totalTaxToDate":654321.08,"numberOfNormalHoursWorked":"E","payFrequency":"M1","paymentDate":"2017-02-03","earningsPeriodsCovered":11,"uniquePaymentId":666666,"paymentConfidenceStatus":"1","taxCode":"11100L","trivialCommutationPaymentTypeA":99998,"hmrcReceiptTimestamp":"2018-04-16T09:23:55Z","rtiReceivedDate":"2018-04-16","apiAvailableTimestamp":"2018-04-16T09:23:55Z"},{"taxYear":"15-16","taxYearIndicator":"P","hmrcOfficeNumber":"099","employerPayeRef":"A1B2c3d4e5","employerName1":"Donald Duck","nationalInsuranceNumber":"AB123456C","surname":"Disney","gender":"M","uniqueEmploymentSequenceNumber":6666,"taxablePayInPeriod":666666.66,"taxDeductedOrRefunded":12345.67,"grossEarningsForNICs":777777.66,"taxablePayToDate":999999.99,"totalTaxToDate":43210,"numberOfNormalHoursWorked":"E","payFrequency":"M3","paymentDate":"2017-02-03","earningsPeriodsCovered":12,"uniquePaymentId":654321,"paymentConfidenceStatus":"2","taxCode":"K15432","trivialCommutationPaymentTypeB":-99998,"hmrcReceiptTimestamp":"2018-04-16T10:34:55Z","rtiReceivedDate":"2018-04-16","apiAvailableTimestamp":"2018-04-16T09:23:55Z"}]}' \
      http://localhost:9359/seed/individuals/income

To add an envelope to the `Retrieve Citizen Income` stub to return a failure response, use a `curl` request like the following:

    curl -XPOST \
      "http://localhost:9359/seed/individuals/income?status=503&description=Service+unavailable"

A call to the `Retrieve Citizen Income` stub endpoint will be made by the application in the following way:

    curl -v http://localhost:9359/individuals/AB216913B/income

> Note: the `Nino` (AB216913B) has no effect on the response, it will always return the active profile.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")