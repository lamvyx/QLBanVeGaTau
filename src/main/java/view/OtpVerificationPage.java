package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import service.OtpEmailService;

public class OtpVerificationPage extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final Color NEUTRAL_BG = Color.decode("#F0F5F9");
	private static final int OTP_LENGTH = 6;
	private static final int OTP_EXPIRE_SECONDS = 180;
	private static final int RESEND_COOLDOWN_SECONDS = 60;
	private static final int MAX_FAILED_ATTEMPTS = 5;

	private final String expectedEmail;
	private final JTextField txtEmail = new JTextField(22);
	private final JTextField[] otpFields = new JTextField[OTP_LENGTH];
	private final Consumer<Boolean> onClose;
	private final JLabel lblHint = new JLabel(" ");
	private JButton btnSend;
	private Timer countdownTimer;
	private String currentOtp;
	private long otpExpiresAtMillis;
	private long resendAvailableAtMillis;
	private int failedAttempts;

	public OtpVerificationPage(JFrame parent, String expectedEmail, Consumer<Boolean> onClose) {
		super(parent, "Quên mật khẩu", true);
		this.expectedEmail = expectedEmail;
		this.onClose = onClose;
		setSize(560, 460);
		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setContentPane(createContent());
		initState();
	}

	private JPanel createContent() {
		JPanel root = new JPanel(new GridBagLayout());
		root.setBackground(NEUTRAL_BG);
		root.setBorder(new EmptyBorder(18, 20, 18, 20));

		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(Color.WHITE);
		card.setBorder(AppTheme.cardBorder());
		card.setPreferredSize(new Dimension(500, 370));

		JLabel title = new JLabel("Quên mật khẩu", SwingConstants.CENTER);
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setAlignmentX(CENTER_ALIGNMENT);

		JLabel subtitle = new JLabel("Nhập email để nhận mã OTP xác nhận", SwingConstants.CENTER);
		subtitle.setFont(AppTheme.font(Font.PLAIN, 13));
		subtitle.setForeground(new Color(98, 116, 142));
		subtitle.setAlignmentX(CENTER_ALIGNMENT);

		card.add(title);
		card.add(Box.createVerticalStrut(6));
		card.add(subtitle);
		card.add(Box.createVerticalStrut(18));

		JLabel lblEmail = new JLabel("Địa chỉ Email");
		lblEmail.setFont(AppTheme.font(Font.BOLD, 14));
		card.add(lblEmail);
		card.add(Box.createVerticalStrut(8));

		JPanel emailRow = new JPanel(new BorderLayout(8, 0));
		emailRow.setOpaque(false);
		emailRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		txtEmail.setFont(AppTheme.font(Font.PLAIN, 13));
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(186, 199, 214)),
				new EmptyBorder(8, 10, 8, 10)));
		emailRow.add(txtEmail, BorderLayout.CENTER);

		btnSend = new JButton("Gửi OTP");
		AppTheme.stylePrimaryButton(btnSend);
		btnSend.setPreferredSize(new Dimension(110, 40));
		btnSend.addActionListener(e -> sendOtp());
		emailRow.add(btnSend, BorderLayout.EAST);
		card.add(emailRow);

		card.add(Box.createVerticalStrut(6));
		lblHint.setFont(AppTheme.font(Font.PLAIN, 12));
		lblHint.setForeground(new Color(98, 116, 142));
		card.add(lblHint);

		card.add(Box.createVerticalStrut(18));
		JLabel lblOtp = new JLabel("Mã OTP (6 số)");
		lblOtp.setFont(AppTheme.font(Font.BOLD, 14));
		card.add(lblOtp);
		card.add(Box.createVerticalStrut(8));

		JPanel otpRow = new JPanel(new GridLayout(1, 6, 10, 0));
		otpRow.setOpaque(false);
		otpRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
		for (int i = 0; i < otpFields.length; i++) {
			otpFields[i] = buildOtpField(i);
			otpRow.add(otpFields[i]);
		}
		card.add(otpRow);

		card.add(Box.createVerticalStrut(18));
		JButton btnConfirm = new JButton("Xác nhận OTP");
		AppTheme.stylePrimaryButton(btnConfirm);
		btnConfirm.setFont(AppTheme.font(Font.BOLD, 15));
		btnConfirm.setPreferredSize(new Dimension(0, 42));
		btnConfirm.addActionListener(e -> confirmOtp());
		card.add(wrapFullWidth(btnConfirm));

		card.add(Box.createVerticalStrut(10));
		JButton btnBack = new JButton("← Quay về đăng nhập");
		AppTheme.styleSecondaryButton(btnBack);
		btnBack.setFont(AppTheme.font(Font.BOLD, 14));
		btnBack.setPreferredSize(new Dimension(0, 40));
		btnBack.addActionListener(e -> {
			dispose();
			if (onClose != null) {
				onClose.accept(false);
			}
		});
		card.add(wrapFullWidth(btnBack));

		root.add(card);
		return root;
	}

	@Override
	public void dispose() {
		if (countdownTimer != null) {
			countdownTimer.stop();
		}
		super.dispose();
	}

	private JPanel wrapFullWidth(JButton button) {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setOpaque(false);
		wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
		wrap.add(button, BorderLayout.CENTER);
		return wrap;
	}

	private JTextField buildOtpField(int index) {
		JTextField field = new JTextField();
		field.setHorizontalAlignment(SwingConstants.CENTER);
		field.setFont(AppTheme.font(Font.BOLD, 22));
		field.setPreferredSize(new Dimension(52, 52));
		field.setBorder(BorderFactory.createLineBorder(new Color(196, 206, 218)));
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = field.getText().replaceAll("\\D", "");
				field.setText(text.length() > 1 ? text.substring(0, 1) : text);
				if (!field.getText().isEmpty() && index < otpFields.length - 1) {
					otpFields[index + 1].requestFocus();
				}
			}
		});
		return field;
	}

	private void sendOtp() {
		String inputEmail = txtEmail.getText().trim();
		if (inputEmail.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập email.");
			return;
		}
		if (!inputEmail.equalsIgnoreCase(expectedEmail)) {
			JOptionPane.showMessageDialog(this, "Email không trùng với tài khoản.");
			return;
		}
		if (System.currentTimeMillis() < resendAvailableAtMillis) {
			JOptionPane.showMessageDialog(this, "Vui lòng chờ trước khi gửi lại OTP.");
			return;
		}
		if (!OtpEmailService.isConfigured()) {
			JOptionPane.showMessageDialog(this,
				"Chưa cấu hình SMTP. Vui lòng thiết lập MAIL_USERNAME và MAIL_PASSWORD trước khi gửi OTP.");
			return;
		}

		currentOtp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
		try {
			OtpEmailService.sendOtp(inputEmail, currentOtp);
			failedAttempts = 0;
			otpExpiresAtMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(OTP_EXPIRE_SECONDS);
			resendAvailableAtMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(RESEND_COOLDOWN_SECONDS);
			clearOtpFields();
			startCountdown();
			updateHintText();
			JOptionPane.showMessageDialog(this, "OTP đã được gửi tới email của bạn.");
		} catch (Exception ex) {
			currentOtp = null;
			otpExpiresAtMillis = 0;
			ex.printStackTrace();
			String errorMsg = ex.getMessage();
			if (errorMsg == null || errorMsg.isBlank()) {
				errorMsg = ex.getClass().getSimpleName();
				if (ex.getCause() != null && ex.getCause().getMessage() != null) {
					errorMsg += ": " + ex.getCause().getMessage();
				}
			}
			JOptionPane.showMessageDialog(this, "Gửi OTP thất bại: " + errorMsg);
		}
	}

	private void confirmOtp() {
		if (currentOtp == null) {
			JOptionPane.showMessageDialog(this, "Hãy gửi OTP trước.");
			return;
		}
		if (System.currentTimeMillis() > otpExpiresAtMillis) {
			invalidateOtp("OTP đã hết hạn. Vui lòng gửi lại mã mới.");
			return;
		}
		if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
			JOptionPane.showMessageDialog(this, "Bạn đã nhập sai quá số lần cho phép. Vui lòng gửi lại OTP.");
			return;
		}

		StringBuilder otpInput = new StringBuilder();
		for (JTextField field : otpFields) {
			otpInput.append(field.getText().trim());
		}
		if (otpInput.length() != OTP_LENGTH) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ 6 số OTP.");
			return;
		}

		if (!currentOtp.equals(otpInput.toString())) {
			failedAttempts++;
			if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
				invalidateOtp("OTP không đúng quá nhiều lần. Vui lòng gửi lại mã mới.");
				return;
			}
			updateHintText();
			JOptionPane.showMessageDialog(this,
				"OTP không đúng. Bạn còn " + (MAX_FAILED_ATTEMPTS - failedAttempts) + " lần thử.");
			return;
		}

		if (countdownTimer != null) {
			countdownTimer.stop();
		}
		dispose();
		if (onClose != null) {
			onClose.accept(true);
		}
	}

	private void initState() {
		txtEmail.setText(expectedEmail);
		txtEmail.setEditable(false);
		updateHintText();
	}

	private void startCountdown() {
		if (countdownTimer != null) {
			countdownTimer.stop();
		}
		countdownTimer = new Timer(1000, e -> updateHintText());
		countdownTimer.start();
	}

	private void updateHintText() {
		long now = System.currentTimeMillis();
		boolean hasOtp = currentOtp != null;

		if (!hasOtp) {
			lblHint.setText("Nhấn Gửi OTP để nhận mã xác thực qua email.");
			btnSend.setText("Gửi OTP");
			btnSend.setEnabled(true);
			if (countdownTimer != null) {
				countdownTimer.stop();
			}
			return;
		}

		long resendRemain = Math.max(0, (resendAvailableAtMillis - now + 999) / 1000);
		long expireRemain = Math.max(0, (otpExpiresAtMillis - now + 999) / 1000);

		if (expireRemain == 0) {
			invalidateOtp("OTP đã hết hạn. Vui lòng gửi lại mã mới.");
			return;
		}

		if (resendRemain > 0) {
			btnSend.setEnabled(false);
			btnSend.setText("Gửi lại (" + resendRemain + "s)");
		} else {
			btnSend.setEnabled(true);
			btnSend.setText("Gửi lại OTP");
		}

		lblHint.setText("OTP còn " + expireRemain + " giây. Số lần sai còn lại: "
				+ (MAX_FAILED_ATTEMPTS - failedAttempts) + ".");
	}

	private void invalidateOtp(String message) {
		currentOtp = null;
		otpExpiresAtMillis = 0;
		failedAttempts = 0;
		clearOtpFields();
		updateHintText();
		JOptionPane.showMessageDialog(this, message);
	}

	private void clearOtpFields() {
		for (JTextField field : otpFields) {
			field.setText("");
		}
		if (otpFields.length > 0) {
			otpFields[0].requestFocusInWindow();
		}
	}
}
