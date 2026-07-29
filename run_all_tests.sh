#!/usr/bin/env bash

sbt clean coverage compile test coverageOff coverageReport
