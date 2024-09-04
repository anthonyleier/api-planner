package br.com.anthonycruz.planner.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.anthonycruz.planner.models.Link;
import br.com.anthonycruz.planner.services.LinkService;

@RestController
@RequestMapping("/links")
public class LinkController {
    private final LinkService service;

    public LinkController(LinkService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<Link> optionalLink = this.service.findById(id);

        if (optionalLink.isPresent()) {
            Link link = optionalLink.get();
            this.service.delete(link);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
