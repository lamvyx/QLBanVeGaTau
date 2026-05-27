package view;

import controller.TaiKhoanController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
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

public class ResetPasswordPage extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final Color NEUTRAL_BG = Color.decode("#F0F5F9");

	private final String username;
	private final TaiKhoanController taiKhoanController;
	private final Runnable onSuccess;
	private final JPasswordField txtMatKhauMoi = new JPasswordField(24);
	private final JPasswordField txtXacNhan = new JPasswordField(24);

	public ResetPasswordPage(JFrame parent, String username, TaiKhoanController taiKhoanController,
			Runnable onSuccess) {
		super(parent, "Đặt mật khẩu mới", true);
		this.username = username;
		this.taiKhoanController = taiKhoanController;
		this.onSuccess = onSuccess;
		setSize(560, 420);
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
		card.setPreferredSize(new Dimension(500, 330));

		JLabel title = new JLabel("Đặt mật khẩu mới");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setAlignmentX(CENTER_ALIGNMENT);

		JLabel subtitle = new JLabel("Nhập mật khẩu mới cho tài khoản của bạn");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 13));
		subtitle.setForeground(new Color(98, 116, 142));
		subtitle.setAlignmentX(CENTER_ALIGNMENT);

		card.add(title);
		card.add(Box.createVerticalStrut(6));
		card.add(subtitle);
		card.add(Box.createVerticalStrut(16));

		JLabel lblMatKhauMoi = new JLabel("Mật khẩu mới");
		lblMatKhauMoi.setAlignmentX(CENTER_ALIGNMENT);
		lblMatKhauMoi.setFont(AppTheme.font(Font.BOLD, 14));
		card.add(lblMatKhauMoi);
		card.add(Box.createVerticalStrut(8));

		stylePasswordField(txtMatKhauMoi, "Ít nhất 6 ký tự");
		card.add(txtMatKhauMoi);
		card.add(Box.createVerticalStrut(12));

		JLabel lblXacNhan = new JLabel("Xác nhận mật khẩu");
		lblXacNhan.setAlignmentX(CENTER_ALIGNMENT);
		lblXacNhan.setFont(AppTheme.font(Font.BOLD, 14));
		card.add(lblXacNhan);
		card.add(Box.createVerticalStrut(8));

		stylePasswordField(txtXacNhan, "Nhập lại mật khẩu");
		card.add(txtXacNhan);
		card.add(Box.createVerticalStrut(18));

		JPanel buttonRow = new JPanel();
		buttonRow.setOpaque(false);
		buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));

		JButton btnCancel = new JButton("← Hủy");
		AppTheme.styleSecondaryButton(btnCancel);
		btnCancel.setFont(AppTheme.font(Font.BOLD, 14));
		btnCancel.setPreferredSize(new Dimension(0, 40));
		btnCancel.addActionListener(e -> dispose());

		JButton btnConfirm = new JButton("Xác nhận");
		AppTheme.stylePrimaryButton(btnConfirm);
		btnConfirm.setFont(AppTheme.font(Font.BOLD, 14));
		btnConfirm.setPreferredSize(new Dimension(0, 40));
		btnConfirm.addActionListener(e -> submitReset());

		buttonRow.add(btnCancel);
		buttonRow.add(Box.createHorizontalStrut(10));
		buttonRow.add(btnConfirm);
		card.add(wrapFullWidth(buttonRow));

		root.add(card);
		return root;
	}

	private void stylePasswordField(JPasswordField field, String tooltip) {
		field.setToolTipText(tooltip);
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		field.setPreferredSize(new Dimension(0, 40));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(186, 199, 214)),
				new EmptyBorder(10, 10, 10, 10)));
		field.setFont(AppTheme.font(Font.PLAIN, 14));
	}

	private JPanel wrapFullWidth(JPanel panel) {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setOpaque(false);
		wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		wrap.add(panel, BorderLayout.CENTER);
		return wrap;
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

		service.TaiKhoanService.KetQuaXuLy ketQua = taiKhoanController.datLaiMatKhau(username, matKhauMoi, xacNhan);
		if (!ketQua.thanhCong) {
			JOptionPane.showMessageDialog(this, ketQua.thongBao);
			return;
		}

		JOptionPane.showMessageDialog(this, ketQua.thongBao);
		dispose();
		if (onSuccess != null) {
			onSuccess.run();
		}
	}
}
