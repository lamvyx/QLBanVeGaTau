package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import controller.ChuyenTauController;
import controller.DoiTraController;
import controller.HoaDonController;
import controller.ToaController;
import entity.ChuyenTau;
import entity.Toa;
import entity.VeTau;
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

	private final JComboBox<ChuyenTauOption> cboChuyenMoi = new JComboBox<>();
	private final JComboBox<ToaOption> cboToaMoi = new JComboBox<>();
	private final JComboBox<String> cboGheMoi = new JComboBox<>();
	private final JButton btnXacNhan = new JButton("Xác nhận đổi vé");
	
	private String maVeDangXuLy;
	private VeTau veDangXuLy;
	
	private final DoiTraController doiTraController = new DoiTraController();
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final ToaController toaController = new ToaController();
	private final HoaDonController hoaDonController = new HoaDonController();
	
	private final List<ChuyenTauOption> dsChuyen = new ArrayList<>();
	private final Map<String, List<ToaOption>> toaTheoTau = new HashMap<>();
	private final Set<String> bookedSeats = new java.util.HashSet<>();

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

		JPanel selectionPanel = new JPanel(new GridLayout(3, 1, 0, 8));
		selectionPanel.setOpaque(false);

		// Chọn chuyến
		JPanel row1 = new JPanel(new BorderLayout(8, 0));
		row1.setOpaque(false);
		row1.add(new JLabel("Chuyến mới:"), BorderLayout.WEST);
		cboChuyenMoi.addActionListener(e -> taiToaMoi());
		row1.add(cboChuyenMoi, BorderLayout.CENTER);
		selectionPanel.add(row1);

		// Chọn toa
		JPanel row2 = new JPanel(new BorderLayout(8, 0));
		row2.setOpaque(false);
		row2.add(new JLabel("Toa mới:      "), BorderLayout.WEST);
		cboToaMoi.addActionListener(e -> taiGheMoi());
		row2.add(cboToaMoi, BorderLayout.CENTER);
		selectionPanel.add(row2);

		// Chọn ghế
		JPanel row3 = new JPanel(new BorderLayout(8, 0));
		row3.setOpaque(false);
		row3.add(new JLabel("Chỗ mới:      "), BorderLayout.WEST);
		cboGheMoi.addActionListener(e -> xuLyChonVeMoi());
		row3.add(cboGheMoi, BorderLayout.CENTER);
		selectionPanel.add(row3);

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
		center.add(selectionPanel, BorderLayout.NORTH);
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
		veDangXuLy = veHienTai;
		lblCurrentMa.setText("Mã vé: " + maVe);
		lblCurrentKhach.setText("KH: " + veHienTai.getMaKH());
		lblCurrentTuyen.setText(veHienTai.getMaChuyenTau() + " | Ghế " + veHienTai.getViTriGhe());

		taiDuLieuChuyenMoi();
		
		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("Đã tìm thấy vé " + maVe + ". Vui lòng chọn phương án đổi.");
		btnXacNhan.setEnabled(false);
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("—");
		lblChenhLech.setText("Chênh lệch: —");
	}

	private void taiDuLieuChuyenMoi() {
		dsChuyen.clear();
		for (ChuyenTau ct : chuyenTauController.timKiemChuyenTau("")) {
			dsChuyen.add(new ChuyenTauOption(ct.getMaCT(), ct.getMaTau(), ct.getMaTuyenTau(), ct.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
		}
		cboChuyenMoi.removeAllItems();
		for (ChuyenTauOption opt : dsChuyen) {
			cboChuyenMoi.addItem(opt);
		}
		
		toaTheoTau.clear();
		List<Toa> dsToa = toaController.timKiemToa(null);
		for (Toa t : dsToa) {
			toaTheoTau.computeIfAbsent(t.getMaTau(), k -> new ArrayList<>()).add(new ToaOption(t.getMaToa(), t.getLoaiToa(), t.getSoGhe()));
		}
		
		cboChuyenMoi.setEnabled(true);
		cboToaMoi.setEnabled(true);
		cboGheMoi.setEnabled(true);
	}

	private void taiToaMoi() {
		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		if (ct == null) return;
		cboToaMoi.removeAllItems();
		List<ToaOption> toas = toaTheoTau.getOrDefault(ct.maTau, Collections.emptyList());
		for (ToaOption t : toas) {
			cboToaMoi.addItem(t);
		}
	}

	private void taiGheMoi() {
		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		ToaOption toa = (ToaOption) cboToaMoi.getSelectedItem();
		if (ct == null || toa == null) return;
		
		cboGheMoi.removeAllItems();
		cboGheMoi.addItem("Chọn chỗ...");
		Set<String> daDat = hoaDonController.layGheDaDat(ct.maCT, toa.maToa);
		
		int rows = (int) Math.ceil((double) toa.soGhe / 4);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < 4; c++) {
				int idx = r * 4 + c;
				if (idx >= toa.soGhe) break;
				String seat = String.valueOf((char)('A' + c)) + String.format("%02d", r + 1);
				if (!daDat.contains(seat)) {
					cboGheMoi.addItem(seat);
				}
			}
		}
	}

	private void xuLyChonVeMoi() {
		if (cboGheMoi.getSelectedIndex() <= 0) {
			btnXacNhan.setEnabled(false);
			lblNewMa.setText("—");
			lblNewKhach.setText("—");
			lblNewTuyen.setText("—");
			lblChenhLech.setText("Chênh lệch: —");
			return;
		}

		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		ToaOption toa = (ToaOption) cboToaMoi.getSelectedItem();
		String ghe = (String) cboGheMoi.getSelectedItem();

		lblNewMa.setText("Vé mới: " + ghe);
		lblNewKhach.setText(lblCurrentKhach.getText());
		lblNewTuyen.setText(ct.maCT + " | Toa " + toa.maToa);
		
		// Giả định giá mới tương đương (Cần logic tính giá thực tế nếu có bảng giá)
		lblChenhLech.setText("Chênh lệch: 0đ (Dự kiến)");
		btnXacNhan.setEnabled(true);
	}

	private void xuLyXacNhan() {
		if (maVeDangXuLy == null || cboGheMoi.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn chỗ mới trước khi xác nhận.");
			return;
		}

		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		ToaOption toa = (ToaOption) cboToaMoi.getSelectedItem();
		String ghe = (String) cboGheMoi.getSelectedItem();

		// Hiện tại hệ thống yêu cầu maVeMoi tồn tại. 
		// Ta sẽ tìm xem trong Trip mới đã có Ticket ID nào cho vị trí này chưa.
		// Nếu chưa, hệ thống này cần được mở rộng thêm DAO để tạo vé.
		// Tạm thời lấy mã vé giả định hoặc báo lỗi nếu không khớp.
		
		String ghiChu = "Đổi sang " + ct.maCT + " Toa " + toa.maToa + " Ghế " + ghe;
		KetQuaXuLy xacNhan = doiTraController.taoDonDoiTra(maVeDangXuLy, null, "DOI", ghiChu);
		
		if (xacNhan.thanhCong) {
			JOptionPane.showMessageDialog(this, "Đã ghi nhận yêu cầu đổi vé sang:\n" + ghiChu);
			lblTrangThai.setText("Đổi vé thành công.");
			resetThongTin();
		} else {
			JOptionPane.showMessageDialog(this, "Lỗi: " + xacNhan.thongBao);
		}
	}

	private void resetThongTin() {
		maVeDangXuLy = null;
		veDangXuLy = null;
		btnXacNhan.setEnabled(false);
		cboChuyenMoi.removeAllItems();
		cboToaMoi.removeAllItems();
		cboGheMoi.removeAllItems();
		cboChuyenMoi.setEnabled(false);
		cboToaMoi.setEnabled(false);
		cboGheMoi.setEnabled(false);
		lblCurrentMa.setText("—");
		lblCurrentKhach.setText("—");
		lblCurrentTuyen.setText("—");
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("—");
		lblChenhLech.setText("Chênh lệch: —");
	}

	private static class ChuyenTauOption {
		String maCT, maTau, maTuyen, thoiGianKhoiHanh;
		ChuyenTauOption(String maCT, String maTau, String maTuyen, String thoiGian) {
			this.maCT = maCT; this.maTau = maTau; this.maTuyen = maTuyen; this.thoiGianKhoiHanh = thoiGian;
		}
		@Override public String toString() { return maCT + " (" + thoiGianKhoiHanh + ")"; }
	}

	private static class ToaOption {
		String maToa, loaiToa; int soGhe;
		ToaOption(String maToa, String loai, int ghe) {
			this.maToa = maToa; this.loaiToa = loai; this.soGhe = ghe;
		}
		@Override public String toString() { return maToa + " - " + loaiToa + " (" + soGhe + " ghế)"; }
	}
}
