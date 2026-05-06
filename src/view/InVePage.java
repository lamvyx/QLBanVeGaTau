package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InVePage extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField txtMaVe = new JTextField();
	private final JLabel lblTrangThai = new JLabel("Chưa tìm vé");
	private final JLabel lblMaVe = taoValue("—");
	private final JLabel lblMaHoaDon = taoValue("—");
	private final JLabel lblKhachHang = taoValue("—");
	private final JLabel lblChuyenTau = taoValue("—");
	private final JLabel lblTuyen = taoValue("—");
	private final JLabel lblKhoiHanh = taoValue("—");
	private final JLabel lblToaGhe = taoValue("—");
	private final JLabel lblGiaVe = taoValue("—");
	private final JLabel lblTrangThaiVe = taoValue("—");
	private final JButton btnPrint = new JButton("In vé");
	private final JButton btnNew = new JButton("Bán vé mới");
	private String maVeDangXuLy;

	public InVePage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
		header.setPreferredSize(new Dimension(0, 62));

		JLabel title = new JLabel("In vé");
		title.setFont(AppTheme.font(Font.BOLD, 20));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(0, 16, 0, 0));
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Tìm vé đã lập hóa đơn và in theo đúng trạng thái nghiệp vụ");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		subtitle.setBorder(new EmptyBorder(0, 0, 0, 16));
		header.add(subtitle, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 16));
		content.setOpaque(false);
		content.setBorder(new EmptyBorder(16, 16, 16, 16));
		content.add(taoSearchCard(), BorderLayout.NORTH);
		content.add(taoReceiptCard(), BorderLayout.CENTER);
		return content;
	}

	private JPanel taoSearchCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("1. Tìm vé cần in");
		title.setFont(AppTheme.font(Font.BOLD, 28));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(4, 2, 2, 2));
		card.add(title, BorderLayout.NORTH);

		JPanel search = new JPanel(new BorderLayout(10, 0));
		search.setOpaque(false);

		txtMaVe.setToolTipText("Nhập mã vé đã lập hóa đơn, ví dụ VE0008");
		txtMaVe.setFont(AppTheme.font(Font.PLAIN, 13));
		txtMaVe.setPreferredSize(new Dimension(0, 42));
		txtMaVe.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(8, 12, 8, 12)));

		JButton btnFind = new JButton("Tìm");
		AppTheme.stylePrimaryButton(btnFind);
		btnFind.setPreferredSize(new Dimension(96, 42));
		btnFind.addActionListener(e -> xuLyTimVe());

		search.add(txtMaVe, BorderLayout.CENTER);
		search.add(btnFind, BorderLayout.EAST);
		card.add(search, BorderLayout.CENTER);

		lblTrangThai.setFont(AppTheme.font(Font.PLAIN, 13));
		lblTrangThai.setForeground(AppTheme.TEXT_MUTED);
		card.add(lblTrangThai, BorderLayout.SOUTH);
		return card;
	}

	private JPanel taoReceiptCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("2. Vé sẵn sàng để in");
		title.setFont(AppTheme.font(Font.BOLD, 28));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(4, 2, 2, 2));
		card.add(title, BorderLayout.NORTH);

		JPanel receipt = new JPanel(new GridBagLayout());
		receipt.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		addReceiptRow(receipt, gbc, 0, "Mã vé", lblMaVe);
		addReceiptRow(receipt, gbc, 1, "Mã hóa đơn", lblMaHoaDon);
		addReceiptRow(receipt, gbc, 2, "Khách hàng", lblKhachHang);
		addReceiptRow(receipt, gbc, 3, "Chuyến tàu", lblChuyenTau);
		addReceiptRow(receipt, gbc, 4, "Tuyến", lblTuyen);
		addReceiptRow(receipt, gbc, 5, "Khởi hành", lblKhoiHanh);
		addReceiptRow(receipt, gbc, 6, "Toa / Ghế", lblToaGhe);
		addReceiptRow(receipt, gbc, 7, "Giá vé", lblGiaVe);
		addReceiptRow(receipt, gbc, 8, "Trạng thái", lblTrangThaiVe);

		JScrollPane scrollPane = new JScrollPane(receipt);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		card.add(scrollPane, BorderLayout.CENTER);

		JPanel footer = new JPanel(new java.awt.GridLayout(1, 2, 10, 0));
		footer.setOpaque(false);
		AppTheme.stylePrimaryButton(btnPrint);
		btnPrint.setEnabled(false);
		btnPrint.addActionListener(e -> xuLyInVe());
		AppTheme.styleSecondaryButton(btnNew);
		btnNew.addActionListener(e -> resetForm());
		footer.add(btnPrint);
		footer.add(btnNew);
		card.add(footer, BorderLayout.SOUTH);

		return card;
	}

	private void addReceiptRow(JPanel parent, GridBagConstraints gbc, int row, String label, JLabel value) {
		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.weightx = 0.28;
		JLabel lbl = new JLabel(label + ":");
		lbl.setFont(AppTheme.font(Font.PLAIN, 13));
		lbl.setForeground(AppTheme.TEXT_MUTED);
		parent.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.72;
		value.setFont(AppTheme.font(Font.BOLD, 13));
		value.setForeground(AppTheme.TEXT_PRIMARY);
		value.setHorizontalAlignment(JLabel.RIGHT);
		parent.add(value, gbc);
	}

	private void xuLyTimVe() {
		String maVe = txtMaVe.getText().trim().toUpperCase();
		if (maVe.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã vé cần in.");
			return;
		}

		if (!"VE0008".equals(maVe) && !"VE0009".equals(maVe)) {
			lblTrangThai.setForeground(Color.decode("#B42318"));
			lblTrangThai.setText("Không tìm thấy vé " + maVe + ".");
			resetForm();
			return;
		}

		maVeDangXuLy = maVe;
		lblMaVe.setText(maVe);
		lblMaHoaDon.setText("HD03042608");
		lblKhachHang.setText("Nguyễn Thị Lan");
		lblChuyenTau.setText("SE1-030426");
		lblTuyen.setText("Sài Gòn - Hà Nội");
		lblKhoiHanh.setText("2026-04-03 19:00");
		lblToaGhe.setText("Toa 2 (Ghế mềm) · E03");
		lblGiaVe.setText("1.105.000 đ");
		lblTrangThaiVe.setText("Đã lập hóa đơn");
		btnPrint.setEnabled(true);
		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("Đã tìm thấy vé. Có thể in vé ngay.");
	}

	private void xuLyInVe() {
		if (maVeDangXuLy == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng tìm vé trước khi in.");
			return;
		}
		JOptionPane.showMessageDialog(this, "Đã in vé " + maVeDangXuLy + ".");
		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("In vé thành công. Có thể tìm vé khác hoặc bán vé mới.");
	}

	private void resetForm() {
		maVeDangXuLy = null;
		txtMaVe.setText("");
		btnPrint.setEnabled(false);
		lblMaVe.setText("—");
		lblMaHoaDon.setText("—");
		lblKhachHang.setText("—");
		lblChuyenTau.setText("—");
		lblTuyen.setText("—");
		lblKhoiHanh.setText("—");
		lblToaGhe.setText("—");
		lblGiaVe.setText("—");
		lblTrangThaiVe.setText("—");
		lblTrangThai.setForeground(AppTheme.TEXT_MUTED);
		lblTrangThai.setText("Chưa tìm vé");
	}

	private JLabel taoValue(String text) {
		JLabel label = new JLabel(text);
		label.setFont(AppTheme.font(Font.PLAIN, 13));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		return label;
	}
}
