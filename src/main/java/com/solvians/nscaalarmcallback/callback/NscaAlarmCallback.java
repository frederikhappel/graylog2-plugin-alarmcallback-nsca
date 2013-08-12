package com.solvians.nscaalarmcallback.callback;

import java.util.HashMap;
import java.util.Map;

import org.graylog2.plugin.alarms.Alarm;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;

/**
 * @author Frederik Happel <frederik.happel@solvians.com>
 */
public class NscaAlarmCallback implements AlarmCallback {
	public static final String NAME = "NSCA alarm callback";

	private String senderHostName;
	private String host;

	@Override
	public void initialize(Map<String, String> config)
			throws AlarmCallbackConfigurationException {
		if (config == null) {
			throw new AlarmCallbackConfigurationException(
					"Configuration not finished.");
		} else if (!config.containsKey("senderHostName")
				|| config.get("senderHostName").isEmpty()) {
			throw new AlarmCallbackConfigurationException(
					"Required config parameter senderHostName is missing.");
		} else if (!config.containsKey("host") || config.get("host").isEmpty()) {
			throw new AlarmCallbackConfigurationException(
					"Required config parameter host is missing.");
		}

		this.senderHostName = config.get("senderHostName");
		this.host = config.get("host");
	}

	@Override
	public void call(Alarm alarm) throws AlarmCallbackException {
		NscaTrigger trigger = new NscaTrigger(this.senderHostName, this.host);
		trigger.trigger(alarm);
	}

	@Override
	public Map<String, String> getRequestedConfiguration() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("senderHostName", "Nagios host object name");
		config.put("host", "NSCA host to submit alerts (<hostname>[:<port>])");
		return config;
	}

	@Override
	public String getName() {
		return NAME;
	}
}
