package com.moneyApp.bill;

import com.moneyApp.bill.csv.BillPositionsCsvFileGenerator;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/bills")
public class BillController
{
    private final BillService billService;
    private final JwtService jwtService;
    private final BillPositionsCsvFileGenerator csvGenerator;

    public BillController(
            final BillService billService
            , final JwtService jwtService
            , final BillPositionsCsvFileGenerator csvGenerator)
    {
        this.billService = billService;
        this.jwtService = jwtService;
        this.csvGenerator = csvGenerator;
    }

    @PostMapping("/addBill")
    ResponseEntity<?> createBill(@RequestBody BillDTO toSave, HttpServletRequest request)
    {
        var result = this.billService.createBillByUserId(toSave, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.created(URI.create("/" + result.getNumber())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getBillsByUser(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.billService.getBillsByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }

    @GetMapping("/view/{number}")
    ResponseEntity<?> getBillByNumberAndUser(@PathVariable String number, HttpServletRequest request)
    {
        return ResponseEntity.ok(this.billService.getBillByNumberAndUserIdAsDto(number, this.jwtService.getUserIdFromToken(request)));
    }

    @PutMapping("/update/{number}")
    ResponseEntity<?> updateBillByNumberAndUser(@PathVariable String number, @RequestBody BillDTO toUpdate, HttpServletRequest request)
    {
        var userId = this.jwtService.getUserIdFromToken(request);

        if (!this.billService.existsByNumberAndUserId(number, userId))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(this.billService.updateBillByNumberAndUserAsDto(toUpdate, userId));
    }

    @DeleteMapping("/{number}/delete")
    ResponseEntity<?> deleteBillByNumberAndUser(@PathVariable String number, HttpServletRequest request)
    {
        this.billService.deleteBillByNumberAndUserId(number, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.ok("Bill deleted!");
    }

//    //    TODO test
//    @GetMapping("/exportPositions")
//    void exportTransactionsByDatesToCSV(HttpServletRequest request, HttpServletResponse response, @RequestParam LocalDate startDate,
//                                        @RequestParam LocalDate endDate) throws IOException
//    {
//        response.setContentType("text/csv");
//        response.addHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");
//
//        this.csvGenerator.writeTransactionsDtoToCsv(this.billQueryService.getBillPositionsByDatesAndUserIdAsDto(
//                startDate, endDate, this.jwtService.getUserIdFromToken(request)), response.getWriter());
//    }
}
