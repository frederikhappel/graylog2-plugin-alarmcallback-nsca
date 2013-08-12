graylog2-plugin-alarmcallback-nsca
==================================

Nagios NSCA Alarm Plugin for Graylog2

Alarm callback that is submitting passive check results to an NSCA service.

Configuration needed:
- the Nagios host object for which the check is submitted
- the host:port combination where to submit the checks - may be a comma separated list of multiple hosts

The plugin will submit a result for the service "Stream: streamName" on configured host.


Limitations:
- currently no authentication is supported
- only XOR encryption is supported

