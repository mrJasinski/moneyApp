package com.moneyApp.bill;

import com.moneyApp.bill.csv.BillPositionsCsvFileGenerator;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/bills")
public class BillController
{
    private final BillService billService;
    private final BillQueryService billQueryService;
    private final JwtService jwtService;
    private final BillPositionsCsvFileGenerator csvGenerator;

    public BillController(
            final BillService billService
            , final BillQueryService billQueryService
            , final JwtService jwtService
            , BillPositionsCsvFileGenerator csvGenerator)
    {
        this.billService = billService;
        this.billQueryService = billQueryService;
        this.jwtService = jwtService;
        this.csvGenerator = csvGenerator;
    }

    @PostMapping("/addBill")
    ResponseEntity<?> createBill(@RequestBody BillDTO toSave, HttpServletRequest request)
    {
        var result = this.billService.createBillByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getBillsByUserEmail(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.billQueryService.getBillsByUserEmailAsDto(this.jwtService.getUserIdFromToken(request)));
    }

    //    TODO test
    @GetMapping("/exportPositions")
    void exportTransactionsByDatesToCSV(HttpServletRequest request, HttpServletResponse response, @RequestParam LocalDate startDate,
                                        @RequestParam LocalDate endDate) throws IOException
    {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");

        this.csvGenerator.writeTransactionsDtoToCsv(this.billQueryService.getBillPositionsByDatesAndUserIdAsDto(
                startDate, endDate, this.jwtService.getUserIdFromToken(request)), response.getWriter());
    }
}
