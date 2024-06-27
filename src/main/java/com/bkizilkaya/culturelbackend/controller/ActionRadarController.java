package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.actionlog.ActionLogDTO;
import com.bkizilkaya.culturelbackend.service.concrete.ActionLogServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/actions")
public class ActionRadarController {
    private final ActionLogServiceImpl actionLogService;

    public ActionRadarController(ActionLogServiceImpl actionLogService) {
        this.actionLogService = actionLogService;
    }

    @GetMapping()
    public String radarPanel(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        try {
            int pageSize = 10;
            Page<ActionLogDTO> page = actionLogService.findPaginated(pageNo, pageSize);
            List<ActionLogDTO> actionLogs = page.getContent();
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("actionLogs", actionLogs);
            return "action_logs";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
            return "error";
        }
    }
}
