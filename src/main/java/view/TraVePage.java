package view;

import controller.ChuyenTauController;
import controller.DoiTraController;
import controller.TuyenTauController;
import entity.ChuyenTau;
import entity.TuyenTau;
import entity.VeTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import service.DoiTraService.KetQuaXuLy;

public class TraVePage extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField txtMaVe = new JTextField();
	private final JLabel lblTrangThai = new JLabel("Chưa tìm vé", SwingConstants.LEFT);
	
	private final JLabel lblMaVe = taoDongGiaTri("Mã vé: —");
	private final JLabel lblKhachHang = taoDongGiaTri("Khách hàng: —");
	private final JLabel lblTuyen = taoDongGiaTri("Chuyến: —");
	private final JLabel lblToaGhe = taoDongGiaTri("Toa/Ghế: —");
	private final JLabel lblGiaVe = taoDongGiaTri("Giá vé gốc: —");
	private final JLabel lblPhiHoan = taoDongGiaTri("Số tiền hoàn (80%): —");
	private final JLabel lblTrangThaiVe = taoDongGiaTri("Trạng thái: —");

	private final JButton btnTraVe = new JButton("Xác nhận trả vé");
	private String maVeDangXuLy;
	
	private final DoiTraController doiTraController = new DoiTraController();
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final TuyenTauController tuyenTauController = new TuyenTauController();

	public TraVePage() {
		setLayout(new BorderLayout(0, 16));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoCardTimVe(), BorderLayout.NORTH);
		add(taoCardThongTin(), BorderLayout.CENTER);
	}

	private JPanel taoCardTimVe() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Tìm vé cần trả");
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

	private JPanel taoCardThongTin() {
		JPanel card = new JPanel(new BorderLayout(0, 14));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Thông tin hoàn vé");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(4, 2, 2, 2));
		card.add(title, BorderLayout.NORTH);

		// Khung chứa thông tin vé giống hệt giao diện DoiVePage
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.decode("#F8FAFD"));
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(14, 14, 14, 14)));

		JPanel lines = new JPanel(new GridLayout(7, 1, 0, 8));
		lines.setOpaque(false);
		lines.add(lblMaVe);
		lines.add(lblKhachHang);
		lines.add(lblTuyen);
		lines.add(lblToaGhe);
		lines.add(lblGiaVe);
		lines.add(lblPhiHoan);
		lines.add(lblTrangThaiVe);
		
		panel.add(lines, BorderLayout.CENTER);
		card.add(panel, BorderLayout.CENTER);

		AppTheme.stylePrimaryButton(btnTraVe);
		btnTraVe.setPreferredSize(new Dimension(0, 42));
		btnTraVe.setEnabled(false);
		btnTraVe.addActionListener(e -> xuLyTraVe());
		card.add(btnTraVe, BorderLayout.SOUTH);
		return card;
	}

	private JLabel taoDongGiaTri(String value) {
		JLabel label = new JLabel(value);
		label.setFont(AppTheme.font(Font.BOLD, 17));
		label.setForeground(AppTheme.PRIMARY);
		return label;
	}

	private void xuLyTimVe() {
		String maVe = txtMaVe.getText().trim().toUpperCase();
		if (maVe.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã vé cần trả.");
			return;
		}
		VeTau ve = doiTraController.timVeTheoMa(maVe);
		if (ve == null) {
			lblTrangThai.setForeground(Color.decode("#B42318"));
			lblTrangThai.setText("Không tìm thấy vé " + maVe + ".");
			resetThongTin();
			return;
		}

		maVeDangXuLy = maVe;
		
		// Tìm thông tin ga đi ga đến của chuyến tàu
		String gaDi = "—";
		String gaDen = "—";
		try {
			var trips = chuyenTauController.timKiemChuyenTau(ve.getMaChuyenTau());
			if (trips != null && !trips.isEmpty()) {
				var ct = trips.get(0);
				var routes = tuyenTauController.timKiemTuyenTau(ct.getMaTuyenTau(), null, null);
				if (routes != null && !routes.isEmpty()) {
					var tt = routes.get(0);
					gaDi = tt.getMaGaDi() != null ? tt.getMaGaDi() : "—";
					gaDen = tt.getMaGaDen() != null ? tt.getMaGaDen() : "—";
				}
			}
		} catch (Exception ignored) {}

		lblMaVe.setText("Mã vé: " + maVe);
		lblKhachHang.setText("Khách hàng: " + ve.getMaKH());
		lblTuyen.setText("Chuyến: " + ve.getMaChuyenTau() + " | Ga đi: " + gaDi + " → Ga đến: " + gaDen);
		lblToaGhe.setText("Toa/Ghế: " + ve.getMaToa() + " / " + ve.getViTriGhe());
		
		BigDecimal giaGoc = ve.getGiaVe() != null ? ve.getGiaVe() : BigDecimal.ZERO;
		BigDecimal hoanLai = giaGoc.multiply(new BigDecimal("0.8"));
		
		lblGiaVe.setText("Giá vé gốc: " + BanVeUtils.formatMoney(giaGoc));
		lblPhiHoan.setText("Số tiền hoàn (80%): " + BanVeUtils.formatMoney(hoanLai));
		lblTrangThaiVe.setText("Trạng thái: Có thể hoàn vé");
		
		btnTraVe.setEnabled(true);
		lblTrangThai.setForeground(Color.decode("#027A48"));
		lblTrangThai.setText("Đã tìm thấy vé. Kiểm tra thông tin và xác nhận trả vé.");
	}

	private void xuLyTraVe() {
		if (maVeDangXuLy == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng tìm vé trước khi xác nhận trả.");
			return;
		}

		KetQuaXuLy taoDon = doiTraController.taoDonDoiTra(maVeDangXuLy, null, "TRA", "Trả vé tại quầy");
		if (!taoDon.thanhCong) {
			JOptionPane.showMessageDialog(this, taoDon.thongBao);
			return;
		}
		KetQuaXuLy xacNhan = doiTraController.xacNhanDonDoiTra(taoDon.maThamChieu);
		JOptionPane.showMessageDialog(this, xacNhan.thongBao);
		if (xacNhan.thanhCong) {
			lblTrangThai.setForeground(Color.decode("#027A48"));
			lblTrangThai.setText("Đã hoàn vé thành công. Có thể tìm mã vé khác.");
		}
		resetThongTin();
		txtMaVe.setText("");
	}

	private void resetThongTin() {
		maVeDangXuLy = null;
		btnTraVe.setEnabled(false);
		lblMaVe.setText("Mã vé: —");
		lblKhachHang.setText("Khách hàng: —");
		lblTuyen.setText("Chuyến: —");
		lblToaGhe.setText("Toa/Ghế: —");
		lblGiaVe.setText("Giá vé gốc: —");
		lblPhiHoan.setText("Số tiền hoàn (80%): —");
		lblTrangThaiVe.setText("Trạng thái: —");
	}
}
