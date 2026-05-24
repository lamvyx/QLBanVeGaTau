package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NhanVienThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField txtTen, txtUsername, txtSdt, txtEmail, txtPassword;
	private JComboBox<String> cbChucVu;
	private JButton btnThem;
	private JButton btnLamMoi;

	public NhanVienThemPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoForm(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Thêm nhân viên mới");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(AppTheme.PRIMARY);
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Nhập thông tin và xác nhận tạo mới nhân viên");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		header.add(subtitle, BorderLayout.SOUTH);

		return header;
	}

	private JPanel taoForm() {
		JPanel wrapper = new JPanel(new BorderLayout(0, 16));
		wrapper.setOpaque(false);

		JPanel card = new JPanel(new BorderLayout(0, 16));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JPanel body = new JPanel(new BorderLayout(16, 0));
		body.setOpaque(false);

		JPanel formCard = taoFormCard();
		body.add(formCard, BorderLayout.CENTER);

		// Nút hành động
		JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
		actions.setOpaque(false);

		btnThem = new JButton("Thêm nhân viên");
		AppTheme.stylePrimaryButton(btnThem);
		btnThem.setPreferredSize(new Dimension(150, 40));

		btnLamMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnLamMoi);
		btnLamMoi.setPreferredSize(new Dimension(120, 40));

		btnLamMoi.addActionListener(e -> {
			txtTen.setText("");
			txtUsername.setText("");
			txtSdt.setText("");
			txtEmail.setText("");
			txtPassword.setText("");
			cbChucVu.setSelectedIndex(0);
		});

		actions.add(btnLamMoi);
		actions.add(btnThem);

		card.add(body, BorderLayout.CENTER);
		card.add(actions, BorderLayout.SOUTH);
		wrapper.add(card, BorderLayout.CENTER);
		return wrapper;
	}

	private JPanel taoFormCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(BorderFactory.createCompoundBorder(
			AppTheme.cardBorder(),
			new EmptyBorder(4, 4, 4, 4)
		));

		JLabel title = new JLabel("Thông tin nhân viên");
		title.setFont(AppTheme.font(Font.BOLD, 16));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		card.add(title, BorderLayout.NORTH);

		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setBackground(AppTheme.CARD_BG);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;

		addField(formContainer, gbc, 0, 0, "Họ và tên *", txtTen = new JTextField());
		addField(formContainer, gbc, 1, 0, "Tên tài khoản *", txtUsername = new JTextField());
		addField(formContainer, gbc, 0, 1, "Số điện thoại", txtSdt = new JTextField());
		addComboField(formContainer, gbc, 1, 1, "Chức vụ", cbChucVu = new JComboBox<>(new String[]{"Quản lý", "Bán vé", "Hỗ trợ", "Kỹ thuật"}));
		addField(formContainer, gbc, 0, 2, "Email *", txtEmail = new JTextField());
		addField(formContainer, gbc, 1, 2, "Mật khẩu *", txtPassword = new JTextField());

		JScrollPane scrollPane = new JScrollPane(formContainer);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		card.add(scrollPane, BorderLayout.CENTER);
		return card;
	}

	private void addField(JPanel parent, GridBagConstraints gbc, int col, int row, String labelText, JTextField field) {
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.font(Font.BOLD, 13));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setPreferredSize(new Dimension(0, 38));
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(8, 8, 8, 8)
		));

		gbc.gridx = col * 2;
		gbc.gridy = row;
		gbc.weightx = 0.26;
		parent.add(label, gbc);

		gbc.gridx = col * 2 + 1;
		gbc.weightx = 0.74;
		parent.add(field, gbc);
	}

	private void addComboField(JPanel parent, GridBagConstraints gbc, int col, int row, String labelText, JComboBox<String> comboBox) {
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.font(Font.BOLD, 13));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		comboBox.setFont(AppTheme.font(Font.PLAIN, 13));
		comboBox.setPreferredSize(new Dimension(0, 38));
		comboBox.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

		gbc.gridx = col * 2;
		gbc.gridy = row;
		gbc.weightx = 0.26;
		parent.add(label, gbc);

		gbc.gridx = col * 2 + 1;
		gbc.weightx = 0.74;
		parent.add(comboBox, gbc);
	}

}
