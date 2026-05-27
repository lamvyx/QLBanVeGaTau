package view;

import controller.HoaDonController;
import dao.DichVu_DAO;
import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.NhanVien_DAO;
import dao.VeTau_DAO;
import entity.DichVu;
import entity.HoaDon;
import entity.HoaDonTongKetDTO;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.VeTau;
import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PrinterException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import entity.ChiTietHoaDonItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoaDonTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final Color MAU_XANH_LA = Color.decode("#0F766E");
	private static final Color MAU_TIM = Color.decode("#7C3AED");
	private static final int ACTION_BUTTON_WIDTH = 104;
	private static final int ACTION_BUTTON_GAP = 8;
	private static final NumberFormat TIEN_TE = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private final HoaDonController hoaDonController = new HoaDonController();
	private final VeTau_DAO veTauDAO = new VeTau_DAO();
	private final DichVu_DAO dichVuDAO = new DichVu_DAO();
	private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
	private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
	private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
	private JLabel lblLoadingMsg;
	private int hoveredActionRow = -1;
	private int hoveredActionIndex = -1;
	private DefaultTableModel model;
	private JTable table;

	public HoaDonTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Tra cứu hóa đơn");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 15));
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(14, 14, 14, 14)));

		// Phần tìm kiếm
		JPanel searchPanel = taoSearchPanel();
		content.add(searchPanel, BorderLayout.NORTH);

		// Phần bảng kết quả
		String[] columns = { "#", "Mã Hóa Đơn", "Nhân Viên", "Khách Hàng", "Thời Gian Lập", "Tổng Tiền",
				"Khuyến Mãi", "Thanh toán", "Hành động" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Lấy dữ liệu từ database
		loadDataFromDatabase("");

		table = new JTable(model);
		table.setRowHeight(58);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(180);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(130);
		table.getColumnModel().getColumn(6).setPreferredWidth(130);
		table.getColumnModel().getColumn(7).setPreferredWidth(130);
		table.getColumnModel().getColumn(8).setPreferredWidth(360);
		table.getColumnModel().getColumn(8).setCellRenderer(new ActionButtonsRenderer());

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if (row < 0 || col < 0) {
					return;
				}

				String maHD = model.getValueAt(row, 1).toString();
				if (col == 8) {
					xuLyClickHanhDong(row, e.getX());
				} else if (e.getClickCount() == 2) {
					hienThiChiTietHoaDon(maHD);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				capNhatHoverHanhDong(-1, -1);
			}
		});
		table.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if (row >= 0 && col == 8) {
					capNhatHoverHanhDong(row, layChiSoNutHanhDong(row, e.getX()));
					return;
				}
				capNhatHoverHanhDong(-1, -1);
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void loadDataFromDatabase(String keyword) {
		model.setRowCount(0);
		List<HoaDon> danhSach = hoaDonController.timKiemHoaDon(keyword);

		for (int i = 0; i < danhSach.size(); i++) {
			HoaDon hd = danhSach.get(i);
			model.addRow(new Object[] {
					i + 1,
					hd.getMaHD(),
					hienThiTenKemMa(hd.getTenNV(), hd.getMaNV()),
					hienThiTenKemMa(hd.getTenKH(), hd.getMaKH()),
					hd.getThoiGian() != null ? hd.getThoiGian().format(DATE_TIME_FORMAT) : "",
					hd.getTongTien() != null ? TIEN_TE.format(hd.getTongTien()) : "0 đ",
					hd.getMaKM() != null ? hd.getMaKM() : "Không có",
					hd.isTrangThaiThanhToan() ? "Đã thanh toán" : "Chưa thanh toán",
					"Hành động"
			});
		}
	}

	private void xuLyClickHanhDong(int row, int mouseX) {
		String maHD = model.getValueAt(row, 1).toString();
		int actionIndex = layChiSoNutHanhDong(row, mouseX);
		if (actionIndex == 0) {
			inVeTheoHoaDon(maHD);
			return;
		}
		if (actionIndex == 1) {
			inHoaDon(maHD);
			return;
		}
		if (actionIndex == 2) {
			hienThiChiTietHoaDon(maHD);
		}
	}

	private int layChiSoNutHanhDong(int row, int mouseX) {
		int column = 8;
		int cellX = table.getCellRect(row, column, false).x;
		int cellWidth = table.getColumnModel().getColumn(column).getWidth();
		int totalWidth = ACTION_BUTTON_WIDTH * 3 + ACTION_BUTTON_GAP * 2;
		int startX = cellX + Math.max(10, (cellWidth - totalWidth) / 2);
		int relativeX = mouseX - startX;
		if (relativeX < 0 || relativeX > totalWidth) {
			return -1;
		}
		if (relativeX <= ACTION_BUTTON_WIDTH) {
			return 0;
		}
		if (relativeX >= ACTION_BUTTON_WIDTH + ACTION_BUTTON_GAP
				&& relativeX <= ACTION_BUTTON_WIDTH * 2 + ACTION_BUTTON_GAP) {
			return 1;
		}
		if (relativeX >= ACTION_BUTTON_WIDTH * 2 + ACTION_BUTTON_GAP * 2
				&& relativeX <= ACTION_BUTTON_WIDTH * 3 + ACTION_BUTTON_GAP * 2) {
			return 2;
		}
		return -1;
	}

	private void capNhatHoverHanhDong(int row, int actionIndex) {
		if (hoveredActionRow == row && hoveredActionIndex == actionIndex) {
			return;
		}
		hoveredActionRow = row;
		hoveredActionIndex = actionIndex;
		table.repaint();
	}

	private void hienThiChiTietHoaDon(String maHD) {
		List<ChiTietHoaDonItem> details = hoaDonController.layChiTietHoaDon(maHD);
		if (details.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết cho hóa đơn này.");
			return;
		}

		JDialog dialog = new JDialog();
		dialog.setTitle("Chi tiết hóa đơn: " + maHD);
		dialog.setSize(800, 500);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());

		String[] cols = { "STT", "Mã CTHD", "Mã Vé/Dịch vụ", "Loại", "Số lượng", "Đơn giá", "Thành tiền" };
		DefaultTableModel m = new DefaultTableModel(cols, 0);
		NumberFormat nf = TIEN_TE;
		BigDecimal calculateTong = BigDecimal.ZERO;
		for (int i = 0; i < details.size(); i++) {
			ChiTietHoaDonItem it = details.get(i);
			String maSp;
			if (it.getMaVeTau() != null) {
				maSp = it.getMaVeTau();
			} else {
				DichVu dv = dichVuDAO.timDichVuTheoMa(it.getMaDV());
				maSp = dv != null ? dv.getTenDV() + " (" + it.getMaDV() + ")" : it.getMaDV();
			}
			String loai = it.getMaVeTau() != null ? "Vé tàu" : "Dịch vụ";
			BigDecimal thanhTien = it.getDonGia().multiply(BigDecimal.valueOf(it.getSoLuong()));
			calculateTong = calculateTong.add(thanhTien);

			m.addRow(new Object[] {
					i + 1,
					it.getMaCTHD(),
					maSp,
					loai,
					it.getSoLuong(),
					nf.format(it.getDonGia()),
					nf.format(thanhTien)
			});
		}
		HoaDonTongKetDTO tongKet = hoaDonController.layTongKetHoaDon(maHD);
		final BigDecimal tong = tongKet.getTongThanhToan() != null ? tongKet.getTongThanhToan() : calculateTong;
		BigDecimal tienThue = tongKet.getTongSauThue().subtract(tongKet.getTongTruocThue());
		BigDecimal tienKhuyenMai = tongKet.getTongSauThue().subtract(tongKet.getTongThanhToan());

		JTable t = new JTable(m);
		t.setRowHeight(30);
		t.getTableHeader().setBackground(MAU_CHINH);
		t.getTableHeader().setForeground(Color.WHITE);

		JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlNorth.add(new JLabel("Hóa đơn: " + maHD));

		JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
		pnlSouth.add(new JLabel("Tạm tính: " + nf.format(tongKet.getTongTruocThue())));
		pnlSouth.add(new JLabel("Thuế: " + nf.format(tienThue.max(BigDecimal.ZERO))));
		pnlSouth.add(new JLabel("Khuyến mãi: -" + nf.format(tienKhuyenMai.max(BigDecimal.ZERO))));
		JLabel lblTong = new JLabel("Tổng cộng: " + nf.format(tong));
		lblTong.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTong.setForeground(Color.RED);
		pnlSouth.add(lblTong);

		dialog.add(pnlNorth, BorderLayout.NORTH);
		dialog.add(new JScrollPane(t), BorderLayout.CENTER);

		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.add(pnlSouth, BorderLayout.NORTH);

		JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
		pnlActions.setOpaque(false);

		JButton btnIn = new JButton("In hóa đơn");
		btnIn.setBackground(MAU_CHINH);
		btnIn.setForeground(Color.WHITE);
		btnIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnIn.addActionListener(e -> inHoaDon(maHD));

		JButton btnInVe = new JButton("In vé");
		btnInVe.setBackground(Color.decode("#0F766E"));
		btnInVe.setForeground(Color.WHITE);
		btnInVe.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnInVe.addActionListener(e -> inVeTheoHoaDon(maHD));

		pnlActions.add(btnIn);
		pnlActions.add(btnInVe);
		pnlBottom.add(pnlActions, BorderLayout.SOUTH);

		dialog.add(pnlBottom, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}

	private void inHoaDon(String maHD) {
		HoaDon hoaDon = hoaDonController.timHoaDonTheoMa(maHD);
		List<ChiTietHoaDonItem> details = hoaDonController.layChiTietHoaDon(maHD);
		HoaDonTongKetDTO tongKet = hoaDonController.layTongKetHoaDon(maHD);
		if (hoaDon == null || details.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu để in hóa đơn.");
			return;
		}

		KhachHang khachHang = khachHangDAO.timKhachHangTheoMa(hoaDon.getMaKH());
		NhanVien nhanVien = nhanVienDAO.timNhanVienTheoMa(hoaDon.getMaNV());
		KhuyenMai khuyenMai = hoaDon.getMaKM() == null ? null : khuyenMaiDAO.timKhuyenMaiTheoMa(hoaDon.getMaKM());

		PrinterJob job = PrinterJob.getPrinterJob();
		job.setJobName("HoaDon_" + maHD);

		job.setPrintable(new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex > 0)
					return NO_SUCH_PAGE;

				Graphics2D g2d = (Graphics2D) graphics;
				g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
				int pageWidth = (int) pageFormat.getImageableWidth();
				int contentWidth = Math.min(430, pageWidth - 20);
				int x = Math.max(10, (pageWidth - contentWidth) / 2);
				int y = 30;

				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
				drawCentered(g2d, "HÓA ĐƠN BÁN VÉ TÀU", x, contentWidth, y);
				y += 22;
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
				drawCentered(g2d, "CÔNG TY CỔ PHẦN VẬN TẢI ĐƯỜNG SẮT SÀI GÒN", x, contentWidth, y);
				y += 24;

				g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
				y = drawKeyValue(g2d, x, y, contentWidth, "Mã hóa đơn", maHD);
				y = drawKeyValue(g2d, x, y, contentWidth, "Ngày lập",
						hoaDon.getThoiGian() != null ? hoaDon.getThoiGian().format(DATE_TIME_FORMAT) : "—");
				y = drawKeyValue(g2d, x, y, contentWidth, "Nhân viên",
						nhanVien != null ? nhanVien.getTenNV() + " (" + hoaDon.getMaNV() + ")" : hoaDon.getMaNV());
				y = drawKeyValue(g2d, x, y, contentWidth, "Khách hàng",
						khachHang != null ? khachHang.getTenKH() + " (" + hoaDon.getMaKH() + ")" : hoaDon.getMaKH());
				y = drawKeyValue(g2d, x, y, contentWidth, "Trạng thái",
						hoaDon.isTrangThaiThanhToan() ? "Đã thanh toán" : "Chưa thanh toán");
				y = drawKeyValue(g2d, x, y, contentWidth, "Khuyến mãi",
						khuyenMai != null
								? khuyenMai.getTenKM() + " (-" + khuyenMai.getTyLeKM().stripTrailingZeros().toPlainString() + "%)"
								: "Không áp dụng");
				y += 8;

				g2d.setStroke(new BasicStroke(1f));
				g2d.drawLine(x, y, x + contentWidth, y);
				y += 18;

				NumberFormat nf = TIEN_TE;
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
				g2d.drawString("STT", x, y);
				g2d.drawString("Nội dung", x + 35, y);
				g2d.drawString("SL", x + 270, y);
				g2d.drawString("Đơn giá", x + 305, y);
				g2d.drawString("Thành tiền", x + 365, y);
				y += 10;
				g2d.drawLine(x, y, x + contentWidth, y);
				y += 16;

				g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
				for (int i = 0; i < details.size(); i++) {
					ChiTietHoaDonItem it = details.get(i);
					String desc;
					if (it.getMaVeTau() != null) {
						desc = "Vé tàu " + it.getMaVeTau();
					} else {
						DichVu dv = dichVuDAO.timDichVuTheoMa(it.getMaDV());
						desc = dv != null ? dv.getTenDV() : "Dịch vụ " + it.getMaDV();
					}
					BigDecimal thanhTien = it.getDonGia().multiply(BigDecimal.valueOf(it.getSoLuong()));
					g2d.drawString(String.valueOf(i + 1), x, y);
					g2d.drawString(desc, x + 35, y);
					g2d.drawString(String.valueOf(it.getSoLuong()), x + 270, y);
					g2d.drawString(rutGonTien(nf.format(it.getDonGia())), x + 305, y);
					g2d.drawString(rutGonTien(nf.format(thanhTien)), x + 365, y);
					y += 16;
				}

				y += 8;
				g2d.drawLine(x, y, x + contentWidth, y);
				y += 18;
				BigDecimal tienThue = tongKet.getTongSauThue().subtract(tongKet.getTongTruocThue());
				BigDecimal tienKhuyenMai = tongKet.getTongSauThue().subtract(tongKet.getTongThanhToan());
				y = drawKeyValue(g2d, x, y, contentWidth, "Tạm tính", nf.format(tongKet.getTongTruocThue()));
				y = drawKeyValue(g2d, x, y, contentWidth, "Thuế VAT", nf.format(tienThue.max(BigDecimal.ZERO)));
				y = drawKeyValue(g2d, x, y, contentWidth, "Khuyến mãi",
						"-" + nf.format(tienKhuyenMai.max(BigDecimal.ZERO)));
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
				y = drawKeyValue(g2d, x, y + 4, contentWidth, "Tổng thanh toán", nf.format(tongKet.getTongThanhToan()));

				y += 24;
				g2d.setFont(new Font("Segoe UI", Font.ITALIC, 10));
				drawCentered(g2d, "Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi!", x, contentWidth, y);

				return PAGE_EXISTS;
			}
		});

		thucThiPrintJobAsync(job, "Đang kết nối máy in hóa đơn, vui lòng chờ...",
				"Đang thực hiện in hóa đơn, vui lòng chờ...",
				"Đã gửi lệnh in hóa đơn. Vui lòng chọn 'Save as PDF' nếu muốn lưu file.");
	}

	private void inVeTheoHoaDon(String maHD) {
		HoaDon hoaDon = hoaDonController.timHoaDonTheoMa(maHD);
		List<VeTau> dsVe = chonDanhSachVeCanIn(maHD);
		if (hoaDon == null || dsVe.isEmpty()) {
			if (hoaDon != null) {
				JOptionPane.showMessageDialog(this, "Không có vé nào được chọn để in.");
			}
			return;
		}
		String tenKhach = hoaDon.getMaKH();
		KhachHang kh = khachHangDAO.timKhachHangTheoMa(hoaDon.getMaKH());
		if (kh != null && kh.getTenKH() != null && !kh.getTenKH().isBlank()) {
			tenKhach = kh.getTenKH();
		}
		final String tenKhachFinal = tenKhach;

		PrinterJob job = PrinterJob.getPrinterJob();
		job.setJobName("VeTau_" + maHD);
		job.setPrintable(new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex >= dsVe.size()) {
					return NO_SUCH_PAGE;
				}

				VeTau ve = dsVe.get(pageIndex);
				Graphics2D g2d = (Graphics2D) graphics;
				g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
				int pageWidth = (int) pageFormat.getImageableWidth();
				int cardWidth = Math.min(300, pageWidth - 20);
				int x = Math.max(10, (pageWidth - cardWidth) / 2);
				int y = 28;

				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
				drawCentered(g2d, "CÔNG TY CỔ PHẦN VẬN TẢI", x, cardWidth, y);
				y += 15;
				drawCentered(g2d, "ĐƯỜNG SẮT SÀI GÒN", x, cardWidth, y);
				y += 22;
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
				drawCentered(g2d, "THẺ LÊN TÀU HỎA / BOARDING PASS", x, cardWidth, y);
				y += 14;

				int barcodeWidth = 140;
				int barcodeX = x + (cardWidth - barcodeWidth) / 2;
				int hash = ve.getMaVeTau().hashCode();
				int curX = barcodeX;
				for (int i = 0; i < 28; i++) {
					int w = ((hash >> (i % 16)) & 3) + 1;
					if (i % 2 == 0) {
						g2d.fillRect(curX, y, w, 34);
					}
					curX += w + 2;
				}
				y += 48;

				g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
				drawCentered(g2d, "Mã vé/TicketID: " + ve.getMaVeTau() + " (" + maHD + ")", x, cardWidth, y);
				y += 18;

				g2d.setStroke(new BasicStroke(0.7f));
				g2d.drawLine(x, y, x + cardWidth, y);
				y += 16;

				String giaVe = TIEN_TE.format(ve.getGiaVe() != null ? ve.getGiaVe() : BigDecimal.ZERO);
				String ngayIn = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String ngayLap = hoaDon.getThoiGian() != null ? hoaDon.getThoiGian().format(DATE_TIME_FORMAT) : "—";

				y = drawKeyValue(g2d, x, y, cardWidth, "Khách", tenKhachFinal);
				y = drawKeyValue(g2d, x, y, cardWidth, "Mã hóa đơn", maHD);
				y = drawKeyValue(g2d, x, y, cardWidth, "Ngày lập", ngayLap);
				y = drawKeyValue(g2d, x, y, cardWidth, "Toa", ve.getMaToa());
				y = drawKeyValue(g2d, x, y, cardWidth, "Ghế", ve.getViTriGhe());
				y = drawKeyValue(g2d, x, y, cardWidth, "Giá vé", giaVe);
				y = drawKeyValue(g2d, x, y, cardWidth, "Trạng thái",
						hoaDon.isTrangThaiThanhToan() ? "Đã thanh toán" : "Chưa thanh toán");

				y += 8;
				g2d.drawLine(x, y, x + cardWidth, y);
				y += 16;
				g2d.setFont(new Font("Segoe UI", Font.ITALIC, 8));
				drawCentered(g2d, "Thẻ này không có giá trị thay cho hóa đơn.", x, cardWidth, y);
				y += 12;
				drawCentered(g2d, "Vui lòng dùng chức năng in hóa đơn để lấy chứng từ đầy đủ.", x, cardWidth, y);
				y += 14;
				drawCentered(g2d, "Printed date: " + ngayIn, x, cardWidth, y);

				return PAGE_EXISTS;
			}
		});

		thucThiPrintJobAsync(job, "Đang kết nối máy in vé, vui lòng chờ...",
				"Đang thực hiện in vé, vui lòng chờ...",
				"Yêu cầu in vé đã được gửi đến máy in thành công!");
	}

	private List<VeTau> chonDanhSachVeCanIn(String maHD) {
		List<VeTau> allTickets = layDanhSachVeTheoHoaDon(maHD);
		if (allTickets.isEmpty()) {
			return allTickets;
		}

		DefaultTableModel ticketModel = new DefaultTableModel(
				new Object[] { "Chọn", "STT", "Mã vé", "Toa", "Ghế", "Giá vé" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 0 ? Boolean.class : Object.class;
			}
		};
		for (int i = 0; i < allTickets.size(); i++) {
			VeTau ve = allTickets.get(i);
			ticketModel.addRow(new Object[] {
					Boolean.TRUE,
					i + 1,
					ve.getMaVeTau(),
					ve.getMaToa(),
					ve.getViTriGhe(),
					TIEN_TE.format(ve.getGiaVe() != null ? ve.getGiaVe() : BigDecimal.ZERO)
			});
		}
		JTable ticketTable = new JTable(ticketModel);
		ticketTable.setRowHeight(28);
		ticketTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ticketTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
		ticketTable.getTableHeader().setBackground(Color.decode("#EEF3FB"));
		ticketTable.getTableHeader().setForeground(Color.decode("#1F2E43"));
		ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ticketTable.getColumnModel().getColumn(0).setPreferredWidth(52);
		ticketTable.getColumnModel().getColumn(1).setPreferredWidth(48);

		JScrollPane tableScroll = new JScrollPane(ticketTable);
		tableScroll.setPreferredSize(new Dimension(560, 190));
		tableScroll.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 10));
		panel.add(new JLabel("Chọn các vé cần in cho hóa đơn " + maHD + ":"), BorderLayout.NORTH);
		panel.add(tableScroll, BorderLayout.CENTER);

		int result = JOptionPane.showConfirmDialog(this, panel, "Chọn vé cần in",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result != JOptionPane.OK_OPTION) {
			return List.of();
		}

		List<VeTau> selected = new ArrayList<>();
		for (int index = 0; index < ticketModel.getRowCount(); index++) {
			Boolean chosen = (Boolean) ticketModel.getValueAt(index, 0);
			if (Boolean.TRUE.equals(chosen) && index < allTickets.size()) {
				selected.add(allTickets.get(index));
			}
		}
		return selected;
	}

	private List<VeTau> layDanhSachVeTheoHoaDon(String maHD) {
		List<VeTau> dsVe = new ArrayList<>();
		for (ChiTietHoaDonItem item : hoaDonController.layChiTietHoaDon(maHD)) {
			if (item.getMaVeTau() != null && !item.getMaVeTau().isBlank()) {
				VeTau ve = veTauDAO.timTheoMaVe(item.getMaVeTau());
				if (ve != null) {
					dsVe.add(ve);
				}
			}
		}
		return dsVe;
	}

	private int drawKeyValue(Graphics2D g2d, int x, int y, int width, String key, String value) {
		g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		g2d.drawString(key + ":", x, y);
		g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
		String safeValue = value == null || value.isBlank() ? "—" : value;
		int valueWidth = g2d.getFontMetrics().stringWidth(safeValue);
		g2d.drawString(safeValue, x + width - valueWidth, y);
		return y + 16;
	}

	private void drawCentered(Graphics2D g2d, String text, int x, int width, int y) {
		int textWidth = g2d.getFontMetrics().stringWidth(text);
		g2d.drawString(text, x + Math.max(0, (width - textWidth) / 2), y);
	}

	private String rutGonTien(String text) {
		return text == null ? "" : text.replace("₫", "đ");
	}

	private JDialog createLoadingDialog(String message) {
		java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
		JDialog dialog = new JDialog(parentWindow, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setUndecorated(true);
		dialog.getRootPane().setOpaque(false);

		JPanel panel = new JPanel(new BorderLayout(0, 16));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(MAU_CHINH, 2),
				new EmptyBorder(24, 32, 24, 32)));

		lblLoadingMsg = new JLabel(message, SwingConstants.CENTER);
		lblLoadingMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblLoadingMsg.setForeground(Color.decode("#35506B"));
		panel.add(lblLoadingMsg, BorderLayout.NORTH);

		JProgressBar progress = new JProgressBar();
		progress.setIndeterminate(true);
		progress.setForeground(MAU_CHINH);
		progress.setPreferredSize(new Dimension(240, 6));
		progress.setBorder(BorderFactory.createEmptyBorder());
		panel.add(progress, BorderLayout.CENTER);

		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(parentWindow);
		return dialog;
	}

	private void thucThiPrintJobAsync(PrinterJob job, String connectingMessage, String printingMessage,
			String successMessage) {
		boolean accept = job.printDialog();
		if (!accept) {
			return;
		}

		final JDialog loadingDialog = createLoadingDialog(printingMessage);
		new Thread(() -> {
			try {
				SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
				job.print();
				SwingUtilities.invokeLater(() -> {
					loadingDialog.dispose();
					JOptionPane.showMessageDialog(this, successMessage, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				});
			} catch (PrinterException ex) {
				SwingUtilities.invokeLater(() -> {
					loadingDialog.dispose();
					JOptionPane.showMessageDialog(this, "Lỗi in ấn: " + ex.getMessage(), "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				});
			}
		}).start();
	}

	private class ActionButtonsRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, ACTION_BUTTON_GAP, 8));
			panel.setOpaque(true);
			panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
			panel.add(taoNut("In Vé", MAU_XANH_LA, row, 0));
			panel.add(taoNut("In Hóa Đơn", MAU_CHINH, row, 1));
			panel.add(taoNut("Xem chi tiết", MAU_TIM, row, 2));
			return panel;
		}

		private JButton taoNut(String text, Color color, int row, int actionIndex) {
			JButton button = new JButton(text);
			button.setPreferredSize(new Dimension(ACTION_BUTTON_WIDTH, 30));
			button.setFont(new Font("Segoe UI", Font.BOLD, 11));
			button.setForeground(Color.WHITE);
			button.setBackground(hoveredActionRow == row && hoveredActionIndex == actionIndex
					? lamSangMau(color)
					: color);
			button.setFocusPainted(false);
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			return button;
		}
	}

	private Color lamSangMau(Color color) {
		int red = color.getRed() + (int) ((255 - color.getRed()) * 0.18);
		int green = color.getGreen() + (int) ((255 - color.getGreen()) * 0.18);
		int blue = color.getBlue() + (int) ((255 - color.getBlue()) * 0.18);
		return new Color(Math.min(255, red), Math.min(255, green), Math.min(255, blue), color.getAlpha());
	}

	private String hienThiTenKemMa(String ten, String ma) {
		if (ten != null && !ten.isBlank()) {
			return ten;
		}
		return ma == null ? "" : ma;
	}

	private JPanel taoSearchPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(15, 15, 15, 15)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 8, 6, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// Từ khóa tìm kiếm
		JLabel lblTuKhoa = new JLabel("Từ khóa (Mã HĐ, KH..):");
		lblTuKhoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTuKhoa.setForeground(Color.decode("#2B4B74"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		panel.add(lblTuKhoa, gbc);

		JTextField txtTuKhoa = new JTextField();
		txtTuKhoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTuKhoa.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panel.add(txtTuKhoa, gbc);

		// Nút Tìm kiếm
		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(new EmptyBorder(6, 12, 6, 12));

		btnTimKiem.addActionListener(e -> {
			String keyword = txtTuKhoa.getText().trim();
			loadDataFromDatabase(keyword);
		});

		// Nút Làm mới
		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 12, 6, 12)));

		btnLamMoi.addActionListener(e -> {
			txtTuKhoa.setText("");
			loadDataFromDatabase("");
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(btnTimKiem);
		buttonPanel.add(btnLamMoi);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(buttonPanel, gbc);

		return panel;
	}
}
