package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.service.abstraction.ArtworkService;
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
@RequestMapping("/artworks-list")
public class PanelArtworkController {
    private final ArtworkService artworkService;
    private final PaginationValidator paginationValidator;

    public PanelArtworkController(ArtworkService artworkService, PaginationValidator paginationValidator) {
        this.artworkService = artworkService;
        this.paginationValidator = paginationValidator;
    }

    @GetMapping()
    public String artworkPanel(Model model) {
        return findPaginated(1, model);
    }

    @PostMapping("/addArtwork")
    public String addArtworkPanel(@ModelAttribute("artwork") @Valid ArtworkCreateDTO artworkCreateDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("submitted", true);
        if (bindingResult.hasErrors()) {
            return "new_artwork";
        }
        artworkService.addArtwork(artworkCreateDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Kayıt başarıyla eklendi!");
        return "redirect:/artworks-list";
    }

    @PostMapping("/saveArtwork")
    public String saveArtworkPanel(@ModelAttribute("artwork") ArtworkCreateDTO model, RedirectAttributes redirectAttributes) {
        artworkService.updateArtwork(model.getId(), model);
        redirectAttributes.addFlashAttribute("successMessage", "Kayıt başarıyla güncellendi!");
        return "redirect:/artworks-list";
    }

    @GetMapping("/showNewArtworkForm")
    public String showNewArtworkForm(Model model) {
        ArtworkCreateDTO artworkCreateDTO = new ArtworkCreateDTO();
        model.addAttribute("artwork", artworkCreateDTO);
        model.addAttribute("submitted", false);
        return "new_artwork";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        ArtworkResponseDTO artworkResponseDTO = artworkService.getArtworkGivenId(id);

        model.addAttribute("artwork", artworkResponseDTO);
        return "update_artwork";
    }

    @GetMapping("/deleteArtwork/{id}")
    public String deleteArtwork(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        this.artworkService.deleteArtwork(id);
        redirectAttributes.addFlashAttribute("successMessage", "Kayıt silindi!");
        return "redirect:/artworks-list";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        try {
            int pageSize = 5;
            Page<ArtworkResponseDTO> page = artworkService.findPaginated(pageNo, pageSize);
            paginationValidator.validatePageNumberIsExists(pageNo, page);
            List<ArtworkResponseDTO> listOfArtworks = page.getContent();
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("listArtworks", listOfArtworks);
            return "artworks";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
            return "error";
        }
    }

    @GetMapping("/{artworkId}/deleteImage/{fileId}")
    public String deleteImage(@PathVariable("artworkId") Long artworkId, @PathVariable("fileId") Long fileId, Model model, RedirectAttributes redirectAttributes) {
        try {
            artworkService.removeArtworkImageFromArtwork(artworkId, fileId);
            redirectAttributes.addFlashAttribute("successMessage", "Resim başarıyla silindi!");
            return "redirect:/artworks-list/showFormForUpdate/" + artworkId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{artworkId}/addImage")
    public String addImage(@PathVariable("artworkId") Long artworkId, @RequestParam("image") MultipartFile file, Model model) {
        try {
            artworkService.addImageToArtwork(artworkId, file);
            return "redirect:/artworks-list/showFormForUpdate/" + artworkId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/updateArtworkImages/{id}")
    public String updateArtworkImages(@PathVariable(value = "id") Long id, Model model) {
        try {
            ArtworkResponseDTO artworkResponseDTO = artworkService.getArtworkGivenId(id);

            model.addAttribute("artwork", artworkResponseDTO);
            return "update_image_artwork";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("status", HttpStatus.NOT_FOUND.value());
            return "error";
        }
    }

    @PostMapping("/updateArtworkImages/{id}/addImages")
    public String addImagesToArtwork(@PathVariable(value = "id") Long id, Model model, MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        try {
            ArtworkResponseDTO artworkResponseDTO = artworkService.getArtworkGivenId(id);
            artworkService.addImageToArtwork(id, multipartFile);
            model.addAttribute("artwork", artworkResponseDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Resim başarıyla eklendi!");
            return "redirect:/artworks-list/showFormForUpdate/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
    @GetMapping("/search")
    public String searchArtworks(@RequestParam("query") String query, Model model) {
        List<ArtworkResponseDTO> listArtworks = artworkService.searchArtworks(query);
        model.addAttribute("listArtworks", listArtworks);
        model.addAttribute("query", query);
        return "artworks";
    }
}