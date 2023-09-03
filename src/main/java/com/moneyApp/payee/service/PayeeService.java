package com.moneyApp.payee.service;

import com.moneyApp.payee.Payee;
import com.moneyApp.payee.PayeeRole;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.payee.repository.PayeeRepository;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayeeService
{
    private final PayeeRepository payeeRepo;
    private final UserService userService;

    PayeeService(PayeeRepository payeeRepo, UserService userService)
    {
        this.payeeRepo = payeeRepo;
        this.userService = userService;
    }

    public Payee getPayeeByNameAndUserId(String name, long userId)
    {
        return this.payeeRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("No payee found for given name!"));
    }

    public Payee createPayeeByUserEmail(PayeeDTO toSave, String email)
    {
//        sprawdzenie czy kontrahent o danej nazwie już istnieje dla danego użytkownika
        if (this.payeeRepo.existsByNameAndUserId(toSave.getName(), this.userService.getUserIdByEmail(email)))
            throw new IllegalArgumentException("Payee with given name already exists!");

        return this.payeeRepo.save(new Payee(toSave.getName(), toSave.getRole(), this.userService.getUserByEmail(email)));
    }

    List<Payee> gePayeesByUserEmail(String email)
    {
        return this.payeeRepo.findByUserId(this.userService.getUserIdByEmail(email));
    }

    public List<PayeeDTO> gePayeesByUserEmailAsDto(String email)
    {
        return gePayeesByUserEmail(email)
                .stream()
                .map(Payee::toDto)
                .collect(Collectors.toList());
    }

    List<Payee> getPayeesByRoleAndUserEmail(PayeeRole role, String email)
    {
        return this.payeeRepo.findByRoleAndUserId(role, this.userService.getUserIdByEmail(email));
    }

    public List<PayeeDTO> getPayeesByRoleAndUserEmailAsDto(PayeeRole role, String email)
    {
        return getPayeesByRoleAndUserEmail(role, email)
                .stream()
                .map(Payee::toDto)
                .collect(Collectors.toList());
    }
}
