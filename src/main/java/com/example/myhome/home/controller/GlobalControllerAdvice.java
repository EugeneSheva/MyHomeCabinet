package com.example.myhome.home.controller;

import com.example.myhome.home.dto.AdminDTO;
import com.example.myhome.home.mapper.AdminDTOMapper;
import com.example.myhome.home.model.Admin;
import com.example.myhome.home.model.Owner;
import com.example.myhome.home.model.PageRoleDisplay;
import com.example.myhome.home.repository.PageRoleDisplayRepository;
import com.example.myhome.home.service.impl.AdminServiceImpl;
import com.example.myhome.home.service.OwnerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Log
public class GlobalControllerAdvice {

    @Autowired
    private PageRoleDisplayRepository repository;
    @Autowired
    private AdminServiceImpl adminService;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingRequestParameter(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // ловит любой оставшийся эксепшн (?)
    @ExceptionHandler(Exception.class)
    public String redirect(Exception e) {
        log.severe("Captured exception of class: " + e.getClass().toString());
        log.severe(e.getMessage());
        e.printStackTrace();
        return "redirect:/cabinet/500";
    }

    @ModelAttribute
    public void addPagePermissions(Model model) {
        List<PageRoleDisplay> pageRoles = repository.findAll();
        model.addAttribute("pageRoles", pageRoles);
    }

    @ModelAttribute
    public void addLoggedInAdmin(Model model) {
        if(SecurityContextHolder.getContext().getAuthentication() == null) return;
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(object instanceof Admin)) return;
        else {
            Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            admin = adminService.findAdminByLogin(admin.getUsername());
            AdminDTO dto = new AdminDTO();
            dto.setId(admin.getId());
            dto.setFirst_name(admin.getFirst_name());
            dto.setLast_name(admin.getLast_name());
            model.addAttribute("auth_admin", dto);
        }

    }

}
