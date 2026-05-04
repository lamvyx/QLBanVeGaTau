package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class OtpEmailService {
	private static Map<String, String> dotEnvCache;

	private OtpEmailService() {
	}

	public static boolean isConfigured() {
		String username = readSetting("MAIL_USERNAME", "mail.username", "");
		String password = readSetting("MAIL_PASSWORD", "mail.password", "");
		return !username.isBlank() && !password.isBlank();
	}

	public static void sendOtp(String recipientEmail, String otpCode) throws Exception {
		try {
			String host = readSetting("MAIL_SMTP_HOST", "mail.smtp.host", "smtp.gmail.com");
			String port = readSetting("MAIL_SMTP_PORT", "mail.smtp.port", "587");
			String username = readSetting("MAIL_USERNAME", "mail.username", "");
			String password = readSetting("MAIL_PASSWORD", "mail.password", "");
			String from = readSetting("MAIL_FROM", "mail.from", username);
			String subject = readSetting("MAIL_OTP_SUBJECT", "mail.otp.subject", "Ma OTP dat lai mat khau");

			if (username.isBlank() || password.isBlank()) {
				throw new IllegalStateException("Chua cau hinh MAIL_USERNAME va MAIL_PASSWORD");
			}

			sendViaSmtp(host, Integer.parseInt(port), username, password, from, recipientEmail, subject, buildOtpBody(otpCode));
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg == null || msg.isBlank()) {
				msg = e.getClass().getSimpleName() + ": " + (e.getCause() != null ? e.getCause().getMessage() : "unknown error");
			}
			throw new Exception(msg, e);
		}
	}

	private static void sendViaSmtp(String host, int port, String username, String password,
			String from, String to, String subject, String body) throws Exception {
		Socket socket = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;

		try {
			if (port == 465) {
				socket = SSLSocketFactory.getDefault().createSocket(host, port);
			} else {
				socket = new Socket(host, port);
			}

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

			requireCode(readResponse(reader), "220");
			sendCommand(writer, "EHLO localhost");
			requireCode(readResponse(reader), "250");

			if (port != 465) {
				sendCommand(writer, "STARTTLS");
				requireCode(readResponse(reader), "220");

				SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket(socket, host, port, true);
				sslSocket.startHandshake();
				socket = sslSocket;

				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

				sendCommand(writer, "EHLO localhost");
				requireCode(readResponse(reader), "250");
			}

			sendCommand(writer, "AUTH LOGIN");
			requireCode(readResponse(reader), "334");
			sendCommand(writer, Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8)));
			requireCode(readResponse(reader), "334");
			sendCommand(writer, Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
			requireCode(readResponse(reader), "235");

			sendCommand(writer, "MAIL FROM:<" + from + ">");
			requireCode(readResponse(reader), "250");
			sendCommand(writer, "RCPT TO:<" + to + ">");
			requireCode(readResponse(reader), "250", "251");
			sendCommand(writer, "DATA");
			requireCode(readResponse(reader), "354");

			String encodedSubject = "=?UTF-8?B?" + Base64.getEncoder().encodeToString(subject.getBytes(StandardCharsets.UTF_8)) + "?=";
			String normalizedBody = body.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "\r\n");
			String data = "From: <" + from + ">\r\n"
					+ "To: <" + to + ">\r\n"
					+ "Subject: " + encodedSubject + "\r\n"
					+ "MIME-Version: 1.0\r\n"
					+ "Content-Type: text/plain; charset=UTF-8\r\n"
					+ "Content-Transfer-Encoding: 8bit\r\n"
					+ "\r\n"
					+ normalizedBody
					+ "\r\n.\r\n";

			writer.write(data);
			writer.flush();
			requireCode(readResponse(reader), "250");

			sendCommand(writer, "QUIT");
			readResponse(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
	}

	private static void sendCommand(BufferedWriter writer, String command) throws IOException {
		writer.write(command);
		writer.write("\r\n");
		writer.flush();
	}

	private static String readResponse(BufferedReader reader) throws IOException {
		String first = reader.readLine();
		if (first == null) {
			throw new IOException("SMTP server did not return a response");
		}

		StringBuilder response = new StringBuilder(first);
		if (first.length() >= 4 && first.charAt(3) == '-') {
			String code = first.substring(0, 3);
			String line;
			do {
				line = reader.readLine();
				if (line == null) {
					throw new IOException("SMTP server closed connection unexpectedly");
				}
				response.append("\n").append(line);
			} while (!line.startsWith(code + " "));
		}

		return response.toString();
	}

	private static void requireCode(String response, String... acceptedCodes) throws IOException {
		for (String code : acceptedCodes) {
			if (response.startsWith(code)) {
				return;
			}
		}
		throw new IOException(response);
	}

	private static String buildOtpBody(String otpCode) {
		return """
				Xin chào!

				Mã OTP của bạn là: %s
				Mã có hiệu lực trong phiên hiện tại.

				Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.
				""".formatted(otpCode);
	}

	private static String readSetting(String envKey, String propKey, String fallback) {
		String fromEnv = System.getenv(envKey);
		if (fromEnv != null && !fromEnv.isBlank()) {
			return fromEnv.trim();
		}

		String fromDotEnv = readFromDotEnv(envKey);
		if (fromDotEnv != null && !fromDotEnv.isBlank()) {
			return fromDotEnv.trim();
		}

		String fromProp = System.getProperty(propKey);
		if (fromProp != null && !fromProp.isBlank()) {
			return fromProp.trim();
		}

		return fallback;
	}

	private static String readFromDotEnv(String key) {
		Map<String, String> data = loadDotEnv();
		return data.get(key);
	}

	private static synchronized Map<String, String> loadDotEnv() {
		if (dotEnvCache != null) {
			return dotEnvCache;
		}

		Map<String, String> map = new HashMap<>();
		Path dotEnvPath = Path.of(".env");
		if (Files.exists(dotEnvPath)) {
			try {
				List<String> lines = Files.readAllLines(dotEnvPath);
				for (String raw : lines) {
					String line = raw.trim();
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}
					int idx = line.indexOf('=');
					if (idx <= 0) {
						continue;
					}
					String k = line.substring(0, idx).trim();
					String v = line.substring(idx + 1).trim();
					if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
						v = v.substring(1, v.length() - 1);
					}
					map.put(k, v);
				}
			} catch (IOException ignored) {
				// Keep empty map so app can still run with env vars or defaults.
			}
		}

		dotEnvCache = map;
		return dotEnvCache;
	}
}
