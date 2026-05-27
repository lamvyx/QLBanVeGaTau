package view;

import controller.ChuyenTauController;
import controller.DoiTraController;
import controller.HoaDonController;
import controller.ToaController;
import controller.TuyenTauController;
import entity.ChuyenTau;
import entity.Toa;
import entity.TuyenTau;
import entity.VeTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
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
	private final JLabel lblCurrentGhe = taoDongGiaTri("—");
	private final JLabel lblCurrentGia = taoDongGiaTri("—");

	private final JLabel lblNewMa = taoDongGiaTri("—");
	private final JLabel lblNewKhach = taoDongGiaTri("—");
	private final JLabel lblNewTuyen = taoDongGiaTri("—");
	private final JLabel lblNewGhe = taoDongGiaTri("—");
	private final JLabel lblNewGia = taoDongGiaTri("—");
	private final JLabel lblChenhLech = new JLabel("Chênh lệch: —");

	private final JComboBox<String> cboGaDiMoi = new JComboBox<>();
	private final JComboBox<String> cboGaDenMoi = new JComboBox<>();
	private final JButton btnTimChuyenMoi = new JButton("Tìm chuyến");
	private final JComboBox<ChuyenTauOption> cboChuyenMoi = new JComboBox<>();
	private final JComboBox<ToaOption> cboToaMoi = new JComboBox<>();
	private final JComboBox<String> cboGheMoi = new JComboBox<>();
	private final JPanel seatMapPanel = new JPanel();
	private String selectedSeat = null;
	private final JLabel lblSelectedSeatSmall = new JLabel("Chưa chọn");
	private final JLabel lblSeatLegendAvailable = new JLabel();
	private final JLabel lblSeatLegendBooked = new JLabel();
	private final JLabel lblSeatLegendSelected = new JLabel();
	private final JButton btnXacNhan = new JButton("Xác nhận đổi vé");
	
	private String maVeDangXuLy;
	private VeTau veDangXuLy;
	
	private final DoiTraController doiTraController = new DoiTraController();
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final TuyenTauController tuyenTauController = new TuyenTauController();
	private final ToaController toaController = new ToaController();
	private final HoaDonController hoaDonController = new HoaDonController();
	
	private final List<ChuyenTauOption> dsChuyen = new ArrayList<>();
	private final Map<String, List<ToaOption>> toaTheoTau = new HashMap<>();

	public DoiVePage() {
		setLayout(new BorderLayout(0, 16));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoCardTimVe(), BorderLayout.NORTH);
		add(taoCardSoSanh(), BorderLayout.CENTER);
		napDanhSachGa();
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

		JPanel lines = new JPanel(new GridLayout(5, 1, 0, 8));
		lines.setOpaque(false);
		lines.add(lblCurrentMa);
		lines.add(lblCurrentKhach);
		lines.add(lblCurrentTuyen);
		lines.add(lblCurrentGhe);
		lines.add(lblCurrentGia);

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

		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new javax.swing.BoxLayout(selectionPanel, javax.swing.BoxLayout.Y_AXIS));
		selectionPanel.setOpaque(false);

		JPanel routePanel = new JPanel(new GridLayout(1, 3, 8, 0));
		routePanel.setOpaque(false);
		cboGaDiMoi.setToolTipText("Chọn ga đi");
		cboGaDenMoi.setToolTipText("Chọn ga đến");
		AppTheme.stylePrimaryButton(btnTimChuyenMoi);
		btnTimChuyenMoi.setEnabled(true);
		btnTimChuyenMoi.addActionListener(e -> timChuyenTheoGa());
		routePanel.add(cboGaDiMoi);
		routePanel.add(cboGaDenMoi);
		routePanel.add(btnTimChuyenMoi);
		selectionPanel.add(routePanel);
		selectionPanel.add(javax.swing.Box.createVerticalStrut(8));

		// Chọn chuyến
		JPanel row1 = new JPanel(new BorderLayout(8, 0));
		row1.setOpaque(false);
		row1.add(new JLabel("Chuyến mới:"), BorderLayout.WEST);
		cboChuyenMoi.addActionListener(e -> taiToaMoi());
		row1.add(cboChuyenMoi, BorderLayout.CENTER);
		selectionPanel.add(row1);
		selectionPanel.add(javax.swing.Box.createVerticalStrut(8));

		// Chọn toa
		JPanel row2 = new JPanel(new BorderLayout(8, 0));
		row2.setOpaque(false);
		row2.add(new JLabel("Toa mới:      "), BorderLayout.WEST);
		cboToaMoi.addActionListener(e -> taiGheMoi());
		row2.add(cboToaMoi, BorderLayout.CENTER);
		selectionPanel.add(row2);
		selectionPanel.add(javax.swing.Box.createVerticalStrut(8));

		// Chọn ghế (sử dụng seat map hoặc combo box)
		JPanel row3 = new JPanel(new BorderLayout(8, 0));
		row3.setOpaque(false);
		row3.add(new JLabel("Chỗ mới:      "), BorderLayout.WEST);
		cboGheMoi.addActionListener(e -> {
			String seat = (String) cboGheMoi.getSelectedItem();
			if (seat != null && !seat.startsWith("Chọn")) {
				selectedSeat = seat;
				lblSelectedSeatSmall.setText(seat);
				for (java.awt.Component comp : seatMapPanel.getComponents()) {
					if (comp instanceof JButton btn) {
						if (seat.equals(btn.getText())) {
							btn.setBackground(Color.decode("#BEE3FF"));
						} else if (btn.isEnabled()) {
							btn.setBackground(Color.decode("#E6F4FF"));
						}
					}
				}
				xuLyChonVeMoi();
			}
		});
		row3.add(cboGheMoi, BorderLayout.CENTER);
		selectionPanel.add(row3);
		selectionPanel.add(javax.swing.Box.createVerticalStrut(8));

		// Seat map (clickable preview) - primary selector wrapped in JScrollPane
		seatMapPanel.setOpaque(false);
		seatMapPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		javax.swing.JScrollPane seatScrollPane = new javax.swing.JScrollPane(seatMapPanel);
		seatScrollPane.setOpaque(false);
		seatScrollPane.getViewport().setOpaque(false);
		seatScrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DDE5F2"), 1));
		seatScrollPane.setPreferredSize(new java.awt.Dimension(0, 240));
		seatScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		selectionPanel.add(seatScrollPane);
		selectionPanel.add(javax.swing.Box.createVerticalStrut(8));

		// Legend
		JPanel legend = new JPanel(new GridLayout(1, 6, 8, 0));
		legend.setOpaque(false);
		lblSeatLegendAvailable.setOpaque(true);
		lblSeatLegendAvailable.setBackground(Color.decode("#E6F4FF"));
		lblSeatLegendAvailable.setBorder(BorderFactory.createLineBorder(Color.decode("#CFEAFF")));
		lblSeatLegendAvailable.setText("  Trống");
		lblSeatLegendBooked.setOpaque(true);
		lblSeatLegendBooked.setBackground(Color.decode("#F3F4F6"));
		lblSeatLegendBooked.setBorder(BorderFactory.createLineBorder(Color.decode("#E8EAEE")));
		lblSeatLegendBooked.setText("  Đã đặt");
		lblSeatLegendSelected.setOpaque(true);
		lblSeatLegendSelected.setBackground(Color.decode("#BEE3FF"));
		lblSeatLegendSelected.setBorder(BorderFactory.createLineBorder(Color.decode("#9FD6FF")));
		lblSeatLegendSelected.setText("  Đã chọn");
		legend.add(lblSeatLegendAvailable);
		legend.add(new JLabel(" "));
		legend.add(lblSeatLegendBooked);
		legend.add(new JLabel(" "));
		legend.add(lblSeatLegendSelected);
		legend.add(new JLabel(" "));
		selectionPanel.add(legend);

		JPanel lines = new JPanel(new GridLayout(6, 1, 0, 8));
		lines.setOpaque(false);
		lines.add(lblNewMa);
		lines.add(lblNewKhach);
		lines.add(lblNewTuyen);
		lines.add(lblNewGhe);
		lines.add(lblNewGia);
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

	private void capNhatThongTinVeHienTai(VeTau veHienTai) {
		TuyenTau tuyen = timTuyenTheoMa(veHienTai.getMaChuyenTau());
		String gaDi = tuyen != null && tuyen.getMaGaDi() != null ? tuyen.getMaGaDi() : "—";
		String gaDen = tuyen != null && tuyen.getMaGaDen() != null ? tuyen.getMaGaDen() : "—";
		String toa = veHienTai.getMaToa() == null ? "—" : veHienTai.getMaToa();
		String ghe = veHienTai.getViTriGhe() == null ? "—" : veHienTai.getViTriGhe();
		BigDecimal gia = veHienTai.getGiaVe() == null ? BigDecimal.ZERO : veHienTai.getGiaVe();
		lblCurrentMa.setText("Mã vé: " + veHienTai.getMaVeTau());
		lblCurrentKhach.setText("KH: " + veHienTai.getMaKH());
		lblCurrentTuyen.setText("Chuyến: " + veHienTai.getMaChuyenTau() + " | Ga đi: " + gaDi + " -> Ga đến: " + gaDen);
		lblCurrentGhe.setText("Toa/Ghế: " + toa + " / " + ghe);
		lblCurrentGia.setText("Giá vé: " + BanVeUtils.formatMoney(gia));
	}

	private TuyenTau timTuyenTheoMa(String maChuyenTau) {
		if (maChuyenTau == null || maChuyenTau.isBlank()) {
			return null;
		}
		List<ChuyenTau> ds = chuyenTauController.timKiemChuyenTau(maChuyenTau.trim());
		if (ds.isEmpty()) {
			return null;
		}
		ChuyenTau ct = ds.get(0);
		TuyenTau tuyen = tuyenTauController.timKiemTuyenTau(ct.getMaTuyenTau(), null, null)
				.stream()
				.findFirst()
				.orElse(null);
		return tuyen;
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
		capNhatThongTinVeHienTai(veHienTai);
		napDanhSachGa();
		taiDuLieuChuyenMoi();

		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("Đã tìm thấy vé " + maVe + ". Chọn ga đi/ga đến để tìm chuyến mới.");
		btnXacNhan.setEnabled(false);
		selectedSeat = null;
		lblSelectedSeatSmall.setText("Chưa chọn");
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("—");
		lblNewGhe.setText("—");
		lblNewGia.setText("—");
		lblChenhLech.setText("Chênh lệch: —");
	}

	private void napDanhSachGa() {
		List<TuyenTau> dsTuyen = tuyenTauController.timKiemTuyenTau(null, null, null);
		java.util.Set<String> gaDiSet = new java.util.LinkedHashSet<>();
		java.util.Set<String> gaDenSet = new java.util.LinkedHashSet<>();
		for (TuyenTau tt : dsTuyen) {
			if (tt.getMaGaDi() != null && !tt.getMaGaDi().isBlank()) {
				gaDiSet.add(tt.getMaGaDi().trim());
			}
			if (tt.getMaGaDen() != null && !tt.getMaGaDen().isBlank()) {
				gaDenSet.add(tt.getMaGaDen().trim());
			}
		}
		cboGaDiMoi.removeAllItems();
		cboGaDenMoi.removeAllItems();
		cboGaDiMoi.addItem("Chọn ga đi");
		cboGaDenMoi.addItem("Chọn ga đến");
		for (String ga : gaDiSet) {
			cboGaDiMoi.addItem(ga);
		}
		for (String ga : gaDenSet) {
			cboGaDenMoi.addItem(ga);
		}
	}

	private void timChuyenTheoGa() {
		String gaDi = (String) cboGaDiMoi.getSelectedItem();
		String gaDen = (String) cboGaDenMoi.getSelectedItem();
		if (gaDi == null || gaDen == null || gaDi.startsWith("Chọn") || gaDen.startsWith("Chọn")) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ga đi và ga đến trước khi tìm chuyến.");
			return;
		}
		List<ChuyenTau> ds = chuyenTauController.traCuuChuyenTau(gaDi, gaDen, null);
		fillDanhSachChuyen(ds);
		if (ds.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu phù hợp với ga đã chọn.");
		}
	}

	private void fillDanhSachChuyen(List<ChuyenTau> danhSach) {
		dsChuyen.clear();
		cboChuyenMoi.removeAllItems();
		for (ChuyenTau ct : danhSach) {
			dsChuyen.add(new ChuyenTauOption(ct.getMaCT(), ct.getMaTau(), ct.getMaTuyenTau(), ct.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
		}
		for (ChuyenTauOption opt : dsChuyen) {
			cboChuyenMoi.addItem(opt);
		}
		cboToaMoi.removeAllItems();
		cboGheMoi.removeAllItems();
		seatMapPanel.removeAll();
		cboToaMoi.setEnabled(!dsChuyen.isEmpty());
		cboGheMoi.setEnabled(false);
		if (!dsChuyen.isEmpty()) {
			cboChuyenMoi.setSelectedIndex(0);
		}
	}

	private void taiDuLieuChuyenMoi() {
		toaTheoTau.clear();
		List<Toa> dsToa = toaController.timKiemToa(null);
		for (Toa t : dsToa) {
			toaTheoTau.computeIfAbsent(t.getMaTau(), k -> new ArrayList<>()).add(new ToaOption(t.getMaToa(), t.getLoaiToa(), t.getSoGhe()));
		}
		cboChuyenMoi.setEnabled(true);
		cboToaMoi.setEnabled(false);
		cboGheMoi.setEnabled(false);
	}

	private void taiToaMoi() {
		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		if (ct == null) {
			return;
		}
		cboToaMoi.removeAllItems();
		cboGheMoi.removeAllItems();
		seatMapPanel.removeAll();
		selectedSeat = null;
		lblSelectedSeatSmall.setText("Chưa chọn");
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("Chuyến: " + ct.maCT + " | Tàu: " + ct.maTau + " | Tuyến: " + ct.maTuyen);
		lblNewGhe.setText("Toa/Ghế: —");
		lblNewGia.setText("Giá vé: —");
		lblChenhLech.setText("Chênh lệch: —");
		List<ToaOption> toas = toaTheoTau.getOrDefault(ct.maTau, Collections.emptyList());
		for (ToaOption t : toas) {
			cboToaMoi.addItem(t);
		}
		cboToaMoi.setEnabled(!toas.isEmpty());
	}

	private void taiGheMoi() {
		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		ToaOption toa = (ToaOption) cboToaMoi.getSelectedItem();
		if (ct == null || toa == null) return;
		
		cboGheMoi.removeAllItems();
		cboGheMoi.addItem("Chọn chỗ...");
		Set<String> daDat = hoaDonController.layGheDaDat(ct.maCT, toa.maToa);
		// Build combo list
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

		// Build seat map buttons
		seatMapPanel.removeAll();
		seatMapPanel.setLayout(new java.awt.GridLayout(rows, 4, 8, 8));
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < 4; c++) {
				int idx = r * 4 + c;
				if (idx >= toa.soGhe) {
					seatMapPanel.add(new JPanel());
					continue;
				}
				String seat = String.valueOf((char)('A' + c)) + String.format("%02d", r + 1);
				JButton b = new JButton(seat);
				b.setFont(AppTheme.font(Font.BOLD, 12));
				b.setFocusPainted(false);
				BigDecimal priceForSeat = BanVeUtils.xacDinhGiaVeTheoLoaiToa(toa.loaiToa);
				b.setToolTipText("Loại: " + toa.loaiToa + " — Giá: " + BanVeUtils.formatMoney(priceForSeat));
				if (daDat.contains(seat)) {
					b.setEnabled(false);
					b.setBackground(Color.decode("#F3F4F6"));
					b.setToolTipText("Đã đặt");
				} else {
					b.setBackground(Color.decode("#E6F4FF"));
					b.addActionListener(evt -> chonGheMoi(seat));
				}
				seatMapPanel.add(b);
			}
		}
		seatMapPanel.revalidate();
		seatMapPanel.repaint();
	}

	private void chonGheMoi(String seat) {
		selectedSeat = seat;
		cboGheMoi.setSelectedItem(seat);
		for (java.awt.Component comp : seatMapPanel.getComponents()) {
			if (comp instanceof JButton) {
				comp.setBackground(Color.decode("#E6F4FF"));
			}
		}
		for (java.awt.Component comp : seatMapPanel.getComponents()) {
			if (comp instanceof JButton btn && seat.equals(btn.getText())) {
				btn.setBackground(Color.decode("#BEE3FF"));
				break;
			}
		}
		xuLyChonVeMoi();
	}

	private void xuLyChonVeMoi() {
		if (selectedSeat == null || selectedSeat.isEmpty()) {
			btnXacNhan.setEnabled(false);
			lblNewMa.setText("—");
			lblNewKhach.setText("—");
			lblNewTuyen.setText("—");
			lblNewGhe.setText("—");
			lblNewGia.setText("—");
			lblChenhLech.setText("Chênh lệch: —");
			return;
		}

		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenMoi.getSelectedItem();
		ToaOption toa = (ToaOption) cboToaMoi.getSelectedItem();
		if (ct == null || toa == null) {
			return;
		}
		String ghe = selectedSeat;
		TuyenTau tuyen = timTuyenTheoMa(ct.maCT);
		String gaDi = tuyen != null && tuyen.getMaGaDi() != null ? tuyen.getMaGaDi() : "—";
		String gaDen = tuyen != null && tuyen.getMaGaDen() != null ? tuyen.getMaGaDen() : "—";

		lblNewMa.setText("Vé mới: " + ghe);
		lblNewKhach.setText(lblCurrentKhach.getText());
		lblNewTuyen.setText("Chuyến: " + ct.maCT + " | Ga đi: " + gaDi + " -> Ga đến: " + gaDen);
		lblNewGhe.setText("Toa/Ghế: " + toa.maToa + " / " + ghe);
		lblSelectedSeatSmall.setText(ghe);

		BigDecimal oldPrice = veDangXuLy != null && veDangXuLy.getGiaVe() != null ? veDangXuLy.getGiaVe() : BigDecimal.ZERO;
		BigDecimal newPrice = BanVeUtils.xacDinhGiaVeTheoLoaiToa(toa.loaiToa);
		BigDecimal diff = newPrice.subtract(oldPrice);
		lblNewGia.setText("Giá vé: " + BanVeUtils.formatMoney(newPrice));
		String diffText = (diff.signum() >= 0 ? "+" : "") + BanVeUtils.formatMoney(diff.abs());
		lblChenhLech.setText("Chênh lệch: " + (diff.signum() == 0 ? "0 đ" : diffText));
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
		lblCurrentGhe.setText("—");
		lblCurrentGia.setText("—");
		lblNewMa.setText("—");
		lblNewKhach.setText("—");
		lblNewTuyen.setText("—");
		lblNewGhe.setText("—");
		lblNewGia.setText("—");
		lblChenhLech.setText("Chênh lệch: —");
		selectedSeat = null;
		lblSelectedSeatSmall.setText("Chưa chọn");
		seatMapPanel.removeAll();
		seatMapPanel.revalidate();
		seatMapPanel.repaint();
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
