package service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class OtpService {
	
	private static final long OTP_EXPIRE_MS = 2 * 60 * 1000; 	  // Thời hạn OTP (2 phút)
	private static final long RESEND_COOLDOWN_MS = 1 * 60 * 1000; // Thời gian chờ để gửi lại OTP (30 giây)
	
	// Lưu OTP tạm thời trong bộ nhớ (email -> OtpData)
	private Map<String, OtpData> otpStore = new ConcurrentHashMap<>(); 
	
	private static class OtpData{
		String otp;
		long expirationTime;
		long lastSentTime;
		
		public OtpData(String otp, long expirationTime, long lastSentTime) {
			this.otp = otp;
			this.expirationTime = expirationTime;
			this.lastSentTime = lastSentTime;
		}
	}
	
	public boolean verifyOtp(String email, String inputOtp) {

        OtpData data = otpStore.get(email);

        if (data == null) {
            return false;
        }

        long now = System.currentTimeMillis();

        // Hết hạn
        if (now > data.expirationTime) {
            otpStore.remove(email);
            return false;
        }

        // Đúng OTP
        if (data.otp.equals(inputOtp)) {
            otpStore.remove(email);
            return true;
        }

        return false;
    }
	
	private void sendEmail(String toEmail, String otp) throws Exception {
	    final String fromEmail = "nhommtvcn@gmail.com";      // email gửi
	    final String appPassword = "smpx cswm qigb ajlh";       // app password

	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.ssl.trust", "*");
	    Session session = Session.getInstance(props,
	        new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(fromEmail, appPassword);
	            }
	        });

	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(fromEmail));
	    message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(toEmail));
	    message.setSubject("Mã OTP xác nhận");

	    message.setText("Mã OTP của bạn là: " + otp + "\nHiệu lực trong 2 phút.");

	    Transport.send(message);
	}
	
	public void sendOtpWithValidation(String inputEmail, String expectedEmail) {

	    if (inputEmail == null || inputEmail.trim().isEmpty()) {
	        throw new IllegalArgumentException("Vui lòng nhập email.");
	    }

	    if (!inputEmail.equalsIgnoreCase(expectedEmail)) {
	        throw new IllegalArgumentException("Email không trùng với tài khoản.");
	    }

	    long now = System.currentTimeMillis();
	    OtpData existing = otpStore.get(inputEmail);

	    if (existing != null && (now - existing.lastSentTime) < RESEND_COOLDOWN_MS) {
	        long remain = (RESEND_COOLDOWN_MS - (now - existing.lastSentTime)) / 1000;
	        throw new IllegalArgumentException("Vui lòng đợi " + remain + "s trước khi gửi lại OTP.");
	    }

	    String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));

	    long expireTime = now + OTP_EXPIRE_MS;

	    otpStore.put(inputEmail, new OtpData(otp, expireTime, now));

	    try {
	        sendEmail(inputEmail, otp);
	        System.out.println("OTP gửi đến " + inputEmail + ": " + otp);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new RuntimeException("Không thể gửi email OTP. Vui lòng thử lại.");
	    }
	}
	
	
	
}
