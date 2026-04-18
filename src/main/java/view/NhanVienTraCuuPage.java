package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

public class NhanVienTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final int COL_MA_NV = 1;
	private static final int COL_TEN_NV = 2;
	private static final int COL_CHUC_VU = 6;

	public NhanVienTraCuuPage() {
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

		JLabel title = new JLabel("Tra cứu nhân viên");
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

		// Phần bảng kết quả
		String[] columns = { "Mã NV", "Họ và tên", "Tài khoản", "Email", "Điện thoại", "Chức vụ" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.addRow(new Object[] { "U001", "Nguyễn Văn Admin", "admin", "admin@saigontrain.vn", "0901234567", "Quản lý" });
		model.addRow(new Object[] { "U002", "Trần Thị Nhân Viên", "staff", "staff@saigontrain.vn", "0901234568", "Bán vé" });
		model.addRow(new Object[] { "U003", "Nguyễn Phát Đạt", "nguyen.phat", "phat@saigontrain.vn", "0901234569", "Bán vé" });
		model.addRow(new Object[] { "U004", "Lê Thị Mai", "le.mai", "mai@saigontrain.vn", "0901234570", "Hỗ trợ" });
		model.addRow(new Object[] { "U005", "Phạm Văn Cường", "pham.cuong", "cuong@saigontrain.vn", "0901234571", "Quản lý" });

		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

		// Phần tìm kiếm
		JPanel searchPanel = taoSearchPanel(sorter);
		content.add(searchPanel, BorderLayout.NORTH);

		JTable table = new JTable(model);
		table.setRowSorter(sorter);
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

	private JPanel taoSearchPanel(TableRowSorter<DefaultTableModel> sorter) {
		JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(15, 15, 15, 15)
		));

		JLabel lblTuKhoa = new JLabel("Mã hoặc tên nhân viên:");
		lblTuKhoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTuKhoa.setForeground(Color.decode("#2B4B74"));
		panel.add(lblTuKhoa);

		JTextField txtTuKhoa = new JTextField();
		txtTuKhoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTuKhoa.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		panel.add(txtTuKhoa);

		// Nút Tìm kiếm
		JButton btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnTimKiem.setBackground(MAU_CHINH);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFocusPainted(false);
		btnTimKiem.setBorder(new EmptyBorder(6, 12, 6, 12));
		panel.add(btnTimKiem);

		JLabel lblChucVu = new JLabel("Chức vụ:");
		lblChucVu.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblChucVu.setForeground(Color.decode("#2B4B74"));
		panel.add(lblChucVu);

		JComboBox<String> cbChucVu = new JComboBox<>(new String[] { "Tất cả", "Quản lý", "Bán vé", "Hỗ trợ" });
		cbChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbChucVu.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		panel.add(cbChucVu);

		// Nút Làm mới
		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setForeground(Color.decode("#3A4D66"));
		btnLamMoi.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 12, 6, 12)
		));
		panel.add(btnLamMoi);

		Runnable apDungLoc = () -> {
			String tuKhoa = txtTuKhoa.getText().trim().toLowerCase();
			String chucVu = String.valueOf(cbChucVu.getSelectedItem());

			List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
			if (!tuKhoa.isEmpty()) {
				filters.add(new RowFilter<DefaultTableModel, Integer>() {
					@Override
					public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
						String maNV = String.valueOf(entry.getValue(COL_MA_NV)).toLowerCase();
						String tenNV = String.valueOf(entry.getValue(COL_TEN_NV)).toLowerCase();
						return maNV.contains(tuKhoa) || tenNV.contains(tuKhoa);
					}
				});
			}

			if (!"Tất cả".equals(chucVu)) {
				filters.add(RowFilter.regexFilter("(?i)^" + java.util.regex.Pattern.quote(chucVu) + "$", COL_CHUC_VU));
			}

			if (filters.isEmpty()) {
				sorter.setRowFilter(null);
			} else {
				sorter.setRowFilter(RowFilter.andFilter(filters));
			}
		};

		txtTuKhoa.addActionListener(e -> apDungLoc.run());
		btnTimKiem.addActionListener(e -> apDungLoc.run());
		cbChucVu.addActionListener(e -> apDungLoc.run());

		btnLamMoi.addActionListener(e -> {
			txtTuKhoa.setText("");
			cbChucVu.setSelectedIndex(0);
			sorter.setRowFilter(null);
		});

		return panel;
	}
}
