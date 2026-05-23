package view;

import controller.PhieuDatVeController;
import entity.ChiTietPhieuDat;
import entity.PhieuDatVeInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PhieuDatVeTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final PhieuDatVeController phieuDatVeController = new PhieuDatVeController();
	private final DefaultTableModel model;
	private final JTable table;
	private final JTextField txtMaPhieu = new JTextField();

	public PhieuDatVeTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		String[] columns = { "maPhieu", "maKH", "maNV", "ngayDat", "hanThanhToan", "soGhe", "tongTien", "trangThai" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		AppTheme.styleTable(table);
		table.setRowHeight(30);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					txtMaPhieu.setText(String.valueOf(table.getValueAt(row, 0)));
				}
			}
		});

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);

		loadDataFromDatabase();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Tra cứu phiếu đặt");
		title.setFont(AppTheme.font(Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Chọn hoặc nhập mã phiếu để nạp qua màn bán vé");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 13));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		header.add(subtitle, BorderLayout.SOUTH);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 14));
		content.setBackground(AppTheme.PAGE_BG);
		content.setBorder(new EmptyBorder(14, 0, 0, 0));

		content.add(taoSearchBar(), BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private JPanel taoSearchBar() {
		JPanel bar = new JPanel(new BorderLayout(8, 0));
		bar.setBackground(AppTheme.CARD_BG);
		bar.setBorder(AppTheme.cardBorder());

		JLabel lbl = new JLabel("Mã phiếu:");
		lbl.setFont(AppTheme.font(Font.BOLD, 13));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);
		bar.add(lbl, BorderLayout.WEST);

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		right.setOpaque(false);

		txtMaPhieu.setPreferredSize(new Dimension(180, 32));
		txtMaPhieu.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(AppTheme.BORDER),
			new EmptyBorder(4, 8, 4, 8)
		));
		right.add(txtMaPhieu);

		JButton btnTim = new JButton("Tìm phiếu");
		AppTheme.styleSecondaryButton(btnTim);
		btnTim.addActionListener(e -> timPhieuTheoMa());
		right.add(btnTim);

		JButton btnNạp = new JButton("Nạp qua bán vé");
		AppTheme.stylePrimaryButton(btnNạp);
		btnNạp.addActionListener(e -> napQuaBanVe());
		right.add(btnNạp);

		JButton btnTaiLai = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnTaiLai);
		btnTaiLai.addActionListener(e -> {
			txtMaPhieu.setText("");
			loadDataFromDatabase();
		});
		right.add(btnTaiLai);

		bar.add(right, BorderLayout.CENTER);
		return bar;
	}

	private void loadDataFromDatabase() {
		model.setRowCount(0);
		List<PhieuDatVeInfo> danhSach = phieuDatVeController.layTatCaPhieuDat();
		for (PhieuDatVeInfo info : danhSach) {
			if (info == null || info.getPhieuDatVe() == null) {
				continue;
			}
			String maPhieu = info.getPhieuDatVe().getMaPhieu();
			String maKH = info.getPhieuDatVe().getMaKH();
			String maNV = info.getPhieuDatVe().getMaNV();
			String ngayDat = info.getPhieuDatVe().getNgayDat() != null ? info.getPhieuDatVe().getNgayDat().format(DATE_FMT) : "";
			String hanThanhToan = info.getPhieuDatVe().getHanThanhToan() != null ? info.getPhieuDatVe().getHanThanhToan().format(DATE_FMT) : "";
			int soGhe = info.getChiTietList() == null ? 0 : info.getChiTietList().size();
			BigDecimal tongTien = tinhTongTien(info);
			String trangThai = info.getPhieuDatVe().isTrangThai() ? "1" : "0";
			model.addRow(new Object[] { maPhieu, maKH, maNV, ngayDat, hanThanhToan, soGhe, tongTien, trangThai });
		}
	}

	private BigDecimal tinhTongTien(PhieuDatVeInfo info) {
		BigDecimal tong = BigDecimal.ZERO;
		if (info == null || info.getChiTietList() == null) {
			return tong;
		}
		for (ChiTietPhieuDat ct : info.getChiTietList()) {
			if (ct != null) {
				tong = tong.add(BigDecimal.valueOf(ct.getGiaVe()));
			}
		}
		return tong;
	}

	private void timPhieuTheoMa() {
		String maPhieu = txtMaPhieu.getText() == null ? "" : txtMaPhieu.getText().trim();
		if (maPhieu.isEmpty()) {
			loadDataFromDatabase();
			return;
		}
		PhieuDatVeInfo info = phieuDatVeController.layPhieuDatTheoMa(maPhieu);
		model.setRowCount(0);
		if (info != null && info.getPhieuDatVe() != null) {
			String ngayDat = info.getPhieuDatVe().getNgayDat() != null ? info.getPhieuDatVe().getNgayDat().format(DATE_FMT) : "";
			String hanThanhToan = info.getPhieuDatVe().getHanThanhToan() != null ? info.getPhieuDatVe().getHanThanhToan().format(DATE_FMT) : "";
			int soGhe = info.getChiTietList() == null ? 0 : info.getChiTietList().size();
			BigDecimal tongTien = tinhTongTien(info);
			model.addRow(new Object[] { info.getPhieuDatVe().getMaPhieu(), info.getPhieuDatVe().getMaKH(),
					info.getPhieuDatVe().getMaNV(), ngayDat, hanThanhToan, soGhe, tongTien,
					info.getPhieuDatVe().isTrangThai() ? "1" : "0" });
		}
	}

	private void napQuaBanVe() {
		String maPhieu = txtMaPhieu.getText() == null ? "" : txtMaPhieu.getText().trim();
		if (maPhieu.isEmpty()) {
			int row = table.getSelectedRow();
			if (row >= 0) {
				maPhieu = String.valueOf(table.getValueAt(row, 0));
			}
		}
		if (maPhieu.isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc chọn mã phiếu đặt.");
			return;
		}
		PhieuDatVeInfo info = phieuDatVeController.layPhieuDatTheoMa(maPhieu);
		if (info == null || info.getPhieuDatVe() == null) {
			javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt.");
			return;
		}
		JFrame frame = new JFrame("Bán vé từ phiếu đặt");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(new BanVePage(info));
		frame.setSize(1400, 900);
		frame.setLocationRelativeTo(this);
		frame.setVisible(true);
	}

	public static void moTrangMoi() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Tra cứu phiếu đặt");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setContentPane(new PhieuDatVeTraCuuPage());
			frame.setSize(1200, 800);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}