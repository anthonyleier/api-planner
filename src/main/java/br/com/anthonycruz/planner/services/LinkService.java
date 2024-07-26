package br.com.anthonycruz.planner.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonycruz.planner.dtos.LinkDTO;
import br.com.anthonycruz.planner.models.Link;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.LinkRepository;
import br.com.anthonycruz.planner.requests.LinkRequest;

@Service
public class LinkService {
    @Autowired
    LinkRepository repository;

    public Link registerLink(LinkRequest request, Trip trip) {
        Link newLink = new Link(request.title(), request.url(), trip);
        Link savedLink = this.repository.save(newLink);
        return savedLink;
    }

    public List<LinkDTO> getAllLinksFromTrip(UUID id) {
        return this.repository.findByTripId(id)
                .stream()
                .map(link -> new LinkDTO(link.getId(), link.getTitle(), link.getUrl()))
                .toList();
    }
}
