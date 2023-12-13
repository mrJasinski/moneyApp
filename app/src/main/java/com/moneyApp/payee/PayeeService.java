package com.moneyApp.payee;

import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.payee.dto.PayeeWithIdAndNameDTO;
import com.moneyApp.user.UserService;
import com.moneyApp.vo.PayeeSource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayeeService
{
    private final PayeeRepository payeeRepo;
    private final PayeeQueryRepository payeeQueryRepo;
    private final UserService userService;

    PayeeService(
            final PayeeRepository payeeRepo
            , final PayeeQueryRepository payeeQueryRepo
            , final UserService userService)
    {
        this.payeeRepo = payeeRepo;
        this.payeeQueryRepo = payeeQueryRepo;
        this.userService = userService;
    }

    PayeeSnapshot getPayeeByNameAndUserId(String name, long userId)
    {
        return this.payeeRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("Payee with given name not found!"));
    }

    PayeeDTO createPayeeByUserIdAsDto(PayeeDTO toSave, Long userId)
    {
//        check if payee with given name already exists for given user
        if (this.payeeRepo.existsByNameAndUserId(toSave.getName(), userId))
            throw new IllegalArgumentException("Payee with given name already exists!");

        return toDto(this.payeeRepo.save(new PayeeSnapshot(
                null
                , toSave.getName()
                , toSave.getRole()
//                , toSave.getBills().stream().map(BillDTO::toSource).collect(Collectors.toSet())
//                , toSave.getBillPositions().stream().map(BillPositionDTO::toSource).collect(Collectors.toSet())
                , new UserSource(userId)
        )));
    }

    PayeeDTO toDto(PayeeSnapshot snap)
    {
        return new PayeeDTO(
                snap.getName()
                , snap.getRole()
        );
    }

    List<PayeeSnapshot> gePayeesByUserId(Long userId)
    {
        return this.payeeRepo.findByUserId(userId);
    }

    public List<PayeeDTO> gePayeesByUserIdAsDto(Long userId)
    {
        return gePayeesByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    List<PayeeSnapshot> getPayeesByRoleAndUserId(PayeeRole role, Long userId)
    {
        return this.payeeRepo.findByRoleAndUserId(role, userId);
    }

    public PayeeSource getPayeeSourceByNameAndUserId(final String payeeName, final Long userId)
    {
        return new PayeeSource(this.payeeQueryRepo.findIdByNameAndUserId(payeeName, userId)
                .orElseThrow(() -> new IllegalArgumentException("Payee with given name and user id not found!")));
    }

    public String getPayeeNameById(final long payeeId)
    {
        return this.payeeQueryRepo.findNameById(payeeId)
                .orElseThrow(() -> new IllegalArgumentException("Payee with given id not found!"));
    }

    PayeeDTO getPayeeByNameAndUserIdAsDto(final String name, final Long userId)
    {
        var result = this.payeeQueryRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("Payee with given name and user id not found!"));

        return toDto(result);
    }

    public List<PayeeWithIdAndNameDTO> getPayeesByNamesAndUserIdAsDto(final List<String> payeeNames, final Long userId)
    {
        return this.payeeQueryRepo.findPayeesIdsAndNamesByNamesAndUserId(payeeNames, userId);
    }

    public List<PayeeWithIdAndNameDTO> getPayeesByIdsAsDto(final List<Long> payeeIds)
    {
        return this.payeeQueryRepo.findPayeesIdsAndNamesByIds(payeeIds);
    }

//    public List<PayeeDTO> getPayeesByRoleAndUserIdAsDto(PayeeRole role, Long userId)
//    {
//        return getPayeesByRoleAndUserId(role, userId)
//                .stream()
//                .map(Payee::toDto)
//                .collect(Collectors.toList());
//    }
}
