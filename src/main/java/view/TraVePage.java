package view;

import controller.DoiTraController;
import entity.VeTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import service.DoiTraService.KetQuaXuLy;

public class TraVePage extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField txtMaVe = new JTextField();
	private final JLabel lblTrangThai = new JLabel("Chưa tìm vé");
	private final JLabel lblMaVe = taoValue("—");
	private final JLabel lblKhachHang = taoValue("—");
	private final JLabel lblTrangThaiVe = taoValue("—");
	private final JLabel lblPhiHoan = taoValue("—");
	private final JButton btnTraVe = new JButton("Xác nhận trả vé");
	private String maVeDangXuLy;
	private final DoiTraController doiTraController = new DoiTraController();

	public TraVePage() {
		setLayout(new BorderLayout(0, 16));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoCardTimVe(), BorderLayout.NORTH);
		add(taoCardThongTin(), BorderLayout.CENTER);
	}

	private JPanel taoCardTimVe() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Tìm vé cần trả");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(4, 2, 2, 2));
		card.add(title, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
		searchPanel.setOpaque(false);

		txtMaVe.setFont(AppTheme.font(Font.PLAIN, 13));
		txtMaVe.setPreferredSize(new Dimension(0, 42));
		txtMaVe.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(8, 12, 8, 12)));
		txtMaVe.setToolTipText("Nhập mã vé (VD: VE0001)");

		JButton btnTim = new JButton("Tìm");
		AppTheme.stylePrimaryButton(btnTim);
		btnTim.setPreferredSize(new Dimension(96, 42));
		btnTim.addActionListener(e -> xuLyTimVe());

		searchPanel.add(txtMaVe, BorderLayout.CENTER);
		searchPanel.add(btnTim, BorderLayout.EAST);
		card.add(searchPanel, BorderLayout.CENTER);

		lblTrangThai.setFont(AppTheme.font(Font.PLAIN, 13));
		lblTrangThai.setForeground(AppTheme.TEXT_MUTED);
		card.add(lblTrangThai, BorderLayout.SOUTH);
		return card;
	}

	private JPanel taoCardThongTin() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Thông tin hoàn vé");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(4, 2, 2, 2));
		card.add(title, BorderLayout.NORTH);

		JPanel infoGrid = new JPanel(new GridLayout(4, 2, 12, 10));
		infoGrid.setOpaque(false);
		infoGrid.add(taoLabel("Mã vé:"));
		infoGrid.add(lblMaVe);
		infoGrid.add(taoLabel("Khách hàng:"));
		infoGrid.add(lblKhachHang);
		infoGrid.add(taoLabel("Trạng thái:"));
		infoGrid.add(lblTrangThaiVe);
		infoGrid.add(taoLabel("Phí hoàn trả:"));
		infoGrid.add(lblPhiHoan);
		card.add(infoGrid, BorderLayout.CENTER);

		AppTheme.stylePrimaryButton(btnTraVe);
		btnTraVe.setPreferredSize(new Dimension(0, 42));
		btnTraVe.setEnabled(false);
		btnTraVe.addActionListener(e -> xuLyTraVe());
		card.add(btnTraVe, BorderLayout.SOUTH);
		return card;
	}

	private JLabel taoLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(AppTheme.font(Font.BOLD, 14));
		label.setForeground(AppTheme.TEXT_MUTED);
		return label;
	}

	private JLabel taoValue(String text) {
		JLabel label = new JLabel(text);
		label.setFont(AppTheme.font(Font.PLAIN, 14));
		label.setForeground(AppTheme.TEXT_PRIMARY);
		return label;
	}

	private void xuLyTimVe() {
		String maVe = txtMaVe.getText().trim().toUpperCase();
		if (maVe.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã vé cần trả.");
			return;
		}
		VeTau ve = doiTraController.timVeTheoMa(maVe);
		if (ve == null) {
			lblTrangThai.setForeground(Color.decode("#B42318"));
			lblTrangThai.setText("Không tìm thấy vé " + maVe + ".");
			resetThongTin();
			return;
		}

		maVeDangXuLy = maVe;
		lblMaVe.setText(maVe);
		lblKhachHang.setText(ve.getMaKH());
		lblTrangThaiVe.setText("Đã thanh toán - Có thể hoàn");
		lblPhiHoan.setText("Hoàn 80%: " + ve.getGiaVe().multiply(new java.math.BigDecimal("0.8")) + "đ");
		btnTraVe.setEnabled(true);
		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("Đã tìm thấy vé. Kiểm tra thông tin và xác nhận trả vé.");
	}

	private void xuLyTraVe() {
		if (maVeDangXuLy == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng tìm vé trước khi xác nhận trả.");
			return;
		}

		KetQuaXuLy taoDon = doiTraController.taoDonDoiTra(maVeDangXuLy, null, "TRA", "Trả vé tại quầy");
		if (!taoDon.thanhCong) {
			JOptionPane.showMessageDialog(this, taoDon.thongBao);
			return;
		}
		KetQuaXuLy xacNhan = doiTraController.xacNhanDonDoiTra(taoDon.maThamChieu);
		JOptionPane.showMessageDialog(this, xacNhan.thongBao);
		if (xacNhan.thanhCong) {
			lblTrangThai.setForeground(Color.decode("#027A48"));
			lblTrangThai.setText("Đã hoàn vé thành công. Có thể tìm mã vé khác.");
		}
		resetThongTin();
		txtMaVe.setText("");
	}

	private void resetThongTin() {
		maVeDangXuLy = null;
		btnTraVe.setEnabled(false);
		lblMaVe.setText("—");
		lblKhachHang.setText("—");
		lblTrangThaiVe.setText("—");
		lblPhiHoan.setText("—");
	}
}
