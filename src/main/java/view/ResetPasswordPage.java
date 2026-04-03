package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import dao.TaiKhoan_DAO;

public class ResetPasswordPage extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final Color PRIMARY = Color.decode("#2563EB");
	private static final Color NEUTRAL_BG = Color.decode("#F0F5F9");

	private final String username;
	private final TaiKhoan_DAO taiKhoanDAO;
	private final Runnable onSuccess;
	private final JPasswordField txtMatKhauMoi = new JPasswordField(24);
	private final JPasswordField txtXacNhan = new JPasswordField(24);

	public ResetPasswordPage(JFrame parent, String username, TaiKhoan_DAO taiKhoanDAO, Runnable onSuccess) {
		super(parent, "Đặt mật khẩu mới", true);
		this.username = username;
		this.taiKhoanDAO = taiKhoanDAO;
		this.onSuccess = onSuccess;
		setSize(430, 360);
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

		JLabel title = new JLabel("Đặt mật khẩu mới");
		title.setFont(new Font("Segoe UI", Font.BOLD, 32));
		title.setAlignmentX(CENTER_ALIGNMENT);

		JLabel subtitle = new JLabel("Nhập mật khẩu mới cho tài khoản của bạn");
		subtitle.setForeground(new Color(98, 116, 142));
		subtitle.setAlignmentX(CENTER_ALIGNMENT);

		root.add(title);
		root.add(Box.createVerticalStrut(4));
		root.add(subtitle);
		root.add(Box.createVerticalStrut(16));

		JLabel lblMatKhauMoi = new JLabel("Mật khẩu mới");
		lblMatKhauMoi.setFont(new Font("Segoe UI", Font.BOLD, 18));
		root.add(lblMatKhauMoi);
		root.add(Box.createVerticalStrut(8));

		stylePasswordField(txtMatKhauMoi, "Ít nhất 6 ký tự");
		root.add(txtMatKhauMoi);
		root.add(Box.createVerticalStrut(14));

		JLabel lblXacNhan = new JLabel("Xác nhận mật khẩu");
		lblXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 18));
		root.add(lblXacNhan);
		root.add(Box.createVerticalStrut(8));

		stylePasswordField(txtXacNhan, "Nhập lại mật khẩu");
		root.add(txtXacNhan);
		root.add(Box.createVerticalStrut(18));

		JPanel buttonRow = new JPanel();
		buttonRow.setOpaque(false);
		buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));

		JButton btnCancel = new JButton("← Hủy");
		btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		btnCancel.setFocusPainted(false);
		btnCancel.setBackground(Color.WHITE);
		btnCancel.setBorder(BorderFactory.createLineBorder(new Color(206, 213, 222)));
		btnCancel.addActionListener(e -> dispose());

		JButton btnConfirm = new JButton("Xác nhận");
		btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 18));
		btnConfirm.setFocusPainted(false);
		btnConfirm.setBackground(PRIMARY);
		btnConfirm.setForeground(Color.WHITE);
		btnConfirm.addActionListener(e -> submitReset());

		buttonRow.add(btnCancel);
		buttonRow.add(Box.createHorizontalStrut(10));
		buttonRow.add(btnConfirm);
		root.add(buttonRow);

		return root;
	}

	private void stylePasswordField(JPasswordField field, String tooltip) {
		field.setToolTipText(tooltip);
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(186, 199, 214)),
				new EmptyBorder(10, 10, 10, 10)));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	}

	private void submitReset() {
		String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
		String xacNhan = new String(txtXacNhan.getPassword()).trim();

		if (matKhauMoi.length() < 6) {
			JOptionPane.showMessageDialog(this, "Mật khẩu cần tối thiểu 6 ký tự.");
			return;
		}
		if (!matKhauMoi.equals(xacNhan)) {
			JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp.");
			return;
		}

		if (!taiKhoanDAO.doiMatKhau(username, matKhauMoi)) {
			JOptionPane.showMessageDialog(this, "Không thể cập nhật mật khẩu.");
			return;
		}

		JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công.");
		dispose();
		if (onSuccess != null) {
			onSuccess.run();
		}
	}
}
