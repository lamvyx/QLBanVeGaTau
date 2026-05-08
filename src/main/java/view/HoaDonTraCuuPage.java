package view;

import controller.HoaDonController;
import entity.HoaDon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import entity.ChiTietHoaDonItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoaDonTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final HoaDonController hoaDonController = new HoaDonController();
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
		String[] columns = { "#", "Mã Hóa Đơn", "Mã Nhân Viên", "Mã Khách Hàng", "Thời Gian Lập", "Tổng Tiền",
				"Khuyến Mãi", "Thanh toán", "Thao tác" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Lấy dữ liệu từ database
		loadDataFromDatabase("");

		table = new JTable(model);
		table.setRowHeight(50);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				if (row >= 0 && col == 8) { // Cột Thao tác
					String maHD = model.getValueAt(row, 1).toString();
					hienThiChiTietHoaDon(maHD);
				}
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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

		for (int i = 0; i < danhSach.size(); i++) {
			HoaDon hd = danhSach.get(i);
			model.addRow(new Object[] {
					i + 1,
					hd.getMaHD(),
					hd.getMaNV(),
					hd.getMaKH(),
					hd.getThoiGian() != null ? hd.getThoiGian().format(formatter) : "",
					hd.getTongTien() != null ? currencyFormat.format(hd.getTongTien()) : "0 đ",
					hd.getMaKM() != null ? hd.getMaKM() : "Không có",
					hd.isTrangThaiThanhToan() ? "Đã trả" : "Chờ",
					"👁 Xem"
			});
		}
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
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		BigDecimal calculateTong = BigDecimal.ZERO;
		for (int i = 0; i < details.size(); i++) {
			ChiTietHoaDonItem it = details.get(i);
			String maSp = it.getMaVeTau() != null ? it.getMaVeTau() : it.getMaDV();
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
		final BigDecimal tong = calculateTong;

		JTable t = new JTable(m);
		t.setRowHeight(30);
		t.getTableHeader().setBackground(MAU_CHINH);
		t.getTableHeader().setForeground(Color.WHITE);

		JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlNorth.add(new JLabel("Hóa đơn: " + maHD));

		JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

		JButton btnThanhToan = new JButton("💳 Thanh toán ngay");
		btnThanhToan.setBackground(new Color(34, 197, 94));
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnThanhToan.setEnabled(!hoaDonController.kiemTraThanhToan(maHD));
		btnThanhToan.addActionListener(e -> {
			if (hoaDonController.updateTrangThaiThanhToan(maHD, true)) {
				JOptionPane.showMessageDialog(dialog, "Thanh toán hóa đơn " + maHD + " thành công!");
				btnThanhToan.setEnabled(false);
				loadDataFromDatabase(""); // Refresh main table
			} else {
				JOptionPane.showMessageDialog(dialog, "Lỗi thanh toán.");
			}
		});

		JButton btnIn = new JButton("🖨 In hóa đơn (PDF)");
		btnIn.setBackground(MAU_CHINH);
		btnIn.setForeground(Color.WHITE);
		btnIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnIn.addActionListener(e -> inHoaDon(maHD, details, tong));

		pnlActions.add(btnThanhToan);
		pnlActions.add(btnIn);
		pnlBottom.add(pnlActions, BorderLayout.SOUTH);

		dialog.add(pnlBottom, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}

	private void inHoaDon(String maHD, List<ChiTietHoaDonItem> details, BigDecimal tong) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setJobName("HoaDon_" + maHD);

		job.setPrintable(new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex > 0)
					return NO_SUCH_PAGE;

				Graphics2D g2d = (Graphics2D) graphics;
				g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
				g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
				int y = 50;

				g2d.drawString("HÓA ĐƠN BÁN VÉ TÀU - " + maHD, 100, y);
				y += 30;
				g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
				g2d.drawString(
						"Ngày lập: "
								+ java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
						50, y);
				y += 40;

				g2d.drawString(String.format("%-10s %-20s %-10s %-15s", "STT", "Mô tả", "SL", "Đơn giá"), 50, y);
				y += 20;
				g2d.drawString("------------------------------------------------------------", 50, y);
				y += 20;

				NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
				for (int i = 0; i < details.size(); i++) {
					ChiTietHoaDonItem it = details.get(i);
					String desc = it.getMaVeTau() != null ? "Vé: " + it.getMaVeTau() : "DV: " + it.getMaDV();
					g2d.drawString(String.format("%-10d %-20s %-10d %-15s", (i + 1), desc, it.getSoLuong(),
							nf.format(it.getDonGia())), 50, y);
					y += 20;
				}

				y += 20;
				g2d.drawString("------------------------------------------------------------", 50, y);
				y += 30;
				g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
				g2d.drawString("TỔNG CỘNG: " + nf.format(tong), 50, y);

				y += 50;
				g2d.setFont(new Font("Monospaced", Font.ITALIC, 10));
				g2d.drawString("Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi!", 100, y);

				return PAGE_EXISTS;
			}
		});

		if (job.printDialog()) {
			try {
				job.print();
				JOptionPane.showMessageDialog(this,
						"Đã gửi lệnh in. Vui lòng chọn 'Save as PDF' trong hộp thoại hệ thống nếu muốn lưu file.");
			} catch (PrinterException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi in ấn: " + ex.getMessage());
			}
		}
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
