package com.solvians.nscaalarmcallback.callback;

import org.graylog2.plugin.alarms.Alarm;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;

import com.googlecode.jsendnsca.Level;
import com.googlecode.jsendnsca.encryption.Encryption;

/**
 * @author Frederik Happel <frederik.happel@solvians.com>
 */
public class NscaTrigger {
	private final String senderHostName;
	private final String host;

	public NscaTrigger(String senderHostName, String host) {
		this.senderHostName = senderHostName;
		this.host = host;
	}

	public void trigger(Alarm alarm) throws AlarmCallbackException {
		String[] nsca_hosts = host.split(",");
		int limit = alarm.getStream().getAlarmMessageLimit();
		int current = alarm.getMessageCount();
		Level level = limit * 2 > current ? Level.WARNING : Level.CRITICAL;
		for (String nsca_host : nsca_hosts) {
			String[] nsca_host_parts = nsca_host.split(":");
			NscaHost nscaHost = new NscaHost(this.senderHostName,
					nsca_host_parts[0],
					nsca_host_parts.length > 1 ? Integer
							.parseInt(nsca_host_parts[0]) : 5667,
					Encryption.XOR);
			try {
				nscaHost.send(level, "Stream: " + alarm.getStream().getTitle(),
						alarm.getDescription());
			} catch (Exception e) {
				throw new AlarmCallbackException("Could not submit alert to "
						+ nsca_host + ". IOException");
			}
		}
	}
}