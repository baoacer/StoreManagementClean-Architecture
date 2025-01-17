package com.example.usecase.getProductList;

import com.example.database.MysqlGetProductList;
import com.example.dtos.getProductListDTOs.GetProductListInputDTO;
import com.example.dtos.getProductListDTOs.GetProductListOutputDTO;
import com.example.dtos.getProductListDTOs.GetProductListResponseData;
import com.example.entity.HangHoa;
import com.example.interfaces.DatabaseBoundary;
import com.example.interfaces.InputBoundary;
import com.example.interfaces.OutputBoundary;
import com.example.interfaces.RequestData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetProductListUseCase implements InputBoundary {

    private OutputBoundary outputBoundary = null;
    private DatabaseBoundary databaseBoundary = null;

    public GetProductListUseCase(OutputBoundary outputBoundary, DatabaseBoundary databaseBoundary) {
        this.outputBoundary = outputBoundary;
        this.databaseBoundary = databaseBoundary;
    }

    @Override
    public void execute(RequestData requestData) throws SQLException {
        String type = ((GetProductListInputDTO) requestData).getType();

        List<HangHoa> listHangHoa = ((MysqlGetProductList) databaseBoundary).getProductList(type);
        List<GetProductListOutputDTO> listOutputDTOS = new ArrayList<>();

        for (HangHoa hangHoa : listHangHoa) {
            GetProductListOutputDTO getProductOutputDTO = new GetProductListOutputDTO(
                    hangHoa.getMaHang(),
                    hangHoa.getTenHang(),
                    hangHoa.getTenLoai(),
                    hangHoa.getSoLuongTon(),
                    hangHoa.getDonGia(),
                    hangHoa.tinhVat());
            listOutputDTOS.add(getProductOutputDTO);
        }

        GetProductListResponseData getProductListResponseData = new GetProductListResponseData(listOutputDTOS);

        outputBoundary.exportResult(getProductListResponseData);
    }
}
