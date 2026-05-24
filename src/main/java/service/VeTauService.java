package service;

import dao.VeTau_DAO;
import entity.LichSuVeDTO;
import java.util.List;

public class VeTauService {
    private final VeTau_DAO veTauDAO = new VeTau_DAO();

    public List<LichSuVeDTO> layLichSuVe(String maKH) {
        return veTauDAO.layLichSuVe(maKH);
    }
}
