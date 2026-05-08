package view;

import dao.KhachHang_DAO;
import entity.KhachHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class KhachHangTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();

	public KhachHangTraCuuPage() {
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
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Tra cứu khách hàng");
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
			new EmptyBorder(14, 14, 14, 14)
		));

		// Phần tìm kiếm
		String[] columns = { "Mã KH", "Tên khách hàng", "Số ĐT", "Email", "CCCD", "Loại KH" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		napDuLieuKhachHang(model);

		JTable table = new JTable(model);
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		JPanel searchPanel = taoSearchPanel(sorter);
		content.add(searchPanel, BorderLayout.NORTH);
		table.setRowHeight(50);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setBackground(MAU_CHINH);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.setGridColor(Color.decode("#E4EBF3"));
		table.setSelectionBackground(Color.decode("#B3D9FF"));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void napDuLieuKhachHang(DefaultTableModel model) {
		// Nạp dữ liệu thật từ SQL để tra cứu luôn phản ánh DB hiện tại.
		List<KhachHang> dsKhachHang = khachHangDAO.layTatCa();
		for (KhachHang khachHang : dsKhachHang) {
			model.addRow(new Object[] {
				khachHang.getMaKH(),
				khachHang.getTenKH(),
				khachHang.getSdt(),
				khachHang.getEmail(),
				khachHang.getCccd(),
				khachHang.isLoaiKH() ? "VIP" : "Thường"
			});
		}
	}

	private JPanel taoSearchPanel(TableRowSorter<DefaultTableModel> sorter) {
		JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		JLabel lblSdt = new JLabel("Số điện thoại khách hàng:");
		lblSdt.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblSdt.setForeground(Color.decode("#2B4B74"));
		panel.add(lblSdt);

		JTextField txtSdt = new JTextField();
		txtSdt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtSdt.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		panel.add(txtSdt);

		// Nút Tìm kiếm
		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(new EmptyBorder(6, 12, 6, 12));
		panel.add(btnTimKiem);

		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 12, 6, 12)
		));
		panel.add(btnLamMoi);

		btnTimKiem.addActionListener(e -> {
			String sdt = txtSdt.getText().trim();
			if (sdt.isEmpty()) {
				sorter.setRowFilter(null);
				return;
			}
			sorter.setRowFilter(RowFilter.regexFilter(Pattern.quote(sdt), 3));
		});

		txtSdt.addActionListener(e -> btnTimKiem.doClick());

		btnLamMoi.addActionListener(e -> {
			txtSdt.setText("");
			sorter.setRowFilter(null);
		});

		return panel;
	}
}
