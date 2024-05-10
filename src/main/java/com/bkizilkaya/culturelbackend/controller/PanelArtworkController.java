package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.service.abstraction.ArtworkService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/artworks-list")
public class PanelArtworkController {
    private final ArtworkService artworkService;

    public PanelArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping()
    public String artworkPanel(Model model) {
        return findPaginated(1, model);
    }

    @PostMapping("/addArtwork")
    public String addArtworkPanel(@ModelAttribute("artwork") ArtworkCreateDTO model) {
        artworkService.addArtwork(model);
        return "redirect:/artworks-list";
    }

    @PostMapping("/saveArtwork")
    public String saveArtworkPanel(@ModelAttribute("artwork") ArtworkCreateDTO model) {
        model.setModifiedDate(LocalDateTime.now());
        artworkService.addArtwork(model);
        return "redirect:/artworks-list";
    }

    @GetMapping("/showNewArtworkForm")
    public String showNewArtworkForm(Model model) {
        ArtworkCreateDTO artworkCreateDTO = new ArtworkCreateDTO();
        model.addAttribute("artwork", artworkCreateDTO);
        return "new_artwork";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        ArtworkResponseDTO artworkResponseDTO = artworkService.getArtworkGivenId(id);

        model.addAttribute("artwork", artworkResponseDTO);
        return "update_artwork";
    }

    @GetMapping("/deleteArtwork/{id}")
    public String deleteArtwork(@PathVariable(value = "id") Long id) {
        this.artworkService.deleteArtwork(id);
        return "redirect:/artworks-list";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<ArtworkResponseDTO> page = artworkService.findPaginated(pageNo, pageSize);
        List<ArtworkResponseDTO> listOfArtworks = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listArtworks", listOfArtworks);
        return "artworks";
    }

    @GetMapping("/{artworkId}/deleteImage/{fileId}")
    public String deleteImage(@PathVariable("artworkId") Long artworkId, @PathVariable("fileId") Long fileId) {
        try {
            artworkService.removeArtworkImageFromArtwork(artworkId, fileId);
            return "redirect:/artworks-list/showFormForUpdate/" + artworkId;
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/{artworkId}/addImage")
    public ResponseEntity<?> addImage(@PathVariable("artworkId") Long artworkId, @RequestParam("image") MultipartFile file) {
        try {
            artworkService.addImageToArtwork(artworkId, file);
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating image: " + e.getMessage());
        }
    }
    @GetMapping("/updateArtworkImages/{id}")
    public String updateArtworkImages(@PathVariable(value = "id") Long id, Model model) {
        try {
            // ArtworkService'e yeni bir metod ekleyin:
            // void updateArtworkImages(Long artworkId, MultipartFile file)
            // Bu metod, artwork'e yeni bir resim eklemeli ve veritabanına kaydetmelidir.
            //artworkService.addImageToArtwork(model.getId(), file);
            ArtworkResponseDTO artworkResponseDTO = artworkService.getArtworkGivenId(id);

            model.addAttribute("artwork", artworkResponseDTO);
            return "update_image_artwork";
        } catch (Exception e) {
            // Hata durumunda gerekli işlemleri yapabilirsiniz, örneğin:
            // model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    @PostMapping("/updateArtworkImages/{id}/addImages")
    public String updateAddImagesToArtwork(@PathVariable(value = "id") Long id, Model model, MultipartFile multipartFile) {
        try {
            // ArtworkService'e yeni bir metod ekleyin:
            // void updateArtworkImages(Long artworkId, MultipartFile file)
            // Bu metod, artwork'e yeni bir resim eklemeli ve veritabanına kaydetmelidir.
            //artworkService.addImageToArtwork(model.getId(), file);
            ArtworkResponseDTO artworkResponseDTO = artworkService.getArtworkGivenId(id);
            artworkService.addImageToArtwork(id, multipartFile);
            model.addAttribute("artwork", artworkResponseDTO);
            return "redirect:/artworks-list/showFormForUpdate/"+id;
        } catch (Exception e) {
            // Hata durumunda gerekli işlemleri yapabilirsiniz, örneğin:
            // model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}