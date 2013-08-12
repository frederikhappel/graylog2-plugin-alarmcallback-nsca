package com.solvians.nscaalarmcallback.callback;

import java.io.IOException;

import com.googlecode.jsendnsca.Level;
import com.googlecode.jsendnsca.MessagePayload;
import com.googlecode.jsendnsca.NagiosException;
import com.googlecode.jsendnsca.NagiosPassiveCheckSender;
import com.googlecode.jsendnsca.NagiosSettings;
import com.googlecode.jsendnsca.builders.MessagePayloadBuilder;
import com.googlecode.jsendnsca.builders.NagiosSettingsBuilder;
import com.googlecode.jsendnsca.encryption.Encryption;

/**
 * @author Frederik Happel <frederik.happel@solvians.com>
 */
public class NscaHost {
	private final NagiosSettings settings;
	private final NagiosPassiveCheckSender sender;
	private final String senderHostName;

	public NscaHost(String senderHostName, String host, int port,
			Encryption encryption) {
		this.settings = new NagiosSettingsBuilder()
				.withLargeMessageSupportEnabled().withNagiosHost(host)
				.withPort(port).withEncryption(encryption).create();
		this.senderHostName = senderHostName;
		this.sender = new NagiosPassiveCheckSender(settings);
	}

	public void send(Level level, String serviceName, String message)
			throws NagiosException, IOException {
		message = message.replace("\n", "\\n");
		MessagePayload payload = new MessagePayloadBuilder()
				.withHostname(senderHostName).withLevel(level)
				.withServiceName(serviceName).withMessage(message).create();

		sender.send(payload);
	}

	public String getAddress() {
		return settings.getNagiosHost() + ":" + settings.getPort();
	}
}
