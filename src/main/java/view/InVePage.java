package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class InVePage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2563EB");
	private static final Color MAU_NEN = Color.decode("#F3F6FB");
	private static final Color MAU_TEXT = Color.decode("#35506B");

	public InVePage() {
		setLayout(new BorderLayout());
		setBackground(MAU_NEN);
		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDE5F2")));
		header.setPreferredSize(new Dimension(0, 62));
		JLabel title = new JLabel("In vé");
		title.setFont(new Font("Segoe UI", Font.BOLD, 20));
		title.setForeground(MAU_TEXT);
		title.setBorder(new EmptyBorder(0, 16, 0, 0));
		header.add(title, BorderLayout.WEST);
		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout());
		content.setOpaque(false);
		content.setBorder(new EmptyBorder(16, 16, 16, 16));

		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DDE5F2")),
			new EmptyBorder(24, 24, 24, 24)
		));

		JLabel title = new JLabel("Vé đã sẵn sàng để in");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		title.setForeground(MAU_TEXT);
		card.add(title, BorderLayout.NORTH);

		JPanel receipt = new JPanel(new GridBagLayout());
		receipt.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		String[][] fields = {
			{ "Mã vé", "VE0008" },
			{ "Mã hóa đơn", "HD03042608" },
			{ "Khách hàng", "Nguyễn Thị Lan" },
			{ "Chuyến tàu", "SE1-030426" },
			{ "Tuyến", "Sài Gòn - Hà Nội" },
			{ "Khởi hành", "2026-04-03 19:00" },
			{ "Toa / Ghế", "Toa 2 (Ghế mềm) · E03" },
			{ "Giá vé", "1.105.000 đ" },
			{ "Trạng thái", "Đã lập hóa đơn" }
		};

		for (int i = 0; i < fields.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 0.28;
			JLabel lbl = new JLabel(fields[i][0] + ":");
			lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			lbl.setForeground(MAU_TEXT);
			receipt.add(lbl, gbc);
			gbc.gridx = 1;
			gbc.weightx = 0.72;
			JLabel val = new JLabel(fields[i][1]);
			val.setFont(new Font("Segoe UI", Font.BOLD, 13));
			val.setForeground(MAU_TEXT);
			val.setHorizontalAlignment(JLabel.RIGHT);
			receipt.add(val, gbc);
		}

		card.add(new JScrollPane(receipt), BorderLayout.CENTER);

		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		footer.setOpaque(false);
		JButton btnPrint = new JButton("In vé");
		btnPrint.setBackground(MAU_CHINH);
		btnPrint.setForeground(Color.WHITE);
		btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnPrint.setFocusPainted(false);
		btnPrint.setBorder(new EmptyBorder(8, 16, 8, 16));
		JButton btnNew = new JButton("Bán vé mới");
		btnNew.setBackground(Color.WHITE);
		btnNew.setForeground(MAU_TEXT);
		btnNew.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnNew.setFocusPainted(false);
		btnNew.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		footer.add(btnPrint);
		footer.add(btnNew);
		card.add(footer, BorderLayout.SOUTH);

		content.add(card, BorderLayout.CENTER);
		return content;
	}
}
