package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
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
import javax.swing.border.EmptyBorder;

public class OtpVerificationPage extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final Color PRIMARY = Color.decode("#2563EB");
	private static final Color NEUTRAL_BG = Color.decode("#F0F5F9");

	private final String expectedEmail;
	private final JTextField txtEmail = new JTextField(22);
	private final JTextField[] otpFields = new JTextField[6];
	private final Consumer<Boolean> onClose;
	private String currentOtp;

	public OtpVerificationPage(JFrame parent, String expectedEmail, Consumer<Boolean> onClose) {
		super(parent, "Quên mật khẩu", true);
		this.expectedEmail = expectedEmail;
		this.onClose = onClose;
		setSize(430, 420);
		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setContentPane(createContent());
	}

	private JPanel createContent() {
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setBackground(NEUTRAL_BG);
		root.setBorder(new EmptyBorder(18, 20, 18, 20));

		JLabel title = new JLabel("Quên mật khẩu", SwingConstants.CENTER);
		title.setFont(new Font("Segoe UI", Font.BOLD, 34));
		title.setAlignmentX(CENTER_ALIGNMENT);

		JLabel subtitle = new JLabel("Nhập email để nhận mã OTP xác nhận", SwingConstants.CENTER);
		subtitle.setForeground(new Color(98, 116, 142));
		subtitle.setAlignmentX(CENTER_ALIGNMENT);

		root.add(title);
		root.add(Box.createVerticalStrut(4));
		root.add(subtitle);
		root.add(Box.createVerticalStrut(18));

		JLabel lblEmail = new JLabel("Địa chỉ Email");
		lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 18));
		root.add(lblEmail);
		root.add(Box.createVerticalStrut(8));

		JPanel emailRow = new JPanel(new BorderLayout(8, 0));
		emailRow.setOpaque(false);
		txtEmail.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(186, 199, 214)),
				new EmptyBorder(8, 10, 8, 10)));
		emailRow.add(txtEmail, BorderLayout.CENTER);

		JButton btnSend = new JButton("Gửi OTP");
		btnSend.setBackground(PRIMARY);
		btnSend.setForeground(Color.WHITE);
		btnSend.setFocusPainted(false);
		btnSend.addActionListener(e -> sendOtp());
		emailRow.add(btnSend, BorderLayout.EAST);
		root.add(emailRow);

		root.add(Box.createVerticalStrut(18));
		JLabel lblOtp = new JLabel("Mã OTP (6 số)");
		lblOtp.setFont(new Font("Segoe UI", Font.BOLD, 18));
		root.add(lblOtp);
		root.add(Box.createVerticalStrut(8));

		JPanel otpRow = new JPanel(new GridLayout(1, 6, 10, 0));
		otpRow.setOpaque(false);
		for (int i = 0; i < otpFields.length; i++) {
			otpFields[i] = buildOtpField(i);
			otpRow.add(otpFields[i]);
		}
		root.add(otpRow);

		root.add(Box.createVerticalStrut(18));
		JButton btnConfirm = new JButton("Xác nhận OTP");
		btnConfirm.setBackground(new Color(114, 146, 233));
		btnConfirm.setForeground(Color.WHITE);
		btnConfirm.setFocusPainted(false);
		btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 20));
		btnConfirm.setBorder(new EmptyBorder(12, 10, 12, 10));
		btnConfirm.addActionListener(e -> confirmOtp());
		root.add(btnConfirm);

		root.add(Box.createVerticalStrut(10));
		JButton btnBack = new JButton("← Quay về đăng nhập");
		btnBack.setBackground(Color.WHITE);
		btnBack.setFocusPainted(false);
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnBack.setBorder(BorderFactory.createLineBorder(new Color(206, 213, 222)));
		btnBack.addActionListener(e -> {
			dispose();
			if (onClose != null) {
				onClose.accept(false);
			}
		});
		root.add(btnBack);

		return root;
	}

	private JTextField buildOtpField(int index) {
		JTextField field = new JTextField();
		field.setHorizontalAlignment(SwingConstants.CENTER);
		field.setFont(new Font("Segoe UI", Font.BOLD, 22));
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

		currentOtp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
		JOptionPane.showMessageDialog(this, "OTP đã gửi. (Demo: " + currentOtp + ")");
	}

	private void confirmOtp() {
		if (currentOtp == null) {
			JOptionPane.showMessageDialog(this, "Hãy gửi OTP trước.");
			return;
		}

		StringBuilder otpInput = new StringBuilder();
		for (JTextField field : otpFields) {
			otpInput.append(field.getText().trim());
		}

		if (!currentOtp.equals(otpInput.toString())) {
			JOptionPane.showMessageDialog(this, "OTP không đúng.");
			return;
		}

		dispose();
		if (onClose != null) {
			onClose.accept(true);
		}
	}
}
