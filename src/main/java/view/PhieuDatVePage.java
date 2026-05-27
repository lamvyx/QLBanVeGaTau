package view;

import controller.ChuyenTauController;
import controller.HoaDonController;
import controller.KhachHangController;
import controller.PhieuDatVeController;
import controller.ToaController;
import controller.TuyenTauController;
import entity.ChiTietPhieuDat;
import entity.ChuyenTau;
import entity.KhachHang;
import entity.PhieuDatVeInfo;
import entity.Toa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PhieuDatVePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_NEN = Color.decode("#F3F6FB");
	private static final Color MAU_TEXT = Color.decode("#35506B");

	private final JPanel contentPanel = new JPanel(new BorderLayout());
	private final JPanel mainCard = new JPanel(new BorderLayout());

	private BanVeSearchPanel searchPanel;
	private BanVeTripListPanel tripListPanel;
	private BanVeSeatPanel seatPanel;
	private PhieuDatVeSummaryPanel summaryPanel;
	private JPanel seatCardContainer;

	private final Set<String> selectedSeats = new LinkedHashSet<>();
	private final Set<String> bookedSeats = new LinkedHashSet<>();

	private JComboBox<KhachHangOption> cboKhachHang;
	private JComboBox<ToaOption> cboToaTau;
	private JTextField txtMaPhieuTim;

	private final PhieuDatVeController phieuDatVeController = new PhieuDatVeController();
	private final KhachHangController khachHangController = new KhachHangController();
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final HoaDonController hoaDonController = new HoaDonController();
	private final ToaController toaController = new ToaController();
	private final TuyenTauController tuyenTauController = new TuyenTauController();

	private final List<KhachHangOption> dsKhachHang = new ArrayList<>();
	private final Map<String, List<ToaOption>> toaTheoChuyen = new HashMap<>();

	private String selectedMaKH;
	private String selectedMaCT;
	private String selectedMaToa;
	private int currentSeatCount = 32;
	private BigDecimal selectedPrice = new BigDecimal("1105000");

	private String selectedKhachHang = "Chưa chọn";
	private String selectedChuyen = "Chưa chọn";
	private String selectedTuyen = "";
	private String selectedKhoiHanh = "";
	private String selectedToa = "Chưa chọn";

	private PhieuDatVeInfo currentReservation;

	public PhieuDatVePage() {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);

		searchPanel = new BanVeSearchPanel(this::onSearchTrips);
		tripListPanel = new BanVeTripListPanel(this::onTripSelected);
		seatPanel = new BanVeSeatPanel(selectedSeats, bookedSeats, this::refreshSummary);
		summaryPanel = new PhieuDatVeSummaryPanel(this::saveReservation, this::convertToSale, this::resetForm);

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);

		buildMainCard();
		taiDuLieuBanDau();
		resetForm();
		showCard(mainCard);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDE5F2")));
		header.setPreferredSize(new Dimension(0, 62));

		JLabel title = new JLabel("Đặt vé khách gọi điện");
		title.setFont(new Font("Segoe UI", Font.BOLD, 20));
		title.setForeground(MAU_TEXT);
		title.setBorder(new EmptyBorder(0, 16, 0, 0));
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Lưu phiếu đặt rồi chuyển trực tiếp sang bán vé khi khách đến");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		subtitle.setForeground(new Color(108, 122, 138));
		subtitle.setBorder(new EmptyBorder(0, 0, 0, 16));
		header.add(subtitle, BorderLayout.EAST);
		return header;
	}

	private JPanel taoBody() {
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
		return contentPanel;
	}

	private void buildMainCard() {
		mainCard.setOpaque(false);
		mainCard.setLayout(new BorderLayout(16, 16));

		JPanel left = new JPanel(new BorderLayout(0, 16));
		left.setOpaque(false);
		left.add(createLoadBar(), BorderLayout.NORTH);
		left.add(searchPanel, BorderLayout.CENTER);

		JPanel centerWrap = new JPanel(new GridBagLayout());
		centerWrap.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(0, 0, 16, 0);
		centerWrap.add(tripListPanel, gbc);

		gbc.gridy = 1;
		centerWrap.add(createBookingInfoPanel(), gbc);

		gbc.gridy = 2; gbc.weighty = 0.5; gbc.fill = GridBagConstraints.BOTH;
		seatCardContainer = createSeatContainer();
		centerWrap.add(seatCardContainer, gbc);

		JScrollPane leftScroll = new JScrollPane(centerWrap);
		leftScroll.setOpaque(false);
		leftScroll.getViewport().setOpaque(false);
		leftScroll.setBorder(null);
		leftScroll.getVerticalScrollBar().setUnitIncrement(16);
		left.add(leftScroll, BorderLayout.CENTER);

		mainCard.add(left, BorderLayout.CENTER);
		mainCard.add(summaryPanel, BorderLayout.EAST);
	}

	private JPanel createLoadBar() {
		JPanel bar = new JPanel(new BorderLayout(8, 0));
		bar.setBackground(Color.WHITE);
		bar.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
				new EmptyBorder(12, 14, 12, 14)));

		JLabel lbl = new JLabel("Mã phiếu đặt:");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lbl.setForeground(MAU_TEXT);
		bar.add(lbl, BorderLayout.WEST);

		JPanel right = new JPanel(new BorderLayout(6, 0));
		right.setOpaque(false);
		txtMaPhieuTim = new JTextField();
		right.add(txtMaPhieuTim, BorderLayout.CENTER);

		JButton btnTai = new JButton("Nạp phiếu");
		AppTheme.stylePrimaryButton(btnTai);
		btnTai.addActionListener(e -> loadReservation());
		right.add(btnTai, BorderLayout.EAST);
		bar.add(right, BorderLayout.CENTER);
		return bar;
	}

	private JPanel createBookingInfoPanel() {
		JPanel wrap = new JPanel(new GridBagLayout());
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
				new EmptyBorder(14, 14, 14, 14)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		addLabelField(wrap, gbc, 0, 0, "Khách hàng *", createCustomerCombo());
		addLabelField(wrap, gbc, 1, 0, "Toa tàu *", createToaCombo());
		return wrap;
	}

	private void addLabelField(JPanel p, GridBagConstraints gbc, int col, int row, String label, java.awt.Component field) {
		gbc.gridy = row; gbc.gridx = col * 2;
		gbc.weightx = 0.15;
		JLabel l = new JLabel(label);
		l.setFont(new Font("Segoe UI", Font.BOLD, 13));
		l.setForeground(MAU_TEXT);
		p.add(l, gbc);

		gbc.gridx = col * 2 + 1;
		gbc.weightx = 0.35;
		p.add(field, gbc);
	}

	private JPanel createSeatContainer() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
				new EmptyBorder(14, 14, 14, 14)));
		JLabel title = new JLabel("2. Chọn chỗ ngồi");
		title.setFont(new Font("Segoe UI", Font.BOLD, 15));
		title.setForeground(MAU_TEXT);
		wrap.add(title, BorderLayout.NORTH);
		wrap.add(seatPanel, BorderLayout.CENTER);
		wrap.setVisible(false);
		return wrap;
	}

	private JComboBox<KhachHangOption> createCustomerCombo() {
		cboKhachHang = new JComboBox<>();
		cboKhachHang.addActionListener(e -> {
			KhachHangOption opt = (KhachHangOption) cboKhachHang.getSelectedItem();
			if (opt != null) {
				selectedKhachHang = opt.tenKH;
				selectedMaKH = opt.maKH;
				refreshSummary();
			}
		});
		return cboKhachHang;
	}

	private JComboBox<ToaOption> createToaCombo() {
		cboToaTau = new JComboBox<>();
		cboToaTau.addActionListener(e -> {
			ToaOption opt = (ToaOption) cboToaTau.getSelectedItem();
			if (opt != null) {
				selectedMaToa = opt.maToa;
				selectedToa = opt.maToa + " (" + opt.loaiToa + ")";
				selectedPrice = opt.giaVe;
				currentSeatCount = opt.soGhe;
				selectedSeats.clear();
				capNhatGheDaDatTheoToa();
				seatPanel.setSeatCount(currentSeatCount);
				if (seatCardContainer != null) seatCardContainer.setVisible(true);
				refreshSummary();
			}
		});
		return cboToaTau;
	}

	private void onSearchTrips(String gaDi, String gaDen, LocalDate ngayDi) {
		List<ChuyenTau> trips = chuyenTauController.traCuuChuyenTau(gaDi, gaDen, ngayDi);
		tripListPanel.setTrips(trips);
		if (trips.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào phù hợp.", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void onTripSelected(ChuyenTau trip) {
		if (trip == null) return;
		selectedMaCT = trip.getMaCT();
		selectedChuyen = trip.getMaCT();
		try {
			var list = tuyenTauController.timKiemTuyenTau(trip.getMaTuyenTau(), null, null);
			if (list != null && !list.isEmpty()) {
				var tt = list.get(0);
				selectedTuyen = (tt.getMaGaDi() == null ? "" : tt.getMaGaDi()) + " → " + (tt.getMaGaDen() == null ? "" : tt.getMaGaDen());
			} else {
				selectedTuyen = trip.getMaTuyenTau();
			}
		} catch (Exception ex) {
			selectedTuyen = trip.getMaTuyenTau();
		}
		selectedKhoiHanh = trip.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		taiToaTheoChuyen(trip.getMaCT());
		refreshSummary();
	}

	private void taiToaTheoChuyen(String maCT) {
		cboToaTau.removeAllItems();
		List<ToaOption> ds = toaTheoChuyen.getOrDefault(maCT, Collections.emptyList());
		for (ToaOption t : ds) cboToaTau.addItem(t);
		if (cboToaTau.getItemCount() > 0) cboToaTau.setSelectedIndex(0);
		else {
			selectedMaToa = null; selectedToa = "Chưa chọn";
			bookedSeats.clear(); selectedSeats.clear();
			seatPanel.refreshUI();
		}
	}

	private void refreshSummary() {
		BigDecimal base = selectedPrice.multiply(BigDecimal.valueOf(selectedSeats.size()));
		String gheStr = selectedSeats.isEmpty() ? "Chưa chọn" : String.join(", ", selectedSeats);
		summaryPanel.updateOrderInfo(selectedKhachHang, selectedChuyen, selectedTuyen, selectedKhoiHanh, selectedToa,
				gheStr, selectedSeats.size());
		summaryPanel.updatePriceInfo(selectedPrice, base);
		summaryPanel.updateReservationCode(currentReservation == null ? null : currentReservation.getPhieuDatVe().getMaPhieu());
	}

	private void saveReservation() {
		if (selectedSeats.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một chỗ ngồi.");
			return;
		}
		if (selectedMaKH == null || selectedMaCT == null || selectedMaToa == null) {
			JOptionPane.showMessageDialog(this, "Thiếu thông tin cần thiết.");
			return;
		}
		var res = phieuDatVeController.taoPhieuDat("NV001", selectedMaKH, selectedMaCT, selectedMaToa,
				new ArrayList<>(selectedSeats), selectedPrice, "Đặt vé qua điện thoại");
		if (!res.thanhCong) {
			JOptionPane.showMessageDialog(this, "Lỗi: " + res.thongBao);
			capNhatGheDaDatTheoToa();
			seatPanel.refreshUI();
			return;
		}
		currentReservation = res.phieuDatVeInfo;
		summaryPanel.updateReservationCode(res.maPhieu);
		summaryPanel.setChuyenSangBanEnabled(true);
		JOptionPane.showMessageDialog(this, "Đã lưu phiếu đặt: " + res.maPhieu);
	}

	private void convertToSale() {
		if (currentReservation == null || currentReservation.getPhieuDatVe() == null) {
			JOptionPane.showMessageDialog(this, "Hãy nạp hoặc lưu một phiếu đặt trước.");
			return;
		}
		javax.swing.JFrame frame = new javax.swing.JFrame("Bán vé từ phiếu đặt");
		frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(new BanVePage(currentReservation));
		frame.setSize(1400, 900);
		frame.setLocationRelativeTo(this);
		frame.setVisible(true);
	}

	private void loadReservation() {
		String maPhieu = txtMaPhieuTim.getText() == null ? "" : txtMaPhieuTim.getText().trim();
		if (maPhieu.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Nhập mã phiếu đặt cần nạp.");
			return;
		}
		PhieuDatVeInfo info = phieuDatVeController.layPhieuDatTheoMa(maPhieu);
		if (info == null || info.getPhieuDatVe() == null) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt.");
			return;
		}
		applyReservation(info);
	}

	private void applyReservation(PhieuDatVeInfo info) {
		currentReservation = info;
		selectedSeats.clear();
		bookedSeats.clear();
		if (info.getPhieuDatVe() != null) {
			KhachHang kh = khachHangController.timKiemKhachHang(info.getPhieuDatVe().getMaKH(), null, null).stream().findFirst().orElse(null);
			if (kh != null) {
				selectedMaKH = kh.getMaKH();
				selectedKhachHang = kh.getTenKH();
			}
		}
		if (info.getChiTietList() != null && !info.getChiTietList().isEmpty()) {
			ChiTietPhieuDat ct = info.getChiTietList().get(0);
			selectedMaCT = ct.getMaCT();
			selectedMaToa = ct.getMaToa();
			selectedPrice = BigDecimal.valueOf(ct.getGiaVe());
			List<ChuyenTau> chuyenList = chuyenTauController.timKiemChuyenTau(selectedMaCT);
			if (chuyenList != null && !chuyenList.isEmpty()) {
				ChuyenTau chuyen = chuyenList.get(0);
				selectedChuyen = chuyen.getMaCT();
				selectedKhoiHanh = chuyen.getNgayKhoiHanh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				try {
					var tuyenList = tuyenTauController.timKiemTuyenTau(chuyen.getMaTuyenTau(), null, null);
					if (tuyenList != null && !tuyenList.isEmpty()) {
						var tt = tuyenList.get(0);
						selectedTuyen = (tt.getMaGaDi() == null ? "" : tt.getMaGaDi()) + " → " + (tt.getMaGaDen() == null ? "" : tt.getMaGaDen());
					} else {
						selectedTuyen = chuyen.getMaTuyenTau();
					}
				} catch (Exception ex) {
					selectedTuyen = chuyen.getMaTuyenTau();
				}
			}
			List<Toa> toaList = toaController.timKiemToa(selectedMaToa);
			if (toaList != null && !toaList.isEmpty()) {
				Toa toa = toaList.get(0);
				currentSeatCount = toa.getSoGhe();
				selectedToa = toa.getMaToa() + " (" + toa.getLoaiToa() + ")";
				seatPanel.setSeatCount(currentSeatCount);
			} else {
				selectedToa = ct.getMaToa();
			}
			for (ChiTietPhieuDat item : info.getChiTietList()) {
				if (item.getViTriGhe() != null && !item.getViTriGhe().isBlank()) {
					selectedSeats.add(item.getViTriGhe().trim().toUpperCase());
				}
			}
		}
		capNhatGheDaDatTheoToa();
		bookedSeats.removeAll(selectedSeats);
		if (seatCardContainer != null) seatCardContainer.setVisible(true);
		seatPanel.refreshUI();
		summaryPanel.setChuyenSangBanEnabled(true);
		summaryPanel.updateReservationCode(info.getPhieuDatVe().getMaPhieu());
		refreshSummary();
	}

	private void capNhatGheDaDatTheoToa() {
		bookedSeats.clear();
		if (selectedMaCT == null || selectedMaToa == null) return;
		Set<String> soldAndHeld = hoaDonController.layGheDaDat(selectedMaCT, selectedMaToa);
		bookedSeats.addAll(soldAndHeld);
		bookedSeats.removeAll(selectedSeats);
	}

	private void resetForm() {
		selectedSeats.clear();
		currentReservation = null;
		selectedMaKH = null;
		selectedMaCT = null;
		selectedMaToa = null;
		currentSeatCount = 32;
		selectedPrice = new BigDecimal("1105000");
		selectedKhachHang = "Chưa chọn";
		selectedChuyen = "Chưa chọn";
		selectedTuyen = "";
		selectedKhoiHanh = "";
		selectedToa = "Chưa chọn";
		summaryPanel.updateReservationCode(null);
		summaryPanel.setChuyenSangBanEnabled(false);
		if (cboKhachHang != null && cboKhachHang.getItemCount() > 0) cboKhachHang.setSelectedIndex(0);
		if (cboToaTau != null) cboToaTau.removeAllItems();
		if (tripListPanel != null) tripListPanel.setTrips(new ArrayList<>());
		if (seatCardContainer != null) seatCardContainer.setVisible(false);
		capNhatGheDaDatTheoToa();
		seatPanel.refreshUI();
		refreshSummary();
	}

	private void taiDuLieuBanDau() {
		dsKhachHang.clear();
		for (KhachHang kh : khachHangController.layTatCaKhachHang()) {
			dsKhachHang.add(new KhachHangOption(kh.getMaKH(), kh.getTenKH(), kh.getSdt()));
		}
		cboKhachHang.removeAllItems();
		for (KhachHangOption o : dsKhachHang) cboKhachHang.addItem(o);

		toaTheoChuyen.clear();
		for (ChuyenTau trip : chuyenTauController.timKiemChuyenTau(null)) {
			List<ToaOption> dsToa = new ArrayList<>();
			for (Toa toa : toaController.layToaTheoChuyenTau(trip.getMaCT())) {
				dsToa.add(new ToaOption(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe(),
						BanVeUtils.xacDinhGiaVeTheoLoaiToa(toa.getLoaiToa())));
			}
			toaTheoChuyen.put(trip.getMaCT(), dsToa);
		}
	}

	private void showCard(JPanel card) {
		contentPanel.removeAll();
		contentPanel.add(card, BorderLayout.CENTER);
		contentPanel.revalidate(); contentPanel.repaint();
	}
}
