package view;

import connectDB.Database;
import entity.KhachHang;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BanVePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final Color MAU_NEN = AppTheme.PAGE_BG;
	private static final Color MAU_TEXT = AppTheme.TEXT_PRIMARY;
	private static final Color MAU_XANH = Color.decode("#22C55E");
	private static final Color MAU_DO = Color.decode("#EF4444");
	private static final DateTimeFormatter TICKET_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final DateTimeFormatter CHUYEN_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

	private final JPanel contentPanel = new JPanel(new BorderLayout());
	private final JPanel saleCard = new JPanel(new BorderLayout());
	private final JPanel successCard = new JPanel(new BorderLayout());

	private final Set<String> selectedSeats = new LinkedHashSet<>();
	private final Set<String> bookedSeats = new LinkedHashSet<>();
	private final List<JButton> seatButtons = new ArrayList<>();
	private final List<KhachHang> danhSachKhachHang = new ArrayList<>();
	private final List<ChuyenTauView> danhSachChuyenTau = new ArrayList<>();
	private final List<ToaView> danhSachToa = new ArrayList<>();
	private final Map<String, BigDecimal> khuyenMaiHieuLuc = new HashMap<>();
	private final Map<String, TicketDetailForm> ticketFormMap = new LinkedHashMap<>(); // Store form for each seat

	private JLabel lblTongTien;
	private JLabel lblSeatCount;
	private JLabel lblSeatSummary;
	private JLabel lblSeatPanelTitle;
	private JLabel lblMaHD;
	private JLabel lblSoVe;
	private JLabel lblKhachHang;
	private JLabel lblChuyen;
	private JLabel lblTuyen;
	private JLabel lblKhoiHanh;
	private JLabel lblThoiGianDen;
	private JLabel lblToaGhe;
	private JLabel lblGia;
	private JLabel lblNgayLap;
	private JPanel seatGridContainer;

	private JTextField txtMaKM;
	private JTextField txtGiayTo;
	private JComboBox<String> cboKhachHang;
	private JComboBox<String> cboChuyenTau;
	private JComboBox<String> cboToaTau;
	private JComboBox<String> cboDoiTuong;
	private JComboBox<String> cboThanhToan;
	private JCheckBox chkTreDuoi6CoNguoiLon;
	private JCheckBox chkDatKhuHoi;
	private JLabel lblKiemTraKhuHoi;
	private boolean dangCapNhatDanhSachToa;

	private BigDecimal selectedPrice = new BigDecimal("1105000");
	private BigDecimal discountAmount = BigDecimal.ZERO;

	private String selectedKhachHang = "Nguyễn Thị Lan";
	private String selectedHangThe = "KHACH_LE";
	private String selectedNhanVien = "Chưa xác định";
	private String selectedChuyen = "-";
	private String selectedTuyen = "-";
	private String selectedKhoiHanh = "-";
	private String selectedThoiGianDen = "-";
	private String selectedToa = "-";
	private String selectedDoiTuong = "NGUOI_LON";
	private String selectedGiayTo = "";
	private String selectedPhuongThucThanhToan = "TIEN_MAT";
	private String selectedChuyenKhuHoi = "-";
	private String selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
	private String generatedMaVe = "VE03042601";
	private List<String> generatedVeList = new ArrayList<>();
	private String selectedMaKH = null;
	private String selectedMaCT = null;
	private String selectedMaToa = null;
	private int selectedSoGhe = 32;

	public BanVePage() {
		this(null);
	}

	public BanVePage(TaiKhoan taiKhoanDangNhap) {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);
		if (taiKhoanDangNhap != null) {
			selectedNhanVien = taiKhoanDangNhap.getHoTen() + " (" + taiKhoanDangNhap.getTenDangNhap() + ")";
		}

		taiDuLieuDanhMucTuCSDL();

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);

		buildSaleCard();
		buildSuccessCard();
		resetForm();
		hienThiCard(saleCard);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
		header.setPreferredSize(new Dimension(0, 56));

		JLabel title = new JLabel("Bán vé tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 20));
		title.setForeground(MAU_TEXT);
		title.setBorder(new EmptyBorder(0, 16, 0, 0));
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Bán vé, chọn chỗ và lưu vé nháp; hóa đơn được lập ở màn riêng");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		subtitle.setBorder(new EmptyBorder(0, 0, 0, 16));
		header.add(subtitle, BorderLayout.EAST);
		return header;
	}

	private JPanel taoBody() {
		contentPanel.setOpaque(false);
		return taoKhungGiuaNoiDung(contentPanel, 1320, new EmptyBorder(8, 8, 8, 8));
	}

	private static final class TicketDetailForm {
		private final String seatCode;
		private JComboBox<String> cboKhachHang;
		private JComboBox<String> cboDoiTuong;
		private JCheckBox chkTreDuoi6CoNguoiLon;
		private JTextField txtGiayTo;
		private JComboBox<String> cboThanhToan;
		private String selectedKhachHang = "";
		private String selectedMaKH = "";
		private String selectedHangThe = "KHACH_LE";
		private String selectedDoiTuong = "NGUOI_LON";
		private String selectedGiayTo = "";
		private String selectedPhuongThucThanhToan = "TIEN_MAT";

		TicketDetailForm(String seatCode) {
			this.seatCode = seatCode;
		}

		String getSeatCode() {
			return seatCode;
		}

		void fillForm(List<KhachHang> danhSachKhachHang) {
			// Reset form values
			selectedKhachHang = "";
			selectedMaKH = "";
			selectedHangThe = "KHACH_LE";
			selectedDoiTuong = "NGUOI_LON";
			selectedGiayTo = "";
			selectedPhuongThucThanhToan = "TIEN_MAT";

			// Populate customer combo
			if (cboKhachHang != null) {
				cboKhachHang.removeAllItems();
				for (KhachHang kh : danhSachKhachHang) {
					String cccd = kh.getCccd() == null ? "" : kh.getCccd();
					String sdt = kh.getSdt() == null ? "" : kh.getSdt();
					String loai = kh.isLoaiKH() ? "THANH_VIEN" : "KHACH_LE";
					cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getTenKH() + " - " + sdt + " - " + loai + " - " + cccd);
				}
			}

			if (cboDoiTuong != null) {
				cboDoiTuong.setSelectedIndex(0);
			}
			if (txtGiayTo != null) {
				txtGiayTo.setText("");
			}
			if (cboThanhToan != null) {
				cboThanhToan.setSelectedIndex(0);
			}
			if (chkTreDuoi6CoNguoiLon != null) {
				chkTreDuoi6CoNguoiLon.setSelected(false);
			}
		}

		boolean isValid() {
			return !selectedMaKH.isBlank() && !selectedGiayTo.isBlank();
		}
	}

	private static final class ChuyenTauView {
		private final String maCT;
		private final String gaDi;
		private final String gaDen;
		private final LocalDateTime khoiHanh;

		private ChuyenTauView(String maCT, String gaDi, String gaDen, LocalDateTime khoiHanh) {
			this.maCT = maCT;
			this.gaDi = gaDi;
			this.gaDen = gaDen;
			this.khoiHanh = khoiHanh;
		}

		private String getMaCT() { return maCT; }
		private String getGaDi() { return gaDi; }
		private String getGaDen() { return gaDen; }
		private LocalDateTime getKhoiHanh() { return khoiHanh; }
	}

	private static final class ToaView {
		private final String maToa;
		private final String loaiToa;
		private final int soGhe;

		private ToaView(String maToa, String loaiToa, int soGhe) {
			this.maToa = maToa;
			this.loaiToa = loaiToa;
			this.soGhe = soGhe;
		}

		private String getMaToa() { return maToa; }
		private String getLoaiToa() { return loaiToa; }
		private int getSoGhe() { return soGhe; }
	}

	private void taiDuLieuDanhMucTuCSDL() {
		danhSachKhachHang.clear();
		danhSachKhachHang.addAll(layDanhSachKhachHang());

		danhSachChuyenTau.clear();
		danhSachChuyenTau.addAll(layDanhSachChuyenTau());

		khuyenMaiHieuLuc.clear();
		khuyenMaiHieuLuc.putAll(layKhuyenMaiConHieuLuc());
	}

	private List<KhachHang> layDanhSachKhachHang() {
		List<KhachHang> result = new ArrayList<>();
		String sql = "SELECT maKH, tenKH, sdt, CCCD, diaChi, email, gioiTinh, ngaySinh, loaiKH FROM KhachHang ORDER BY maKH";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Date ngaySinhSql = rs.getDate("ngaySinh");
				result.add(new KhachHang(
					rs.getString("maKH"),
					rs.getString("tenKH"),
					rs.getString("sdt"),
					rs.getString("CCCD"),
					rs.getString("diaChi"),
					rs.getString("email"),
					rs.getBoolean("gioiTinh"),
					ngaySinhSql == null ? null : ngaySinhSql.toLocalDate(),
					rs.getBoolean("loaiKH")
				));
			}
		} catch (SQLException ignored) {
		}
		return result;
	}

	private List<ChuyenTauView> layDanhSachChuyenTau() {
		List<ChuyenTauView> result = new ArrayList<>();
		String sql = """
				SELECT ct.maCT, ct.ngayKhoiHanh, ct.gioKhoiHanh, tt.maGaDi, tt.maGaDen
				FROM ChuyenTau ct
				JOIN TuyenTau tt ON tt.maTT = ct.maTuyenTau
				WHERE ct.trangThai = 1
				ORDER BY ct.ngayKhoiHanh, ct.gioKhoiHanh, ct.maCT
				""";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Date ngaySql = rs.getDate("ngayKhoiHanh");
				Time gioSql = rs.getTime("gioKhoiHanh");
				LocalDateTime khoiHanh = LocalDateTime.of(
					ngaySql == null ? LocalDate.now() : ngaySql.toLocalDate(),
					gioSql == null ? java.time.LocalTime.MIDNIGHT : gioSql.toLocalTime());
				result.add(new ChuyenTauView(
					rs.getString("maCT"),
					rs.getString("maGaDi"),
					rs.getString("maGaDen"),
					khoiHanh));
			}
		} catch (SQLException ignored) {
		}
		return result;
	}

	private List<ToaView> layDanhSachToaTheoChuyen(String maCT) {
		List<ToaView> result = new ArrayList<>();
		if (maCT == null || maCT.isBlank()) {
			return result;
		}
		String sql = """
				SELECT toa.maToa, toa.loaiToa, toa.soGhe
				FROM ChuyenTau ct
				JOIN Toa toa ON toa.maTau = ct.maTau
				WHERE ct.maCT = ? AND toa.trangThai = 1
				ORDER BY toa.viTriToa, toa.maToa
				""";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maCT);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result.add(new ToaView(
						rs.getString("maToa"),
						rs.getString("loaiToa"),
						rs.getInt("soGhe")
					));
				}
			}
		} catch (SQLException ignored) {
		}
		return result;
	}

	private Map<String, BigDecimal> layKhuyenMaiConHieuLuc() {
		Map<String, BigDecimal> result = new HashMap<>();
		String sql = "SELECT maKM, tyLeKM FROM KhuyenMai WHERE CAST(GETDATE() AS DATE) BETWEEN ngayBD AND ngayKT";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				BigDecimal tyLe = rs.getBigDecimal("tyLeKM");
				if (tyLe != null) {
					result.put(rs.getString("maKM"), tyLe.divide(new BigDecimal("100")));
				}
			}
		} catch (SQLException ignored) {
		}
		return result;
	}

	private BigDecimal layGiaMacDinhTheoChuyenToa(String maCT, String maToa) {
		if (maCT == null || maToa == null) {
			return null;
		}
		String sql = "SELECT AVG(giaVe) AS giaTB FROM VeTau WHERE maCT = ? AND maToa = ?";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maCT);
			ps.setString(2, maToa);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBigDecimal("giaTB");
				}
			}
		} catch (SQLException ignored) {
		}
		return null;
	}

	private List<String> layDanhSachGheDaBan(String maCT, String maToa) {
		List<String> result = new ArrayList<>();
		if (maCT == null || maToa == null) {
			return result;
		}
		String sql = """
				SELECT ctv.viTriGhe
				FROM ChiTietVeTau ctv
				JOIN VeTau v ON v.maVeTau = ctv.maVeTau
				WHERE v.maCT = ? AND v.maToa = ? AND v.trangThai IN ('DA_THANH_TOAN', 'DA_SU_DUNG')
				""";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maCT);
			ps.setString(2, maToa);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String ghe = rs.getString("viTriGhe");
					if (ghe != null && !ghe.isBlank()) {
						result.add(ghe.trim().toUpperCase());
					}
				}
			}
		} catch (SQLException ignored) {
		}
		return result;
	}

	private void capNhatKhachHangDangChon() {
		int idx = cboKhachHang == null ? -1 : cboKhachHang.getSelectedIndex();
		if (idx < 0 || idx >= danhSachKhachHang.size()) {
			selectedMaKH = null;
			selectedKhachHang = "-";
			selectedHangThe = "KHACH_LE";
			return;
		}

		KhachHang kh = danhSachKhachHang.get(idx);
		selectedMaKH = kh.getMaKH();
		selectedKhachHang = kh.getTenKH();
		selectedHangThe = kh.isLoaiKH() ? "THANH_VIEN" : "KHACH_LE";
		selectedGiayTo = kh.getCccd() == null ? "" : kh.getCccd();
		if (txtGiayTo != null) {
			txtGiayTo.setText(selectedGiayTo);
		}
		refreshSummary();
	}

	private void capNhatChuyenTauDangChon() {
		int idx = cboChuyenTau == null ? -1 : cboChuyenTau.getSelectedIndex();
		if (idx < 0 || idx >= danhSachChuyenTau.size()) {
			selectedMaCT = null;
			selectedChuyen = "-";
			selectedTuyen = "-";
			selectedKhoiHanh = "-";
			selectedThoiGianDen = "-";
			return;
		}

		ChuyenTauView ct = danhSachChuyenTau.get(idx);
		selectedMaCT = ct.getMaCT();
		selectedChuyen = ct.getMaCT();
		selectedTuyen = ct.getGaDi() + " - " + ct.getGaDen();
		selectedKhoiHanh = ct.getKhoiHanh().format(CHUYEN_TIME_FORMAT);
		selectedThoiGianDen = tinhThoiGianDen(selectedKhoiHanh, selectedTuyen);

		capNhatDanhSachToaTheoChuyen();
		selectedChuyenKhuHoi = "-";
		if (lblKiemTraKhuHoi != null) {
			lblKiemTraKhuHoi.setText("Chua kiem tra");
			lblKiemTraKhuHoi.setForeground(new Color(108, 122, 138));
		}
		refreshSummary();
	}

	private JPanel taoKhungGiuaNoiDung(JPanel panel, int maxWidth, EmptyBorder padding) {
		JPanel row = new JPanel();
		row.setOpaque(false);
		row.setLayout(new javax.swing.BoxLayout(row, javax.swing.BoxLayout.X_AXIS));

		JPanel bounded = new JPanel(new BorderLayout());
		bounded.setOpaque(false);
		bounded.setBorder(padding);
		bounded.add(panel, BorderLayout.CENTER);
		bounded.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
		bounded.setPreferredSize(new Dimension(maxWidth, 10));

		row.add(Box.createHorizontalGlue());
		row.add(bounded);
		row.add(Box.createHorizontalGlue());
		return row;
	}

	private void buildSaleCard() {
		saleCard.setOpaque(false);
		saleCard.setLayout(new BorderLayout(12, 12));

		JPanel left = new JPanel(new BorderLayout(0, 12));
		left.setOpaque(false);
		left.add(createTripInfoPanel(), BorderLayout.NORTH);
		left.add(createSeatPanel(), BorderLayout.CENTER);

		saleCard.add(left, BorderLayout.CENTER);
		saleCard.add(createTicketListPanel(), BorderLayout.EAST);
	}

	private JPanel createTripInfoPanel() {
		JPanel wrap = new JPanel(new GridBagLayout());
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(10, 10, 10, 10)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;

		addLabelField(wrap, gbc, 0, 0, "Chuyến tàu *", createChuyenCombo());
		addLabelField(wrap, gbc, 1, 0, "Toa tàu *", createToaCombo());
		addLabelField(wrap, gbc, 0, 1, "Mã khuyến mãi", createPromotionField());
		return wrap;
	}

	private void addLabelField(JPanel parent, GridBagConstraints gbc, int column, int row, String label, java.awt.Component field) {
		gbc.gridy = row;
		gbc.gridx = column * 2;
		gbc.weightx = 0.18;
		JLabel lbl = new JLabel(label);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lbl.setForeground(MAU_TEXT);
		parent.add(lbl, gbc);

		gbc.gridx = column * 2 + 1;
		gbc.weightx = 0.32;
		parent.add(field, gbc);
	}

	private JComboBox<String> createCustomerCombo() {
		cboKhachHang = new JComboBox<>();
		for (KhachHang kh : danhSachKhachHang) {
			String cccd = kh.getCccd() == null ? "" : kh.getCccd();
			String sdt = kh.getSdt() == null ? "" : kh.getSdt();
			String loai = kh.isLoaiKH() ? "THANH_VIEN" : "KHACH_LE";
			cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getTenKH() + " - " + sdt + " - " + loai + " - " + cccd);
		}
		if (cboKhachHang.getItemCount() == 0) {
			cboKhachHang.addItem("Khong co du lieu khach hang");
			cboKhachHang.setEnabled(false);
		}
		cboKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboKhachHang.setPreferredSize(new Dimension(220, 30));
		cboKhachHang.addActionListener(e -> {
			capNhatKhachHangDangChon();
		});
		capNhatKhachHangDangChon();
		return cboKhachHang;
	}

	private JPanel createGiayToField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);
		txtGiayTo = new JTextField(selectedGiayTo);
		txtGiayTo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtGiayTo.setPreferredSize(new Dimension(220, 30));
		txtGiayTo.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				selectedGiayTo = txtGiayTo.getText().trim();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				selectedGiayTo = txtGiayTo.getText().trim();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				selectedGiayTo = txtGiayTo.getText().trim();
			}
		});
		wrap.add(txtGiayTo);
		return wrap;
	}

	private JComboBox<String> createThanhToanCombo() {
		cboThanhToan = new JComboBox<>(new String[] { "Tien mat", "Chuyen khoan", "Vi dien tu" });
		cboThanhToan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboThanhToan.setPreferredSize(new Dimension(220, 30));
		cboThanhToan.addActionListener(e -> {
			int idx = cboThanhToan.getSelectedIndex();
			selectedPhuongThucThanhToan = switch (idx) {
			case 1 -> "CHUYEN_KHOAN";
			case 2 -> "VI_DIEN_TU";
			default -> "TIEN_MAT";
			};
		});
		return cboThanhToan;
	}

	private JComboBox<String> createChuyenCombo() {
		cboChuyenTau = new JComboBox<>();
		for (ChuyenTauView ct : danhSachChuyenTau) {
			String item = ct.getMaCT() + " - " + ct.getGaDi() + " - " + ct.getGaDen() + " - "
					+ ct.getKhoiHanh().format(CHUYEN_TIME_FORMAT);
			cboChuyenTau.addItem(item);
		}
		if (cboChuyenTau.getItemCount() == 0) {
			cboChuyenTau.addItem("Khong co du lieu chuyen tau");
			cboChuyenTau.setEnabled(false);
		}
		cboChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboChuyenTau.setPreferredSize(new Dimension(220, 30));
		cboChuyenTau.addActionListener(e -> {
			capNhatChuyenTauDangChon();
		});
		capNhatChuyenTauDangChon();
		return cboChuyenTau;
	}

	private JComboBox<String> createToaCombo() {
		cboToaTau = new JComboBox<>();
		cboToaTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboToaTau.setPreferredSize(new Dimension(220, 30));
		cboToaTau.addActionListener(e -> {
			if (dangCapNhatDanhSachToa) {
				return;
			}
			capNhatThongTinToaTheoLuaChon();
			refreshSummary();
		});
		capNhatDanhSachToaTheoChuyen();
		return cboToaTau;
	}

	private void capNhatDanhSachToaTheoChuyen() {
		if (cboToaTau == null) {
			return;
		}

		dangCapNhatDanhSachToa = true;
		cboToaTau.removeAllItems();

		danhSachToa.clear();
		danhSachToa.addAll(layDanhSachToaTheoChuyen(selectedMaCT));
		for (ToaView toa : danhSachToa) {
			cboToaTau.addItem(toa.getMaToa() + " - " + toa.getLoaiToa() + " - " + toa.getSoGhe() + " ghe");
		}
		if (cboToaTau.getItemCount() == 0) {
			cboToaTau.addItem("Khong co toa cho chuyen nay");
			cboToaTau.setEnabled(false);
		} else {
			cboToaTau.setEnabled(true);
		}

		dangCapNhatDanhSachToa = false;
		if (cboToaTau.getItemCount() > 0) {
			cboToaTau.setSelectedIndex(0);
			capNhatThongTinToaTheoLuaChon();
		}
	}

	private void capNhatThongTinToaTheoLuaChon() {
		int idx = cboToaTau == null ? -1 : cboToaTau.getSelectedIndex();
		if (idx < 0 || idx >= danhSachToa.size()) {
			selectedMaToa = null;
			selectedToa = "-";
			selectedSoGhe = 32;
			return;
		}

		ToaView toa = danhSachToa.get(idx);
		selectedMaToa = toa.getMaToa();
		selectedSoGhe = Math.max(4, toa.getSoGhe());
		selectedToa = toa.getMaToa();

		BigDecimal giaTheoLichSu = layGiaMacDinhTheoChuyenToa(selectedMaCT, selectedMaToa);
		if (giaTheoLichSu != null && giaTheoLichSu.compareTo(BigDecimal.ZERO) > 0) {
			selectedPrice = giaTheoLichSu;
		} else if (toa.getLoaiToa() != null && toa.getLoaiToa().toLowerCase().contains("nam")) {
			selectedPrice = new BigDecimal("1530000");
		} else {
			selectedPrice = new BigDecimal("980000");
		}

		bookedSeats.clear();
		bookedSeats.addAll(layDanhSachGheDaBan(selectedMaCT, selectedMaToa));
		selectedSeats.removeIf(bookedSeats::contains);
		// Also remove forms for booked seats
		ticketFormMap.keySet().removeIf(bookedSeats::contains);
		refreshSeatGrid();
		refreshSeatButtonStyles();

		if (lblSeatPanelTitle != null) {
			lblSeatPanelTitle.setText("2. Chon cho ngoi - " + toa.getMaToa() + " (" + toa.getLoaiToa() + ")");
		}
	}

	private JPanel createPromotionField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);
		txtMaKM = new JTextField();
		txtMaKM.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtMaKM.setPreferredSize(new Dimension(140, 30));
		txtMaKM.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshSummary();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshSummary();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshSummary();
			}
		});
		wrap.add(txtMaKM);
		JLabel hint = new JLabel("Nhap ma khuyen mai tu CSDL (vi du: KM001, KM002)");
		hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		hint.setForeground(new Color(108, 122, 138));
		wrap.add(hint);
		return wrap;
	}

	private JComboBox<String> createDoiTuongCombo() {
		cboDoiTuong = new JComboBox<>(new String[] {
			"Người lớn",
			"Sinh viên (giảm 10%)",
			"Người cao tuổi > 60 (giảm 15%)",
			"Trẻ em dưới 6 tuổi (đi cùng người lớn, chung chỗ)",
			"Trẻ em 6-10 tuổi (quốc tịch Việt Nam)"
		});
		cboDoiTuong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboDoiTuong.setPreferredSize(new Dimension(220, 30));
		cboDoiTuong.addActionListener(e -> {
			int idx = cboDoiTuong.getSelectedIndex();
			selectedDoiTuong = switch (idx) {
			case 1 -> "SINH_VIEN";
			case 2 -> "CAO_TUOI";
			case 3 -> "TRE_DUOI_6";
			case 4 -> "TRE_6_10_VN";
			default -> "NGUOI_LON";
			};
			refreshSummary();
		});
		return cboDoiTuong;
	}

	private JPanel createTreEmDieuKienField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);
		chkTreDuoi6CoNguoiLon = new JCheckBox("Có người lớn đi kèm và chung chỗ");
		chkTreDuoi6CoNguoiLon.setOpaque(false);
		chkTreDuoi6CoNguoiLon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		chkTreDuoi6CoNguoiLon.addActionListener(e -> refreshSummary());
		wrap.add(chkTreDuoi6CoNguoiLon);
		return wrap;
	}

	private JPanel createKhuHoiField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);
		chkDatKhuHoi = new JCheckBox("Khách đặt vé khứ hồi");
		chkDatKhuHoi.setOpaque(false);
		chkDatKhuHoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		chkDatKhuHoi.addActionListener(e -> {
			if (chkDatKhuHoi.isSelected()) {
				kiemTraChuyenKhuHoi();
			} else {
				selectedChuyenKhuHoi = "-";
				if (lblKiemTraKhuHoi != null) {
					lblKiemTraKhuHoi.setText("Chưa kiểm tra");
					lblKiemTraKhuHoi.setForeground(AppTheme.TEXT_MUTED);
				}
				refreshSummary();
			}
		});
		wrap.add(chkDatKhuHoi);
		return wrap;
	}

	private JPanel createKiemTraKhuHoiField() {
		JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		wrap.setOpaque(false);

		JButton btnKiemTra = new JButton("Kiểm tra");
		AppTheme.styleSecondaryButton(btnKiemTra);
		btnKiemTra.addActionListener(e -> kiemTraChuyenKhuHoi());

		lblKiemTraKhuHoi = new JLabel("Chưa kiểm tra");
		lblKiemTraKhuHoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblKiemTraKhuHoi.setForeground(AppTheme.TEXT_MUTED);

		wrap.add(btnKiemTra);
		wrap.add(lblKiemTraKhuHoi);
		return wrap;
	}

	private JPanel createSeatPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(10, 10, 10, 10)
		));

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		lblSeatPanelTitle = new JLabel("2. Chọn chỗ ngồi - " + selectedToa);
		lblSeatPanelTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblSeatPanelTitle.setForeground(MAU_TEXT);
		header.add(lblSeatPanelTitle, BorderLayout.NORTH);

		lblSeatSummary = new JLabel();
		lblSeatSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSeatSummary.setForeground(AppTheme.TEXT_MUTED);
		header.add(lblSeatSummary, BorderLayout.SOUTH);
		wrap.add(header, BorderLayout.NORTH);

		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		legend.setOpaque(false);
		legend.add(createLegend(MAU_XANH, "Trống"));
		legend.add(createLegend(MAU_DO, "Đã đặt"));
		legend.add(createLegend(MAU_CHINH, "Đang chọn"));
		wrap.add(legend, BorderLayout.CENTER);

		seatGridContainer = new JPanel(new BorderLayout());
		seatGridContainer.setOpaque(false);
		seatGridContainer.add(createSeatGrid(), BorderLayout.CENTER);
		wrap.add(seatGridContainer, BorderLayout.SOUTH);
		return wrap;
	}

	private JPanel createLegend(Color color, String text) {
		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		legend.setOpaque(false);
		JLabel dot = new JLabel("●");
		dot.setForeground(color);
		dot.setFont(new Font("Segoe UI", Font.BOLD, 12));
		legend.add(dot);
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		label.setForeground(MAU_TEXT);
		legend.add(label);
		return legend;
	}

	private JScrollPane createSeatGrid() {
		int cols = 4;
		int rows = Math.max(1, (int) Math.ceil(selectedSoGhe / (double) cols));
		JPanel seats = new JPanel(new GridLayout(rows, cols, 8, 8));
		seats.setOpaque(false);
		seatButtons.clear();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int thuTu = row * cols + col + 1;
				if (thuTu > selectedSoGhe) {
					JLabel blank = new JLabel();
					seats.add(blank);
					continue;
				}
				String seatCode = String.valueOf((char) ('A' + col)) + String.format("%02d", row + 1);
				JButton seat = new JButton(seatCode);
				seat.setActionCommand(seatCode);
				seat.setPreferredSize(new Dimension(68, 38));
				seat.setFocusPainted(false);
				seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
				seat.addActionListener(e -> onSeatSelected((JButton) e.getSource()));
				seatButtons.add(seat);
				seats.add(seat);
			}
		}
		refreshSeatButtonStyles();
		return new JScrollPane(seats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	private void refreshSeatGrid() {
		if (seatGridContainer == null) {
			return;
		}
		seatGridContainer.removeAll();
		seatGridContainer.add(createSeatGrid(), BorderLayout.CENTER);
		seatGridContainer.revalidate();
		seatGridContainer.repaint();
	}

	private JPanel createTicketListPanel() {
		JPanel wrap = new JPanel(new BorderLayout(0, 12));
		wrap.setPreferredSize(new Dimension(520, 0));
		wrap.setOpaque(false);

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		JLabel title = new JLabel("Chi tiết vé (" + selectedSeats.size() + ")");
		title.setFont(new Font("Segoe UI", Font.BOLD, 15));
		title.setForeground(MAU_TEXT);
		header.add(title, BorderLayout.WEST);

		wrap.add(header, BorderLayout.NORTH);

		// Create scrollable panel for tickets
		JPanel ticketsContainer = new JPanel();
		ticketsContainer.setLayout(new javax.swing.BoxLayout(ticketsContainer, javax.swing.BoxLayout.Y_AXIS));
		ticketsContainer.setOpaque(false);

		// Add ticket form for each selected seat
		for (String seatCode : selectedSeats) {
			TicketDetailForm ticketForm = ticketFormMap.get(seatCode);
			if (ticketForm != null) {
				JPanel ticketPanel = createTicketFormPanel(ticketForm, seatCode);
				ticketsContainer.add(ticketPanel);
				ticketsContainer.add(Box.createVerticalStrut(8));
			}
		}

		JScrollPane scrollPane = new JScrollPane(ticketsContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		wrap.add(scrollPane, BorderLayout.CENTER);

		JButton btnSaveAll = new JButton("Lưu tất cả vé");
		AppTheme.stylePrimaryButton(btnSaveAll);
		btnSaveAll.addActionListener(e -> luuTatCaVe());

		JButton btnReset = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnReset);
		btnReset.addActionListener(e -> resetForm());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		buttons.setOpaque(false);
		buttons.add(btnSaveAll);
		buttons.add(btnReset);
		wrap.add(buttons, BorderLayout.SOUTH);

		return wrap;
	}

	private JPanel createTicketFormPanel(TicketDetailForm ticketForm, String seatCode) {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setBackground(Color.WHITE);
		wrap.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(10, 10, 10, 10)
		));
		wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		JLabel seatLabel = new JLabel("Ghế: " + seatCode);
		seatLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		seatLabel.setForeground(MAU_TEXT);
		header.add(seatLabel, BorderLayout.WEST);
		wrap.add(header, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 6, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Customer selection
		JLabel lblKhachHangForm = new JLabel("Khách hàng *");
		lblKhachHangForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblKhachHangForm.setForeground(MAU_TEXT);
		gbc.gridy = 0;
		form.add(lblKhachHangForm, gbc);

		ticketForm.cboKhachHang = new JComboBox<>();
		for (KhachHang kh : danhSachKhachHang) {
			String cccd = kh.getCccd() == null ? "" : kh.getCccd();
			String sdt = kh.getSdt() == null ? "" : kh.getSdt();
			String loai = kh.isLoaiKH() ? "THANH_VIEN" : "KHACH_LE";
			ticketForm.cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getTenKH() + " - " + sdt + " - " + loai + " - " + cccd);
		}
		ticketForm.cboKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		ticketForm.cboKhachHang.addActionListener(e -> {
			int idx = ticketForm.cboKhachHang.getSelectedIndex();
			if (idx >= 0 && idx < danhSachKhachHang.size()) {
				KhachHang kh = danhSachKhachHang.get(idx);
				ticketForm.selectedMaKH = kh.getMaKH();
				ticketForm.selectedKhachHang = kh.getTenKH();
				ticketForm.selectedHangThe = kh.isLoaiKH() ? "THANH_VIEN" : "KHACH_LE";
				if (kh.getCccd() != null) {
					ticketForm.selectedGiayTo = kh.getCccd();
					if (ticketForm.txtGiayTo != null) {
						ticketForm.txtGiayTo.setText(ticketForm.selectedGiayTo);
					}
				}
			}
		});
		gbc.gridy = 1;
		form.add(ticketForm.cboKhachHang, gbc);

		// Object type
		JLabel lblDoiTuongForm = new JLabel("Đối tượng");
		lblDoiTuongForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblDoiTuongForm.setForeground(MAU_TEXT);
		gbc.gridy = 2;
		form.add(lblDoiTuongForm, gbc);

		ticketForm.cboDoiTuong = new JComboBox<>(new String[] {
			"NGUOI_LON - Người lớn",
			"CAO_TUOI - Cao tuổi",
			"SINH_VIEN - Sinh viên",
			"TRE_6_10_VN - Trẻ 6-10 tuổi",
			"TRE_DUOI_6 - Trẻ dưới 6 tuổi"
		});
		ticketForm.cboDoiTuong.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		ticketForm.cboDoiTuong.addActionListener(e -> {
			String selected = (String) ticketForm.cboDoiTuong.getSelectedItem();
			if (selected != null) {
				ticketForm.selectedDoiTuong = selected.split(" - ")[0];
			}
		});
		gbc.gridy = 3;
		form.add(ticketForm.cboDoiTuong, gbc);

		// ID number
		JLabel lblGiayToForm = new JLabel("CCCD/Hộ chiếu");
		lblGiayToForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblGiayToForm.setForeground(MAU_TEXT);
		gbc.gridy = 4;
		form.add(lblGiayToForm, gbc);

		ticketForm.txtGiayTo = new JTextField();
		ticketForm.txtGiayTo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		ticketForm.txtGiayTo.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) { ticketForm.selectedGiayTo = ticketForm.txtGiayTo.getText().trim(); }
			@Override
			public void removeUpdate(DocumentEvent e) { ticketForm.selectedGiayTo = ticketForm.txtGiayTo.getText().trim(); }
			@Override
			public void changedUpdate(DocumentEvent e) { ticketForm.selectedGiayTo = ticketForm.txtGiayTo.getText().trim(); }
		});
		gbc.gridy = 5;
		form.add(ticketForm.txtGiayTo, gbc);

		// Payment method
		JLabel lblThanhToanForm = new JLabel("Thanh toán");
		lblThanhToanForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblThanhToanForm.setForeground(MAU_TEXT);
		gbc.gridy = 6;
		form.add(lblThanhToanForm, gbc);

		ticketForm.cboThanhToan = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Ví điện tử" });
		ticketForm.cboThanhToan.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		ticketForm.cboThanhToan.addActionListener(e -> {
			int idx = ticketForm.cboThanhToan.getSelectedIndex();
			ticketForm.selectedPhuongThucThanhToan = switch (idx) {
			case 1 -> "CHUYEN_KHOAN";
			case 2 -> "VI_DIEN_TU";
			default -> "TIEN_MAT";
			};
		});
		gbc.gridy = 7;
		form.add(ticketForm.cboThanhToan, gbc);

		wrap.add(form, BorderLayout.CENTER);
		return wrap;
	}

	private JPanel createOrderPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 12));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(10, 10, 10, 10)
		));

		JLabel title = new JLabel("Tóm tắt đơn hàng");
		title.setFont(new Font("Segoe UI", Font.BOLD, 16));
		title.setForeground(MAU_TEXT);
		panel.add(title, BorderLayout.NORTH);

		JPanel details = new JPanel(new GridBagLayout());
		details.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 0, 8, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weighty = 0;

		String[] labels = { "Nhân viên:", "Khách hàng:", "Chuyến:", "Tuyến:", "Khởi hành:", "Thời gian đến:", "Toa:", "Ghế:" };
		String[] values = {
			selectedNhanVien,
			selectedKhachHang + " (" + selectedHangThe + ")",
			selectedChuyen,
			selectedTuyen,
			selectedKhoiHanh,
			selectedThoiGianDen,
			selectedToa,
			getSelectedSeatSummary()
		};
		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 0.30;
			gbc.anchor = GridBagConstraints.WEST;
			JLabel lbl = new JLabel(labels[i]);
			lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			lbl.setForeground(MAU_TEXT);
			details.add(lbl, gbc);

			gbc.gridx = 1;
			gbc.weightx = 0.70;
			gbc.anchor = GridBagConstraints.EAST;
			JLabel val = new JLabel(values[i]);
			val.setFont(new Font("Segoe UI", Font.BOLD, 13));
			val.setForeground(MAU_TEXT);
			val.setHorizontalAlignment(SwingConstants.RIGHT);
			details.add(val, gbc);
		}
		details.setBorder(new EmptyBorder(4, 0, 0, 0));
		panel.add(details, BorderLayout.CENTER);

		lblSeatCount = new JLabel();
		lblSeatCount.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblSeatCount.setForeground(MAU_TEXT);
		lblSeatCount.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblSeatCount, BorderLayout.SOUTH);
		updateSeatSummary();
		return panel;
	}

	private JPanel createPricePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(10, 10, 10, 10)
		));

		JPanel priceRows = new JPanel(new GridLayout(4, 1, 0, 8));
		priceRows.setOpaque(false);
		priceRows.add(priceRow("Giá vé gốc:", formatMoney(selectedPrice)));
		priceRows.add(priceRow("Chiết khấu nghiệp vụ:", getDiscountAmount().compareTo(BigDecimal.ZERO) == 0 ? "0 đ" : formatMoney(getDiscountAmount())));
		priceRows.add(priceRow("Số lượng vé:", selectedSeats.size() + " vé"));
		priceRows.add(priceRow("Tổng tạm tính:", formatMoney(getPreviewTotal())));
		panel.add(priceRows, BorderLayout.NORTH);

		JPanel totalWrap = new JPanel(new BorderLayout());
		totalWrap.setOpaque(false);
		JLabel totalLabel = new JLabel("Tổng cộng:");
		totalLabel.setForeground(MAU_TEXT);
		totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		totalWrap.add(totalLabel, BorderLayout.WEST);

		lblTongTien = new JLabel(formatMoney(getPreviewTotal()));
		lblTongTien.setForeground(MAU_CHINH);
		lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
		totalWrap.add(lblTongTien, BorderLayout.EAST);
		panel.add(totalWrap, BorderLayout.CENTER);

		JButton btnBanVe = new JButton("Bán vé");
		AppTheme.stylePrimaryButton(btnBanVe);
		btnBanVe.addActionListener(e -> luuVeNhap());

		JButton btnMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnMoi);
		btnMoi.addActionListener(e -> resetForm());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		buttons.setOpaque(false);
		buttons.add(btnBanVe);
		buttons.add(btnMoi);
		panel.add(buttons, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel priceRow(String label, String value) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		JLabel lbl = new JLabel(label);
		lbl.setForeground(MAU_TEXT);
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		row.add(lbl, BorderLayout.WEST);
		JLabel val = new JLabel(value);
		val.setForeground(MAU_TEXT);
		val.setFont(new Font("Segoe UI", Font.BOLD, 13));
		row.add(val, BorderLayout.EAST);
		return row;
	}

	private void onSeatSelected(JButton source) {
		String seatCode = source.getActionCommand();
		if (bookedSeats.contains(seatCode)) {
			return;
		}

		if (selectedSeats.contains(seatCode)) {
			selectedSeats.remove(seatCode);
			ticketFormMap.remove(seatCode);
			NghiepVuVeService.huyGiuCho(selectedChuyen, selectedToa, seatCode);
		} else {
			try {
				NghiepVuVeService.giuCho(selectedChuyen, selectedToa, seatCode, selectedNhanVien, 15);
				selectedSeats.add(seatCode);
				// Create a new ticket form for this seat
				TicketDetailForm ticketForm = new TicketDetailForm(seatCode);
				ticketForm.fillForm(danhSachKhachHang);
				ticketFormMap.put(seatCode, ticketForm);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể giữ chỗ", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		refreshSeatButtonStyles();
		updateSeatSummary();
		updateTicketListPanel();
		refreshSummary();
	}

	private void refreshSeatButtonStyles() {
		for (JButton seat : seatButtons) {
			String seatCode = seat.getActionCommand();
			if (bookedSeats.contains(seatCode)) {
				seat.setBackground(new Color(255, 240, 240));
				seat.setForeground(MAU_DO);
				seat.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
			} else if (selectedSeats.contains(seatCode)) {
				seat.setBackground(MAU_CHINH);
				seat.setForeground(Color.WHITE);
				seat.setBorder(BorderFactory.createLineBorder(MAU_CHINH.darker(), 2));
			} else {
				seat.setBackground(Color.WHITE);
				seat.setForeground(MAU_TEXT);
				seat.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
			}
		}
	}

	private void updateSeatSummary() {
		int count = selectedSeats.size();
		if (lblSeatSummary != null) {
			lblSeatSummary.setText("Đã chọn: " + count + " ghế | " + getSelectedSeatSummary());
		}
		if (lblSeatCount != null) {
			lblSeatCount.setText(count + " vé");
		}
	}

	private void updateTicketListPanel() {
		// Find the scrollable ticket list panel and rebuild it
		// This will be called when seats are selected/deselected
		if (saleCard.getComponentCount() > 1) {
			JPanel rightPanel = (JPanel) saleCard.getComponent(1);
			saleCard.remove(rightPanel);
			saleCard.add(createTicketListPanel(), BorderLayout.EAST);
			saleCard.revalidate();
			saleCard.repaint();
		}
	}

	private String getSelectedSeatSummary() {
		if (selectedSeats.isEmpty()) {
			return "Chưa chọn ghế";
		}
		return String.join(", ", selectedSeats);
	}

	private BigDecimal getTotalBase() {
		return selectedPrice.multiply(BigDecimal.valueOf(selectedSeats.size()));
	}

	private String selectedPromotion() {
		return txtMaKM == null ? "" : txtMaKM.getText().trim().toUpperCase();
	}

	private BigDecimal getDiscountAmount() {
		return getTotalBase().multiply(getDiscountRate()).setScale(0, RoundingMode.HALF_UP);
	}

	private BigDecimal getDiscountRate() {
		if (isTreDuoi6MienVe()) {
			return BigDecimal.ONE;
		}

		BigDecimal rate = getTierDiscountRate();
		rate = rate.max(getChildDiscountRate());
		BigDecimal kmRate = khuyenMaiHieuLuc.get(selectedPromotion());
		if (kmRate != null) {
			rate = rate.max(kmRate);
		}

		if (isKhuyenMaiKhuHoi() || isLuotVeKhuHoi() || isDatKhuHoiHopLe()) {
			rate = rate.add(new BigDecimal("0.05"));
		}

		if (rate.compareTo(BigDecimal.ONE) > 0) {
			return BigDecimal.ONE;
		}
		return rate;
	}

	private BigDecimal getChildDiscountRate() {
		if ("CAO_TUOI".equals(selectedDoiTuong)) {
			return new BigDecimal("0.15");
		}
		if ("SINH_VIEN".equals(selectedDoiTuong)) {
			return new BigDecimal("0.10");
		}
		if ("TRE_6_10_VN".equals(selectedDoiTuong)) {
			return new BigDecimal("0.25");
		}
		return BigDecimal.ZERO;
	}

	private boolean isTreDuoi6MienVe() {
		return "TRE_DUOI_6".equals(selectedDoiTuong)
			&& chkTreDuoi6CoNguoiLon != null
			&& chkTreDuoi6CoNguoiLon.isSelected();
	}

	private BigDecimal getTierDiscountRate() {
		if ("THANH_VIEN".equals(selectedHangThe)) {
			return new BigDecimal("0.05");
		}
		return BigDecimal.ZERO;
	}

	private boolean isKhuyenMaiKhuHoi() {
		return "KHUHOI".equals(selectedPromotion());
	}

	private boolean isLuotVeKhuHoi() {
		return cboChuyenTau != null && String.valueOf(cboChuyenTau.getSelectedItem()).contains("LƯỢT VỀ");
	}

	private boolean isDatKhuHoiHopLe() {
		return chkDatKhuHoi != null && chkDatKhuHoi.isSelected() && selectedChuyenKhuHoi != null
				&& !"-".equals(selectedChuyenKhuHoi);
	}

	private BigDecimal getPreviewTotal() {
		discountAmount = getDiscountAmount();
		return getTotalBase().subtract(discountAmount);
	}

	private void refreshSummary() {
		if (lblTongTien != null) {
			lblTongTien.setText(formatMoney(getPreviewTotal()));
		}
		updateSeatSummary();
		refreshSuccessCard();
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void luuTatCaVe() {
		if (selectedSeats.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một chỗ ngồi để lưu vé nháp.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Validate all ticket forms
		List<String> invalidSeats = new ArrayList<>();
		for (String seatCode : selectedSeats) {
			TicketDetailForm form = ticketFormMap.get(seatCode);
			if (form == null || !form.isValid()) {
				invalidSeats.add(seatCode);
			}
		}

		if (!invalidSeats.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng điền đầy đủ thông tin (Khách hàng và CCCD/Hộ chiếu) cho các ghế: " + String.join(", ", invalidSeats),
					"Thiếu dữ liệu",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Save all tickets
		try {
			List<String> allMaVes = new ArrayList<>();
			for (String seatCode : selectedSeats) {
				TicketDetailForm form = ticketFormMap.get(seatCode);
				if (form != null) {
					// Create a Set with single seat
					Set<String> singleSeat = new LinkedHashSet<>();
					singleSeat.add(seatCode);

					// Save this ticket
					List<NghiepVuVeService.VeThongTin> veNhap = NghiepVuVeService.taoVeNhapTuBanVe(
						form.selectedKhachHang,
						form.selectedGiayTo,
						selectedChuyen,
						selectedTuyen,
						selectedKhoiHanh,
						selectedThoiGianDen,
						selectedToa,
						singleSeat,
						selectedPrice,
						form.selectedPhuongThucThanhToan,
						selectedNhanVien
					);

					for (NghiepVuVeService.VeThongTin ve : veNhap) {
						allMaVes.add(ve.getMaVe());
					}
				}
			}

			generatedVeList = allMaVes;
			generatedMaVe = generatedVeList.isEmpty() ? "-" : generatedVeList.get(0);
			selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
			refreshSuccessCard();
			hienThiCard(successCard);
			JOptionPane.showMessageDialog(
				this,
				"Đã lưu " + allMaVes.size() + " vé nháp.\nMã vé: " + String.join(", ", allMaVes)
						+ "\nBạn có thể sang màn Lập hóa đơn để chốt.",
				"Lưu vé nháp thành công",
				JOptionPane.INFORMATION_MESSAGE
			);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Không thể lưu vé nháp", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void luuVeNhap() {
		if (selectedMaKH == null || selectedMaKH.isBlank()) {
			JOptionPane.showMessageDialog(this,
					"Khong tim thay khach hang tu CSDL. Vui long kiem tra du lieu KhachHang.",
					"Thieu du lieu",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		selectedGiayTo = txtGiayTo == null ? "" : txtGiayTo.getText().trim();
		if (selectedGiayTo.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Bắt buộc nhập CCCD/Hộ chiếu trước khi giữ chỗ và lưu vé nháp.",
					"Thiếu định danh",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if ("TRE_DUOI_6".equals(selectedDoiTuong)
				&& (chkTreDuoi6CoNguoiLon == null || !chkTreDuoi6CoNguoiLon.isSelected())) {
			JOptionPane.showMessageDialog(
				this,
				"Trẻ dưới 6 tuổi chỉ miễn vé khi đi cùng người lớn và sử dụng chung chỗ.",
				"Thiếu điều kiện nghiệp vụ",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		if (selectedSeats.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một chỗ ngồi để lưu vé nháp.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (chkDatKhuHoi != null && chkDatKhuHoi.isSelected() && !isDatKhuHoiHopLe()) {
			JOptionPane.showMessageDialog(this,
					"Chưa có chuyến về hợp lệ. Vui lòng bấm 'Kiểm tra' để xác nhận chuyến khứ hồi trước khi lưu.",
					"Thiếu dữ liệu khứ hồi",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			List<NghiepVuVeService.VeThongTin> veNhap = NghiepVuVeService.taoVeNhapTuBanVe(
				selectedKhachHang,
				selectedGiayTo,
				selectedChuyen,
				selectedTuyen,
				selectedKhoiHanh,
				selectedThoiGianDen,
				selectedToa,
				selectedSeats,
				selectedPrice,
				selectedPhuongThucThanhToan,
				selectedNhanVien
			);
			List<String> maVes = new ArrayList<>();
			for (NghiepVuVeService.VeThongTin ve : veNhap) {
				maVes.add(ve.getMaVe());
			}
			generatedVeList = maVes;
			generatedMaVe = generatedVeList.isEmpty() ? "-" : generatedVeList.get(0);
			selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
			refreshSuccessCard();
			hienThiCard(successCard);
			JOptionPane.showMessageDialog(
				this,
				"Đã lưu " + veNhap.size() + " vé nháp.\nMã vé: " + String.join(", ", maVes)
						+ (isDatKhuHoiHopLe() ? "\nChuyến về gợi ý: " + selectedChuyenKhuHoi : "")
						+ "\nBạn có thể sang màn Lập hóa đơn để chốt.",
				"Lưu vé nháp thành công",
				JOptionPane.INFORMATION_MESSAGE
			);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể lưu vé nháp", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void refreshSuccessCard() {
		if (lblMaHD != null) {
			lblMaHD.setText(generatedMaVe);
		}
		if (lblSoVe != null) {
			lblSoVe.setText((generatedVeList.isEmpty() ? selectedSeats.size() : generatedVeList.size()) + " vé");
		}
		if (lblKhachHang != null) {
			lblKhachHang.setText(selectedKhachHang);
		}
		if (lblChuyen != null) {
			lblChuyen.setText(selectedChuyen);
		}
		if (lblTuyen != null) {
			lblTuyen.setText(selectedTuyen);
		}
		if (lblKhoiHanh != null) {
			lblKhoiHanh.setText(selectedKhoiHanh);
		}
		if (lblThoiGianDen != null) {
			lblThoiGianDen.setText(selectedThoiGianDen);
		}
		if (lblToaGhe != null) {
			lblToaGhe.setText(selectedToa + " · " + getSelectedSeatSummary());
		}
		if (lblGia != null) {
			lblGia.setText(formatMoney(getPreviewTotal()));
		}
		if (lblNgayLap != null) {
			lblNgayLap.setText(selectedNgayLap);
		}
	}

	private void buildSuccessCard() {
		successCard.setOpaque(false);
		JPanel center = new JPanel(new BorderLayout(0, 12));
		center.setBackground(Color.WHITE);
		center.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(20, 20, 20, 20)
		));

		JLabel icon = new JLabel("✓", SwingConstants.CENTER);
		icon.setOpaque(true);
		icon.setBackground(new Color(220, 252, 231));
		icon.setForeground(MAU_XANH);
		icon.setFont(new Font("Segoe UI", Font.BOLD, 38));
		icon.setPreferredSize(new Dimension(76, 76));
		icon.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		iconWrap.setOpaque(false);
		iconWrap.add(icon);
		center.add(iconWrap, BorderLayout.NORTH);

		JLabel title = new JLabel("Lưu vé nháp thành công!");
		title.setFont(new Font("Segoe UI", Font.BOLD, 20));
		title.setForeground(MAU_TEXT);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		center.add(title, BorderLayout.CENTER);

		JLabel sub = new JLabel("Đây là bản giữ chỗ. Hóa đơn sẽ được lập ở màn riêng khi chốt thanh toán.");
		sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		sub.setForeground(AppTheme.TEXT_MUTED);
		sub.setHorizontalAlignment(SwingConstants.CENTER);
		center.add(sub, BorderLayout.SOUTH);

		JPanel ticket = new JPanel(new BorderLayout());
		ticket.setBackground(MAU_CHINH);
		ticket.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JLabel shop = new JLabel("GA TÀU SÀI GÒN");
		shop.setForeground(new Color(191, 219, 254));
		shop.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblMaHD = new JLabel(generatedMaVe);
		JLabel code = lblMaHD;
		code.setForeground(Color.WHITE);
		code.setFont(new Font("Segoe UI", Font.BOLD, 26));
		JLabel price = new JLabel(formatMoney(getPreviewTotal()));
		price.setForeground(Color.WHITE);
		price.setFont(new Font("Segoe UI", Font.BOLD, 20));
		JPanel head = new JPanel(new BorderLayout());
		head.setOpaque(false);
		head.add(shop, BorderLayout.WEST);
		head.add(price, BorderLayout.EAST);
		ticket.add(head, BorderLayout.NORTH);
		ticket.add(code, BorderLayout.CENTER);

		JPanel detail = new JPanel(new GridBagLayout());
		detail.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 0, 8, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lblSoVe = new JLabel((generatedVeList.isEmpty() ? selectedSeats.size() : generatedVeList.size()) + " vé");
		lblKhachHang = new JLabel(selectedKhachHang);
		lblChuyen = new JLabel(selectedChuyen);
		lblTuyen = new JLabel(selectedTuyen);
		lblKhoiHanh = new JLabel(selectedKhoiHanh);
		lblThoiGianDen = new JLabel(selectedThoiGianDen);
		lblToaGhe = new JLabel(selectedToa + " · " + getSelectedSeatSummary());
		lblGia = new JLabel(formatMoney(getPreviewTotal()));
		lblNgayLap = new JLabel(selectedNgayLap);
		addDetail(detail, gbc, 0, "Số vé", lblSoVe);
		addDetail(detail, gbc, 1, "Khách hàng", lblKhachHang);
		addDetail(detail, gbc, 2, "Nhân viên", new JLabel(selectedNhanVien));
		addDetail(detail, gbc, 3, "Chuyến", lblChuyen);
		addDetail(detail, gbc, 4, "Tuyến", lblTuyen);
		addDetail(detail, gbc, 5, "Giờ khởi hành", lblKhoiHanh);
		addDetail(detail, gbc, 6, "Thời gian đến", lblThoiGianDen);
		addDetail(detail, gbc, 7, "Toa / Ghế", lblToaGhe);
		addDetail(detail, gbc, 8, "Tổng tạm tính", lblGia);
		addDetail(detail, gbc, 9, "Thời điểm lưu", lblNgayLap);
		ticket.add(detail, BorderLayout.SOUTH);

		JButton btnInVe = new JButton("Xem vé nháp");
		AppTheme.stylePrimaryButton(btnInVe);
		btnInVe.addActionListener(e -> JOptionPane.showMessageDialog(
			this,
			"Mã vé nháp: " + String.join(", ", generatedVeList),
			"Vé nháp",
			JOptionPane.INFORMATION_MESSAGE
		));

		JButton btnMoi = new JButton("Bán vé mới");
		AppTheme.styleSecondaryButton(btnMoi);
		btnMoi.addActionListener(e -> resetForm());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		buttons.setOpaque(false);
		buttons.add(btnInVe);
		buttons.add(btnMoi);

		JPanel stack = new JPanel(new BorderLayout(0, 12));
		stack.setOpaque(false);
		stack.add(center, BorderLayout.NORTH);
		stack.add(ticket, BorderLayout.CENTER);
		stack.add(buttons, BorderLayout.SOUTH);
		successCard.add(stack, BorderLayout.CENTER);
	}

	private void addDetail(JPanel parent, GridBagConstraints gbc, int row, String label, JLabel value) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.34;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel lbl = new JLabel(label);
		lbl.setForeground(new Color(191, 219, 254));
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		parent.add(lbl, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.66;
		gbc.anchor = GridBagConstraints.EAST;
		value.setForeground(Color.WHITE);
		value.setFont(new Font("Segoe UI", Font.BOLD, 13));
		value.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(value, gbc);
	}

	private void resetForm() {
		for (String seat : selectedSeats) {
			NghiepVuVeService.huyGiuCho(selectedChuyen, selectedToa, seat);
		}
		selectedSeats.clear();
		ticketFormMap.clear();
		generatedVeList = new ArrayList<>();
		if (cboChuyenTau != null) {
			cboChuyenTau.setSelectedIndex(0);
		}
		if (cboToaTau != null) {
			cboToaTau.setSelectedIndex(0);
		}
		if (txtMaKM != null) {
			txtMaKM.setText("");
		}
		selectedNgayLap = LocalDateTime.now().format(TICKET_TIME_FORMAT);
		selectedThoiGianDen = tinhThoiGianDen(selectedKhoiHanh, selectedTuyen);
		generatedMaVe = "VE" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "01";
		refreshSeatButtonStyles();
		updateSeatSummary();
		updateTicketListPanel();
		refreshSummary();
		hienThiCard(saleCard);
	}

	private void hienThiCard(JPanel card) {
		contentPanel.removeAll();
		contentPanel.add(card, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void kiemTraChuyenKhuHoi() {
		String chuyenVe = timChuyenKhuHoiPhuHop();
		if (chuyenVe == null) {
			selectedChuyenKhuHoi = "-";
			if (chkDatKhuHoi != null) {
				chkDatKhuHoi.setSelected(false);
			}
			if (lblKiemTraKhuHoi != null) {
				lblKiemTraKhuHoi.setText("Không tìm thấy chuyến về phù hợp");
				lblKiemTraKhuHoi.setForeground(MAU_DO);
			}
			JOptionPane.showMessageDialog(this,
					"Hiện chưa có chuyến về phù hợp cho hành trình này.\nBạn có thể bán một chiều hoặc chọn chuyến đi khác.",
					"Không có chuyến khứ hồi",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		selectedChuyenKhuHoi = chuyenVe;
		if (chkDatKhuHoi != null) {
			chkDatKhuHoi.setSelected(true);
		}
		if (lblKiemTraKhuHoi != null) {
			lblKiemTraKhuHoi.setText(chuyenVe);
			lblKiemTraKhuHoi.setForeground(MAU_XANH.darker());
		}
		refreshSummary();
	}

	private String timChuyenKhuHoiPhuHop() {
		if (cboChuyenTau == null || cboChuyenTau.getSelectedItem() == null) {
			return null;
		}

		String selectedItem = String.valueOf(cboChuyenTau.getSelectedItem());
		String[] selectedParts = selectedItem.split(" - ");
		if (selectedParts.length < 4) {
			return null;
		}

		String from = selectedParts[1].trim();
		String to = selectedParts[2].trim();
		LocalDateTime gioDi = parseGioKhoiHanh(selectedParts[3]);

		String ungVienTotNhat = null;
		LocalDateTime gioVeGanNhat = null;

		for (int i = 0; i < cboChuyenTau.getItemCount(); i++) {
			String item = String.valueOf(cboChuyenTau.getItemAt(i));
			String[] parts = item.split(" - ");
			if (parts.length < 4) {
				continue;
			}
			if (!to.equals(parts[1].trim()) || !from.equals(parts[2].trim())) {
				continue;
			}

			LocalDateTime gioVe = parseGioKhoiHanh(parts[3]);
			if (gioDi != null && gioVe != null && gioVe.isBefore(gioDi)) {
				continue;
			}

			if (ungVienTotNhat == null || (gioVeGanNhat != null && gioVe != null && gioVe.isBefore(gioVeGanNhat))) {
				ungVienTotNhat = item;
				gioVeGanNhat = gioVe;
			}
		}

		return ungVienTotNhat;
	}

	private LocalDateTime parseGioKhoiHanh(String text) {
		try {
			return LocalDateTime.parse(text.trim(), CHUYEN_TIME_FORMAT);
		} catch (Exception ex) {
			return null;
		}
	}

	private String tinhThoiGianDen(String khoiHanh, String tuyenTau) {
		LocalDateTime gioDi = parseGioKhoiHanh(khoiHanh);
		if (gioDi == null) {
			return "-";
		}
		long soGio = layThoiLuongHanhTrinhGio(tuyenTau);
		return gioDi.plus(Duration.ofHours(soGio)).format(CHUYEN_TIME_FORMAT);
	}

	private long layThoiLuongHanhTrinhGio(String tuyenTau) {
		if (tuyenTau == null) {
			return 8;
		}
		if (tuyenTau.contains("Hà Nội")) {
			return 31;
		}
		if (tuyenTau.contains("Đà Nẵng")) {
			return 16;
		}
		if (tuyenTau.contains("Nha Trang")) {
			return 8;
		}
		return 10;
	}

	private String formatMoney(BigDecimal value) {
		return String.format("%,.0f đ", value);
	}
}
