package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.spot.request.TouristSpotCreateDTO;
import com.bkizilkaya.culturelbackend.dto.spot.response.TouristSpotResponseDTO;
import com.bkizilkaya.culturelbackend.service.abstraction.TouristSpotService;
import com.bkizilkaya.culturelbackend.utils.PaginationValidator;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/touristic-spots-list")
public class PanelTouristSpotController {
    private final TouristSpotService touristSpotService;
    private final PaginationValidator paginationValidator;

    public PanelTouristSpotController(TouristSpotService touristSpotService, PaginationValidator paginationValidator) {
        this.touristSpotService = touristSpotService;
        this.paginationValidator = paginationValidator;
    }

    @GetMapping()
    public String touristicPanel(Model model) {
        model.addAttribute("title", "");
        return findPaginated(1, model);
    }

    @PostMapping("/addSpot")
    public String addSpotPanel(@ModelAttribute("spot") @Valid TouristSpotCreateDTO touristSpotCreateDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("submitted", true);
        if (bindingResult.hasErrors()) {
            return "new_spot";
        }
        touristSpotService.addSpot(touristSpotCreateDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Kayıt başarıyla eklendi!");
        return "redirect:/touristic-spots-list";
    }

    @GetMapping("/showNewSpotForm")
    public String showNewSpotForm(Model model) {
        TouristSpotCreateDTO touristSpotCreateDTO = new TouristSpotCreateDTO();
        model.addAttribute("spot", touristSpotCreateDTO);
        model.addAttribute("submitted", false);
        return "new_spot";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        try {
            TouristSpotResponseDTO touristSpotResponseDTO = touristSpotService.getSpotById(id);

            model.addAttribute("spot", touristSpotResponseDTO);
            model.addAttribute("submitted", false);
            return "update_spot";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
            return "error";
        }
    }

    @PostMapping("/saveSpot")
    public String saveSpotPanel(
            @ModelAttribute("spot") @Valid TouristSpotCreateDTO touristSpotCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        model.addAttribute("submitted", true);

        if (bindingResult.hasErrors()) {
            return "update_spot";
        }

        try {
            touristSpotService.updateSpot(touristSpotCreateDTO.getId(), touristSpotCreateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Kayıt başarıyla güncellendi!");
            return "redirect:/touristic-spots-list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
            return "error";
        }
    }

    @GetMapping("/updateSpotImages/{id}")
    public String updateSpotImages(@PathVariable(value = "id") Long id, Model model) {
        try {
            TouristSpotResponseDTO touristSpotResponseDTO = touristSpotService.getSpotById(id);

            model.addAttribute("spot", touristSpotResponseDTO);
            return "update_image_spot";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.NOT_FOUND.value());
            return "error";
        }
    }

    @PostMapping("/updateSpotImages/{id}/addImages")
    public String addImagesToSpot(@PathVariable(value = "id") Long id, Model model, MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        try {
            TouristSpotResponseDTO touristSpotResponseDTO = touristSpotService.getSpotById(id);
            touristSpotService.addImageToSpot(id, multipartFile);
            model.addAttribute("spot", touristSpotResponseDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Resim başarıyla eklendi!");
            return "redirect:/touristic-spots-list/showFormForUpdate/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/deleteSpot/{id}")
    public String deleteSpot(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        this.touristSpotService.deleteSpot(id);
        redirectAttributes.addFlashAttribute("successMessage", "Kayıt silindi!");
        return "redirect:/touristic-spots-list";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        try {
            int pageSize = 5;
            Page<TouristSpotResponseDTO> page = touristSpotService.findPaginated(pageNo, pageSize);
            paginationValidator.validatePageNumberIsExists(pageNo, page);
            List<TouristSpotResponseDTO> touristicSpots = page.getContent();
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("touristicSpots", touristicSpots);
            return "touristic_spots";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
            return "error";
        }
    }

    @GetMapping("/search")
    public String searchSpots(@RequestParam("title") String title, @RequestParam(defaultValue = "1") int pageNo, Model model) {
        int pageSize = 5;
        Page<TouristSpotResponseDTO> page = touristSpotService.searchSpotsPaginated(title, pageNo, pageSize);
        List<TouristSpotResponseDTO> listOfSpots = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("touristicSpots", listOfSpots);
        model.addAttribute("title", title);
        return "touristic_spots";
    }

}
