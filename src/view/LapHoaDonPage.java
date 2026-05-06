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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class LapHoaDonPage extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField txtMaHD = new JTextField("HD-NEW");
	private final JTextField txtMaNV = new JTextField("NV-1024");
	private final JTextField txtMaKH = new JTextField();
	private final JTextField txtVAT = new JTextField("0.08");
	private final JTextField txtTongTien = new JTextField("0");
	private final JComboBox<String> cboMaKM = new JComboBox<>(new String[] { "Không áp dụng", "KM10", "KM15", "KM20" });
	private final JTable tblVeChoLapHoaDon;

	public LapHoaDonPage() {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		String[] cols = { "Mã vé", "Khách hàng", "Chuyến", "Toa/Ghế", "Giá vé" };
		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		model.addRow(new Object[] { "VE0101", "Nguyễn Văn Minh", "SE1", "Toa 3 - Ghế 06", "1.220.000" });
		model.addRow(new Object[] { "VE0102", "Trần Thị Hoa", "SE2", "Toa 1 - Ghế 18", "980.000" });
		model.addRow(new Object[] { "VE0103", "Lê Tuấn Anh", "SE1", "Toa 2 - Ghế 09", "1.050.000" });
		tblVeChoLapHoaDon = new JTable(model);
		AppTheme.styleTable(tblVeChoLapHoaDon);
		tblVeChoLapHoaDon.setRowHeight(32);

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout(0, 6));
		header.setOpaque(false);

		JLabel title = new JLabel("Lập hóa đơn");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.PRIMARY);

		JLabel subtitle = new JLabel("Chốt hóa đơn từ danh sách vé đã chọn và áp dụng khuyến mãi nếu có");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);

		header.add(title, BorderLayout.NORTH);
		header.add(subtitle, BorderLayout.CENTER);
		return header;
	}

	private JPanel taoBody() {
		JPanel body = new JPanel(new BorderLayout(12, 0));
		body.setOpaque(false);

		JPanel left = new JPanel(new BorderLayout(0, 8));
		left.setBackground(AppTheme.CARD_BG);
		left.setBorder(AppTheme.cardBorder());
		JLabel lblLeft = new JLabel("Vé chờ lập hóa đơn");
		lblLeft.setFont(AppTheme.font(Font.BOLD, 14));
		lblLeft.setForeground(AppTheme.TEXT_PRIMARY);
		left.add(lblLeft, BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(tblVeChoLapHoaDon);
		scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		left.add(scroll, BorderLayout.CENTER);

		JPanel right = new JPanel(new BorderLayout(0, 10));
		right.setBackground(AppTheme.CARD_BG);
		right.setBorder(AppTheme.cardBorder());
		right.setPreferredSize(new Dimension(420, 10));

		JLabel lblRight = new JLabel("Thông tin hóa đơn");
		lblRight.setFont(AppTheme.font(Font.BOLD, 14));
		lblRight.setForeground(AppTheme.TEXT_PRIMARY);
		right.add(lblRight, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtMaHD.setEditable(false);
		txtMaNV.setEditable(false);
		styleInput(txtMaHD);
		styleInput(txtMaNV);
		styleInput(txtMaKH);
		styleInput(txtVAT);
		styleInput(txtTongTien);
		cboMaKM.setFont(AppTheme.font(Font.PLAIN, 13));
		cboMaKM.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

		themDong(form, gbc, 0, "Mã hóa đơn", txtMaHD);
		themDong(form, gbc, 1, "Mã nhân viên", txtMaNV);
		themDong(form, gbc, 2, "Mã khách hàng", txtMaKH);
		themDong(form, gbc, 3, "VAT", txtVAT);
		themDong(form, gbc, 4, "Mã khuyến mãi", cboMaKM);
		themDong(form, gbc, 5, "Tổng tiền", txtTongTien);

		right.add(form, BorderLayout.CENTER);

		JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);
		JButton btnTinh = new JButton("Tính tiền");
		AppTheme.styleSecondaryButton(btnTinh);
		btnTinh.addActionListener(e -> txtTongTien.setText("3.250.000"));
		JButton btnLap = new JButton("Lập hóa đơn");
		AppTheme.stylePrimaryButton(btnLap);
		actions.add(btnTinh);
		actions.add(btnLap);
		right.add(actions, BorderLayout.SOUTH);

		body.add(left, BorderLayout.CENTER);
		body.add(right, BorderLayout.EAST);
		return body;
	}

	private void themDong(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.weightx = 0.36;
		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.font(Font.BOLD, 12));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);
		form.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.64;
		field.setPreferredSize(new Dimension(220, 34));
		form.add(field, gbc);
	}

	private void styleInput(JTextField field) {
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(6, 8, 6, 8)));
	}
}
