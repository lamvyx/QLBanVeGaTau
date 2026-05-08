package view;

import connectDB.Database;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class NhanVienTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final int COL_MA_NV = 0;
	private static final int COL_TEN_NV = 1;
	private static final int COL_CHUC_VU = 5;

	private DefaultTableModel model;
	private JTable table;

	public NhanVienTraCuuPage() {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
		napDuLieuTuDB();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);

		JLabel title = new JLabel("Tra cứu nhân viên");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 15));
		content.setOpaque(false);

		// Phần bảng kết quả
		String[] columns = { "Mã NV", "Họ và tên", "Tài khoản", "Email", "Điện thoại", "Chức vụ" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		AppTheme.styleTable(table);
		table.setRowHeight(40);
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		// Phần tìm kiếm
		JPanel searchPanel = taoSearchPanel(sorter);
		content.add(searchPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private JPanel taoSearchPanel(TableRowSorter<DefaultTableModel> sorter) {
		JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
		panel.setBackground(AppTheme.CARD_BG);
		panel.setBorder(AppTheme.cardBorder());

		JLabel lblTuKhoa = new JLabel("Mã hoặc tên nhân viên:");
		lblTuKhoa.setFont(AppTheme.font(Font.BOLD, 12));
		panel.add(lblTuKhoa);

		JTextField txtTuKhoa = new JTextField();
		styleInput(txtTuKhoa);
		panel.add(txtTuKhoa);

		JButton btnTimKiem = new JButton("Tìm kiếm");
		AppTheme.stylePrimaryButton(btnTimKiem);
		panel.add(btnTimKiem);

		JLabel lblChucVu = new JLabel("Chức vụ:");
		lblChucVu.setFont(AppTheme.font(Font.BOLD, 12));
		panel.add(lblChucVu);

		JComboBox<String> cbChucVu = new JComboBox<>(new String[] { "Tất cả", "Quản lý", "Bán vé", "Hỗ trợ" });
		cbChucVu.setFont(AppTheme.font(Font.PLAIN, 13));
		panel.add(cbChucVu);

		JButton btnLamMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnLamMoi);
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
			napDuLieuTuDB();
		});

		return panel;
	}

	private void napDuLieuTuDB() {
		model.setRowCount(0);
		String sql = "SELECT maNV, tenNV, username, email, sdt, chucVu FROM NhanVien WHERE trangThai = 1";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getString("maNV"),
					rs.getString("tenNV"),
					rs.getString("username"),
					rs.getString("email"),
					rs.getString("sdt"),
					rs.getString("chucVu")
				});
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu nhân viên: " + ex.getMessage());
		}
	}

	private void styleInput(JTextField field) {
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(6, 8, 6, 8)));
	}
}
