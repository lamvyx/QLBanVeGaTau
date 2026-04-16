package view;

import controller.DoiTraController;
import entity.VeTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import service.DoiTraService.KetQuaXuLy;

public class DoiVePage extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField txtMaVe = new JTextField();
	private final JLabel lblTrangThai = new JLabel("Chưa tìm vé", SwingConstants.LEFT);

	private final JLabel lblCurrentMa = taoDongGiaTri("—");
	private final JLabel lblCurrentKhach = taoDongGiaTri("—");
	private final JLabel lblCurrentTuyen = taoDongGiaTri("—");

	private final JLabel lblNewMa = taoDongGiaTri("—");
	private final JLabel lblNewKhach = taoDongGiaTri("—");
	private final JLabel lblNewTuyen = taoDongGiaTri("—");
	private final JLabel lblChenhLech = new JLabel("Chênh lệch: —");

	private final JComboBox<String> cboVeMoi = new JComboBox<>();
	private final JButton btnXacNhan = new JButton("Xác nhận đổi vé");
	private String maVeDangXuLy;
	private final DoiTraController doiTraController = new DoiTraController();

	public DoiVePage() {
		setLayout(new BorderLayout(0, 16));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoCardTimVe(), BorderLayout.NORTH);
		add(taoCardSoSanh(), BorderLayout.CENTER);
	}

	private JPanel taoCardTimVe() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("1. Tìm vé cần đổi");
		title.setFont(AppTheme.font(Font.BOLD, 29));
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

	private JPanel taoCardSoSanh() {
		JPanel card = new JPanel(new BorderLayout(0, 14));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("So sánh & Xác nhận");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(4, 2, 2, 2));
		card.add(title, BorderLayout.NORTH);

		JPanel compareWrap = new JPanel(new GridLayout(1, 2, 14, 0));
		compareWrap.setOpaque(false);
		compareWrap.add(taoKhungThongTinHienTai());
		compareWrap.add(taoKhungThongTinMoi());
		card.add(compareWrap, BorderLayout.CENTER);

		AppTheme.stylePrimaryButton(btnXacNhan);
		btnXacNhan.setPreferredSize(new Dimension(0, 42));
		btnXacNhan.setEnabled(false);
		btnXacNhan.addActionListener(e -> xuLyXacNhan());

		JPanel bottom = new JPanel(new BorderLayout());
		bottom.setOpaque(false);
		bottom.add(btnXacNhan, BorderLayout.CENTER);
		card.add(bottom, BorderLayout.SOUTH);
		return card;
	}

	private JPanel taoKhungThongTinHienTai() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.decode("#F8FAFD"));
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(14, 14, 14, 14)));

		JLabel lblTitle = new JLabel("Vé hiện tại");
		lblTitle.setFont(AppTheme.font(Font.BOLD, 17));
		lblTitle.setForeground(AppTheme.TEXT_PRIMARY);

		JPanel lines = new JPanel(new GridLayout(3, 1, 0, 8));
		lines.setOpaque(false);
		lines.add(lblCurrentMa);
		lines.add(lblCurrentKhach);
		lines.add(lblCurrentTuyen);

		panel.add(lblTitle, BorderLayout.NORTH);
		panel.add(lines, BorderLayout.CENTER);
		return panel;
	}

	private JPanel taoKhungThongTinMoi() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.decode("#F4F8FF"));
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#BBD4FF")),
				new EmptyBorder(14, 14, 14, 14)));

		JLabel lblTitle = new JLabel("Vé mới");
		lblTitle.setFont(AppTheme.font(Font.BOLD, 17));
		lblTitle.setForeground(AppTheme.PRIMARY);

		JPanel top = new JPanel(new BorderLayout(8, 0));
		top.setOpaque(false);
		JLabel lblLuaChon = new JLabel("Phương án đổi:");
		lblLuaChon.setFont(AppTheme.font(Font.PLAIN, 13));
		lblLuaChon.setForeground(AppTheme.TEXT_MUTED);
		top.add(lblLuaChon, BorderLayout.WEST);

		cboVeMoi.setModel(new DefaultComboBoxModel<>(new String[] { "Chọn vé mới..." }));
		cboVeMoi.setEnabled(false);
		cboVeMoi.setFont(AppTheme.font(Font.PLAIN, 13));
		cboVeMoi.addActionListener(e -> xuLyChonVeMoi());
		top.add(cboVeMoi, BorderLayout.CENTER);

		JPanel lines = new JPanel(new GridLayout(4, 1, 0, 8));
		lines.setOpaque(false);
		lines.add(lblNewMa);
		lines.add(lblNewKhach);
		lines.add(lblNewTuyen);
		lblChenhLech.setFont(AppTheme.font(Font.BOLD, 14));
		lblChenhLech.setForeground(AppTheme.TEXT_MUTED);
		lines.add(lblChenhLech);

		JPanel center = new JPanel(new BorderLayout(0, 10));
		center.setOpaque(false);
		center.add(top, BorderLayout.NORTH);
		center.add(lines, BorderLayout.CENTER);

		panel.add(lblTitle, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}

	private JLabel taoDongGiaTri(String value) {
		JLabel label = new JLabel(value);
		label.setFont(AppTheme.font(Font.BOLD, 18));
		label.setForeground(AppTheme.PRIMARY);
		return label;
	}

	private void xuLyTimVe() {
		String maVe = txtMaVe.getText().trim().toUpperCase();
		if (maVe.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã vé cần đổi.");
			return;
		}
		VeTau veHienTai = doiTraController.timVeTheoMa(maVe);
		if (veHienTai == null) {
			lblTrangThai.setForeground(Color.decode("#B42318"));
			lblTrangThai.setText("Không tìm thấy vé " + maVe + ". Vui lòng kiểm tra lại.");
			resetThongTin();
			return;
		}

		maVeDangXuLy = maVe;
		lblCurrentMa.setText("Mã vé: " + maVe);
		lblCurrentKhach.setText("KH: " + veHienTai.getMaKH());
		lblCurrentTuyen.setText(veHienTai.getMaChuyenTau() + " | Toa " + veHienTai.getMaToa());

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		model.addElement("Chọn vé mới...");
		List<VeTau> dsVe = doiTraController.layVeTheoChuyenTau(veHienTai.getMaChuyenTau());
		for (VeTau ve : dsVe) {
			if (!maVe.equalsIgnoreCase(ve.getMaVeTau())) {
				model.addElement(ve.getMaVeTau() + " | Toa " + ve.getMaToa() + " | " + ve.getGiaVe() + "đ");
			}
		}
		cboVeMoi.setModel(model);
		cboVeMoi.setSelectedIndex(0);
		cboVeMoi.setEnabled(true);

		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("Đã tìm thấy vé " + maVe + ". Chọn vé mới để xác nhận đổi.");
		btnXacNhan.setEnabled(false);
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("—");
		lblChenhLech.setText("Chênh lệch: —");
	}

	private void xuLyChonVeMoi() {
		if (cboVeMoi.getSelectedIndex() <= 0) {
			btnXacNhan.setEnabled(false);
			lblNewMa.setText("—");
			lblNewKhach.setText("—");
			lblNewTuyen.setText("—");
			lblChenhLech.setText("Chênh lệch: —");
			return;
		}

		String selected = String.valueOf(cboVeMoi.getSelectedItem());
		String[] parts = selected.split("\\\\|", 3);
		String maVeMoi = parts.length > 0 ? parts[0].trim() : selected;
		String moTa = parts.length > 1 ? parts[1].trim() : "";
		String gia = parts.length > 2 ? parts[2].trim() : "";
		lblNewMa.setText("Mã vé mới: " + maVeMoi);
		lblNewKhach.setText(lblCurrentKhach.getText());
		lblNewTuyen.setText(moTa);
		lblChenhLech.setText("Giá mới: " + gia);
		btnXacNhan.setEnabled(maVeDangXuLy != null);
	}

	private void xuLyXacNhan() {
		if (maVeDangXuLy == null || cboVeMoi.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng tìm vé và chọn vé mới trước khi xác nhận.");
			return;
		}

		String selected = String.valueOf(cboVeMoi.getSelectedItem());
		String[] parts = selected.split("\\\\|", 2);
		String maVeMoi = parts.length > 0 ? parts[0].trim() : selected.trim();
		KetQuaXuLy taoDon = doiTraController.taoDonDoiTra(maVeDangXuLy, maVeMoi, "DOI", "Đổi vé tại quầy");
		if (!taoDon.thanhCong) {
			JOptionPane.showMessageDialog(this, taoDon.thongBao);
			return;
		}
		KetQuaXuLy xacNhan = doiTraController.xacNhanDonDoiTra(taoDon.maThamChieu);
		JOptionPane.showMessageDialog(this, xacNhan.thongBao);
		if (xacNhan.thanhCong) {
			lblTrangThai.setForeground(Color.decode("#027A48"));
			lblTrangThai.setText("Đổi vé thành công. Có thể tiếp tục tra mã vé khác.");
		}
		resetThongTin();
		txtMaVe.setText("");
	}

	private void resetThongTin() {
		maVeDangXuLy = null;
		btnXacNhan.setEnabled(false);
		cboVeMoi.setModel(new DefaultComboBoxModel<>(new String[] { "Chọn vé mới..." }));
		cboVeMoi.setEnabled(false);
		lblCurrentMa.setText("—");
		lblCurrentKhach.setText("—");
		lblCurrentTuyen.setText("—");
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("—");
		lblChenhLech.setText("Chênh lệch: —");
	}
}
