package com.example.usecase.getListProductExpired;

import com.example.database.MysqlGetFoodList;
import com.example.dtos.getProductListSevenDayExpiryDTOs.GetProductListSevenDayExpiryInputDTO;
import com.example.dtos.getProductListSevenDayExpiryDTOs.GetProductListSevenDayExpiryOutputDTO;
import com.example.dtos.getProductListSevenDayExpiryDTOs.GetProductListSevenDayExpiryResponseData;
import com.example.entity.HangHoa;
import com.example.entity.HangThucPham;
import com.example.interfaces.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetProductListSevenDayExpiryUseCase implements InputBoundary {
    private OutputBoundary outputBoundary = null;
    private DatabaseBoundary databaseBoundary = null;

    public GetProductListSevenDayExpiryUseCase(OutputBoundary outputBoundary, DatabaseBoundary databaseBoundary) {
        this.outputBoundary = outputBoundary;
        this.databaseBoundary = databaseBoundary;
    }

    @Override
    public void execute(RequestData requestData) throws SQLException {
        String type = ((GetProductListSevenDayExpiryInputDTO) requestData).getType();

        List<HangHoa> listHangHoa = ((MysqlGetFoodList) databaseBoundary).getProductList(type);
        List<GetProductListSevenDayExpiryOutputDTO> listOutputDTOS = new ArrayList<>();

        LocalDate dateNow = LocalDate.now();
        for (HangHoa hangHoa : listHangHoa) {
            if (hangHoa instanceof HangThucPham hangThucPham) {
                LocalDate ngayHetHan = hangThucPham.getNgayHetHan();

                long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(dateNow, ngayHetHan);

                if (daysUntilExpiry >= 0 && daysUntilExpiry <= 7) {
                    GetProductListSevenDayExpiryOutputDTO getProductListOutputDTO = new GetProductListSevenDayExpiryOutputDTO(
                            hangThucPham.getMaHang(),
                            hangThucPham.getTenHang(),
                            hangThucPham.getSoLuongTon(),
                            hangThucPham.getDonGia(),
                            hangThucPham.tinhVat()
                    );
                    listOutputDTOS.add(getProductListOutputDTO);
                }
                System.out.println("Ngay Het Han: " + ngayHetHan);
                System.out.println("Days Until Expiry: " + daysUntilExpiry);
            }
        }
        ResponseData responseData = new GetProductListSevenDayExpiryResponseData(listOutputDTOS);
        outputBoundary.exportResult(responseData);
    }


}
