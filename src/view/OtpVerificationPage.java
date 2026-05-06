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
		setSize(560, 460);
		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setContentPane(createContent());
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

		JButton btnSend = new JButton("Gửi OTP");
		AppTheme.stylePrimaryButton(btnSend);
		btnSend.setPreferredSize(new Dimension(110, 40));
		btnSend.addActionListener(e -> sendOtp());
		emailRow.add(btnSend, BorderLayout.EAST);
		card.add(emailRow);

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
